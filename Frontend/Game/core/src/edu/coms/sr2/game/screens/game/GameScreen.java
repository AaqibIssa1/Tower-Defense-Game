package edu.coms.sr2.game.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.GameObjects.Builds.BuildingEntity;
import edu.coms.sr2.game.GameObjects.CommonSprites;
import edu.coms.sr2.game.GameObjects.projectiles.Arrow;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.interfacing.Activities;
import edu.coms.sr2.game.screens.LobbyScreen;
import edu.coms.sr2.game.screens.social.FriendListScreen;
import edu.coms.sr2.game.sockets.SocketController;
import edu.coms.sr2.game.sockets.controllers.CommandController;

import static com.badlogic.gdx.Gdx.input;

public class GameScreen extends GameComponentScreen{

    public static final String TAG = GameScreen.class.getSimpleName();

    //private Texture blackSquareTexture;
    private boolean gameOver;
    private GlyphLayout gameOverText;
    private boolean towerIsTouched = false;
    private float opponentHealth = 1;
    public float myHealth = 1;
    private static float arrowDmg = 0.1f;
    private int corn = 0;
    private static int cornMax = 15;
    private int timer = 0;
    private int delay = 30;
    public HashMap<Integer, Arrow> myArrows = new HashMap<>();
    public HashMap<Integer, Arrow> opponentArrows = new HashMap<>();
    public HashMap<Integer, BuildingEntity> myBuildings = new HashMap<>();
    public HashMap<Integer, BuildingEntity> opponentBuildings = new HashMap<>();

    private static GameScreen instance;

    public static GameScreen getInstance() {
        if (instance == null)
            instance = newInstance();
        return instance;
    }

    //At the moment using a new instance is the best way for the following reason(s):
    // If LibGDX switches to android context, it will reload next time, and some/all
    // GL-dependent items will be invalidated. Must be reconstructed on next showing, but show() does not
    // run synchronously before draw(), so that is not an option. TODO Implement custom GDX context detection and reload
    public static GameScreen newInstance() {
        instance = Threading.supplyFromGlThread(() -> new GameScreen());
        return instance;
    }

    /**
     * Game Screen constructor.  Makes the game.
     */
    protected GameScreen() {

    }

    /**
     * Shows the screen
     */
    @Override
    public void show() {
        FriendListScreen.getInstance().clearOutgoingInvites();
        LobbyScreen.getInstance().cancelMatchMaking();
    }

    ArrayList<Arrow> arrowsToRemove;

    /**
     * Renders/draws the screen
     *
     * @param delta
     */
    @Override
    public void render(float delta) {
        if(gameOver) {
            drawGameOverScreen();
        }
        else {
            updateGameState(delta);
            readTouch();
            drawScreen();
        }
    }

    private void updateGameState(float delta) {
        updateCorn();
        updateArrows(delta);
        checkHealth();
        removeArrows();
    }

    private void updateCorn() {
        timer++;
        if (timer >= delay) {
            if (corn < cornMax) {
                corn++;
            }
            timer = 0;
        }
    }

    private void updateArrows(float delta) {
        arrowsToRemove = new ArrayList<>();
        for (Arrow arrow : myArrows.values()) {
            arrow.update(delta);
            arrow.sendArrowData();
            if (arrow.isAtDesination()) {
                arrowsToRemove.add(arrow);
            }
        }
        runArrowHitDetection();
    }

    private void runArrowHitDetection() {
        for (Arrow arrow : myArrows.values()) {
            BuildingEntity buildingHit = arrow.buildingHitDetection(opponentBuildings.values());
            if(buildingHit != null) {
                onArrowHitBuilding(arrow, buildingHit);
                break;
            }
            if(arrow.towerHitDetection(components.opponentTowerHitbox)) {
                onArrowHitTower(arrow);
                break;
            }
        }
    }

    private void onArrowHitBuilding(Arrow arrow, BuildingEntity building) {
        building.destroy();
        arrowsToRemove.add(arrow);
        try {
            SocketController.send("destroyBuilding", building.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onArrowHitTower(Arrow arrow) {
        if (opponentHealth > 0) {
            arrowsToRemove.add(arrow);
            opponentHealth -= arrowDmg;
            try {
                SocketController.send("damageEnemyTower");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeArrows() {
        for(Arrow arrow : arrowsToRemove) {
            myArrows.remove(arrow.getId());
            try {
                SocketController.send("removeArrow", arrow.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkHealth(){
        if (opponentHealth <= 0) {
            gameOver = true;
            gameOverText = new GlyphLayout(components.font, "Game Over! \n You Won!");
            SocketController.trySend("gameWon");
        }
        if(myHealth <= 0) {
            gameOver = true;
            gameOverText = new GlyphLayout(components.font, "Game Over! \n You Lost!");
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        sendStopCommand();
    }

    /**
     * Takes in touch input
     */
    private void readTouch() {
        if (input.isTouched()) {

            Vector3 lastTouch_3D = new Vector3(input.getX(), input.getY(), 0);
            components.camera.unproject(lastTouch_3D);
            Vector2 touch = new Vector2(lastTouch_3D.x, lastTouch_3D.y);

            BuildingEntity newBuilding = components.menu.menuAndBuildingTouch(touch, myBuildings.values());
            if(newBuilding != null && corn >= newBuilding.getCost()) {
                myBuildings.put(newBuilding.getId(), newBuilding);
                if(newBuilding.getType() == 1) {
                    cornMax += 5;
                }
                corn -= newBuilding.getCost();
                try {
                    SocketController.send("buildingCreated",
                            newBuilding.getCoordinate().x,
                            newBuilding.getCoordinate().y, newBuilding.getId(), newBuilding.getType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (components.myTowerSpr.getX() <= touch.x && touch.x <= components.myTowerSpr.getX() + 175 &&
                    0 <= touch.y && touch.y <= components.myTowerSpr.getHeight() && !components.menu.isDisplaying()) {
                towerIsTouched = true;
            } else {
                if (towerIsTouched && corn >= 5) {
                    Arrow newArrow = new Arrow(components.screenSize.x / 2 - components.myTowerSpr.getWidth() / 2, components.myTowerSpr.getHeight(),
                            touch.x, touch.y);

                    myArrows.put(newArrow.getId(), newArrow);

                    towerIsTouched = false;
                    corn -= 5;
                }
            }
        }
    }

    private void sendStopCommand() {
        try {
            SocketController.send("stopGame");
        } catch (IOException e) {
            Application.error(TAG, e);
        }
    }

    private void drawBackground(float r, float g, float b, float a) {
        Gdx.gl.glClearColor(r, g, b, a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        components.camera.update();
    }

    private void drawGameOverScreen() {
        drawBackground(0, 0, 0, 0);
        components.batch.begin();
        components.font.draw(components.batch, gameOverText,
                (components.screenSize.x - gameOverText.width) / 2,
                (components.screenSize.y + gameOverText.height) / 2);
        components.batch.end();
    }

    private void drawScreen() {
        drawBackground(0 / 255f, 128 / 255f, 0 / 255f, 1);
        components.batch.begin();

        //components.batch.draw(components.backgroundSpr, 0, 0, 1080 , 1920);

        GlyphLayout cornLayout = new GlyphLayout(components.font, "Corn: " + corn);

        components.font.draw(components.batch, cornLayout, 0, components.screenSize.y);

        if (towerIsTouched) {
            components.batch.draw(components.lightSpr, components.myTowerSpr.getX() - 134, -130, components.myTowerSpr.getWidth() + 280, components.myTowerSpr.getHeight() + 300);
        }

        components.myTowerSpr.draw(components.batch);
        components.opponentTowerSpr.draw(components.batch);

        components.batch.draw(components.redHealthSpr, components.myTowerSpr.getX(), components.myTowerSpr.getHeight() + 10, components.myTowerSpr.getWidth(), 5);
        components.batch.draw(components.greenHealthSpr, components.myTowerSpr.getX(), components.myTowerSpr.getHeight() + 10, components.myTowerSpr.getWidth() * myHealth, 5);

        components.batch.draw(components.redHealthSpr, components.opponentTowerSpr.getX(), components.opponentTowerSpr.getY() - 10, components.opponentTowerSpr.getWidth(), 5);
        components.batch.draw(components.greenHealthSpr, components.opponentTowerSpr.getX(), components.opponentTowerSpr.getY() - 10,
                components.opponentTowerSpr.getWidth() * opponentHealth, 5);

        components.menu.displayMenu(components.batch);

        for (Arrow arrow : myArrows.values())
            arrow.draw(components.batch, false);
        for (Arrow arrow : opponentArrows.values())
            arrow.draw(components.batch, true);

        for(BuildingEntity building : myBuildings.values() )
            building.draw(components.batch, false);
        for(BuildingEntity building : opponentBuildings.values() )
            building.draw(components.batch, false);


        components.batch.end();
    }


    /**
     * Disposes of the images and sprite batch
     */
    @Override
    public void dispose() {
        components.batch.dispose();
        components.greenHealthTex.dispose();
        components.redHealthTex.dispose();
        components.lightTex.dispose();
        components.myTowerTex.dispose();
        components.opponentTowerTex.dispose();
        components.arrowTex.dispose();
        //components.blackSquareTex.dispose();
        components.farmTex.dispose();
        sendStopCommand();
    }

    public static class GameScreenCommandController extends CommandController {
        @SocketCommand("othersArrow")
        public void othersArrow(float x, float y, float xDes, float yDes, int arrowId) {
            Arrow arrow = getInstance().opponentArrows
                    .computeIfAbsent(arrowId, id->new Arrow(x, y, xDes, yDes));
            arrow.setX(x);
            arrow.setY(y);
        }

        @SocketCommand("othersBuilding")
        public void othersBuilding(float xPos, float yPos, int id, int type) {
            BuildingEntity building = BuildingEntity.Factory.make(type);
            building.setCoordinate((int)xPos, (int)yPos);
            building.setId(id, entityIdSetterKey);
            getInstance().opponentBuildings.put(id, building);
        }

        @SocketCommand("othersRemoveArrow")
        public void othersRemoveArrow(int id) {
            getInstance().opponentArrows.remove(id);
        }

        @SocketCommand
        public void damageTower() {
            if(GameScreen.getInstance().myHealth > 0) {
                GameScreen.getInstance().myHealth -= arrowDmg;
            }
        }

        @SocketCommand("selfDestroyBuilding")
        public void selfDestroyBuilding(int id) {
            BuildingEntity building = getInstance().myBuildings.get(id);
            if(building != null) {
                if(building.getType() == 1) {
                    cornMax -= 5;
                }
                building.destroy();
            }
        }

        @SocketCommand
        public void stopGame() {
            Application.log(TAG, "STOPPING GAME");
            Application.startActivity(Activities.LOBBY);
            LobbyScreen.getInstance().toast("Game shut down by server.");
        }
    }
}

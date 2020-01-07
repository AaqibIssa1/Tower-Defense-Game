package edu.coms.sr2.game.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.GameObjects.Builds.BuildingEntity;
import edu.coms.sr2.game.GameObjects.projectiles.Arrow;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.sockets.SocketController;
import edu.coms.sr2.game.sockets.controllers.CommandController;

public class SpectateScreen extends GameComponentScreen implements Screen {
    public static final String TAG = SpectateScreen.class.getSimpleName();

    private boolean gameOver;
    private GlyphLayout gameOverText;

    private float opponentHealth = 1;
    private float myHealth = 1;

    protected SpectateScreen() {
    }
    private static SpectateScreen instance;
    private static int gameId, playerId;
    private HashMap<Integer, Arrow> selfArrows = new HashMap<>();
    private HashMap<Integer, Arrow> enemyArrows = new HashMap<>();
    private HashMap<Integer, BuildingEntity> selfBuildings = new HashMap<>();
    private HashMap<Integer, BuildingEntity> enemyBuildings = new HashMap<>();

    public static SpectateScreen getInstance() {
        if (instance == null)
            instance = newInstance();
        return instance;
    }

    //At the moment using a new instance is the best way for the following reason(s):
    // If LibGDX switches to android context, it will reload next time, and some/all
    // GL-dependent items will be invalidated. Must be reconstructed on next showing, but show() does not
    // run synchronously before draw(), so that is not an option. TODO Implement custom GDX context detection and reload
    public static SpectateScreen newInstance() {
        instance = Threading.supplyFromGlThread(() -> new SpectateScreen());
        return instance;
    }

    public static void setGameId(int id) {
        gameId = id;
    }
    public static void setPlayerPerspective(int perspectivePlayerId) { playerId = perspectivePlayerId; }

    @Override
    public void show() {
        try {
            SocketController.send("startSpectating", gameId, playerId);
        } catch (IOException e) {
            Application.error(TAG, e);
        }
    }

    @Override
    public void render(float delta) {
        if(gameOver)
            drawGameOverScreen();
        else
            drawScreen();
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

        components.myTowerSpr.draw(components.batch);
        components.opponentTowerSpr.draw(components.batch);

        for (Map.Entry<Integer, Arrow> arrow : selfArrows.entrySet())
            arrow.getValue().draw(components.batch, false);
        for (Map.Entry<Integer, Arrow> arrow : enemyArrows.entrySet())
            arrow.getValue().draw(components.batch, true);

        for(Map.Entry<Integer, BuildingEntity> building : selfBuildings.entrySet())
            building.getValue().draw(components.batch, false);
        for(Map.Entry<Integer, BuildingEntity> building : enemyBuildings.entrySet())
            building.getValue().draw(components.batch, true);


        components.batch.draw(components.redHealthSpr, components.myTowerSpr.getX(), components.myTowerSpr.getHeight() + 10, components.myTowerSpr.getWidth(), 5);
        components.batch.draw(components.greenHealthSpr, components.myTowerSpr.getX(), components.myTowerSpr.getHeight() + 10, components.myTowerSpr.getWidth() * myHealth, 5);

        components.batch.draw(components.redHealthSpr, components.opponentTowerSpr.getX(), components.opponentTowerSpr.getY() - 10, components.opponentTowerSpr.getWidth(), 5);
        components.batch.draw(components.greenHealthSpr, components.opponentTowerSpr.getX(), components.opponentTowerSpr.getY() - 10,
                components.opponentTowerSpr.getWidth() * opponentHealth, 5);

        components.batch.end();
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
        SocketController.trySend("stopSpectating", gameId);
    }

    @Override
    public void dispose() {

    }

    public static class SpectateCommandController extends CommandController {

        private void arrowPosition(Map<Integer, Arrow> map, float x, float y, float xDes, float yDes, int arrowId) {
            Arrow arrow = map.computeIfAbsent(arrowId, id->new Arrow(x, y, xDes, yDes));
            arrow.setX(x);
            arrow.setY(y);
        }

        @SocketCommand("specArrow_self")
        public void arrowPosition_self(float x, float y, float xDes, float yDes, int arrowId) {
            arrowPosition(getInstance().selfArrows, x, y, xDes, yDes, arrowId);
        }
        @SocketCommand("specArrow_enemy")
        public void arrowPosition_enemy(float x, float y, float xDes, float yDes, int arrowId) {
            arrowPosition(getInstance().enemyArrows, x, y, xDes, yDes, arrowId);
        }

        private void newBuilding(HashMap<Integer, BuildingEntity> map, float x, float y, int id, int type) {
            BuildingEntity building = BuildingEntity.Factory.make(type);
            //building.id = id;
            building.setCoordinate((int) x, (int) y);
            map.put(id, building);
        }

        @SocketCommand("specBuilding_self")
        public void newBuilding_self(float x, float y, int id, int type) {
            newBuilding(getInstance().selfBuildings, x, y, id, type);
        }
        @SocketCommand("specBuilding_enemy")
        public void newBuilding_enemy(float x, float y, int id, int type) {
            newBuilding(getInstance().enemyBuildings, x, y, id, type);
        }

        @SocketCommand("specRemoveArrow_self")
        public void removeArrow_self(int id) {
            getInstance().selfArrows.remove(id);
        }

        @SocketCommand("specRemoveArrow_enemy")
        public void removeArrow_enemy(int id) {
            getInstance().enemyArrows.remove(id);
        }

        @SocketCommand("specDestroyBuilding_self")
        public void destroyEnemyBuilding(int id) {
            getInstance().enemyBuildings.get(id).destroy();
        }

        @SocketCommand("specDestroyBuilding_enemy")
        public void destroySelfBuilding(int id) {
            getInstance().selfBuildings.get(id).destroy();
        }

        @SocketCommand("specDamageTower_self")
        public void damageEnemyTower() {
            getInstance().opponentHealth -= 0.5;
        }

        @SocketCommand("specDamageTower_enemy")
        public void damageSelfTower() {
            getInstance().myHealth -= 0.5;
        }

        @SocketCommand
        public void specGameOver(String winnerName) {
            getInstance().gameOverText = Threading.supplyFromGlThread(()->
                    new GlyphLayout(getInstance().components.font, " Game Over! \n" + winnerName + " Won!"));
            getInstance().gameOver = true;
        }

    }
}

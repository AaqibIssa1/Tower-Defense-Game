package edu.coms.sr2.game.GameObjects.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.io.IOException;
import java.util.Objects;

import edu.coms.sr2.game.GameObjects.Builds.BuildingEntity;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.objects.gdx.GdxContextObject;
import edu.coms.sr2.game.sockets.SocketController;
import edu.coms.sr2.game.sockets.controllers.CommandController;
import edu.coms.sr2.game.utils.HelperFunctions;

public class Arrow extends Projectile {
    public static final GdxContextObject<Sprite> arrowSprite = new GdxContextObject<>(()-> Threading.supplyFromGlThread(
            ()->new Sprite(new Texture(Gdx.files.internal("realArrow.png")))));
    public static int width = 50;
    private int drawWidth = 100;
    public static int height = 150;
    private static int idGen = 0;
    private int id = idGen++;

    /**
     * Arrow constructor, makes the Arrow object
     * @param x
     * @param y
     */
    public Arrow(float x, float y, float xdest, float ydest) {
        super(x, y, xdest, ydest);
        sprite = arrowSprite.get();
    }

    /**
     * Updates the arrow position
     * @param delta
     */
    @Override
    public void update (float delta){
        super.update(delta);
    }

    public BuildingEntity buildingHitDetection(Iterable<BuildingEntity> buildings)  {
        for(BuildingEntity b : buildings)
            if(b != null && b.getCollision() && HelperFunctions.rectInRect(
                        b.getCoordinate().x - b.getWidth() - 20, b.getCoordinate().y - b.getHeight(), b.getWidth(), b.getHeight(),
                        getX() + width/2, getY(), width, height))
                return b;

        return null;
    }

    public boolean towerHitDetection(Rectangle opponentTowerHitbox) {
        return HelperFunctions.rectInRect(
                getX(), getY(), width, height,
                opponentTowerHitbox.x, opponentTowerHitbox.y, opponentTowerHitbox.width, opponentTowerHitbox.height);
    }

    public void sendArrowData () {
        try {
            SocketController.send("arrow", getX(), getY(), xDestination, yDestination, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renders the arrow
     * @param batch
     */
    public void draw(SpriteBatch batch){
        batch.draw(sprite, x, y, drawWidth, height);
    }

    public void draw(SpriteBatch batch, boolean upsideDown) {
        batch.draw(sprite, x, y,
                0, 0, //origin
                100, 150, //size
                1, 1, //scale
                upsideDown? 180 : 0 //rotation
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id, CommandController.EntityIdSetterKey key) {
        Objects.requireNonNull(key);
        this.id = id;
    }

}

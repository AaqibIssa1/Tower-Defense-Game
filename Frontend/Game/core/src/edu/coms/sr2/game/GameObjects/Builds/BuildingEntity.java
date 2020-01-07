package edu.coms.sr2.game.GameObjects.Builds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Objects;

import edu.coms.sr2.game.GameObjects.Entity;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.objects.gdx.GdxContextObject;
import edu.coms.sr2.game.sockets.controllers.CommandController;

public abstract class BuildingEntity extends Entity {
    public static final GdxContextObject<Sprite> rubbleSprite = new GdxContextObject<>(()-> Threading.supplyFromGlThread(
            ()->new Sprite(new Texture(Gdx.files.internal("rubble.png")))));

    protected int hp;
    protected boolean collision;
    protected Sprite sprite;
    private static int idGen = 0;
    private int id = idGen++;

    public static final int DEFAULT_HP = 1000;
    public static final boolean DEFAULT_COLLISION = true;

    public static class Factory {
        public static BuildingEntity make(int type) {
            switch(type) {
                case WallEntity.typeNum:
                    return new WallEntity();
                case FarmEntity.typeNum:
                    return new FarmEntity();
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
        }
    }

    public BuildingEntity(Sprite sprite, Vector2 coordinate, int width, int height, int hp, boolean collision, Sprite sprite1) {
        super(sprite, coordinate, width, height);
        this.hp = hp;
        this.collision = collision;
        this.sprite = sprite1;
    }

    public BuildingEntity(Sprite sprite, int hp, boolean collision) {
        super(sprite);
        this.hp = hp;
        this.collision = collision;
        this.sprite = sprite;
    }

    public BuildingEntity(Sprite sprite) {
        this(sprite, DEFAULT_HP, DEFAULT_COLLISION);
    }

    public int getHp(){
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean getCollision(){
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public void destroy() {
        setCollision(false);
        setSprite(rubbleSprite.get());
    }

    public void draw(SpriteBatch sb) {
        draw(sb, false);
    }

    public void draw(SpriteBatch sb, boolean rotate) {
        sb.draw(getSprite(),
                getCoordinate().x,
                getCoordinate().y,
                0,0,
                getWidth(), getHeight(),
                1, 1, rotate? 180 : 0);

    }

    public abstract int getType();

    public abstract int getCost();

    public int getId() {
        return id;
    }

    public void setId(int id, CommandController.EntityIdSetterKey key) {
        Objects.requireNonNull(key);
        this.id = id;
    }
}

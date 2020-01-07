package edu.coms.sr2.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

    protected Sprite sprite;
    protected Vector2 coordinate;
    protected int width, height;

    public static final Vector2 DEFAULT_COORDINATE = new Vector2(0, 0);
    public static final int DEFAULT_WIDTH = 100;
    public static final int DEFAULT_HEIGHT = 100;

    public Entity(Sprite sprite, Vector2 coordinate, int width, int height) {
        this.sprite = sprite;
        this.coordinate = coordinate;
        this.width = width;
        this.height = height;
    }

    public Entity(Sprite sprite) {
        this(sprite, DEFAULT_COORDINATE.cpy(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Vector2 getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int x, int y) {
        coordinate.x = x;
        coordinate.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setCoordinate(Vector2 coordinate) {
        this.coordinate = coordinate;
    }
}

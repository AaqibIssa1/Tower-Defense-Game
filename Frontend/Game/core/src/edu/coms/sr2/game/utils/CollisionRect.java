package edu.coms.sr2.game.utils;

public class CollisionRect {

    float x, y;
    int width, height;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Constuctor of the collision rectangle
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public CollisionRect (float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Takes care of movement
     * @param x
     * @param y
     */
    public void move (float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * Checks to see if anything collides with something else
     * @param rect
     * @return
     */
    public boolean collidesWith (CollisionRect rect){
        return x < rect.x + rect.width && y < rect.y + rect.height
                && x + width > rect.x && y + height > rect.y;
    }

    public boolean collidesAtPoint(int _x, int _y) {
        return _x < x + width && _x > x && _y < y + height && _y < y;
    }

}
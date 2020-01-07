package edu.coms.sr2.game.GameObjects.projectiles;

import com.badlogic.gdx.graphics.g2d.Sprite;

import static edu.coms.sr2.game.utils.FloatUtils.areEqual;

public class Projectile {

    protected int speed = 325;
    protected float x, y;
    protected float xDestination, yDestination;
    protected Sprite sprite;

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Projectile(float x, float y, float xdest, float ydest){
        this.x = x;
        this.y = y;
        this.xDestination = xdest;
        this.yDestination = ydest;
    }


    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

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

    public float getxDestination() {
        return xDestination;
    }

    public void setxDestination(float xDestination) {
        this.xDestination = xDestination;
    }

    public float getyDestination() {
        return yDestination;
    }

    public void setyDestination(float yDestination) {
        this.yDestination = yDestination;
    }

    public boolean isAtDesination() {
        return areEqual(x, xDestination) && areEqual(y, yDestination);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }


    float minMagnitude(float x, float y) {
        return Math.abs(x) <= Math.abs(y)? x: y;
    }

    /**
     * Updates the position of the object
     * @param delta
     */
    public void update (float delta){
        float pathX = xDestination - x;
        float pathY = yDestination - y;

        float distance = (float) Math.sqrt(pathX * pathX + pathY * pathY);
        float directionX = pathX / distance;
        float directionY = pathY / distance;

        x += minMagnitude(directionX * speed * delta, pathX);
        y += minMagnitude(directionY * speed * delta, pathY);
    }

}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import java.util.ArrayList;
import java.util.Random;

public class Solid {
    public int xPos;
    public int yPos;


    public Sprite sprite;
    public float spriteWidth;
    public float spriteHeight;

    public float xScale = 1;
    public float yScale = 1;

    private Random random;

    private boolean canDestroy = false;

    public void create(int x, int y, Sprite spr, float xScale, float yScale) {
        xPos = x;
        yPos = y;

        sprite = spr;

        this.xScale = xScale;
        this.yScale = yScale;

        spriteWidth = sprite.getWidth();
        spriteHeight = sprite.getHeight();

    }

    public void render(SpriteBatch batch) {
        batch.draw(sprite, xPos, yPos, spriteWidth*xScale, spriteHeight*yScale);
    }
}

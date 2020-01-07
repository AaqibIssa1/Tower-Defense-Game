package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class BounceObject {

    private float xPos;
    private float yPos;

    private Sprite sprite;
    private int spriteWidth;
    private int spriteHeight;

    private float vspd = 0;
    private float hspd = 0;

    private int speed;

    public void create(int x, int y, Sprite spr, int setSpeed) {
        xPos = x;
        yPos = y;

        sprite = spr;

        speed = setSpeed;

        spriteWidth = spr.getTexture().getWidth();
        spriteHeight = spr.getTexture().getHeight();
    }

    public void update(float worldWidth, float worldHeight, ArrayList<Solid> solidArrayList) {

        hspd = Gdx.input.getAccelerometerY() * speed;
        vspd = -Gdx.input.getAccelerometerX() * speed;

        if(hspd < 1 && hspd > -1) {
            hspd = 0;
        }
        if(vspd < 1 && vspd > -1) {
            vspd = 0;
        }

        //Out of Bounds Detection
        if(xPos < 0 - spriteWidth/2) {
            xPos = (int)worldWidth - (int)spriteWidth/2;
        } else if(xPos + spriteWidth/2 + hspd > worldWidth) {
            xPos = 0 - (int)spriteWidth/2;
        }

        if(yPos + vspd < 0 - spriteHeight/2) {
            yPos = (int)worldHeight - (int)spriteHeight/2;
        } else if (yPos + spriteHeight/2 + vspd > worldHeight) {
            yPos = 0 - (int)spriteHeight/2;
        }

        //Collision Detection against Solids
        if(PlaceMeeting(xPos + hspd, yPos, spriteWidth, spriteHeight, solidArrayList)) {
            while(!PlaceMeeting(xPos + Sign(hspd), yPos, spriteWidth, spriteHeight, solidArrayList)) {
                xPos += Sign(hspd);
            }
            hspd = 0;
        }
        xPos += hspd;

        if(PlaceMeeting(xPos, yPos + vspd, spriteWidth, spriteHeight, solidArrayList)) {
            while(!PlaceMeeting(xPos, yPos + Sign(vspd), spriteWidth, spriteHeight, solidArrayList)) {
                yPos += Sign(vspd);
            }
            vspd = 0;
        }
        yPos += vspd;
    }

    public void render(SpriteBatch batch) {
        batch.draw(sprite, xPos, yPos);
    }

    public float Sign(float x) {
        if(x > 0) {
            return 1;
        } else if(x < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    private boolean PointInBox(float xPos1, float yPos1, float xPos2, float yPos2, float width2, float height2) {

        if( xPos1 < xPos2 + width2 &&
            xPos1 > xPos2 &&
            yPos1 < yPos2 + height2 &&
            yPos1 > yPos2)
        {
            return true;
        }
        return false;
    }

    public boolean PlaceMeeting(float x, float y, float width, float height, ArrayList<Solid> solidArray) {
        int counter = 0;

        for(counter = 0; counter < solidArray.size(); counter++) {
            if(PointInBox(x, y, solidArray.get(counter).xPos, solidArray.get(counter).yPos, solidArray.get(counter).spriteWidth*solidArray.get(counter).xScale, solidArray.get(counter).spriteHeight*solidArray.get(counter).yScale)) {
                return true;
            }
            if(PointInBox(x + width, y + height, solidArray.get(counter).xPos, solidArray.get(counter).yPos, solidArray.get(counter).spriteWidth*solidArray.get(counter).xScale, solidArray.get(counter).spriteHeight*solidArray.get(counter).yScale)) {
                return true;
            }
            if(PointInBox(x, y + height, solidArray.get(counter).xPos, solidArray.get(counter).yPos, solidArray.get(counter).spriteWidth*solidArray.get(counter).xScale, solidArray.get(counter).spriteHeight*solidArray.get(counter).yScale)) {
                return true;
            }
            if(PointInBox(x + width, y, solidArray.get(counter).xPos, solidArray.get(counter).yPos, solidArray.get(counter).spriteWidth*solidArray.get(counter).xScale, solidArray.get(counter).spriteHeight*solidArray.get(counter).yScale)) {
                return true;
            }
        }

        return false;
    }
}

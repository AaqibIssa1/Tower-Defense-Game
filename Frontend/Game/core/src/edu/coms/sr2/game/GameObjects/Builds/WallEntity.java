package edu.coms.sr2.game.GameObjects.Builds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.objects.gdx.GdxContextObject;

public class WallEntity extends BuildingEntity {
    public static final int typeNum = 0;
    public static final int cost = 10;

    public static final GdxContextObject<Sprite> wallSprite = new GdxContextObject<>(()-> Threading.supplyFromGlThread(
            ()->new Sprite(new Texture(Gdx.files.internal("wall.png")))));

    public WallEntity() {
        super(wallSprite.get());
        hp = 1000;
        width = 200;
        height = 50;
    }

    @Override
    public int getType() {
        return typeNum;
    }

    @Override
    public int getCost() { return cost;}
}

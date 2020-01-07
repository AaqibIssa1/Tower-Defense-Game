package edu.coms.sr2.game.GameObjects.Builds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.objects.gdx.GdxContextObject;

public class FarmEntity extends BuildingEntity {
    public static final int typeNum = 1;
    public static final int cost = 5;

    public static final GdxContextObject<Sprite> farmSprite = new GdxContextObject<>(()-> Threading.supplyFromGlThread(
            ()->new Sprite(new Texture(Gdx.files.internal("farm.png")))));

    public FarmEntity() {
        super(farmSprite.get());
        hp = 1000;
        width = 50;
        height = 50;
        //sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    @Override
    public int getType() {
        return typeNum;
    }

    @Override
    public int getCost() { return cost;}
}

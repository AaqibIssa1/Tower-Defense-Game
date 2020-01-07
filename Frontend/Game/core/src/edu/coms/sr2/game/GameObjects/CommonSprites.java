package edu.coms.sr2.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.objects.gdx.GdxContextObject;

public class CommonSprites {
    public static final GdxContextObject<Texture> blackSquareTexture = new GdxContextObject<>(()->
            Threading.supplyFromGlThread(
                    ()->new Texture("blacksqaure.jpg")));
    public static final GdxContextObject<Sprite> blackSquareSprite = new GdxContextObject<>(()->
            Threading.supplyFromGlThread(
                    ()->new Sprite(blackSquareTexture.get())));

    public static final GdxContextObject<Sprite> backgroundSprite = new GdxContextObject<>(()->
            Threading.supplyFromGlThread(()->new Sprite(new Texture("background.png"))));

}

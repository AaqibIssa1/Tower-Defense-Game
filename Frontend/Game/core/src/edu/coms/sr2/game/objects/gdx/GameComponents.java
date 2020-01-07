package edu.coms.sr2.game.objects.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import edu.coms.sr2.game.GameObjects.BuildingMenu;
import edu.coms.sr2.game.GameObjects.CommonSprites;
import edu.coms.sr2.game.screens.game.GameScreen;

public class GameComponents {

    public final Vector2 screenSize = new Vector2(1080, 1920);

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public BitmapFont font;

    public BuildingMenu menu = new BuildingMenu();

    public Sprite greenHealthSpr;
    public Sprite redHealthSpr;
    public Sprite lightSpr;
    public Sprite myTowerSpr;
    public Sprite opponentTowerSpr;
    public Sprite arrowSpr;
    public Sprite blackSquareSpr;
    public Sprite farmSpr;
    public Sprite backgroundSpr;

    public Texture myTowerTex;
    public Texture opponentTowerTex;
    public Texture greenHealthTex;
    public Texture redHealthTex;
    public Texture lightTex;
    public Texture arrowTex;
    //public Texture blackSquareTex;
    public Texture farmTex;
    public Texture backgroundTex;

    public Rectangle opponentTowerHitbox;
    public Rectangle myTowerHitbox;

    public static final String TAG = GameScreen.class.getSimpleName();

    public GameComponents() {

        batch = new SpriteBatch();

        font = new BitmapFont(Gdx.files.internal("myfont.fnt"));
        font.getData().setScale(4f);

        loadTextures();
        initializeSprites();

        myTowerSpr.setSize(175, 300);
        myTowerSpr.setPosition(screenSize.x / 2 - myTowerSpr.getWidth() / 2, 0);

        opponentTowerSpr.setSize(175, 300);
        opponentTowerSpr.setPosition(screenSize.x / 2 - opponentTowerSpr.getWidth() / 2, screenSize.y / 2 + screenSize.y / 3);

        opponentTowerHitbox = new Rectangle(opponentTowerSpr.getX(), opponentTowerSpr.getY(),
                (int) opponentTowerSpr.getWidth(), (int) opponentTowerSpr.getHeight());

        myTowerHitbox = new Rectangle(myTowerSpr.getX(), myTowerSpr.getY(),
                (int) myTowerSpr.getWidth(), (int) myTowerSpr.getHeight());

        camera = new OrthographicCamera(screenSize.x, screenSize.y);

        camera.setToOrtho(false, screenSize.x, screenSize.y);
        batch.setProjectionMatrix(camera.combined);
    }

    public void loadTextures() {
        myTowerTex = new Texture(Gdx.files.internal("tower1.png"));
        opponentTowerTex = new Texture(Gdx.files.internal("tower1.png"));
        greenHealthTex = new Texture(Gdx.files.internal("greenHealth.jpg"));
        redHealthTex = new Texture(Gdx.files.internal("LightRed.jpg"));
        lightTex = new Texture(Gdx.files.internal("light.png"));
        arrowTex = new Texture(Gdx.files.internal("realArrow.png"));
        farmTex = new Texture(Gdx.files.internal("farm_2.png"));
        backgroundTex = new Texture(Gdx.files.internal("Background.png"));
    }

    public void initializeSprites() {
        myTowerSpr = new Sprite(myTowerTex);
        opponentTowerSpr = new Sprite(opponentTowerTex);
        greenHealthSpr = new Sprite(greenHealthTex);
        redHealthSpr = new Sprite(redHealthTex);
        lightSpr = new Sprite(lightTex);
        arrowSpr = new Sprite(arrowTex);
        //blackSquareSpr = new Sprite(blackSquareTex);
        farmSpr = new Sprite(farmTex);
        backgroundSpr = new Sprite(backgroundTex);
    }

}

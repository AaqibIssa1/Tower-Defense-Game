package com.mygdx.game;



import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import io.socket.client.IO;
import io.socket.client.Socket;
import sun.security.util.Debug;

import java.net.URISyntaxException;
import java.util.ArrayList;

//import sun.security.ssl.Debug;import sun.security.ssl.Debug;

public class MyGdxGame extends ApplicationAdapter{

	private Socket soc;
	{
		try{
			soc = IO.socket("http://coms-309-sr-2.misc.iastate.edu:8080/profiles");
		}catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
	}

	//Vars
	public SpriteBatch batch;

	public Texture texPlayer;
	public Texture blackBackground_1920x1080;
	public Texture texEnemy;

	BitmapFont font;

	public Sprite sprite;
	public Sprite sprPlayer;
	public Sprite sprEnemy;

	public OrthographicCamera camera;
	public FitViewport viewport;

	public ArrayList<Solid> solidArrayList = new ArrayList<Solid>();

	public int worldWith = 1920;
	public int worldHeight = 1080;

	public static String text = "O";

	/*
	//Objects
	public BounceObject obj1 = new BounceObject();
	public Solid solid01 = new Solid();
	public Solid solid02 = new Solid();
	public Solid solid03 = new Solid();
	public Solid solid04 = new Solid();

	public Solid solid05 = new Solid();
	public Solid solid06 = new Solid();
	public Solid solid07 = new Solid();
	public Solid solid08 = new Solid();
	*/

	@Override
	public void create () {


		soc.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				soc.emit("foo", "hi");
				soc.disconnect();
			}

		}).on("event", new Emitter.Listener() {

			@Override
			public void call(Object... args) {}

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {}

		}).on("mesg", new Emitter.Listener() {
			@Override
			public void call(Object... objects) {
				JSONObject obj = (JSONObject)objects[0];
				String s = "";
				try {
					s = obj.getString("name");
				} catch (JSONException J) {
					text = "fail";
				}

				text = "SS";
			}
		});

		soc.connect();

		//Game Code:

		font = new BitmapFont();
		font.setUseIntegerPositions(false);
		font.setColor(0f, 0f, 0f, 1.0f);

		camera = new OrthographicCamera();

		viewport = new FitViewport(worldWith, worldHeight, camera);
		viewport.apply();

		camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);

		batch = new SpriteBatch();

		texPlayer = new Texture("Player.png");
		texEnemy = new Texture("Enemy.png");
		blackBackground_1920x1080 = new Texture("BlueBackground_1920x1080.png");

		sprite = new Sprite(blackBackground_1920x1080);
		sprPlayer = new Sprite(texPlayer);
		sprEnemy = new Sprite(texEnemy);

		/*
		obj1.create(400, 400, sprPlayer, 10);

		solid01.create(600, 400, sprEnemy, 1, 4);
		solid02.create(600, 400, sprEnemy, 4, 1);

		solid03.create(800, 600, sprEnemy, 4, 1);
		solid04.create(800, 600, sprEnemy, 1, 4);

		solidArrayList.add(solid01);
		solidArrayList.add(solid02);
		solidArrayList.add(solid03);
		solidArrayList.add(solid04);
	 	*/

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		{
			sprite.draw(batch);

			/*
			for (int i = 0; i < solidArrayList.size(); i++) {
				solidArrayList.get(i).render(batch);
			}

			obj1.render(batch);
		 	*/
			font.draw(batch, text, 100, 100);
		}
		batch.end();

		//obj1.update(worldWith, worldHeight, solidArrayList);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);

		batch.setProjectionMatrix(camera.combined);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		texPlayer.dispose();
		blackBackground_1920x1080.dispose();
		texEnemy.dispose();
	}


}

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.Gdx.*;

public class MyGdxGame extends ApplicationAdapter {
	public static class NickCage {
		Texture texture;
		Vector2 coordinate;

		public NickCage() {
			texture = new Texture("nick_cage.png");
			coordinate = new Vector2(startingPos);
		}
	}

	public static class RainbowGenerator
	{
		public float rate;
		private float[] rgbAngles;

		RainbowGenerator()
		{
			rgbAngles = new float[]{0.f, 120.f, 240.f};
			rate = 1.f;
		}

		public void update()
		{
			for(int i = 0; i < rgbAngles.length; ++i)
			{
				rgbAngles[i] += rate;
				if(rgbAngles[i] > 360.f)
					rgbAngles[i] -= 360.f;
			}
		}

		public float getR() {
			return doSin(rgbAngles[0]);
		}
		public float getG() {
			return doSin(rgbAngles[1]);
		}
		public float getB() {
			return doSin(rgbAngles[2]);
		}

		private float doSin(float degrees) {
			return (float) Math.sin(Math.toRadians(degrees));
		}
	}


	OrthographicCamera camera;
	SpriteBatch batch;

	RainbowGenerator rainbow;
	NickCage nickCage;
	Vector2 lastTouch;

	public static final Vector2 screenSize = new Vector2(1920, 1080);
	public static final Vector2 startingPos = new Vector2(screenSize.x / 2, screenSize.y / 2);
	public static final float restingRainbowRate = 0.5f;
	public static final float maxRainbowRate = 200.f;
	public static final float rainbowAcceleration = 0.1f;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenSize.x, screenSize.y);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		rainbow = new RainbowGenerator();
		nickCage = new NickCage();
		lastTouch = new Vector2(startingPos);
	}

	@Override
	public void render ()
	{
		readTouch(true);
		updateNickCage();

		glClear();
		drawNickCage();
	}

	private void glClear()
	{
		rainbow.update();

		gl.glClearColor(rainbow.getR(), rainbow.getG(), rainbow.getB(), 0.5f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void updateNickCage()
	{
		Vector2 offset = lastTouch.cpy().sub(nickCage.coordinate);
		nickCage.coordinate.mulAdd(offset, 0.1f);
	}

	private void readTouch(boolean doVibrate)
	{
		if(input.isTouched())
		{
			Vector3 lastTouch_3D = new Vector3(input.getX(), input.getY(), 0);
			camera.unproject(lastTouch_3D);

			lastTouch.set(lastTouch_3D.x, lastTouch_3D.y);

			rainbow.rate = Math.min(maxRainbowRate, rainbow.rate + rainbowAcceleration);

			if(doVibrate)
				input.vibrate(100);
		} else {
			input.cancelVibrate();

			rainbow.rate = Math.max(restingRainbowRate, rainbow.rate - rainbowAcceleration);
		}
	}

	private void drawNickCage()
	{
		batch.begin();
		batch.draw(nickCage.texture,
				nickCage.coordinate.x - nickCage.texture.getHeight() / 2,
				nickCage.coordinate.y - nickCage.texture.getWidth() / 2);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		nickCage.texture.dispose();
	}
}

package edu.coms.sr2.backend.utils;

public class ScreenCoordinate {
	private float x = 0;
	private float y = 0;
	private float xMax = 1080;
	private float yMax = 1920;
	
	public ScreenCoordinate() {}
	public ScreenCoordinate(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public ScreenCoordinate(float x, float y, float xMax, float yMax) {
		this(x, y);
		this.xMax = xMax;
		this.yMax = yMax;
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}	
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public float getXmax() { 
		return xMax;
	}
	public float getYMax() {
		return yMax;
	}
	
	public void setXMax(float xMax) {
		this.xMax = xMax;
	}

	public void setYMax(float yMax) {
		this.yMax = yMax;
	}

	public float getXPrime() {
		return xMax - x;
	}
	
	public float getYPrime() {
		return yMax - y;
	}
	
	public ScreenCoordinate getInverse() {
		return new ScreenCoordinate(getXPrime(), getYPrime(), xMax, yMax);
	}
}

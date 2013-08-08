package edu.msu.filajustmanfrommars;

import android.content.Context;

public class Bullet extends Sprite {
	
	/**
	 * x velocity
	 */
	private int mVelX = 0;
	
	/**
	 * y velocity
	 */
	private int mVelY = 0;
	
	/**
	 * constructor
	 * @param image
	 * @param context
	 * @param yOffset
	 * @param xOffset
	 */
	public Bullet(int image, Context context, int yOffset, int xOffset) {
		super(image, context, yOffset, xOffset);
	}

	/**
	 * setter for x velocity
	 * @param velX
	 */
	public void setVelX( int velX ) {
		mVelX = velX;
	}
	
	/**
	 * setter for the y velocity
	 * @param velY
	 */
	public void setVelY( int velY ) {
		mVelY = velY;
	}
	
	public void move() {
		super.setLocation( super.getX() + mVelX, super.getY() + mVelY );
	}
}

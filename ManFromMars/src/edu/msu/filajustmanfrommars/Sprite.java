package edu.msu.filajustmanfrommars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

public class Sprite {
	
	/**
	 * the bitmap that the sprite will hold
	 */
	private Bitmap mBitmap = null;
	
	/**
	 * screen dimensions
	 */
	private Point mScreenDimensions;
	
	/**
	 * offset for y position
	 */
	private int mYOffset = 0;
	
	/**
	 * offset for y position
	 */
	private int mXOffset = 0;
	
	/**
	 * height of image
	 */
	private int mHeight = 0;
	
	/**
	 * width of image
	 */
	private int mWidth = 0;
	
	/**
	 * y location of center
	 */
	private int mY = 0;
	
	/**
	 * x location of center
	 */
	private int mX = 0;
	
	public Sprite(int image, Context context, int yOffset, int xOffset) {
		mBitmap = BitmapFactory.decodeResource(context.getResources(), image);
	    mWidth = mBitmap.getWidth();
	    mHeight = mBitmap.getHeight();
	    mYOffset = yOffset;
	    mXOffset = xOffset;
	}
	
	/**
	 * Draw the puzzle piece
	 * @param canvas Canvas we are drawing on
	 */
	public void draw(Canvas canvas) {
//		canvas.save();
		
		// Convert x,y to pixels and add the margin, then draw
//		canvas.translate(marginX + mWidth * spriteSize, marginY + mHeight * spriteSize);
		
		// Scale it to the right size
//		canvas.scale(scaleFactor, scaleFactor);
		
		// This magic code makes the center of the piece at 0, 0
//		canvas.translate(-mWidth / 2, -mHeight / 2);
		
		// Draw the bitmap
		canvas.drawBitmap(mBitmap, mX + mXOffset, mY + mYOffset, null);
//		canvas.restore();
	}
	
	/**
	 * set the location of the sprite
	 * @param location the location
	 */
	void setLocation(int x, int y) {
		mX = x;
		mY = y;
	}

	public int getX() {
		return mX;
	}
	
	public int getY() {
		return mY;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}
}

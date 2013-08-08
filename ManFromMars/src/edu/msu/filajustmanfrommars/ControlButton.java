package edu.msu.filajustmanfrommars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class ControlButton extends Sprite {
	
	private Bitmap mPressedBitmap = null;
	
	/**
	 * the width and height of the bitmap
	 */
	private int mWidth = 0;
	private int mHeight = 0;
	private int mPressedX = 0;
	private int mPressedY = 0;
	
	/**
	 * boolean that is flagged when the button has been pressed
	 */
	private boolean mPressed = false;

	public ControlButton(int image, int imagePressed, Context context, int yOffset) {
		// note: no x offset needed
		super(image, context, yOffset, 0);
		// TODO Auto-generated constructor stub
		
		mPressedBitmap = BitmapFactory.decodeResource(context.getResources(), imagePressed);
	    mWidth = mPressedBitmap.getWidth();
	    mHeight = mPressedBitmap.getHeight();
	    
	    mPressedX = super.getX();
	    mPressedY = super.getY();
	}
	
	public boolean onTouch(View view) {
		return true;
	}
	
	/**
	 * Draw the puzzle piece
	 * @param canvas Canvas we are drawing on
	 */
	public void draw(Canvas canvas) {
		if (mPressed == true) {
//			canvas.save();
			
			// Convert x,y to pixels and add the margin, then draw
	//		canvas.translate(marginX + mWidth * spriteSize, marginY + mHeight * spriteSize);
			
			// Scale it to the right size
	//		canvas.scale(scaleFactor, scaleFactor);
			
			// This magic code makes the center of the piece at 0, 0
//			canvas.translate(-mWidth / 2, -mHeight / 2);
			
			// Draw the bitmap
			canvas.drawBitmap(mPressedBitmap, mPressedX, mPressedY, null);
//			canvas.restore();
		} else {
			super.draw(canvas);
		}
	}
	
	/**
	 * set the location of the sprite
	 * @param location the location
	 */
	void setLocation(int x, int y) {
		super.setLocation(x, y);
		mPressedX= x;
		mPressedY = y;
	}

	/**
	 * check if the button is hit
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isHit(float x, float y) {
		int xTemp = (int) x;
		int yTemp = (int) y;
		
		if (xTemp >= super.getX() && 
				xTemp <= super.getX() + getWidth() &&
				yTemp >= super.getY() && 
				yTemp <= super.getY() + getHeight() ) {
			return true;
		}
		return false;
	}
	
	/**
	 * set the pressed boolean
	 * @param isPressed
	 */
	public void setPressed(boolean isPressed) {
		mPressed = isPressed;
	}
	
	/**
	 * get pressed
	 */
	public boolean getPressed() {
		return mPressed;
	}
}

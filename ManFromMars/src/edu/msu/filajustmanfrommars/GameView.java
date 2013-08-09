package edu.msu.filajustmanfrommars;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
	
	/**
	 * reference to game
	 */
	private Game mGame = null;
	
	/**
	 * the gesture detector
	 */
	private GestureDetectorCompat mDetector;

	public GameView(Context context) {
		super(context);
		
		init(context);
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context);
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init(context);
	}
	
	/**
	 * initialize variables
	 * @param context
	 */
	private void init(Context context) {		
		mGame = new Game(context);
		mGame.addView(this);
		mDetector = new GestureDetectorCompat(context, new MyGestureListener());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		return mGame.onTouchEvent(this, event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		mGame.draw(canvas, this);
	}
	
	/**
	 * set the screen dimensions
	 * @param dimensions
	 */
	public void setScreenDimensions(Point dimensions) {
		mGame.setScreenDimensions(dimensions);
	}

	/**
	 * initialize the user interface
	 */
	public void initUI() {
		mGame.initUI();
	}

	/**
	 * pause
	 */
	public void onPause() {
		mGame.onPause();
	}

	/**
	 * resume
	 */
	public void onResume() {
		mGame.onResume();
	}

    /**
     * listener for when the user swipes
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, 
                float velocityX, float velocityY) {
  
            if ( velocityY > 0 ) {
            	mGame.crouchPlayer();
            } else {
            	mGame.standPlayer();
            }
            
            return true;
        }
    }
}

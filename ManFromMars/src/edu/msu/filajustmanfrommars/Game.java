package edu.msu.filajustmanfrommars;

import java.util.ArrayList;

import edu.msu.filajustmanfrommars.Player.status;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Game {
	
	/**
	 * number of planets
	 */
	private int mNumPlanets = 1;
	
	/**
	 * list of all the levels
	 */
	private ArrayList<Planet> mPlanets = null;
	
	/**
	 * planet factory
	 */
	private PlanetFactory mFactory = new PlanetFactory();
	
	/**
	 * pointer to the current planet
	 */
	private Planet mCurPlanet = null;
	
	/**
	 * pointer to the player
	 */
	private Player mPlayer = null;
	
	/**
	 * platform that has the player
	 */
	private Platform mCurPlatform = null;
	
	/**
	 * platform that has the player on the side of it
	 */
	private Platform mCurSidePlatform = null;
	
	/**
	 * screen dimensions
	 */
	private Point mScreenDimensions = null;
	
	/**
	 * variables to determine touch
	 */
	private int mTouch1 = 0;
	private int mTouch2 = 0;
	private float mTouch1x = 0;
	private float mTouch1y = 0;
	private float mTouch2x = 0;
	private float mTouch2y = 0;
	
	/**
	 * reference to a button
	 */
	private ControlButton mButtonA = null;
	
	/**
	 * reference to b button
	 */
	private ControlButton mButtonB = null;
	
	/**
	 * reference to right arrow
	 */
	private ControlButton mRightArrow = null;
	
	/**
	 * reference to left arrow
	 */
	private ControlButton mLeftArrow = null;
	
	/**
	 * reference to the top right control
	 */
	private ControlButton mButtonShootingDiagonal = null;
	
	/**
	 * reference to the game view
	 */
	private View mGameView = null;
	
	/**
	 * gameLoop
	 */
	private gameLoop mGameLoop = null;
	
	/**
	 * the physics engine
	 */
	private PhysicsEngine mEngine = null;
	
	/**
	 * Constructor
	 */
	public Game(Context context) {
		mPlanets = new ArrayList<Planet>();
		
		loadNextPlanet(context);
		
		// initialize the player
		mPlayer = new Player(context, this);
		
		// instantiate the physics engine and set the player
		mEngine = new PhysicsEngine();
		mEngine.setPlayer(mPlayer);
			
		// references to the screen controls
		mRightArrow = new ControlButton(R.drawable.right_arrow, 
				R.drawable.right_arrow_pressed, context, 0);
		mLeftArrow = new ControlButton(R.drawable.left_arrow, 
				R.drawable.left_arrow_pressed, context, 0);	
		mButtonA = new ControlButton(R.drawable.button_a,
				R.drawable.button_a_pressed, context, 0);	
		mButtonB = new ControlButton(R.drawable.button_b, 
				R.drawable.button_b_pressed, context, 0);
		mButtonShootingDiagonal = new ControlButton(R.drawable.diagonal_shooting_button_right,
				R.drawable.diagonal_shooting_button_right_pressed, context, 0);
		
		mGameLoop = new gameLoop();
		mGameLoop.start();
	}
	
	/**
	 * set the current planet
	 * @param planet the planet
	 */
	public void setPlanet(int planet) {
		mCurPlanet = mPlanets.get(planet);
	}
	
	/**
	 * add the view as a reference
	 * @param gameView
	 */
	public void addView(GameView gameView) {
		mGameView = gameView;
	}
	
	/**
	 * draws the game
	 * @param canvas
	 */
	public void draw(Canvas canvas, View view) {
		mPlayer.draw(canvas);
		mCurPlanet.draw(canvas);
		drawControls(canvas);
	}

	/**
	 * draw the control buttons
	 * @param canvas
	 */
	private void drawControls(Canvas canvas) {
		mButtonA.draw(canvas);
		mButtonB.draw(canvas);
		mRightArrow.draw(canvas);
		mLeftArrow.draw(canvas);
		mButtonShootingDiagonal.draw(canvas);
	}
	
	/**
	 * set the screen dimensions
	 * @param dimensions
	 */
	public void setScreenDimensions(Point dimensions) {
		mScreenDimensions = dimensions;
		
		int dummy = mScreenDimensions.x;
		mScreenDimensions.x = mScreenDimensions.y;
		mScreenDimensions.y = dummy;
	}
	
	/**
	 * get the screen dimensions
	 * @return the dimensions
	 */
	public Point getScreenDimensions() {
		return mScreenDimensions;
	}

    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        // the locations need to be switched because it is landscape
        float x = event.getX();
        float y = event.getY();
        int id = event.getPointerId(event.getActionIndex());

        boolean result = false;
        
        switch (event.getActionMasked()) {
        
        case MotionEvent.ACTION_DOWN:
        	mTouch1 = id;
        	mTouch2 = -1;
            result = onTouched(event);
            view.invalidate();
            return result;
            
        case MotionEvent.ACTION_POINTER_DOWN:
        	result = onTouched(event);
            view.invalidate();
            return result;
        	
        case MotionEvent.ACTION_POINTER_UP:
        	result = onReleased(event);
            view.invalidate();
            return result;

        case MotionEvent.ACTION_UP:
        	setControlsPressedFalse();
            view.invalidate();
        	return true;
        	
        case MotionEvent.ACTION_CANCEL:
        	setControlsPressedFalse();
        	view.invalidate();
        	return true;

        case MotionEvent.ACTION_MOVE:
            break;
        }
        
        return false;
    }

    /**
     * called when a touch is released
     */
	private boolean onReleased(MotionEvent event) {
		for (int i=0; i < event.getPointerCount(); i++ ) {
			int id = event.getPointerId(i);
			
			// disable the button that was released
			if ( id == event.getPointerId(event.getActionIndex()) ) {
				checkControlsHit(event.getX(i), event.getY(i), false);
				continue;
			}
			
			if ( id == mTouch1 ) {
				mTouch1x = event.getX(i);
				mTouch1y = event.getY(i);
			} else if (id == mTouch2 ) {
				mTouch2x = event.getX(i);
				mTouch2y = event.getY(i);
			}
		}

		return true;
	}

	/** 
	 * called on a touch
	 */
	private boolean onTouched(MotionEvent event) {			
		for (int i=0; i < event.getPointerCount(); i++ ) {
			int id = event.getPointerId(i);
			
			// enabled the button that just pressed
			if ( id == event.getPointerId(event.getActionIndex() ) ) {
				checkControlsHit(event.getX(i), event.getY(i), true);
				continue;
			}
			
			if ( id == mTouch1 ) {
				mTouch1x = event.getX(i);
				mTouch1y = event.getY(i);
			} else if (id == mTouch2 ) {
				mTouch2x = event.getX(i);
				mTouch2y = event.getY(i);
			}
		}

		return true;
	}
	
	/**
	 * checks if the touch hit any of the controls
	 * @param x
	 * @param y
	 */
	private void checkControlsHit(float x, float y, boolean value) {
		if ( mButtonA.isHit(x, y) ) {
//			Log.i("button A", "A");
			mButtonA.setPressed(value);
			// don't call jump if it was on a release
			if ( value ) {
				if ( mPlayer.Jump() );
					setCurSidePlatform( null );
			}
		} else if ( mButtonB.isHit(x, y) ) {
//			Log.i("button B", "B");
			mButtonB.setPressed(value);
			// don't call shoot if it was on a release
			if ( value )
				mPlayer.Shoot( false );
		} else if ( mLeftArrow.isHit(x, y) ) {
//			Log.i("Left Arrow", "Left");
			mLeftArrow.setPressed(value);
			mPlayer.setLeft(value);
		} else if ( mRightArrow.isHit(x, y) ) {
//			Log.i("RightArrow", "Right");
			mRightArrow.setPressed(value);
			mPlayer.setRight(value);
		} else if ( mButtonShootingDiagonal.isHit(x, y) ) {
			mButtonShootingDiagonal.setPressed(value);
			if ( value )
				mPlayer.Shoot( true );
		}
	}
	
	/**
	 * sets all of the control buttons to not pressed
	 */
	private void setControlsPressedFalse() {
		mButtonA.setPressed(false);
		mButtonB.setPressed(false);
		mLeftArrow.setPressed(false);
		mRightArrow.setPressed(false);
		mButtonShootingDiagonal.setPressed(false);
		
		mPlayer.setLeft(false);
		mPlayer.setRight(false);
	}
	
	/**
	 * set the side platform
	 * @param curSidePlatform
	 */
	public void setCurSidePlatform( Platform curSidePlatform ) {
		if ( curSidePlatform == null ) {
			mPlayer.setDisableLeft( false );
			mPlayer.setDisableRight( false );
		}
		mCurSidePlatform = curSidePlatform;
	}
	
	/**
	 * load next Planet
	 */
	public void loadNextPlanet(Context context) {
		mCurPlanet = new Planet();
		
		mFactory.buildNextPlanet(mCurPlanet, context);
	}

	/**
	 * initialize the user interface
	 */
	public void initUI() {
		mLeftArrow.setLocation(50, mScreenDimensions.y - 280);
		mRightArrow.setLocation(320, mScreenDimensions.y - 280);
		mButtonA.setLocation(mScreenDimensions.x - 180, 
				mScreenDimensions.y - 280);
		mButtonB.setLocation(mScreenDimensions.x - 380, 
				mScreenDimensions.y - 280);
		mButtonShootingDiagonal.setLocation(mScreenDimensions.x - 180,
				mScreenDimensions.y - 460);

	}

	/**
	 * the game loop
	 * @author justinfila
	 */
	private class gameLoop implements Runnable {
		
		//TODO make the thread pause when the game pauses or home is pressed
		private final static int NUM_TICKS = 40;
		
		/**
		 * nanoseconds per tick
		 */
		final static long NS_PER_TICK = 1000000000 / NUM_TICKS;
		
		/**
		 * is game running
		 */
		private boolean running = false;
		
		/**
		 * calculates time passed so thread can catch itself up
		 */
		double deltaTicks = 0;
		
		/**
		 * thread
		 */
		private Thread thread = new Thread();
		
		@Override
		public void run() {
			long lastTime = System.nanoTime();
			int updates = 0;
			int frames = 0;
			long timer = System.currentTimeMillis();
			
			// the actual game loop
			while (running) {
				// current time
				long now = System.nanoTime();
				// number of ticks since last time
				deltaTicks = (now - lastTime) / ((double)NS_PER_TICK);
				lastTime = now;
				
				tick(lastTime);
				updates++;
//				Log.i("Ticks: ", Integer.toString(updates) );
				update(frames, deltaTicks);
				render();
				frames++;
				
				if ( System.currentTimeMillis() - timer > 1000 ) {
					timer += 1000;
//					Log.i("Ticks: ", Integer.toString(updates) );
//					Log.i("FPS: ", Integer.toString(frames) );
					updates = 0;
					frames = 0;
				}
			}
			stop();
		}
		
		private void tick(double lastTime) {
		      // we've recorded when we started the frame. We add the lenth of each frame
		      // to this and then factor in the current time to give 
		      // us our final value to wait for
		      // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
			long sleepTime = (long)((lastTime-System.nanoTime() + NS_PER_TICK)/1000000);
			if ( sleepTime > 0 ){
				try{
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// nothing yet
					Log.i("exception","thread can't sleep");
				}
			}
		}
		
		private void render() {
			mGameView.postInvalidate();
		}
		
		private void update(int frame, double delta) {
			mPlayer.setSprite();
			
			// check if the player is still next to a platform
			if ( mCurSidePlatform != null ) {
				if ( mCurSidePlatform.hasPlayerInsideY( mPlayer ) == false ) {
					setCurSidePlatform(null);
				} else {
					mCurSidePlatform.checkIfShouldHang( mPlayer );
				}
			}
			
			mEngine.movePlayer( delta, Game.this );
			
			checkEnvironment();
		}
		
		/**
		 * check if any walls were hit
		 */
		private void checkEnvironment() {
			// check y
			// TODO: fix hard coding
			if ( mPlayer.getY() > mScreenDimensions.y - mPlayer.getHeight() - 30) {
				mPlayer.setStatus( status.GROUND );
				mPlayer.setVelocityY(0);
				mPlayer.setLocation( mPlayer.getX(), mScreenDimensions.y - mPlayer.getHeight() - 30, false, false );
			} 
			
			// check x
			// TODO: better boundaries
			if ( mPlayer.getX() > mScreenDimensions.x - 50) {
				mPlayer.setLocation( mScreenDimensions.x - 50, mPlayer.getY(), false, false );
				
				// since manually changing player's position, we don't want the actual last
				// location saved
				mPlayer.setLastX( mPlayer.getX() );
				mPlayer.setVelocityX( 0 );
						
				mPlayer.setDisableRight( true );  // don't allow the player to move left
			} else if ( mPlayer.getX() < 0 ) {
				mPlayer.setLocation( 0, mPlayer.getY(), false, false );
				// since manually changing player's position, we don't want the actual last
				// location saved
				mPlayer.setLastX( mPlayer.getX() );
				mPlayer.setVelocityX( 0 );
				
				mPlayer.setDisableLeft( true ); // don't allow the player to move right
			}
			
			// check x for bullets
			// TODO: better boundaries
			ArrayList<Sprite> bullets = mPlayer.getBullets();
			ArrayList<Sprite> bulletsToRemove = new ArrayList<Sprite>();
			for ( Sprite bullet : bullets ) {
				if ( bullet.getX() > mScreenDimensions.x - 50) {
					bulletsToRemove.add( bullet );
				} else if ( bullet.getX() < 0 ) {
					bulletsToRemove.add( bullet );					
				}
			}
			// now delete all the ones that were flagged
			for ( Sprite bullet : bulletsToRemove ) {
				mPlayer.deleteBullet( bullet );
			}
			
			// check if the player is still on the current platform
			if ( mCurPlatform != null ) {
				if ( mCurPlatform.hasPlayerInsideX( mPlayer ) == false ) {
					mPlayer.setStatus( Player.status.AIR );
					mCurPlatform = null;
					// return since there is no need to check the others
					return;
				}
			}
			
			// check all the other platforms
			Platform platform = mCurPlanet.checkPlatforms( mPlayer, bullets, Game.this );
			if ( platform != null ) {
				mCurPlatform = platform;
			}
		}
		
		/**
		 * starts the thread
		 */
		private synchronized void start() {
			if (running)
				return;
			
			running = true;
			thread = new Thread(this);
			thread.start();
		}
		
		/**
		 * stops the thread
		 */
		private synchronized void stop() {
			// only do this if the thread isn't running
			if (!running)
				return;
			
			try {
				// ends the thread
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Log.i("Exiting", "Exiting");
			System.exit(1);
		}
	}

	/**
	 * pause
	 */
	public void onPause() {
		mGameLoop.stop();
	}

	/**
	 * resume
	 */
	public void onResume() {
		mGameLoop.start();
	}

	public void crouchPlayer() {
		mPlayer.crouch();
	}
	
	public void standPlayer() {
		mPlayer.stand();
	}
}

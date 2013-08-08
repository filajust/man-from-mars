package edu.msu.filajustmanfrommars;

import edu.msu.filajustmanfrommars.Player.status;
import android.util.Log;

public class PhysicsEngine {
	
	/**
	 * the amount of acceleration on running
	 */
	final static double RUNNING_ACCEL_FACTOR = 2.0;
	
	/**
	 * the max velocity of running
	 */
	final static double MAX_VELX = 11.0;
	
	/**
	 * the max velocity of running
	 */
	final static double MAX_VELY = 25.0;
	
	/**
	 * gravity
	 */
	final static double GRAVITY = 1.0;
	
	/**
	 * reference to the main player
	 */
	Player mPlayer = null;
	
	/**
	 * gravity
	 */
	float mGravity = 0;
	

	public PhysicsEngine() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * add a player to the engine
	 * @param player
	 */
	public void setPlayer(Player player) {
		mPlayer = player;
	}
	
	/**
	 * add an enemy to the engine
	 * @param enemy
	 */
//	public void addEnemy(Enemy enemy) {
//		//TODO EnemyList.add(enemy);
//	}
	
	/**
	 * setter for the gravity
	 * @param gravity
	 */
	public void setGravity(float gravity) {
		mGravity = gravity;
	}

	public void movePlayer(double delta, Game game) {
		// apply left movement
		if ( mPlayer.getLeft() == true && mPlayer.getDisableLeft() == false ) {
			// TODO: Make delay if crouched
			if ( mPlayer.getStatus() == status.CROUCHING && mPlayer.getLeftLast() == true) {
				mPlayer.setStatus( status.GROUND );
			}
			
			if ( mPlayer.getVelocityX() > -MAX_VELX ) {
				mPlayer.setVelocityX( mPlayer.getVelocityX() - (RUNNING_ACCEL_FACTOR * delta));
			} else {
				mPlayer.setVelocityX(-MAX_VELX);
			}
			// since player just moved left, he can move right again
			mPlayer.setDisableRight( false );
			game.setCurSidePlatform( null );
			if ( mPlayer.getStatus() == status.HANGING ) {
				playerReleasedFromHang();
			} 
		// apply right movement
		} else if ( mPlayer.getRight() == true && mPlayer.getDisableRight() == false ) {
			//TODO: Make delay if crouched
			if ( mPlayer.getStatus() == status.CROUCHING && mPlayer.getLeftLast() == false) {
				mPlayer.setStatus( status.GROUND );
			}
			
			if ( mPlayer.getVelocityX() < MAX_VELX ) {
				mPlayer.setVelocityX( mPlayer.getVelocityX() + (RUNNING_ACCEL_FACTOR * delta));
			} else {
				mPlayer.setVelocityX(MAX_VELX);
			}
			// since player just moved right, he can move left again
			mPlayer.setDisableLeft( false );
			game.setCurSidePlatform( null );
			if ( mPlayer.getStatus() == status.HANGING ) {
				playerReleasedFromHang();
			} else if ( mPlayer.getStatus() == status.CROUCHING && mPlayer.getLeftLast() == false) {
				mPlayer.setStatus( status.GROUND );
			}
		// decrease movement
		} else {
			// decrease left movement
			if ( mPlayer.getVelocityX() < 0 ) {
				mPlayer.setVelocityX( mPlayer.getVelocityX() + (RUNNING_ACCEL_FACTOR * delta));
				
				// if that was the last addition, set it to zero
				if ( mPlayer.getVelocityX() > 0) {
					mPlayer.setVelocityX(0);
				}
			// decrease right movement
			} if ( mPlayer.getVelocityX() > 0 ) {
				mPlayer.setVelocityX( mPlayer.getVelocityX() - (RUNNING_ACCEL_FACTOR * delta));
				
				// if that was the last subtraction, set it to zero
				if ( mPlayer.getVelocityX() < 0 ) {
					mPlayer.setVelocityX(0);
				}
			}
		}
		
		// get the players current status
		status curStatus = mPlayer.getStatus();
		if ( curStatus == status.AIR ) {
			// add the vertical acceleration
			if ( mPlayer.getVelocityY() < MAX_VELY ) {
				mPlayer.setVelocityY( mPlayer.getVelocityY() + (GRAVITY * delta));
			} else {
				mPlayer.setVelocityY(MAX_VELY);
			}
		} else if ( curStatus == status.CLIMBING ) {
			// animate climbing
		} else if ( curStatus == status.HANGING ) {
			mPlayer.setVelocityY(0);
		}
		
		// set the location
		mPlayer.setLocation( (int)((mPlayer.getX() + mPlayer.getVelocityX())),
				(int)(mPlayer.getY() + mPlayer.getVelocityY()), true, true );
	}

	/**
	 * sets the appropriate settings for releasing a hang
	 */
	private void playerReleasedFromHang() {
		mPlayer.setStatus( status.AIR );
		mPlayer.setNumJumps( 1 );
	} 
}

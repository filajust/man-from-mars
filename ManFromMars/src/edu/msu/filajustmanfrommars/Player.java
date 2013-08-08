package edu.msu.filajustmanfrommars;

import java.util.ArrayList;

import edu.msu.filajustmanfrommars.Player.status;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

/**
 * class that describes the main character
 * @author justinfila
 *
 */
public class Player {
	
	public enum status {
		AIR, GROUND, HANGING, CLIMBING, CROUCHING
	}
	
	public status mStatus;

	/**
	 * the number of frames to animate standing
	 */
	private static final int NUM_STANDING_FRAMES = 3;
	
	/**
	 * the number of frames to animate running
	 */
	private static final int NUM_RUNNING_FRAMES = 10;
	
	/**
	 * the number of sprites for crouching
	 */
	private static final int NUM_CROUCHING_SPRITES = 2;
	
	/**
	 * the number of frames to animate jumping
	 */
	private static final int NUM_JUMPING_FRAMES = 3;
	
	/**
	 * the delay that we want for shooting
	 */
	private static final int SHOOTING_DELAY = 13;

	/**
	 * the power of the players jump
	 */
	private static final double JUMP_VELOCITY = 14.0;
	
	/**
	 * the count for which animation to show
	 */
	private int mAnimationIndex = 0;
	
	/**
	 * the index of the running sprite
	 */
	private int mRunningIndex = 0;
	
	/**
	 * right direction
	 */
	private boolean mRight = false;
	
	/**
	 * left direction
	 */
	private boolean mLeft = false;
	
	/**
	 * was player facing left when the control was released?
	 */
	private boolean mLeftLast = false;
	
	/**
	 * disable the right direction
	 */
	private boolean mDisableRight = false;
	
	/**
	 * disable the left direction
	 */
	private boolean mDisableLeft = false;
	
	/**
	 * diagonal shooting
	 */
	private boolean mShootingDiagonal = false;

	/**
	 * List of all the player's standing sprites
	 */
	private ArrayList<Sprite> mStandingSprites = null;

	/**
	 * List of all the player's running sprites
	 */
	private ArrayList<Sprite> mRunningSprites = null;
	
	/**
	 * List of all the player's shooting running sprites
	 */
	private ArrayList<Sprite> mRunningShootingSprites = null;
	
	/**
	 * List of all the player's standing shooting sprites
	 */
	private ArrayList<Sprite> mStandingShootingSprites = null;
	
	/**
	 * List of all the player's jumping sprites
	 */
	private ArrayList<Sprite> mJumpingSpritesLeft = null;
	
	/**
	 * List of all the player's standing shooting sprites
	 */
	private ArrayList<Sprite> mJumpingSpritesRight = null;
	
	/**
	 * List of all the player's hanging sprites (left)
	 */
	private ArrayList<Sprite> mHangingSpritesLeft = null;
	
	/**
	 * List of all the player's hanging sprites (right)
	 */
	private ArrayList<Sprite> mHangingSpritesRight = null;
	
	/**
	 * List of all the player's crouching sprites
	 */
	private ArrayList<Sprite> mCrouchingSprites = null;
	
	/**
	 * List of all the bullets
	 */
	private ArrayList<Sprite> mBullets = null;
	
	/**
	 * pointer to the current sprite
	 */
	private Sprite mCurSprite = null;
	
	/**
	 * the player's health
	 */
	private int mHealth = 100;
	
	/**
	 * the player's lives
	 */
	private int mLives = 5;
	
	/**
	 * the player's gold
	 */
	private int mGold = 0;
	
	/**
	 * the player's x velocity
	 */
	private double mVelocityX = 0;
	
	/**
	 * the player's y velocity
	 */
	private double mVelocityY = 0;
	
	/**
	 * the player's height
	 */
	private int mHeight = 0;
	
	/**
	 * the player's with
	 */
	private int mWidth = 0;
	
	/**
	 * the number of jumps
	 */
	private int mNumJumps = 0;
	
	/**
	 * the shooting time delay (for animation)
	 */
	private int mShootingDelay = 20;
	
	/**
	 * reference to the game
	 */
	private Game mGame = null;
	
	/**
	 * order of standing animation
	 */
	private int mLastStandingAnimation = 0;
	
	/**
	 * the x location
	 */
	private int mX = 0;
	
	/**
	 * the last x location
	 */
	private int mLastX = 0;
	
	/**
	 * the y location
	 */
	private int mY = 0;
	
	/**
	 * the last y location
	 */
	private int mLastY = 0;
	
	/**
	 * the gun y location
	 */
	private int mGunY = 0;
	
	/**
	 * the gun x location
	 */
	private int mGunX = 0;
	
	/**
	 * the context
	 */
	private Context mContext = null;
	
	/**
	 * constructor
	 */
	public Player(Context context, Game game) {
		mGame = game;
		mContext = context;
		
		initializeSprites();
		loadSprites();
		
		mCurSprite = mJumpingSpritesRight.get(0);
		setStatus(status.AIR);
	}
	
	/**
	 * initialize the sprite lists
	 */
	private void initializeSprites() {
		mStandingSprites = new ArrayList<Sprite>();
		mRunningSprites = new ArrayList<Sprite>();
		mRunningShootingSprites = new ArrayList<Sprite>();
		mBullets = new ArrayList<Sprite>();
		mStandingShootingSprites = new ArrayList<Sprite>();
		mJumpingSpritesLeft = new ArrayList<Sprite>();
		mJumpingSpritesRight = new ArrayList<Sprite>();
		mHangingSpritesLeft = new ArrayList<Sprite>();
		mHangingSpritesRight = new ArrayList<Sprite>();
		mCrouchingSprites = new ArrayList<Sprite>();
	}

	/**
	 * load all of the bitmaps for each sprite and add to list
	 */
	private void loadSprites() {
		mX = 100;
		mY = 100;
		Point point = new Point(mX, mY);
		
		// load all of the bitmaps
		loadStandingSprites();
		loadRunningSprites();
		loadJumpingSprites();
		loadHangingSprites();
		loadCrouchingSprites();
		
		// set the height and width of the player
		mHeight = mStandingSprites.get(0).getHeight();
		mWidth = mStandingSprites.get(0).getWidth();
	}
	
	/**
	 * load the running sprites
	 */
	private void loadRunningSprites() {
		Sprite sprite = new Sprite(R.drawable.running_left_shooting_straight_1, mContext, 0, 0);
		mRunningSprites.add(sprite);
		sprite.setLocation(100, 100);
		
		sprite = new Sprite(R.drawable.running_left_shooting_straight_2, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_straight_3, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_straight_4, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_straight_5, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_straight_6, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_straight_7, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_straight_8, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_straight_9, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_straight_10, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		// now add the running sprites that face right
		sprite = new Sprite(R.drawable.running_right_shooting_straight_1, mContext, 0, -10);
		mRunningSprites.add(sprite);
		sprite.setLocation(100, 100);
		
		sprite = new Sprite(R.drawable.running_right_shooting_straight_2, mContext, 0, -7);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_straight_3, mContext, 0, 5);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_straight_4, mContext, 0, 5);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_straight_5, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_straight_6, mContext, 0, -10);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_straight_7, mContext, 0, -7);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_straight_8, mContext, 0, 5);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_straight_9, mContext, 0, 5);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_straight_10, mContext, 0, 0);
		sprite.setLocation(100, 100);
		mRunningSprites.add(sprite);
		
		// shooting diagonal left
		sprite = new Sprite(R.drawable.running_left_shooting_diagonal_1, mContext, -8, 14);
		mRunningShootingSprites.add(sprite);
		sprite.setLocation(100, 100);
		
		sprite = new Sprite(R.drawable.running_left_shooting_diagonal_2, mContext, -8, 15);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_diagonal_3, mContext, -8, 27);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_diagonal_4, mContext, -8, 27);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_diagonal_5, mContext, -8, 24);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_diagonal_6, mContext, -8, 13);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_diagonal_7, mContext, -8, 15);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_diagonal_8, mContext, -8, 28);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_diagonal_9, mContext, -8, 26);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_left_shooting_diagonal_10, mContext, -8, 24);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		// shooting diagonal right
		sprite = new Sprite(R.drawable.running_right_shooting_diagonal_1, mContext, -7, -7);
		mRunningShootingSprites.add(sprite);
		sprite.setLocation(100, 100);
		
		sprite = new Sprite(R.drawable.running_right_shooting_diagonal_2, mContext, -7, -3);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_diagonal_3, mContext, -7, 0);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_diagonal_4, mContext, -7, 0);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_diagonal_5, mContext, -7, 0);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_diagonal_6, mContext, -7, -7);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_diagonal_7, mContext, -7, -3);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_diagonal_8, mContext, -7, 0);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_diagonal_9, mContext, -7, 0);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.running_right_shooting_diagonal_10, mContext, -7, 0);
		sprite.setLocation(100, 100);
		mRunningShootingSprites.add(sprite);
	}

	/**
	 * load the standing sprites
	 */
	private void loadStandingSprites() {
		Sprite sprite = new Sprite(R.drawable.astronaut_standing_left_1, mContext, 0, 0);
		mStandingSprites.add(sprite);
		sprite.setLocation(100, 100);
		// set the current sprite to standing
		mCurSprite = sprite;
		
		sprite = new Sprite(R.drawable.astronaut_standing_left_2, mContext, 4, 0);
		sprite.setLocation(100, 100);
		mStandingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.astronaut_standing_left_3, mContext, 8, 0);
		sprite.setLocation(100, 100);
		mStandingSprites.add(sprite);
		
		// now add the standing sprites that face right
		sprite = new Sprite(R.drawable.astronaut_standing_right_1, mContext, 0, 0);
		mStandingSprites.add(sprite);
		sprite.setLocation(100, 100);
		// set the current sprite to standing
		mCurSprite = sprite;
		
		sprite = new Sprite(R.drawable.astronaut_standing_right_2, mContext, 4, 0);
		sprite.setLocation(100, 100);
		mStandingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.astronaut_standing_right_3, mContext, 8, 0);
		sprite.setLocation(100, 100);
		mStandingSprites.add(sprite);
		
		// now add the shooting sprites
		sprite = new Sprite(R.drawable.standing_shooting_left, mContext, -2, 0);
		sprite.setLocation(100, 100);
		mStandingShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.standing_shooting_right, mContext, -2, 10);
		sprite.setLocation(100, 100);
		mStandingShootingSprites.add(sprite);
		
		// and the diagonal shooting sprites
		sprite = new Sprite(R.drawable.standing_shooting_left_diagonal, mContext, -9, 10);
		sprite.setLocation(100, 100);
		mStandingShootingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.standing_shooting_right_diagonal, mContext, -9, 0);
		sprite.setLocation(100, 100);
		mStandingShootingSprites.add(sprite);
	}
	
	/**
	 * load the jumping sprites
	 */
	private void loadJumpingSprites() {
		Sprite sprite = new Sprite(R.drawable.jumping_left_1, mContext, 0, 25);
		mJumpingSpritesLeft.add(sprite);
		sprite.setLocation(100, 100);
		
		sprite = new Sprite(R.drawable.jumping_left_2, mContext, 4, 15);
		sprite.setLocation(100, 100);
		mJumpingSpritesLeft.add(sprite);
		
		sprite = new Sprite(R.drawable.jumping_left_3, mContext, 8, 15);
		sprite.setLocation(100, 100);
		mJumpingSpritesLeft.add(sprite);
		
		// now add the standing sprites that face right
		sprite = new Sprite(R.drawable.jumping_right_1, mContext, 0, 10);
		mJumpingSpritesRight.add(sprite);
		sprite.setLocation(100, 100);
		
		sprite = new Sprite(R.drawable.jumping_right_2, mContext, 4, 10);
		sprite.setLocation(100, 100);
		mJumpingSpritesRight.add(sprite);
		
		sprite = new Sprite(R.drawable.jumping_right_3, mContext, 8, 10);
		sprite.setLocation(100, 100);
		mJumpingSpritesRight.add(sprite);
		
		// now add the shooting sprites
//		sprite = new Sprite(R.drawable.standing_shooting_left, mContext, -2, 0);
//		sprite.setLocation(100, 100);
//		mStandingShootingSprites.add(sprite);
//		
//		sprite = new Sprite(R.drawable.standing_shooting_right, mContext, -2, 10);
//		sprite.setLocation(100, 100);
//		mStandingShootingSprites.add(sprite);
	}
	
	/**
	 * load hanging sprites
	 */
	private void loadHangingSprites() {
		Sprite sprite = new Sprite(R.drawable.jumping_left_1, mContext, 0, 25);
		mHangingSpritesLeft.add(sprite);
		sprite.setLocation(100, 100);
		
		sprite = new Sprite(R.drawable.jumping_right_1, mContext, 0, 10);
		sprite.setLocation(100, 100);
		mHangingSpritesRight.add(sprite);
	}
	
	/**
	 * load crouching sprites
	 */
	private void loadCrouchingSprites() {
		// add right sprites
		Sprite sprite = new Sprite(R.drawable.crouching_shooting_straight_left, mContext, 30, 0);
		mCrouchingSprites.add(sprite);
		sprite.setLocation(100, 100);
		
		sprite = new Sprite(R.drawable.crouching_shooting_diagonal_left, mContext, 20, 15);
		mCrouchingSprites.add(sprite);
		sprite.setLocation(100, 100);
		
		// now add right sprites
		sprite = new Sprite(R.drawable.crouching_shooting_straight_right, mContext, 30, 0);
		sprite.setLocation(100, 100);
		mCrouchingSprites.add(sprite);
		
		sprite = new Sprite(R.drawable.crouching_shooting_diagonal_right, mContext, 20, -10);
		mCrouchingSprites.add(sprite);
		sprite.setLocation(100, 100);
	}
	
	/**
	 * load the bullet sprites
	 */
	private void addBullet(boolean diagonal) {
		Bullet bullet = null;
		// configure the bullet's location and velocity

		//TODO: Fix hard coding bullet position
		if ( diagonal ) {
			if ( mLeftLast == true ) {
				bullet = new Bullet(R.drawable.bullet_left_diagonal_up, mContext, 0, 0);
				bullet.setVelX( -21 );
				bullet.setLocation( mX + 35, mY );
			} else {
				bullet = new Bullet(R.drawable.bullet_right_diagonal_up, mContext, 0, 0);
				bullet.setVelX( 21 );	
				bullet.setLocation( mX + 38, mY );
			}
			bullet.setVelY( -21 );
		} else {
			if ( mLeftLast == true ) {
				bullet = new Bullet(R.drawable.bullet_left_1, mContext, 0, 0);
				bullet.setVelX( -30 );
				bullet.setLocation( mX + 10, mY + 25 );
			} else {
				bullet = new Bullet(R.drawable.bullet_right_1, mContext, 0, 0);
				bullet.setVelX( 30 );
				bullet.setLocation( mX + 60, mY + 25 );
			}
			bullet.setVelY( 0 );
		}	
		
		mBullets.add(bullet);
	}
	
	/**
	 * load the bullet sprites
	 * @param bullet
	 */
	public void deleteBullet(Sprite bullet) {
		mBullets.remove(bullet);
	}
	
	/**
	 * get the list of bullets
	 * @return mBullets
	 */
	public ArrayList<Sprite> getBullets() {
		return mBullets;
	}
	
	/**
	 * draws the player
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
//		for ( Sprite s : mStandingSprites ) {
//			s.draw(canvas);
//		}
		
		for ( Sprite bullet : mBullets ) {
			( (Bullet)bullet ).move();
			bullet.draw(canvas);
		}
		
		mCurSprite.draw(canvas);
	}
	
	/**
	 * set the location of the player
	 * @param x
	 * @param y
	 * @param setLastX should we set the last position of x here?
	 * @param setLastY should we set the last position of y here?
	 */
	public void setLocation(int x, int y, boolean setLastX, boolean setLastY) {
		// have to set the last x and y location
		if ( setLastX )
			mLastX = mX;
		if ( setLastY )
			mLastY = mY;
		
		mX = x;
		mY = y;
		
		// TODO: make this a better algorithm
//		for (Sprite s : mStandingSprites) {
//			s.setLocation(mX, mY);
//		}
//		for (Sprite s : mRunningSprites) {
//			s.setLocation(mX, mY);
//		}
		
		mCurSprite.setLocation(mX, mY);
		
	}
	
	/**
	 * set the next sprite in the standing series
	 */
	public void nextStandingSprite() {
		mAnimationIndex++;
		
		// only run the animation every 6 frames
		if ( mAnimationIndex % 6 == 0) {
			int index = mStandingSprites.indexOf(mCurSprite);
			// check if the list doesn't have the sprite
			if ( index == -1 ) {
				index = 0;
			} else if ( index - NUM_STANDING_FRAMES >= 0 ) {
				index -= NUM_STANDING_FRAMES;
			}
			
			if (index == 0 && mLastStandingAnimation != 0) {
				mLastStandingAnimation = 0;
				index =  0;
			} else if (index == 0 && mLastStandingAnimation == 0) {
				mLastStandingAnimation = 0;
				index = 1;
			} else if (index == 1 && mLastStandingAnimation == 0) {
				mLastStandingAnimation = 1;
				index = 2;
			} else if (index == 1 && mLastStandingAnimation == 2) {
				mLastStandingAnimation = 1;
				index = 0;
			} else if (index == 2) {
				mLastStandingAnimation = 2;
				index = 1;
			}
		
			// check if the last direction of player moving was left
			if ( mLeftLast == true ) {
				mCurSprite = mStandingSprites.get(index);
			} else {
				mCurSprite = mStandingSprites.get(index + NUM_STANDING_FRAMES);
			}
		
		}
	}
	
	/**
	 * set the next sprite in the crouching series
	 */
	public void nextCrouchingSprite() {
		int index = 0;
		int crouchingFrames = 0;
		
		// decide which sprite to choose
		if ( !mShootingDiagonal ) {
			index = 0;
		} else {
			index = 1;
		}
		
		// check if the last direction of player moving was left
		if ( mLeftLast == true ) {
			mCurSprite = mCrouchingSprites.get( index );
		} else {
			mCurSprite = mCrouchingSprites.get( index + NUM_CROUCHING_SPRITES );
		}
	}
	
	public void nextRunningSprite() {
		mAnimationIndex++;
		
		// only run the animation every 2 frames
		if ( mAnimationIndex % 2 == 0) {
			mRunningIndex++;;
			
			// pick if it is left or right running direction
			if ( mLeft == true ) {
				if ( mRunningIndex == 10 ) {
					mRunningIndex = 0;
				} else if ( mRunningIndex > 9 ) {
					mRunningIndex -= NUM_RUNNING_FRAMES;
				} 
			} else if ( mRight == true ) {
				if ( mRunningIndex == 20 ) {
					mRunningIndex = 10;
				} else if ( mRunningIndex < 10 ) {
					mRunningIndex += NUM_RUNNING_FRAMES;
				}
			}
			
			if ( mShootingDelay < SHOOTING_DELAY ) {
				if ( mShootingDiagonal ) {
					mCurSprite = mRunningShootingSprites.get( mRunningIndex );
				} else {
					mCurSprite = mRunningSprites.get( mRunningIndex );
				}
				mShootingDelay += 1;
			} else {
				mCurSprite = mRunningSprites.get( mRunningIndex );
			}			
		}
	}

	public void nextJumpingSprite() {
		mAnimationIndex++;
		
		// only run the animation every 2 frames
		if ( mAnimationIndex % 3 == 0) {
			
			double direction = getVelocityY();
			int index = 0;
			
			if ( direction < 0 ) {
				if ( mLeftLast == true ) {
					index = mJumpingSpritesLeft.indexOf(mCurSprite);
					if ( index != 0 ) {
						mCurSprite = mJumpingSpritesLeft.get(0);
					}
				} else {
					index = mJumpingSpritesRight.indexOf(mCurSprite);
					if ( index != 0 ) {
						mCurSprite = mJumpingSpritesRight.get(0);
					}
				}
			} else {
				if ( mLeftLast == true ) {
				index = mJumpingSpritesLeft.indexOf(mCurSprite);
					
					if ( index == 0 ) {
						mCurSprite = mJumpingSpritesLeft.get(1);
					} else {
						mCurSprite = mJumpingSpritesLeft.get(2);
					}
				} else {
				index = mJumpingSpritesRight.indexOf(mCurSprite);
					
					if ( index == 0 ) {
						mCurSprite = mJumpingSpritesRight.get(1);
					} else {
						mCurSprite = mJumpingSpritesRight.get(2);
					}
				}
			}
		}
	}
	
	public void nextHangingSprite() {
		if ( mLeftLast == true )
			mCurSprite = mHangingSpritesLeft.get( 0 );
		else
			mCurSprite = mHangingSpritesRight.get( 0 );
	}
	
	/**
	 * get the next sprite when shooting
	 */
	public void nextShootingSprite() {
		if ( mLeft == true ) {
			// do stuff
		} else if ( mRight == true ) {
			// do stuff
		} else {
			if ( mLeftLast == true ) {
				if ( mShootingDiagonal ) {
					mCurSprite = mStandingShootingSprites.get(2);
				} else {
					mCurSprite = mStandingShootingSprites.get(0);
				}
			} else {
				if ( mShootingDiagonal ) {
					mCurSprite = mStandingShootingSprites.get(3);
				} else {
					mCurSprite = mStandingShootingSprites.get(1);
				}
			}
		}
		
		mShootingDelay += 1;
	}
	
	/**
	 * get the x location of the player
	 */
	public int getX() {
		return mX;
	}
	
	/**
	 * get the y location of the player
	 */
	public int getY() {
		return mY;
	}
	
	/**
	 * set the y velocity
	 */
	public void setVelocityY(double velocity) {
		mVelocityY = velocity;
	}
	
	/**
	 * get the y velocity
	 * @return mVelocityY
	 */
	public double getVelocityY() {
		return mVelocityY;
	}
	
	/**
	 * set the x velocity
	 */
	public void setVelocityX(double velocity) {
		mVelocityX = velocity;
//		Log.i("Velocity was set to: ", Double.toString(mVelocityX));
	}
	
	/**
	 * get the x velocity
	 * @return mVelocityX
	 */
	public double getVelocityX() {
		return mVelocityX;
	}

	/**
	 * make player jump
	 */
	public boolean Jump() {
		boolean jumped = false;
		if ( mNumJumps <= 1 ) {
			setVelocityY(-JUMP_VELOCITY);
			mStatus = status.AIR;
			jumped = true;
		}	
		mNumJumps++;
		
		return jumped;
	}

	/**
	 * make player shoot
	 */
	public void Shoot(boolean diagonal) {
		addBullet(diagonal);
		mShootingDiagonal = diagonal;
		
		mShootingDelay = 0;
	}

	/**
	 * tell the player whether to go left
	 * @param left
	 */
	public void setLeft(boolean left) {
		mLeft = left;
		
		if ( left == true ) {
			setLeftLast ( true );
		}
	}

	/**
	 * tell the player whether to go right
	 * @param b
	 */
	public void setRight(boolean right) {
		mRight = right;
		
		if ( right == true ) {
			setLeftLast( false );
		}
	}

	/**
	 * set if left is disabled
	 * @param disableLeft
	 */
	public void setDisableLeft(boolean disableLeft) {
		mDisableLeft = disableLeft;
	}

	/**
	 * set if right is disabled
	 * @param disableRight
	 */
	public void setDisableRight(boolean disableRight) {
		mDisableRight = disableRight;
	}
	
	/**
	 * getter for the right direction
	 * @return mRight
	 */
	public boolean getRight() {
		return mRight;
	}
	
	/**
	 * getter for the left direction
	 * @return mLeft
	 */
	public boolean getLeft() {
		return mLeft;
	}
	
	/**
	 * return the left last variable
	 * @return
	 */
	public boolean getLeftLast() {
		return mLeftLast;
	}

	/**
	 * getter for the disable right
	 * @return mDisableRight
	 */
	public boolean getDisableRight() {
		return mDisableRight;
	}

	/**
	 * getter for the disable left
	 * @return mDisableLeft
	 */
	public boolean getDisableLeft() {
		return mDisableLeft;
	}
	
	/**
	 * get the player's status
	 * @return
	 */
	public status getStatus() {
		return mStatus;
	}

	/**
	 *  set the status of the player
	 * @param ground
	 */
	public void setStatus(status curStatus) {
		mStatus = curStatus;
		
		// if the player is on the ground, he can jump again
		if ( mStatus == status.GROUND ) {
			mNumJumps = 0;
			
			// TODO: use better method --> this sets the standing frame right away
			mAnimationIndex = 5;
		}
	}

	/**
	 * get the player's width
	 * @return the width
	 */
	public int getWidth() {
//		return mCurSprite.getWidth();
		return mWidth;
	}

	/** get the height of the player
	 * @return the height
	 */
	public int getHeight() {
//		return mCurSprite.getHeight();
		return mHeight;
	}

	/**
	 * getter for the last y value
	 * @return mLastY
	 */
	public int getLastY() {
		return mLastY;
	}
	
	/**
	 * setter for the last Y
	 * @param lastY
	 */
	public void setLastY( int lastY ) {
		mLastY = lastY;
	}

	/**
	 * getter for the last x value
	 * @return mLastX
	 */
	public int getLastX() {
		return mLastX;
	}
	
	/**
	 * setter for the last X
	 * @param lastX
	 */
	public void setLastX( int lastX ) {
		mLastX = lastX;
	}

	/**
	 * setter for if left was the last direction
	 * @param leftLast
	 */
	public void setLeftLast(boolean leftLast) {
		mLeftLast = leftLast;
	}

	public void setSprite() {
		if ( mStatus == status.AIR ) {
			nextJumpingSprite();
		} else if ( mStatus == status.GROUND ) {
			if ( getRight() == true  || getLeft() == true ) {
				nextRunningSprite();
			} else {
				if ( mShootingDelay < SHOOTING_DELAY ) {
					nextShootingSprite();
				} else {
					nextStandingSprite();
				}
			}
		} else if ( mStatus == status.HANGING ) {
			nextHangingSprite();
		} else if ( mStatus == status.CROUCHING ) {
			nextCrouchingSprite();
		}
	}

	/**
	 * set the number of times the player has jumped
	 * @param i
	 */
	public void setNumJumps(int jumps) {
		mNumJumps = jumps;
	}

	public void crouch() {
		if ( getStatus() == status.GROUND ) {
			setStatus( status.CROUCHING );
		}
	}
	
	public void stand() {
		setStatus( status.GROUND );
	}
}

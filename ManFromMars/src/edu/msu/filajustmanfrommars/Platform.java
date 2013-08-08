package edu.msu.filajustmanfrommars;

import java.util.ArrayList;
import java.util.PriorityQueue;

import edu.msu.filajustmanfrommars.Player.status;

import android.graphics.Canvas;
import android.os.AsyncTask.Status;
import android.util.Log;

public class Platform {

	/**
	 * all the sections that are in this platform
	 */
	private ArrayList< ArrayList<Sprite> > mSections = new ArrayList< ArrayList<Sprite> >();
	
	/**
	 * the width of the platform
	 */
	private int mWidth = 0;
	
	/**
	 * the height of the platform
	 */
	private int mHeight = 0;
	
	/**
	 * the top hit offset
	 */
	private int mHitYTop = 0;
	
	/**
	 * the bottom hit offset
	 */
	private int mHitYBottom = 0;
	
	/**
	 * the left hit offset
	 */
	private int mHitXLeft = 0;
	
	/**
	 * the right hit offset
	 */
	private int mHitXRight = 0;
	
	/**
	 * the "top" location that is visible to the user
	 */
	private int mHitYTopVisibleOffset = 0;
	
	/**
	 * the x location
	 */
	private int mX = 0;
	
	/**
	 * the y location
	 */
	private int mY = 0;
	
	public Platform() {
		ArrayList<Sprite> section = new ArrayList<Sprite>();
		mSections.add(section);
	}
	
	/**
	 * add a section to the platform
	 * @param section
	 * @param level
	 */
	public void addSection( Sprite section, int level ) {
		// make sure that that many levels exist in the array
		if ( mSections.size() - 1 < level) {
			// for each level that 
			for ( int i=mSections.size() - 1; i < level; i++) {

				ArrayList<Sprite> temp = new ArrayList<Sprite>();
				mSections.add( temp );
			}
		}
		
		// add that sprite to the specified level
		mSections.get(level).add(section);
	}
	
	/**
	 * draw the platforms
	 */
	public void draw(Canvas canvas) {
		// draw each section
		for ( ArrayList<Sprite> section : mSections ) {
			// draw each section
			for ( Sprite temp : section ) {
				temp.draw(canvas);
			}
		}
	}
	
	/**
	 * check if the "top" of the platform was hit
	 * @param x
	 * @param y
	 * @return if it was hit
	 */
	public Platform hit( Player player, Game game ) {
		Platform platform = null;
		boolean locationSet = false;
		// check if the current player is inside the platform
		if ( player.getY() > getYHitTop() - player.getHeight() &&
				player.getY() < getYHitBottom() &&
				player.getX() > getXHitLeft() - player.getWidth() &&
				player.getX() < getXHitRight() ) {
			// check which last x/y isn't inside it (determines which side to set the
			// player to)
			if ( player.getLastY() <= getYHitTop() - player.getHeight() ) {
				setPlayerToGround( player );
				// return this platform because the player is currently standing on it
				platform = this;
				locationSet = true;
				Log.i("Location", "T");
			}
			if ( player.getLastY() >= getYHitBottom() ) {
				setPlayerToCeiling( player );
				locationSet = true;
				Log.i("Location", "B");
			}
			if ( player.getLastX() <= getXHitLeft() - player.getWidth() ) {
				setPlayerToLeft( player, game );
				locationSet = true;
				Log.i("Location", "L");
				Log.i("Location lastY: ", Integer.toString(player.getLastY()));
				Log.i("Location of Y: ", Integer.toString(player.getY()));
				Log.i("Location platform top: ", Integer.toString(getYHitTop() - player.getHeight()));
			}
			if ( player.getLastX() >= getXHitRight() ){
				setPlayerToRight( player, game );
				locationSet = true;
				Log.i("Location", "R");
			}
			
			// edge case (if the player is somehow in the middle of a platform)
			if ( locationSet == false ){
				setPlayerBasedOnBiggestChange( player, game );	
				Log.i("Location", "Special Case");
			}
		}
		return platform;
	}
	
	/**
	 * set the location of the player based on the direction he changed most
	 * @param player
	 */
	private void setPlayerBasedOnBiggestChange(Player player, Game game) {
		boolean goingDown = false;
		boolean goingRight = false;
		
		int diffY = player.getY() - player.getLastY();
		if ( diffY > 0 ) {
			goingDown = true;
		}
		
		int diffX = player.getX() - player.getLastX();
		if ( diffX > 0 ) {
			goingRight = true;
		}
		
		// check which direction changed most
		if ( Math.abs( diffX ) > Math.abs( diffY ) ) {
			if ( goingRight == true ) {
				setPlayerToLeft( player, game );
			} else {
				setPlayerToRight( player, game );
			}
		} else {
			if ( goingDown == true ) {
				setPlayerToGround( player );
			} else {
				setPlayerToCeiling( player );
			}
		}
	}

	/**
	 * set player to ceiling of platform
	 * @param player
	 */
	private void setPlayerToCeiling( Player player ) {
		player.setLocation( player.getX(), getYHitBottom(), false, false );
		player.setVelocityY(0);
	}

	/**
	 * set player to the ground of the platform
	 * @param player
	 */
	private void setPlayerToGround( Player player ) {
		player.setLocation( player.getX(), getYHitTop() - player.getHeight(), false, false );
		player.setStatus( status.GROUND );
		player.setVelocityY(0);
	}

	/**
	 * set player to the right of the platform
	 * @param player
	 */
	private void setPlayerToRight( Player player, Game game ) {
		player.setLocation( getXHitRight(), player.getY(), false, false );
		
		// since manually changing player's position, we don't want the actual last
		// location saved
		player.setLastX( player.getX() );
				
		player.setDisableLeft( true );  // don't allow the player to move left
		game.setCurSidePlatform( this );
	}

	/**
	 * set player to the left of the platform
	 * @param player
	 */
	private void setPlayerToLeft( Player player, Game game ) {
		player.setLocation( getXHitLeft() - player.getWidth(), player.getY(), false, false );
		// since manually changing player's position, we don't want the actual last
		// location saved
		player.setLastX( player.getX() );
		
		player.setDisableRight( true ); // don't allow the player to move right
		game.setCurSidePlatform( this );
	}

	/**
	 * check if the bullet hit a platform
	 * @param x
	 * @param y
	 * @return if it was hit
	 */
	public boolean hit( Sprite bullet ) {
		// check if the current bullet is inside the platform
		if ( bullet.getY() > getYHitTop() - bullet.getHeight() &&
				bullet.getY() < getYHitBottom() &&
				bullet.getX() > getXHitLeft() - bullet.getWidth() &&
				bullet.getX() < getXHitRight() ) {
			return true;
		}
		return false;
	}
	
	/**
	 * checks if the player is in x range of platform
	 * @param player
	 * @return
	 */
	public boolean hasPlayerInsideX( Player player ) {
		if ( player.getX() > getXHitLeft() - player.getWidth() &&
				player.getX() < getXHitRight() ) {
			return true;
		}
		return false;
	}
	
	/**
	 * checks if the player is in y range of platform
	 * @param player
	 * @return
	 */
	public boolean hasPlayerInsideY( Player player ) {
		if ( player.getY() > getYHitTop() - player.getHeight() &&
				player.getY() < getYHitBottom() ) {
			return true;
		}
		return false;
	}
	
	/**
	 * get the x hit location for the left
	 * @return
	 */
	private int getXHitLeft() {
		return mX + mHitXLeft;
	}

	/**
	 * get the x hit location for the right
	 * @return
	 */
	private int getXHitRight() {
		return mX + mWidth - mHitXRight;
	}
	
	/**
	 * get the y hit location for the top
	 * @return
	 */
	private int getYHitTop() {
		return mY + mHitYTop;
	}
	
	/**
	 * get the y hit location for the bottom
	 * @return
	 */
	private int getYHitBottom() {
		return mY + mHeight - mHitYBottom;
	}

	/**
	 * calculate width
	 */
	public void calcWidth() {
		int width = 0;
		int maxWidth = 0;
		
		// go through each section
		for ( int i = 0; i < mSections.size(); i++ ) {
			maxWidth = 0;
			// current level of sections
			ArrayList<Sprite> temp = mSections.get(i);
			// get the width of each section
			for ( int j = 0; j < temp.size(); j++ ) {
				maxWidth += temp.get(j).getWidth();
			}
			
			if ( maxWidth > mWidth ) {
				mWidth = maxWidth;
			}
		}
	}
	
	/**
	 * calculate height
	 */
	public void calcHeight() {
		int height = 0;
		
		// go through each section
		for ( int i = 0; i < mSections.size(); i++ ) {
			height += mSections.get(i).get(0).getHeight();
		}
		
		mHeight = height;
	}

	/**
	 * set the x location of the platform
	 * @param x
	 */
	public void setLocation(int x, int y) {
		mX = x;
		mY = y;
		
		int curX;
		int curY = y;
		int largestY = 0;
		
		// set the location of each sprite
		for ( ArrayList<Sprite> section : mSections ) {
			curX = x;
			for ( Sprite s : section ) {
				s.setLocation(curX, curY);
				curX += s.getWidth();
				
				// update the height based on the biggest sprite in the section
				if ( s.getHeight() > largestY ) {
					largestY = s.getHeight();
				}
			}
			curY += largestY;
			largestY = 0;
		}
	}
	
	/**
	 * set the offsets of the platform
	 * @param left
	 * @param right
	 * @param top
	 * @param bottom
	 */
	public void setOffsets(int left, int right, int top, int bottom, int topVisible) {
		mHitXLeft = left;
		mHitXRight = right;
		mHitYTop = top;
		mHitYBottom = bottom;
		mHitYTopVisibleOffset = topVisible;
	}

	/**
	 * set the y location of the platform
	 * @param y
	 */
	public void setY(int y) {
		mY = y;
		
		// update the location of each sprite
		setLocation(mX, mY);
	}
	
	/**
	 * set the x location of the platform
	 * @param y
	 */
	public void setX(int x) {
		mX = x;
		
		// update the location of each sprite
		setLocation(mX, mY);
	}

	/**
	 * check if the player is in a position to hang on the edge
	 * @param player
	 */
	public void checkIfShouldHang(Player player) {
		if ( player.getY() > mY + mHitYTopVisibleOffset ) {
			if ( player.getLastY() <=  mY + mHitYTopVisibleOffset ) {
				player.setLocation( player.getX(),  mY + mHitYTopVisibleOffset, false, false );
				player.setVelocityY( 0 );
				player.setNumJumps( 0 );
				player.setStatus( status.HANGING );
			}
		}
	}
}

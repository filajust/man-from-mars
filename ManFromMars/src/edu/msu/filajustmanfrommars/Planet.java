package edu.msu.filajustmanfrommars;

import java.util.ArrayList;

import android.graphics.Canvas;

public class Planet {
	
	private String mPlanet;
	
	ArrayList< Platform > mPlatforms = new ArrayList< Platform >();

	/**
	 * Constructor
	 */
	public Planet() {
		mPlanet = "";
	}
	
	/**
	 * draw
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		for ( Platform platform : mPlatforms ) {
			platform.draw(canvas);
		}
	}
	
	/**
	 * set the name of the planet
	 */
	public void setPlanetName( String name ) {
		mPlanet = name;
	}

    /**
     * add a platform to the planet
     * @param platform the platform being added
     */
	public void addPlatform(Platform platform) {
		mPlatforms.add(platform);
	}

	/**
	 * check if the player hit any of the platforms
	 * @param mPlayer
	 */
	public Platform checkPlatforms(Player player, ArrayList<Sprite> bullets, Game game) {
		// used for returning the platform that the player is standing on
		Platform platform = null;
		Platform standingPlatform = null;
		// need to keep track of which bullets we need to remove
		ArrayList<Sprite> bulletsToRemove = new ArrayList<Sprite>();
		
		for ( Platform p : mPlatforms ) {
			platform = p.hit( player, game );
			
			if ( platform != null ) {
				standingPlatform = platform;
			}
			
			// TODO: CRASHED HERE
			for ( Sprite bullet : bullets ) {
				if ( p.hit( bullet ) ) {
					bulletsToRemove.add( bullet );
				}
			}
		}
		
		// now remove the flagged bullets
		for ( Sprite bullet : bulletsToRemove ) {
			player.deleteBullet( bullet );
		}
		
		return standingPlatform;
	}

}

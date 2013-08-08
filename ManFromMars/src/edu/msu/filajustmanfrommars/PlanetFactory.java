package edu.msu.filajustmanfrommars;

import android.content.Context;

public class PlanetFactory {
	
	/**
	 * purple platform x offset
	 */
	private static final int PURPLE_X_OFFSET = 40;
	
	/**
	 * purple platform y offset
	 */
	private static final int PURPLE_Y_OFFSET = 15;
	
	/**
	 * purple platform y visible offset
	 */
	private static final int PURPLE_Y_VISIBLE_OFFSET = 5;
	
	private int mPlanetIndex = 0;

	public PlanetFactory() {
		mPlanetIndex = 0;
	}
	
	/**
	 * build the first planet
	 * @param planet
	 * @param context
	 */
	public void buildPlanetOne(Planet planet, Context context) {
		
		addPurplePlatform( 10, 2, 10, 250, planet, context );
		
		addPurplePlatform( 2, 6, 750, 450, planet, context );
		
		addPurplePlatform( 3, 2, 1050, 300, planet, context );
		
		addPurplePlatform( 25, 2, 0, 550, planet, context );
	}
	
	public void addPurplePlatform( int xTiles, int yTiles, int x, int y,
			Planet planet, Context context) {
		
		Platform platform = new Platform();
		
		for ( int j = 0; j < yTiles; j++ ) {
			
			Sprite section = null;
			
			if ( j == 0 ) {
				// add top left corner
				section = new Sprite(R.drawable.purple_ground_corner_top_left, 
						context, 0, 0);
				platform.addSection(section, 0);
				
				// create all the sections
				for ( int i = 1; i < xTiles; i++ ) {
					section = new Sprite(R.drawable.purple_ground_1, 
							context, 0, 0);
					platform.addSection(section, 0);
				}
				
				// add bottom right corner
				section = new Sprite(R.drawable.purple_ground_corner_top_right, 
						context, 0, 0);
				platform.addSection(section, 0);
				
			} else if ( j == yTiles - 1 ) {
				// bottom left corner
				section = new Sprite(R.drawable.purple_ground_corner_bottom_left, 
						context, 0, 0);
				platform.addSection(section, yTiles - 1);
				
				// create all the sections
				for ( int i = 1; i < xTiles; i++ ) {
					section = new Sprite(R.drawable.purple_ground_1_mirror, 
							context, 0, 0);
					platform.addSection(section, yTiles - 1);
				}
				
				// add bottom right corner
				section = new Sprite(R.drawable.purple_ground_corner_bottom_right, 
						context, 0, 0);
				platform.addSection(section, yTiles - 1);
			} else {
				// left edge
				section = new Sprite(R.drawable.purple_ground_middle_left, 
						context, 0, 0);
				platform.addSection(section, j);
				
				// create all the sections
				for ( int i = 1; i < xTiles; i++ ) {
					section = new Sprite(R.drawable.purple_ground_middle, 
							context, 0, 0);
					platform.addSection(section, j);
				}
				
				// add bottom right corner
				section = new Sprite(R.drawable.purple_ground_middle_right, 
						context, 0, 0);
				platform.addSection(section, j);
			}
		}
		
		platform.setLocation( x, y );
		
		// these are standard for purple platforms
		platform.setOffsets(PURPLE_X_OFFSET, PURPLE_X_OFFSET, 
				PURPLE_Y_OFFSET, PURPLE_Y_OFFSET, PURPLE_Y_VISIBLE_OFFSET);
		
		// make sure the height and width is set correctly
		platform.calcHeight();
		platform.calcWidth();
		
		// finally, add it to the planet
		planet.addPlatform(platform);
	}

	/**
	 * build the next planet
	 * @param mCurPlanet
	 */
	public void buildNextPlanet(Planet planet, Context context) {
		if ( mPlanetIndex == 0 ) {
			buildPlanetOne( planet, context );
		}
		
		mPlanetIndex++;
	}

}

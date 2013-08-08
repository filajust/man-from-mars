package edu.msu.filajustmanfrommars;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class GameActivity extends Activity {
	
	/**
	 * reference to the game view
	 */
	private GameView mGameView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		// set the dimensions
	    Point dimensions = new Point();
		
	    DisplayMetrics displaymetrics = new DisplayMetrics();
	    this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	    
	    dimensions.x = displaymetrics.heightPixels;
	    dimensions.y = displaymetrics.widthPixels;
		
	    // set the view
		mGameView = (GameView)this.findViewById(R.id.gameView);
		// set the screen dimensions
		mGameView.setScreenDimensions(dimensions);
		// initialize locations of buttons on the screen
		mGameView.initUI();
		
		//TODO
//		Intent intent = getIntent();
//		
//		if (intent != null) {
//			int planet = intent.getIntExtra("planet", 1);
//			mGame.setPlanet(planet);
//		}
	}

	/**
	 * pause
	 */
	@Override
	protected void onPause() {
		super.onPause();
		
		mGameView.onPause();
	}

	/**
	 * resume
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		mGameView.onResume();
	}

}

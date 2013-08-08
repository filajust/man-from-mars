package edu.msu.filajustmanfrommars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class PlanetSelectActivity extends Activity {
	
	private static final String PLANET = "planet";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planet_select);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_planet_select, menu);
		return true;
	}
	
	/**
	 * called when back button is pressed
	 * @param view
	 */
	public void onBack(View view) {
		this.onBackPressed();
	}
	
	/**
	 * called when planet 1 is slected
	 * @param view
	 */
	public void onPlanet1(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(PLANET, 1);
		startActivity(intent);
	}

}

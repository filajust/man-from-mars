package edu.msu.filajustmanfrommars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * called when the new game button is pressed
	 * @param view
	 */
	public void onNewGame(View view) {
		Intent intent = new Intent(this, PlanetSelectActivity.class);
		startActivity(intent);
	}
	
	/**
	 * called when the load game button is pressed
	 * @param view
	 */
	public void onLoadGame(View view) {
		Log.i("laodgame", "loadgame");
	}
	
	/**
	 * called when the settings button is pressed
	 * @param view
	 */
	public void onSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

}

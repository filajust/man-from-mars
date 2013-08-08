package edu.msu.filajustmanfrommars;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}

	/**
	 * called when back is clicked
	 */
	public void onBack(View view) {
		this.onBackPressed();
	}
}

package com.designproject.controllers;

import com.designproject.R;
import com.designproject.R.layout;
import com.designproject.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SettingsController extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_controller);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_controller, menu);
		return true;
	}

}

package com.designproject.controllers;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.models.XMLReaderWriter;

public class FireAlertSplash extends Activity implements AnimationListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Remove action bar from the window
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fire_alert_splash);

		// Login to the system
		logIn();
	}

	// Animate the logo moving across the screen
	private void animateWithXML(View view) {
		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.animator.animate);
		anim.setFillAfter(true);
		anim.setAnimationListener(this);
		view.startAnimation(anim);

	}

	private void logIn() {
		// If they;re logged in, go to the main menu
		if (isLogInSaved()) {
			Intent openMenu = new Intent(FireAlertSplash.this,
					InspectionController.class);
			openMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(openMenu);
		}
		// Otherwise the logo will move across the screen, the XML will be
		// parsed
		else {
			View image = findViewById(R.id.imageView1);
			animateWithXML(image);

			FireAlertApplication a = (FireAlertApplication) getApplication();
			try {
				XMLReaderWriter init = new XMLReaderWriter(
						getApplicationContext());
				a.setLocation(init.parseXML());
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isLogInSaved() {
		// Get the shared preferences
		SharedPreferences sharedPreferenceLogin = getSharedPreferences("Login",
				Context.MODE_PRIVATE);
		// Get the logged in user, or "NO_CURRENT_USER" if no one is logged in
		String username = sharedPreferenceLogin.getString("CurrentUser",
				"NO_CURRENT_USER");
		// Check this returned user
		if (username.equals("NO_CURRENT_USER"))
			// Not logged in
			return false;

		return true;
	}

	@Override
	// When the animation is complete, start the login screen activity
	public void onAnimationEnd(Animation animation) {
		Intent openLoginScreen = new Intent(FireAlertSplash.this,
				LoginScreen.class);
		openLoginScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		onDestroy();
		startActivity(openLoginScreen);
		finish();
		overridePendingTransition(0, 0);
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}
}

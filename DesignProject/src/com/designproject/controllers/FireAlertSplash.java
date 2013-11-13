package com.designproject.controllers;


import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.R.animator;
import com.designproject.R.id;
import com.designproject.R.layout;
import com.designproject.R.menu;
import com.designproject.models.XMLReaderWriter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class FireAlertSplash extends Activity implements AnimationListener {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fire_alert_splash);
        
        //TODO REMOVE TEMPORARY CODE
        // TEMPORARY CODE TO CREATE USERS FOR TESTING
        
        
        // Gets a shared preference called 'login' if it doesn't exist it will create it
    	SharedPreferences sharedPreferenceLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
    	
    	// To edit you need to call the shared preference editor
    	SharedPreferences.Editor editor = sharedPreferenceLogin.edit();
    	
    	// Create a user with name "username" and password "password"
    	editor.putString("username", "password");

    	//commit changes
    	editor.commit();
    	
    	
        //TODO REMOVE TEMPORARY CODE
        // END OF TEMPORARY CODE
        
        logIn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fire_alert_splash, menu);
        return true;
    }
    
    
    private void animateWithXML(View view)
    {
    	Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.animate);
        anim.setFillAfter( true );
        anim.setAnimationListener( this );
        view.startAnimation( anim );
    	
    }
    
    private void logIn()
    {
		if(isLogInSaved()){
			Intent openMenu = new Intent(FireAlertSplash.this, MainMenu.class);
			openMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(openMenu);
		}
		else{
			View image = findViewById(R.id.imageView1);
			animateWithXML(image);
			
			FireAlertApplication a = (FireAlertApplication)getApplication();
			try {
				XMLReaderWriter init = new XMLReaderWriter( getApplicationContext() );
				a.setLocation( init.parseXML() );
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}								
		}
	}

    private boolean isLogInSaved()
    {
    	// Get the shared preferences
    	SharedPreferences sharedPreferenceLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
    	// Get the logged in user, or "NO_CURRENT_USER" if no one is logged in
    	String username = sharedPreferenceLogin.getString("CurrentUser", "NO_CURRENT_USER");        	
        // Check this returned user
    	if( username.equals("NO_CURRENT_USER") )
    		// Not logged in
    		return false;
    	
    	return true;
    }

	@Override
	public void onAnimationEnd(Animation animation) {
		
		Intent openLoginScreen = new Intent(FireAlertSplash.this,LoginScreen.class);
		openLoginScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		onDestroy();
		startActivity(openLoginScreen);
		finish();
		overridePendingTransition(0, 0);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
		
	}
}

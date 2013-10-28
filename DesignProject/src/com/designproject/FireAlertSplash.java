package com.designproject;


import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.designproject.R;

import android.os.Bundle;
import android.app.Activity;
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
        	SharedPreferences sharedPreferenceLogin = getSharedPreferences("Login",0);
        	
        	String username = sharedPreferenceLogin.getString("Username", "");
        	String password = sharedPreferenceLogin.getString("Password", "");
        	
            
        	if(username.equals("username") && password.equals("password"))
        		return true;
        	
        	return false;
    }

	@Override
	public void onAnimationEnd(Animation animation) {
		
		Intent openLoginScreen = new Intent(FireAlertSplash.this,LoginScreen.class);
		openLoginScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		onDestroy();
		startActivity(openLoginScreen);
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
		
	}
}

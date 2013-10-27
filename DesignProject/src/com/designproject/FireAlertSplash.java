package com.designproject;


import com.designproject.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class FireAlertSplash extends Activity {
	
	static int LOGIN_SCREEN_TIMER= 1000;
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
    	Animation anim = AnimationUtils.loadAnimation(this, R.animator.animate);
        anim.setInterpolator((new  
                   AccelerateDecelerateInterpolator()));
        anim.setFillAfter(true);
        view.setAnimation(anim);
    	
    }
    
    private void logIn()
    {
    	Thread loadData = new Thread(){
    		public void run(){
    			try{
    				if(/*isLogInSaved()*/false)
    				{
    					Intent openMenu = new Intent(FireAlertSplash.this, MainMenu.class);
    					openMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    			    	startActivity(openMenu);
    			    	}
    				else
    				{
    					View image = findViewById(R.id.imageView1);
    			    	animateWithXML(image);
    			    		
    			    	sleep(LOGIN_SCREEN_TIMER);
			    		Intent openLoginScreen = new Intent(FireAlertSplash.this,LoginScreen.class);
    					openLoginScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    					startActivity(openLoginScreen);
    					}
    				} catch (InterruptedException e){e.printStackTrace();}
    			}
    		};
    		loadData.start();
    }
  
    private boolean isLogInSaved()
    {
        	SharedPreferences sharedPreferenceLogin = getSharedPreferences("Login",0);
        	
        	String username = sharedPreferenceLogin.getString("Username", "");
        	String password = sharedPreferenceLogin.getString("Password", "");
        	
            
        	if(username != "" && password != "")
        		return true;
        	
        	return false;
    }
}

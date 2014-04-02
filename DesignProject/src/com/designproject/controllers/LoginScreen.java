package com.designproject.controllers;

import com.designproject.R;
import com.designproject.models.HelperMethods;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.widget.TextView;

public class LoginScreen extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_screen);

		// Password on enter click button listener
		// User name doesn't need an enter listener - it defaults to switching
		// focus to password
		this.findViewById(R.id.password).setOnKeyListener(new OnKeyListener() {
			
			//do nothings

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if( event.getAction() == KeyEvent.ACTION_DOWN &&	
						keyCode == KeyEvent.KEYCODE_ENTER){
					try {
						logIn();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return false;
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.activity_login_screen, menu);
	    return true;
	}
		    
		

	public void logIn() throws Exception {
		// Check if details are correct
		// Get required information
		TextView username = (TextView) findViewById(R.id.username);
		TextView password = (TextView) findViewById(R.id.password);

		String usernameString = username.getText().toString();
		String passwordString = password.getText().toString();

		SharedPreferences preferences = getSharedPreferences("Login",
				Context.MODE_PRIVATE);


		/* Add Admin Account, If it doesn't exist */
		if (!preferences.contains("Admin")) {

    		Editor editor = preferences.edit();
    		editor.putString("Admin", HelperMethods.computeSHAHash("adminPassword")); 
    		editor.commit();
    	}
    	
    	String userPassword = preferences.getString( usernameString, "NO_SUCH_USERNO_SUCH_USERNO_SUCH_USERNO_SUCH_USERNO_SUCH_USERNO_SUCH_USERNO_SUCH_USER");
    	
    	if( userPassword.equals("NO_SUCH_USERNO_SUCH_USERNO_SUCH_USERNO_SUCH_USERNO_SUCH_USERNO_SUCH_USERNO_SUCH_USER") || !userPassword.equals( HelperMethods.computeSHAHash(passwordString) ) )
    		// Log in NOT successful. Return without granting access
    		return;
    	
    	// Save the current user to the saved preferences
    	// so that other parts of the application know that someone is logged in
    	saveUserDetails();
    	
    	// Start the application main menu
    	Intent openLoginScreen = new Intent(LoginScreen.this, InspectionController.class);

		openLoginScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(openLoginScreen);

    }
    
    //Redirect Method
    /**
     * This is a listenter class, but, we don't always want a listener so it just redirects to
     * the typical log in class
     * @param view
     */
    public void logIn(View view){
    	try {
			logIn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void saveUserDetails()
    {
    	// Gets a shared preference called 'login' if it doesn't exist it will create it
    	SharedPreferences sharedPreferenceLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
    	
    	// To edit you need to call the shared preference editor
    	SharedPreferences.Editor editor = sharedPreferenceLogin.edit();
    	
    	// Get required information
    	TextView username= (TextView) findViewById(R.id.username);
    	
    	// Convert to String
    	String usernameString = username.getText().toString();
    	
    	// Save the information
    	editor.putString("CurrentUser", usernameString);
    	
    	editor.commit();

	}
}


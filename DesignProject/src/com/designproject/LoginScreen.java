package com.designproject;

import com.designproject.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class LoginScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_screen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login_screen, menu);
        return true;
    }
    
    public void logIn(View view)
    {
    	//Check if details are correct
    	
    	saveUserDetails();
    	
    	Intent openLoginScreen = new Intent(LoginScreen.this, MainMenu.class);
		openLoginScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(openLoginScreen);
    }
    
    public void saveUserDetails()
    {
    	//Gets a shared preference called 'login' if it doesn't exist it will create it
    	SharedPreferences sharedPreferenceLogin = getSharedPreferences("Login",0);
    	
    	//To edit you need to call the shared preference editor
    	SharedPreferences.Editor editor = sharedPreferenceLogin.edit();
    	
    	//Get required information
    	TextView username= (TextView) findViewById(R.id.username);
    	TextView password= (TextView) findViewById(R.id.password);
    	
    	String usernameString = username.getText().toString();
    	String passwordString = password.getText().toString();
    	
    	//save the information
    	editor.putString("Username", usernameString);
    	editor.putString("Password", passwordString);

    	//commit changes
    	editor.commit();
    	
    	
    }
}

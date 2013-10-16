package com.designproject;

import com.designproject.R;
import com.designproject.R.layout;
import com.designproject.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class MainMenu extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    
    public void signOut(View view)
    {
    	SharedPreferences preferences = getSharedPreferences("Login",0);
    	preferences.edit().remove("Username");
    	preferences.edit().remove("Password");
    	
    	Intent loginScreen = new Intent(MainMenu.this, LoginScreen.class);
    	startActivity(loginScreen);
    }
    
}

package com.designproject.controllers;

import com.designproject.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
	
	public void savePassword(View view){
		
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		
		String oldPassword = ((EditText)findViewById(R.id.oldPassword)).getText().toString();
		
		SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
		String currentUser = preferences.getString("CurrentUser", "");
		String currentPassword =  preferences.getString( currentUser, "");
		
		//check if the current password is right
		if( !currentPassword.equals(oldPassword) ){
			Toast toast = Toast.makeText(context, "Wrong password", duration);
			toast.show();
    		return;
		}
    	
		String newPassword = ((EditText)findViewById(R.id.newPassword)).getText().toString();
		String newPasswordConfirm = ((EditText)findViewById(R.id.newPasswordConfirm)).getText().toString();
		
		//check the passwords are the same
		if( !newPassword.equals(newPasswordConfirm) ){
			Toast toast = Toast.makeText(context, "Passwords don't match", duration);
			toast.show();
			return;
		}
		
		// To edit you need to call the shared preference editor
    	SharedPreferences.Editor editor = preferences.edit();
    	
    	// Save the information
    	editor.putString(currentUser.trim(), newPassword.trim());

    	//commit changes
    	editor.commit();
    	
    	//Gui stuff
    	((EditText)findViewById(R.id.oldPassword)).setText("");
    	((EditText)findViewById(R.id.newPassword)).setText("");
		((EditText)findViewById(R.id.newPasswordConfirm)).setText("");
		
		
		Toast toast = Toast.makeText(context, "Password Changed", duration);
		toast.show();
		
	}
	
	public void createUser(View view){
		
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		
		String newUserName 					= ((EditText)findViewById(R.id.newUserName)).getText().toString();
		String newUserNamePassword 			= ((EditText)findViewById(R.id.newUserPassword)).getText().toString();
		String newUserNamePasswordConfirm 	= ((EditText)findViewById(R.id.newUserPasswordConfirm)).getText().toString();
		
		//Check if the same
		if( !newUserNamePassword.equals(newUserNamePasswordConfirm) ){
			Toast toast = Toast.makeText(context, "Passwords don't match", duration);
			toast.show();
			return;
		}
		
		if(newUserName.length() == 0 || newUserNamePassword.length() == 0 ||  newUserNamePasswordConfirm.length() == 0){
			Toast toast = Toast.makeText(context, "Can't have blank options", duration);
			toast.show();
			return;
		}
		
		//Check if they already exist
		SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
		String doesNotExistPlaceHolder = "DOESNOTEXISTDOESNOTEXISTDOESNOTEXISTDOESNOTEXISTDOESNOTEXISTDOESNOTEXIST";
		String userExists = preferences.getString(newUserName, doesNotExistPlaceHolder);
		if( !userExists.equals(doesNotExistPlaceHolder) ){
			Toast toast = Toast.makeText(context, "User already exists", duration);
			toast.show();
			return;
		}
		
		// To edit you need to call the shared preference editor
    	SharedPreferences.Editor editor = preferences.edit();
    	
    	// Save the information
    	editor.putString(newUserName.trim(), newUserNamePassword.trim());

    	//commit changes
    	editor.commit();
    	
    	//Gui stuff
    	((EditText)findViewById(R.id.newUserName)).setText("");
    	((EditText)findViewById(R.id.newUserPassword)).setText("");
		((EditText)findViewById(R.id.newUserPasswordConfirm)).setText("");
		
		
		Toast toast = Toast.makeText(context, "User Created", duration);
		toast.show();
		
	}
}

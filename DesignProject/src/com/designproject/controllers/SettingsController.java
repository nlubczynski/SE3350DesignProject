package com.designproject.controllers;

import com.designproject.R;
import com.designproject.models.HelperMethods;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsController extends NavigationDrawerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_settings_controller);

		super.onCreate(savedInstanceState);
		
		Spinner spinner = (Spinner)findViewById(R.id.spinner1);				
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner);
		
		String[] users = HelperMethods.getUsers(getApplicationContext());
		
		for(String user: users)
			adapter.add(user);
		spinner.setAdapter(adapter);
	}
	
	public void savePassword(View view) throws Exception{
		
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
    	editor.putString(currentUser.trim(), HelperMethods.computeSHAHash(newPassword.trim()));

    	//commit changes
    	editor.commit();
    	
    	//Gui stuff
    	((EditText)findViewById(R.id.oldPassword)).setText("");
    	((EditText)findViewById(R.id.newPassword)).setText("");
		((EditText)findViewById(R.id.newPasswordConfirm)).setText("");
		
		
		Toast toast = Toast.makeText(context, "Password Changed", duration);
		toast.show();
		
	}
	
	public void createUser(View view) throws Exception{
		
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
    	editor.putString(newUserName.trim(), HelperMethods.computeSHAHash(newUserNamePassword.trim()));

    	//commit changes
    	editor.commit();
    	
    	//Gui stuff
    	((EditText)findViewById(R.id.newUserName)).setText("");
    	((EditText)findViewById(R.id.newUserPassword)).setText("");
		((EditText)findViewById(R.id.newUserPasswordConfirm)).setText("");
		
		
		Toast toast = Toast.makeText(context, "User Created", duration);
		toast.show();
		
		restart();
		
	}
	public void deleteUser(String user){
		
		if( HelperMethods.delteUser(user, getApplicationContext()))
			new AlertDialog.Builder(this)
	    	.setIcon(android.R.drawable.ic_dialog_alert)
	    	.setTitle("User deleted")
	    	.setMessage("User " + user + " deleted.")
			.setPositiveButton("Okay", new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	
	                //Restart the activity
	                restart();
	            }

    		})
			.show();
		else
			new AlertDialog.Builder(this)
	    	.setIcon(android.R.drawable.ic_dialog_alert)
	    	.setTitle("User not deleted")
	    	.setMessage("User " + user + " was not deleted.")
			.setNegativeButton("Okay", null)
			.show();

		
	}
	/**
	 * Restart the activity - after you delete or add an user - so they appear in the proper loactiosn
	 */
	public void restart(){
		finish();
		startActivity(getIntent());
	}
	public void confirmDelete(View view){
		
		Spinner spinner = (Spinner)findViewById(R.id.spinner1);	
		final String user = spinner.getSelectedItem().toString();
		
		new AlertDialog.Builder(this)
        	.setIcon(android.R.drawable.ic_dialog_alert)
        	.setTitle("Delete User Confirmation")
        	.setMessage("Delete " + user + "?")
        	.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	
	                //Stop the activity
	                deleteUser(user);
	            }

    		})
    		.setNegativeButton("No", null)
    		.show();
	}
}

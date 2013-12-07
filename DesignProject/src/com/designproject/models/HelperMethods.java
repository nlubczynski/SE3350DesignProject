package com.designproject.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import com.designproject.controllers.LoginScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

public class HelperMethods {
	public final static int CHECK_IF_LOGGED_IN = 0;
	public final static int LOGOUT = 1;

/**
	 * 
	 * @param
	 * @boolean
	 * @author Braden
	 */
	public final static	ArrayList<DateTime> getInspectionDueDates(DateTime startDateTime, DateTime endDateTime, String terms)
	{
		/*TODO: should probably make this work off of days and not months */
		ArrayList<DateTime> checkDates = new ArrayList<DateTime>();
		
		//Total days between start of contract and end of contract
		int days = Days.daysBetween(startDateTime, endDateTime).getDays();
		
		//Contract length in months
		int spanInMonths = days/30;
		
		int termsInteger = 1;
		
		if (terms.length() != 0)
			termsInteger = Integer.parseInt(terms);
		
		// Figure out the length in time between inspections
		int inspectionSpan = spanInMonths/termsInteger;
		
		//Get the next inspection day
		DateTime nextInspectionDay = startDateTime.plusMonths(inspectionSpan);

		//If the next day is outside the contract time then just use end date
		if(nextInspectionDay.isAfter(endDateTime))
			checkDates.add(endDateTime);
		
		//Add inspection days to array
		while(nextInspectionDay.isBefore(endDateTime))
		{
			checkDates.add(nextInspectionDay);
			nextInspectionDay = nextInspectionDay.plusMonths(inspectionSpan);
		}
		
		return checkDates;
	}
	
	public final static Interval returnNextInspectionInterval(Calendar startDateCalendar, Calendar endDateCalendar, String terms)
	{
		DateTime today = DateTime.now();
		DateTime startDateTime = new DateTime(startDateCalendar);
		DateTime endDateTime = new DateTime(endDateCalendar);
		
		
		ArrayList<DateTime> inspectionDueDates = HelperMethods.getInspectionDueDates(startDateTime, endDateTime, terms);

		if(inspectionDueDates.size() == 0)
			inspectionDueDates.add(endDateTime);
		
		DateTime nextInspectionPeriodStartDate = startDateTime;
		DateTime nextInspectionPeriodEndDate = inspectionDueDates.get(0);
		
		LocalDate todayLocalDate = today.toLocalDate();

		for(DateTime inspectionDueDate : inspectionDueDates )
		{
			nextInspectionPeriodEndDate = inspectionDueDate;
			
			if(today.isBefore(inspectionDueDate) || todayLocalDate.isEqual(inspectionDueDate.toLocalDate()))
				break;
			
			nextInspectionPeriodStartDate = inspectionDueDate;
		}
		
		Interval inspectionInterval = new Interval(nextInspectionPeriodStartDate, nextInspectionPeriodEndDate);

		return inspectionInterval;
	}
	/**
	 *  A helper function to check state, and logout. 
	 *  Checking the state automatically starts the login screen activity,
	 *  if the user is not logged in.
	 *  <p>
	 *  For Logout, this method will first check if you're logged in and regardless of
	 *  whether you're logged in or not, it will close all activities and return to the login
	 *  screen upon successful logout.
	 *  <p>
	 *  For Check if logged in, this method will either do nothing (if the user is logged in)
	 *  or close all activities and call the login screen if they are not
	 *  <p>
	 *  
	 * @param id
	 */
	public final static void logOutHandler( int id, Context context ){
		
		SharedPreferences preferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
		String currentUser = preferences.getString("CurrentUser", "NO_CURRENT_USER");
		
		switch( id ){
		case LOGOUT:
			logOutHandler( CHECK_IF_LOGGED_IN, context );
	        SharedPreferences.Editor editor = preferences.edit();
	        // Remove the current user
	        editor.remove( "CurrentUser" );
	        if( editor.commit() )
	        	// We removed the current user, pass to the check if logged out case to boot us.
	        	logOutHandler( CHECK_IF_LOGGED_IN, context );
			break;
			
		case CHECK_IF_LOGGED_IN:			
			if( currentUser.equals("NO_CURRENT_USER" ) ){
				// User isn't logged in, send them to the main screen
				// Prepare the login screen
		        Intent loginScreen = new Intent(context, LoginScreen.class);
		        loginScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        // Tell the intent to close all open activities
		        // This will also kill this thread, as it resides in the topmost activity
		        loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        // Start the login screen
		        context.startActivity(loginScreen);
			}
			// Else, do nothing, they're logged in.
			break;			
		}
	}
	
	public final static boolean checkIfInspectionCompleted(Interval inspectionInterval, Contract contract)
	{
		boolean isCompleted = true;
		Building[] buildings = contract.getBuildings();
		DateTime lastInspectedDateTime = null;
		
		for(Building building : buildings)
		{
			lastInspectedDateTime = new DateTime(building.getTimeStamp());
			if(inspectionInterval.contains(lastInspectedDateTime))
			{
				isCompleted = false;
				break;
			}
		}
		return isCompleted;
		
	}
	/**
	 * Get all the users from shared preferences
	 * @param context - The application's context
	 * @return
	 */
	public final static String[] getUsers(Context context){
		
		SharedPreferences preferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
		Map<String,?> keys = preferences.getAll();
		ArrayList<String> returnVal = new ArrayList<String>();
		
		//Add all users, except the "currentUser" map
		for(Map.Entry<String,?> entry : keys.entrySet())
			if(!entry.getKey().equals("CurrentUser") && !entry.getKey().equals("Admin"))
				returnVal.add(entry.getKey());
		
		String[] returnArray = new String[ returnVal.size() ];
		
		for(int i = 0; i < returnVal.size(); i++)
			returnArray[i] = returnVal.get(i);
		
		return returnArray;	
			
	}
	/**
	 * Delete a user from the system
	 * @param user - String - the user to delete
	 * @param context - The application's context
	 * @return
	 */
	public final static boolean delteUser(String user, Context context){
		SharedPreferences preferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		
		editor.remove(user);
		
		return editor.commit();
	}
	public final static boolean connectAndSend(SharedPreferences preferences, Context context){		
		
		String ip = preferences.getString("ip", "");
		int port = preferences.getInt("port", -1);
		Sender sender;
		XMLReaderWriter reader;
		
		if(ip.equals("") || port == -1)
			return false;
		
		try {
			sender = new Sender(ip, port);
		} catch (Exception e) {			
			return false;
		}
		try {
			reader = new XMLReaderWriter(context);
		} catch (Exception e) {
			return false;
		}			
		try {
			sender.RTSPSend(reader.getXML());
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	private static String convertToHex(byte[] data) throws java.io.IOException 
	     {
	             
	             
	            StringBuffer sb = new StringBuffer();
	            String hex=null;
	             
	            hex=Base64.encodeToString(data, 0, data.length, Base64.NO_PADDING);
	             
	            sb.append(hex);
	                         
	            return sb.toString();
	        }
	     
	     
    public final static String computeSHAHash(String password) throws Exception
	      {
	          MessageDigest mdSha1 = null;
	          String SHAHash;

              mdSha1 = MessageDigest.getInstance("SHA-1");

              mdSha1.update(password.getBytes("ASCII"));

              byte[] data = mdSha1.digest();

              SHAHash=convertToHex(data);

	          return SHAHash;
	      }
	        
}



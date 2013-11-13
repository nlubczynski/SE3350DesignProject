package com.designproject.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.joda.time.Interval;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.R.id;
import com.designproject.R.layout;
import com.designproject.R.menu;
import com.designproject.models.Client;
import com.designproject.models.Contract;
import com.designproject.models.Franchise;
import com.designproject.models.HelperMethods;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class InspectionController extends ListActivity {

	// for storing the information.
	private ArrayList<String []> inspectionInformationArray;
	// Adapters are used to bind to data. I want 2d so create my own
	private SimpleAdapter simpleAdapter;
	
	//Create arraylist which will hold the string data
	ArrayList<HashMap<String, String>> listInformationString = new 
			ArrayList<HashMap<String, String>>();
	Franchise mFranchise;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspection_view);
		// Show the Up button in the action bar.
		setupActionBar();
		
		FireAlertApplication a = (FireAlertApplication)getApplication();
		mFranchise = (Franchise)a.getLocation(); 
		
		inspectionInformationArray = new ArrayList<String []>();
		
		populateListInformation();
	}

	private void populateListInformation() 
	{
		//TODO: take in data from model classes and write to the member variables
		String clientName;
		String contractDueDate;
		String contractId;
		
		for(Client client : mFranchise.getClients())
		{
			clientName = client.getName();
			
    		for(Contract contract : client.getContracts())
    		{
    			Calendar startDateCalendar = contract.getStartDate();
    			Calendar endDateCalendar = contract.getEndDate();
    			String terms = contract.getTerms();
    			
    			Interval nextInspectionInterval = HelperMethods.returnNextInspectionInterval(startDateCalendar, endDateCalendar, terms);
    			
    			contractDueDate = nextInspectionInterval.getEnd()
    					.toLocalDate()
    					.toString();
    			
    			contractId = contract.getId();
    			
    			String[] s = {clientName, contractDueDate, contractId};
    			inspectionInformationArray.add(s);
    		}
		}
		
		bindListView();
	}
	
	private void bindListView()
	{
		HashMap<String,String> item;
		
		for(String [] inspectionInformation : inspectionInformationArray)
		{
			item = new HashMap<String, String>();
			item.put("line1", inspectionInformation[0]);
			item.put("line2", inspectionInformation[1]);
			item.put("line3", inspectionInformation[2]);
			listInformationString.add(item);
		}
		
		simpleAdapter =  new SimpleAdapter(this,listInformationString, R.layout.custom_two_lines,
				new String[] {"line1", "line2", "line3"},
				new int[] {R.id.clientName, R.id.dueDate, R.id.textViewIdValue});
		
		///bind the data
		setListAdapter(simpleAdapter);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inspection_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void inspectionItemListener(View view)
	{
		Client[] clientList = mFranchise.getClients();
		TextView clientNameChild = (TextView) ((ViewGroup)view).getChildAt(0);
		String clientName = clientNameChild.getText().toString();
		
		//The view is a listView and its second child element is a relative layout. 
		//So I cast that relativeLayout as a ViewGroup and get its child.
		TextView contractIdChild = (TextView)((ViewGroup)((ViewGroup)view).getChildAt(1)).getChildAt(0);
		String contractId = contractIdChild.getText().toString();


		for(Client client : clientList)
		{
			    if(client.getName() == clientName)
			    {
			    	for(Contract contract : client.getContracts())
			    	{
			    		if(contract.getId()== contractId)
			    		{
			    			FireAlertApplication a = (FireAlertApplication)getApplication();
					    	a.setLocation(contract); 	
			    		}
			    	}
			    }	
		}
		
		Intent openInspectionOverview= new Intent(InspectionController.this, ContractController.class);
    	//openInspectionView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    	startActivity(openInspectionOverview);
		
	}
	
	@Override
	public void onResume() 
	{
		// Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
        
	    super.onResume();  // Always call the superclass method first

	    FireAlertApplication a = (FireAlertApplication)getApplication();
    	a.setLocation(mFranchise); 	
	   
	}

}

package com.designproject;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class InspectionView extends ListActivity {

	// for storing the information. Loaded with dummy content
	private String[][] inspectionInformation =
		{
			{"McDonalds", "10/12/2013"},
			{"Wendys", "10/15/2013"}
		};
	
	// Adapters are used to bind to data. I want 2d so craete my own
	private SimpleAdapter simpleAdapter;
	
	//Create arraylist which will hold the string data
	ArrayList<HashMap<String, String>> listInformationString = new 
			ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspection_view);
		// Show the Up button in the action bar.
		setupActionBar();
		
		populateListInformation();
	}

	private void populateListInformation() 
	{
		//TODO: take in data from model classes and write to the member variables
		
		bindListView();
	}
	
	private void bindListView()
	{
		HashMap<String,String> item;
		
		for(int i=0; i<inspectionInformation.length; i++)
		{
			item = new HashMap<String, String>();
			item.put("line1", inspectionInformation[i][0]);
			item.put("line2", inspectionInformation[i][1]);
			listInformationString.add(item);
		}
		
		simpleAdapter =  new SimpleAdapter(this,listInformationString, R.layout.custom_two_lines,
				new String[] {"line1", "line2"},
				new int[] {R.id.line_1,
				R.id.line_2});
		
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
		Intent openInspectionOverview= new Intent(InspectionView.this, InspectionOverview.class);
    	//openInspectionView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    	startActivity(openInspectionOverview);
		
	}

}

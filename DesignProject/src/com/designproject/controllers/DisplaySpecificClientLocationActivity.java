package com.designproject.controllers;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.models.Building;
import com.designproject.models.Client;
import com.designproject.models.Contract;

/**
 * 
 * @author Jess
 * 
 */

public class DisplaySpecificClientLocationActivity extends
		NavigationDrawerActivity {

	private Client clients;
	private Contract[] contracts;
	private Building[] buildings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Content view must be set before call to NavigationDrawerActivity
		setContentView(R.layout.activity_display_specific_client_location);
		super.onCreate(savedInstanceState);

		// Get the listview from the view
		ListView list = (ListView) findViewById(R.id.android_clientlocationlist);

		// Create an adapter to hold values
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		// Get the application
		FireAlertApplication a = (FireAlertApplication) getApplication();
		a = (FireAlertApplication) getApplication();

		// Build the data by getting building address and bind to adapter
		for (Contract theContract : contracts) {
			buildings = theContract.getBuildings();

			for (Building theBuilding : buildings) {
				adapter.add(theBuilding.getAddress());
			}
		}

		list.setAdapter(adapter);
	}
}

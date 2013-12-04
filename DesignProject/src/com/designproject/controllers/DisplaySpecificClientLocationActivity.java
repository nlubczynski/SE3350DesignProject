package com.designproject.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
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
		Activity {

	private Client clients;
	private Contract[] contracts;
	private Building[] buildings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_specific_client_location);

		// Get list view inflated with XML
		ListView list = (ListView) findViewById(R.id.android_clientlocationlist);

		// Create an array adapter with a layout specified in XML
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		// Get application context
		FireAlertApplication a = (FireAlertApplication) getApplication();
		a = (FireAlertApplication) getApplication();

		clients = (Client) a.getClient();
		contracts = clients.getContracts();

		// Set page title
		setTitle(clients.getName() + " Locations");

		//Add building address to adapater
		for (Contract theContract : contracts) {
			buildings = theContract.getBuildings();

			for (Building theBuilding : buildings) {
				adapter.add(theBuilding.getAddress());
			}
		}

		list.setAdapter(adapter);
	}
}
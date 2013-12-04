package com.designproject.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.models.Client;
import com.designproject.models.Franchise;

/**
 * 
 * @author Jess
 * 
 */
public class DisplayListActivity extends NavigationDrawerActivity {

	private Franchise theFranchise;
	private Client[] clients;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Set page title
		setTitle("Clients");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_list);

		// Create adapter to bind data to list view
		ListView list = (ListView) findViewById(R.id.android_clientlist);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		// Get the application context
		FireAlertApplication a = (FireAlertApplication) getApplication();
		a = (FireAlertApplication) getApplication();
		theFranchise = (Franchise) a.getFranchise();
		clients = theFranchise.getClients();

		// Add all the clients to the list adapter
		for (Client theClient : clients) {
			adapter.add(theClient.getName());
		}

		list.setAdapter(adapter);

		// on click event to show selected client's locations
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				TextView selection = (TextView) arg1;
				String clientSelect = selection.getText().toString();
				
				//Find the client associated with clicked item
				//Redirect to page to show client's locations
				for (Client theClient : clients) {
					if (theClient.getName() == clientSelect) {
						FireAlertApplication a = (FireAlertApplication) getApplication();
						a.setClient(theClient);

						// Start display client's locations
						Intent displayClientLocations = new Intent(
								DisplayListActivity.this,
								DisplaySpecificClientLocationActivity.class);
						startActivity(displayClientLocations);
						break;
					}
				}
			}

		});
	}
}

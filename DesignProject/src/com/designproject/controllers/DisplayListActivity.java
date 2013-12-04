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
		// Content view must be set before call to NavigationDrawerActivity
		setContentView(R.layout.activity_display_list);
		super.onCreate(savedInstanceState);

		// Get the layouts list view
		ListView list = (ListView) findViewById(R.id.android_clientlist);

		// Get adapter to bind data to
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

		// Create on listener to redirect to client location's page on click
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				TextView selection = (TextView) arg1;
				String clientSelect = selection.getText().toString();

				for (Client theClient : clients) {
					if (theClient.getName() == clientSelect) {
						// selection.setText(theClient.getId());
						FireAlertApplication a = (FireAlertApplication) getApplication();
						a.setClient(theClient);

						// Display client's locations
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

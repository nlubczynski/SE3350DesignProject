package com.designproject.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.models.Building;
import com.designproject.models.Client;
import com.designproject.models.Contract;
import com.designproject.models.Franchise;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 
 * @author Jess
 * 
 */
public class DisplayLocationsActivity extends Activity {

	private Franchise theFranchise;
	private Client[] clients;
	private Contract[] contracts;
	private Building[] buildings;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Set title for activity
		setTitle("Locations");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_locations);

		// Get the list view inflated by set content view
		ListView list = (ListView) findViewById(R.id.android_locationlist);

		// Initiate a new array
		List<Map<String, String>> locationClientList = new ArrayList<Map<String, String>>();

		// Get the application context
		FireAlertApplication a = (FireAlertApplication) getApplication();
		a = (FireAlertApplication) getApplication();
		theFranchise = (Franchise) a.getFranchise();
		clients = theFranchise.getClients();

		// Bind a location and client name to a map
		for (Client theClient : clients) {
			contracts = theClient.getContracts();

			for (Contract theContract : contracts) {
				buildings = theContract.getBuildings();

				for (Building theBuilding : buildings) {
					Map<String, String> pairList = new HashMap<String, String>(
							2);
					pairList.put("location", theBuilding.getAddress());
					pairList.put("client", theClient.getName());
					locationClientList.add(pairList);
				}
			}
		}

		// Bind map to the adapter
		SimpleAdapter adapter = new SimpleAdapter(this, locationClientList,
				android.R.layout.simple_list_item_2, new String[] { "location",
						"client" }, new int[] { android.R.id.text1,
						android.R.id.text2 });

		list.setAdapter(adapter);
	}
}
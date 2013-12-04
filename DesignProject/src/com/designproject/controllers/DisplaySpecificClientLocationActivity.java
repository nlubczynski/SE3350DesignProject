package com.designproject.controllers;

import java.util.HashMap;
import java.util.Map;

import com.designproject.models.Building;
import com.designproject.models.Client;
import com.designproject.models.Contract;
import com.designproject.models.Floor;
import com.designproject.models.Franchise;
import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.R.layout;
import com.designproject.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author Jess
 *
 */

public class DisplaySpecificClientLocationActivity extends Activity {
	
	private Franchise theFranchise;
	private Client clients;
	private Contract [] contracts;
	private Building [] buildings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_specific_client_location);
		
		ListView list = (ListView) findViewById(R.id.android_clientlocationlist);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
			
		FireAlertApplication a = (FireAlertApplication)getApplication();
		a = (FireAlertApplication)getApplication();
		
		clients = (Client)a.getClient();
		contracts = clients.getContracts();
		
		setTitle(clients.getName() + " Locations");
		
		for (Contract theContract : contracts)
		{
			buildings = theContract.getBuildings();
			
			for (Building theBuilding : buildings)
			{
				adapter.add(theBuilding.getAddress());
			}
		}
		
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
				.inflate(R.menu.display_specific_client_location, menu);
		return true;
	}
}

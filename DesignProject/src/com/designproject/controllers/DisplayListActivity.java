package com.designproject.controllers;

import java.util.ArrayList;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.R.layout;
import com.designproject.R.menu;
import com.designproject.models.Client;
import com.designproject.models.Franchise;

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
public class DisplayListActivity extends Activity {

	private Franchise theFranchise;
	private Client [] clients;
	private Client singleClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setTitle("Clients");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_list);
		
		ListView list = (ListView) findViewById(R.id.android_clientlist);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		/*
		adapter.add("test");
		adapter.add("second test");
		list.setAdapter(adapter);*/
		
		FireAlertApplication a = (FireAlertApplication)getApplication();
		a = (FireAlertApplication)getApplication();
		theFranchise = (Franchise)a.getFranchise();
		clients = theFranchise.getClients();
		
		for (Client theClient : clients)
		{
			adapter.add(theClient.getName());
		}
		
		list.setAdapter(adapter);
		
		//switch between client name and client ID
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				TextView selection = (TextView)arg1;
				String clientSelect = selection.getText().toString();
				
				for (Client theClient : clients)
				{
					if (theClient.getName() == clientSelect)
					{
						selection.setText(theClient.getId());
						break;
					}
					if (theClient.getId() == clientSelect)
					{
						selection.setText(theClient.getName());
						break;
					}
				}	
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_list, menu);
		return true;
	}
}
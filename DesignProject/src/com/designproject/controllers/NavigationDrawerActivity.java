package com.designproject.controllers;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.models.Franchise;
import com.designproject.models.HelperMethods;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NavigationDrawerActivity extends Activity {

	private String[] mDrawerListTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private Franchise theFranchise;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated constructor stub
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.activity_main_menu);


		setupNavigationDrawer();

	}

	private void setupNavigationDrawer()
	{
		mDrawerListTitles = getResources().getStringArray(R.array.drawer_list_options);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set the adapater for the list view
		mDrawerList.setAdapter((new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawerListTitles)));

		// Bind a listener to the drawer list
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}

	private void displayClients()
	{
		Intent displayClientList = new Intent(NavigationDrawerActivity.this, DisplayListActivity.class);
		startActivity(displayClientList);
	}
	private void displayLocations()
	{
		Intent displayLocationList = new Intent(NavigationDrawerActivity.this, DisplayLocationsActivity.class);
		startActivity(displayLocationList);
	}
	private void displayInspections()
	{
		Intent openInspectionView = new Intent(NavigationDrawerActivity.this, InspectionController.class);
		startActivity(openInspectionView);
	}
	private void selectItem(int position) {
		// update the main content by replacing fragments
		// position is the 0-based placement in the toolbox
		switch(position){

		//display list of clients
		case 0:
			displayClients();
			break;
		//display list of locations
		case 1:
			displayLocations();
			break;
		//display list of inspections
		case 2:
			displayInspections();
			break;
		//settings?
		case 3:
			break;
		//logout
		case 4:
			signOut();
			break;    	
		}
	}

	public void signOut()
	{
		// Replace current code with LogOutHelper code
		HelperMethods.logOutHandler( HelperMethods.LOGOUT, this);
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

}
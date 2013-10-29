package com.designproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class InspectionOverview extends Activity {

    private TabHost myTabHost;
    private Contract mContract;
    private Building[] mBuildings;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspection_overview_parent);
		// Show the Up button in the action bar.
		setupActionBar();
		
		FireAlertApplication a = (FireAlertApplication)getApplication();
    	mContract = (Contract)a.getLocation();
		mBuildings = mContract.getBuildings();
		
		//Set up the tabs dynamically
		setUpTabs();
		
		
	}
	
	private void setUpTabs()
	{
		myTabHost = (TabHost)this.findViewById(android.R.id.tabhost);
		myTabHost.setup();
		
		for(Building building : mBuildings)
		{
			TabSpec ts1 = myTabHost.newTabSpec("TAB_TAG_1");
			String buildingName = building.getId();
			ts1.setIndicator("Building: " + buildingName);
			ts1.setContent(new TabHost.TabContentFactory(){
				public View createTabContent(String tag)
				{     
					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.inspection_overview_layout, null);
					
					return view;
					}       
				});
			
			myTabHost.addTab(ts1);
			TextView textView = (TextView)findViewById(R.id.textViewAddress);
			textView.setText(building.getAddress());
			
			textView= (TextView)findViewById(R.id.textViewFloorsValue);
			String numberOfFloorsString = String.valueOf(building.getFloors().length);
			textView.setText(numberOfFloorsString);
			
			int numberOfRooms = 0;
			int numberOfInspectionsElements = 0;
			int currentFloor = 0;
			
			for(Floor floor : building.getFloors())
			{
				numberOfRooms += floor.getRooms().length;
				
				for(Room room : floor.getRooms())
				{
					numberOfInspectionsElements += room.getEquipment().length;
				}
				
				addFloorButton(floor, currentFloor++);
			}
			
			textView= (TextView)findViewById(R.id.textViewRoomsValue);
			textView.setText(String.valueOf(numberOfRooms));
			
			textView = (TextView)findViewById(R.id.textViewItemsValue);
			textView.setText(String.valueOf(numberOfInspectionsElements));
			      
			}
		
		
	}

	private void addFloorButton(Floor floor, int buttonNum) {
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.LinearLayoutFloorButtons);
		
		Button button = new Button(this);
		button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		button.setText(floor.getName());
		button.setId( buttonNum );
		button.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Set the application to the floor we're going into
				FireAlertApplication a = (FireAlertApplication)getApplication();
				a.setLocation( mBuildings[ myTabHost.getCurrentTab() ].getFloors()[ v.getId() ] );
				Intent openRoomList = new Intent(InspectionOverview.this, RoomList.class);
				startActivity(openRoomList);				
			}

		});
		linearLayout.addView(button);
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
		getMenuInflater().inflate(R.menu.inspection_overview, menu);
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
	
	@Override
	public void onResume(){
		// Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
        
		super.onResume();
		FireAlertApplication a = (FireAlertApplication)getApplication();
		a.setLocation(mContract);
	}
	@Override
	public void onDestroy(){
		super.onResume();
		FireAlertApplication a = (FireAlertApplication)getApplication();
		a.setLocation(mContract);
		
	}

}

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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


public class InspectionOverview extends Activity {

    private TabHost myTabHost;
    private Contract mContract;
    private Building[] mBuildings;
    private int mCurrentTag;
    boolean semaphore;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspection_overview_parent);
		// Show the Up button in the action bar.
		setupActionBar();
		
		FireAlertApplication a = (FireAlertApplication)getApplication();
    	mContract = (Contract)a.getLocation();
		mBuildings = mContract.getBuildings();
		
		//Set up the tabs dynamically
		setUpTabs(0);
		
		
	}
	
	private void setUpTabs(int currentTabIndex)
	{
		semaphore = true;
		
		myTabHost = (TabHost)this.findViewById(android.R.id.tabhost);

		myTabHost.setup();
		
		final Building buildingClicked = mBuildings[currentTabIndex];
		int tag = 0;
		
		for(Building building : mBuildings)
		{
			TabSpec ts1 = myTabHost.newTabSpec(String.valueOf(tag));
			tag++;
			
			final String buildingName = building.getId();
			ts1.setIndicator("Building: " + buildingName);
			ts1.setContent(new TabHost.TabContentFactory(){
				public View createTabContent(String tag)
				{
					LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.inspection_overview_layout, null);
					((TextView)view.findViewById(R.id.textViewAddress)).setText(buildingClicked.getAddress());
					((TextView)view.findViewById(R.id.textViewFloorsValue)).setText(String.valueOf(buildingClicked.getFloors().length));
					
					int numberOfRooms = 0;
					int numberOfInspectionsElements = 0;
					int currentFloor = 0;
					
					for(Floor floor : buildingClicked.getFloors())
					{
						numberOfRooms += floor.getRooms().length;
						
						for(Room room : floor.getRooms())
						{
							numberOfInspectionsElements += room.getEquipment().length;
						}
						
						LinearLayout ll = ((LinearLayout)view.findViewById(R.id.LinearLayoutFloorButtons));


						//if((buildingName).equals(buildingClicked.getId()))
							addFloorButton(floor, currentFloor++, ll);
						
					}
					
					((TextView)view.findViewById(R.id.textViewRoomsValue)).setText(String.valueOf(numberOfRooms));
					((TextView)view.findViewById(R.id.textViewItemsValue)).setText(String.valueOf(numberOfInspectionsElements));

					return view;
					}       
				});
			

			myTabHost.addTab(ts1);

			semaphore = false;      
	}
		//Set up the tab host click event that changes and reloads the tab
		myTabHost.setOnTabChangedListener(new OnTabChangeListener(){
			@Override
			public void onTabChanged(String tabId) {
			// TODO Auto-generated method stub
				if(semaphore)
					return;
				
				String selectedTab = myTabHost.getCurrentTabTag();
				
				if(!selectedTab.equals("0"))
				{
					mCurrentTag = Integer.parseInt(selectedTab);
					myTabHost.setCurrentTab(0);
				}
				
				myTabHost.clearAllTabs();

				setUpTabs(mCurrentTag);// selected
				}
			});
		}
			

	private void addFloorButton(Floor floor, int buttonNum, LinearLayout ll) {
		//LinearLayout linearLayout = (LinearLayout)findViewById(R.id.LinearLayoutFloorButtons);
		//HorizontalScrollView hsv= (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
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
		
		ll.addView(button);
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

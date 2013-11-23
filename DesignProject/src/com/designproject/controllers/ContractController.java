package com.designproject.controllers;

import org.xmlpull.v1.XmlPullParserException;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.models.Building;
import com.designproject.models.Contract;
import com.designproject.models.Floor;
import com.designproject.models.HelperMethods;
import com.designproject.models.Room;
import com.designproject.models.XMLReaderWriter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


public class ContractController extends NavigationDrawerActivity {

    private TabHost myTabHost;
    private Contract mContract;
    private Building[] mBuildings;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
		
		setContentView(R.layout.activity_inspection_overview_parent);

		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		setupActionBar();
		
		FireAlertApplication a = (FireAlertApplication)getApplication();
    	mContract = (Contract)a.getLocation();
		mBuildings = mContract.getBuildings();
		
		setUpTabs();
	}
	
	private void setUpTabs()
	{		
		myTabHost = (TabHost)this.findViewById(android.R.id.tabhost);
	
		//setup must be called whenever tabs are created and added
		myTabHost.setup();

		int tag = 0;

		for(final Building building : mBuildings)
		{
			TabSpec ts1 = myTabHost.newTabSpec(String.valueOf(tag));
			tag++;
			
			// Get the context so the once in createTabContent you have it
			final Context context = this;
			
			//Set tab name
			String buildingName = building.getId();
			ts1.setIndicator("Building: " + buildingName);
			
			//Set up content of each tab. This will inflate a view which has only labels.
			//Each corresponding value for the labels is made progmatically then added to its respective linear layout
			ts1.setContent(new TabHost.TabContentFactory(){
				public View createTabContent(String tag)
				{
					//Set the paramaters for the new text fields
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			               0, LayoutParams.WRAP_CONTENT);
			            params.weight=2f;
				       
					LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.inspection_overview_container, null);
					
					//Define a linearlayout
					LinearLayout layout1 = null;
					
					//Get necessary linearLayout and add necessary view
					layout1 = (LinearLayout)(((ViewGroup)view).getChildAt(0));
					final TextView textViewBuildingStatus = new TextView(context);
					textViewBuildingStatus.setTextAppearance(context, android.R.attr.textAppearanceLarge);
					textViewBuildingStatus.setLayoutParams(params);
					textViewBuildingStatus.setGravity(Gravity.RIGHT);
					
					//Check if building is complete
					
					if(building.isCompleted())
					{
						textViewBuildingStatus.setText("COMPLETE");
						textViewBuildingStatus.setTextColor(Color.GREEN);
					}
					else
					{
						textViewBuildingStatus.setText("INCOMPLETE");
						textViewBuildingStatus.setTextColor(Color.RED);
					}
					layout1.addView(textViewBuildingStatus);
					
					layout1 = (LinearLayout)(((ViewGroup)view).getChildAt(1));
					TextView textViewAddress = new TextView(context);
					textViewAddress.setText(building.getAddress());
					textViewAddress.setLayoutParams(params);
					textViewAddress.setGravity(Gravity.RIGHT);
					layout1.addView(textViewAddress);
					
					layout1 = (LinearLayout)(((ViewGroup)view).getChildAt(2));
					TextView  textViewFloor = new TextView(context);
					textViewFloor.setText(String.valueOf(building.getFloors().length));
					textViewFloor.setLayoutParams(params);
					textViewFloor.setGravity(Gravity.RIGHT);
					layout1.addView(textViewFloor);
								
					int numberOfRooms = 0;
					int numberOfInspectionsElements = 0;
					int currentFloor = 0;
					
					//loop through floors and buildings to get required fields
					for(Floor floor : building.getFloors())
					{
						numberOfRooms += floor.getRooms().length;
						
						for(Room room : floor.getRooms())
						{
							numberOfInspectionsElements += room.getEquipment().length;
						}
						
						// layout is not available in addFloorButton so get it and pass it in
						LinearLayout linearLayoutFloorButtonContainer = ((LinearLayout)view.findViewById(R.id.LinearLayoutFloorButtons));
						addFloorButton(floor, currentFloor++, linearLayoutFloorButtonContainer);
					}
					
					layout1 = (LinearLayout)(((ViewGroup)view).getChildAt(3));
					TextView  textViewRooms = new TextView(context);
					textViewRooms.setText(String.valueOf(numberOfRooms));
					textViewRooms.setLayoutParams(params);
					textViewRooms.setGravity(Gravity.RIGHT);
					layout1.addView(textViewRooms);
					
					layout1 = (LinearLayout)(((ViewGroup)view).getChildAt(4));
					TextView  textViewInspectionElements = new TextView(context);
					textViewInspectionElements.setText(String.valueOf(numberOfInspectionsElements));
					textViewInspectionElements.setLayoutParams(params);
					textViewInspectionElements.setGravity(Gravity.RIGHT);
					layout1.addView(textViewInspectionElements);
					
					layout1 = (LinearLayout)(((ViewGroup)view).getChildAt(5));
					Button  submitButton = new Button(context);
					submitButton.setText(R.string.submit_building);
					submitButton.setTag(building.getId());
					submitButton.setEnabled(false);
					submitButton.setOnClickListener(new Button.OnClickListener() {  
				        public void onClick(View v)
			            {
				        	try {
				    			XMLReaderWriter out = new XMLReaderWriter(context);
				    			
				    			FireAlertApplication a = (FireAlertApplication)getApplication();
				    			out.writeXML( a.getFranchise() );
				    			v.setEnabled(false);
				    			textViewBuildingStatus.setText("SENT");
				    		} catch (XmlPullParserException e) {
				    			// TODO Auto-generated catch block
				    			e.printStackTrace();
				    		}
			            }
			         });
					layout1.addView(submitButton);

					return view;
					}       
				});
			
			//add tab spec to the tab host
			myTabHost.addTab(ts1);
		}
	}

	private void addFloorButton(Floor floor, int buttonNum, LinearLayout linearLayoutFloorButtonContainer) {
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
				Intent openRoomList = new Intent(ContractController.this, FloorController.class);
				startActivity(openRoomList);				
			}

		});
		
		linearLayoutFloorButtonContainer.addView(button);
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
        
		//Set up the tabs dynamically
        myTabHost.clearAllTabs();
        setUpTabs();
		
		super.onResume();
		FireAlertApplication a = (FireAlertApplication)getApplication();
		a.setLocation(mContract);
		
		for(Building building : mBuildings) {
			if (building.isCompleted()) {
				View parent = findViewById(R.id.LinearLayout1);
				Button submitButton = (Button) parent.findViewWithTag(building.getId());
				submitButton.setEnabled(true);
			}
		}
	}
	@Override
	public void onDestroy(){
		super.onResume();
		FireAlertApplication a = (FireAlertApplication)getApplication();
		a.setLocation(mContract);
	}
}

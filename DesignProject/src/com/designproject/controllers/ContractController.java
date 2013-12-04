package com.designproject.controllers;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.models.Building;
import com.designproject.models.Contract;
import com.designproject.models.Floor;
import com.designproject.models.HelperMethods;
import com.designproject.models.Room;
import com.designproject.models.XMLReaderWriter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
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
    List<Button> buttons;
    List<TextView> textViews;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Check if Logged in
		HelperMethods.logOutHandler(HelperMethods.CHECK_IF_LOGGED_IN, this);

		setContentView(R.layout.activity_inspection_overview_parent);

		super.onCreate(savedInstanceState);

		// Get the current application instance and the class associated with
		// this activity
		FireAlertApplication a = (FireAlertApplication) getApplication();
		mContract = (Contract) a.getLocation();

		// Get all buildings associated with the current contract
		mBuildings = mContract.getBuildings();
		
		buttons = new ArrayList<Button>();
		textViews = new ArrayList<TextView>();

		setUpTabs();
	}

	/**
	 * Method to initiate the tabs
	 */
	private void setUpTabs() {
		myTabHost = (TabHost) this.findViewById(android.R.id.tabhost);

		// Setup must be called whenever tabs are created and/or added
		myTabHost.setup();

		int tag = 0;

		// Loop through the buildings and create a tab for each one
		for (final Building building : mBuildings) {
			// TabSpec is used to create the information for the tab
			TabSpec ts1 = myTabHost.newTabSpec(String.valueOf(tag));
			tag++;

			// Get the context so that the method createTabContent can use
			// Must be final so that createTabContent can use it
			final Context context = this;

			// Set tab name with an icon
			String buildingName = building.getId();
			ts1.setIndicator("Building: " + buildingName, this.getResources()
					.getDrawable(R.drawable.ic_tab_spec_selected));

			// Set up content of each tab. This will inflate a view which has
			// only labels.
			// Each corresponding value for the label is made progmatically
			// through code in createTabContent
			// The values are then added to the respective linear layout
			ts1.setContent(new TabHost.TabContentFactory() {
				public View createTabContent(String tag) {
					// Set the parameters for the new text fields
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							0, LayoutParams.WRAP_CONTENT);
					params.weight = 2f;

					// Inflate layout
					LayoutInflater inflater = (LayoutInflater) getBaseContext()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(
							R.layout.inspection_overview_container, null);

					// Define a linearlayout
					LinearLayout layout1 = null;

					// Get necessary linearLayout and the building status text
					// to layout
					layout1 = (LinearLayout) (((ViewGroup) view).getChildAt(0));
					final TextView textViewBuildingStatus = new TextView(
							context);
					textViewBuildingStatus.setTextAppearance(context,
							android.R.attr.textAppearanceLarge);
					textViewBuildingStatus.setLayoutParams(params);
					textViewBuildingStatus.setGravity(Gravity.RIGHT);
					textViewBuildingStatus.setTag(building.getId() + " status");
					textViews.add(textViewBuildingStatus);

					// Check if building is complete and update building status
					// label accordingly
					if (building.isCompleted()) {
						textViewBuildingStatus.setText("COMPLETE");
						textViewBuildingStatus.setTextColor(Color.GREEN);
					} else {
						textViewBuildingStatus.setText("INCOMPLETE");
						textViewBuildingStatus.setTextColor(Color.RED);
					}

					layout1.addView(textViewBuildingStatus);

					// Get necessary linearLayout and add the building's address
					// to the view
					layout1 = (LinearLayout) (((ViewGroup) view).getChildAt(1));
					TextView textViewAddress = new TextView(context);
					textViewAddress.setText(building.getAddress());
					textViewAddress.setLayoutParams(params);
					textViewAddress.setGravity(Gravity.RIGHT);
					layout1.addView(textViewAddress);

					// Get necessary linearLayout and add number of floors to
					// the view
					layout1 = (LinearLayout) (((ViewGroup) view).getChildAt(2));
					TextView textViewFloor = new TextView(context);
					textViewFloor.setText(String.valueOf(building.getFloors().length));
					textViewFloor.setLayoutParams(params);
					textViewFloor.setGravity(Gravity.RIGHT);
					layout1.addView(textViewFloor);

					int numberOfRooms = 0;
					int numberOfInspectionsElements = 0;
					int currentFloor = 0;

					// loop through floors and buildings to get number of rooms
					// and inspection elements
					for (Floor floor : building.getFloors()) {
						numberOfRooms += floor.getRooms().length;

						for (Room room : floor.getRooms()) {
							numberOfInspectionsElements += room.getEquipment().length;
						}

						// layout is not available in addFloorButton so get it
						// and pass it in
						LinearLayout linearLayoutFloorButtonContainer = ((LinearLayout) view
								.findViewById(R.id.LinearLayoutFloorButtons));

						// Create a floor button for each floor
						addFloorButton(floor, currentFloor++,
								linearLayoutFloorButtonContainer);
					}

					// Get necessary linearLayout and add number of rooms to the
					// view
					layout1 = (LinearLayout) (((ViewGroup) view).getChildAt(3));
					TextView textViewRooms = new TextView(context);
					textViewRooms.setText(String.valueOf(numberOfRooms));
					textViewRooms.setLayoutParams(params);
					textViewRooms.setGravity(Gravity.RIGHT);
					layout1.addView(textViewRooms);

					// Get necessary linearLayout and add number of inspection
					// elements to the view
					layout1 = (LinearLayout) (((ViewGroup) view).getChildAt(4));
					TextView textViewInspectionElements = new TextView(context);
					textViewInspectionElements.setText(String
							.valueOf(numberOfInspectionsElements));
					textViewInspectionElements.setLayoutParams(params);
					textViewInspectionElements.setGravity(Gravity.RIGHT);
					layout1.addView(textViewInspectionElements);

					// Get necessary linearLayout and add submit button to the
					// view
					layout1 = (LinearLayout) (((ViewGroup) view).getChildAt(5));
					Button submitButton = new Button(context);
					submitButton.setText(R.string.submit_building);
					submitButton.setTag(building.getId());
					submitButton.setEnabled(false);
					submitButton.setBackgroundResource(R.drawable.backgrounds);
					buttons.add(submitButton);
					
					//Create on click listener for the submit button
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

			// Add tab spec to the tab host
			myTabHost.addTab(ts1);
		}
	}

	/**
	 * Adds a button to direct user to a floor
	 */
	private void addFloorButton(Floor floor, int buttonNum,
			LinearLayout linearLayoutFloorButtonContainer) {
		Button button = new Button(this);

		// Set button parameters
		LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.setMargins(5, 5, 5, 5);
		button.setLayoutParams(params);

		button.setText(floor.getName());
		button.setId(buttonNum);
		button.setBackgroundResource(R.drawable.backgrounds);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Set the location to the floor we're going into
				// Open activity that corresponds to that floor
				FireAlertApplication a = (FireAlertApplication) getApplication();
				a.setLocation(mBuildings[myTabHost.getCurrentTab()].getFloors()[v
						.getId()]);
				Intent openRoomList = new Intent(ContractController.this,
						FloorController.class);
				startActivity(openRoomList);
			}

		});

		linearLayoutFloorButtonContainer.addView(button);
	}


	@Override
	public void onRestart() {

		// Check if Logged in
		HelperMethods.logOutHandler(HelperMethods.CHECK_IF_LOGGED_IN, this);

		// Super must be called
		super.onRestart();

		// Set the application's location to the contract which corresponds to
		// this activity
		FireAlertApplication a = (FireAlertApplication) getApplication();
		a.setLocation(mContract);

		// Check if the buildings associated with this contract are complete
		// Update their status accordingly
		for (Building building : mBuildings) {
			if (building.isCompleted()) {
				for(Button b: buttons)
					if(b.getTag().equals(building.getId()))
						b.setEnabled(true);
				
				for(TextView tv: textViews)
					if(tv.getTag().equals(building.getId()+" status")){
						tv.setText("COMPLETE");
						tv.setTextColor(Color.GREEN);
					}
			}
		}
	}
}

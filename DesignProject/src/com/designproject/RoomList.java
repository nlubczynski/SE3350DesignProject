package com.designproject;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class RoomList extends ListActivity {
	
	Room [] rooms;
	Floor mFloor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_list);
		setupActionBar();
		
		
		FireAlertApplication a = (FireAlertApplication)getApplication();
		mFloor = (Floor)a.getLocation();
		rooms = mFloor.getRooms();
		
		ArrayList<HashMap<String,String>> listInformationString = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> item;
		
		for(Room inspectionInformation : rooms)
		{
			item = new HashMap<String, String>();
			item.put( "line1", inspectionInformation.getRoomNo() );
			item.put( "line2", inspectionInformation.getId() );
			item.put( "line3", String.valueOf( inspectionInformation.getEquipment().length ) );
			listInformationString.add(item);
		}
		
		SimpleAdapter simpleAdapter =  new SimpleAdapter(this,listInformationString, R.layout.room_list_item,
			new String[] {"line1", "line2", "line3"},
			new int[] {R.id.RoomNameValue, R.id.RoomIDValue, R.id.numOfElementsValue}
		);
		
		///bind the data
		setListAdapter(simpleAdapter); 
		
		updateStatus();
	}
	
	public void roomItemListener(View view)
	{
		View base =  ((ViewGroup)view).getChildAt(0);
		View second = ((ViewGroup)base).getChildAt(1);
		String text = (String) ((TextView)second).getText();
		
		for(Room r: rooms)
			if( r.getRoomNo().equals(text) ){
				FireAlertApplication a = (FireAlertApplication)getApplication();
				a.setLocation( r );
				break;
			}
		
		Intent openInspectionOverview= new Intent(RoomList.this, EquipmentInspectionList.class);
    	startActivity(openInspectionOverview);
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
		getMenuInflater().inflate(R.menu.room_list, menu);
		return true;
	}

	@Override
	public void onResume(){
		// Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
		super.onResume();
		FireAlertApplication a = (FireAlertApplication)getApplication();
		a.setLocation(mFloor);
		
		updateStatus();
	}
	
	private void updateStatus() {
		ListView listView = getListView();
		if(listView.getChildCount() == rooms.length)
		{

			for(int i = 0; i < rooms.length; i++)
			{
				View listElement = (ViewGroup)listView.getChildAt(i);
				if (rooms[i].isCompleted())
				{
	        		View toBeColored = (View)((ViewGroup)listElement).getChildAt(0);
	        		TextView textToBeColored1 = (TextView)((ViewGroup) toBeColored).getChildAt(0);
	        		TextView textToBeColored2 = (TextView)((ViewGroup) toBeColored).getChildAt(1);
	        		textToBeColored1.setTextColor(getResources().getColor(R.color.green));
	        		textToBeColored2.setTextColor(getResources().getColor(R.color.green));
				}
				Equipment[] equipment = rooms[i].getEquipment();
				int incompleteCount = 0;
				for(int j = 0; j < equipment.length; j++)
				{
					if(!equipment[j].isCompleted())
						incompleteCount++;
				}
				View toBeUpdated = (View)((ViewGroup)listElement).getChildAt(2);
				TextView textToChange = (TextView)((ViewGroup) toBeUpdated).getChildAt(1);
				textToChange.setText(Integer.toString(incompleteCount));
			}
		}
	}

	@Override
	public void onDestroy(){
		super.onResume();
		FireAlertApplication a = (FireAlertApplication)getApplication();
		a.setLocation(mFloor);
		
	}
}

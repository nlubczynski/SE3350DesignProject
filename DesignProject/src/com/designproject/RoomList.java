package com.designproject;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.os.Build;

public class RoomList extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_list);
		setupActionBar();
		
		FireAlertApplication a = (FireAlertApplication)getApplication();
		Room[] rooms = ((Building)a.getLocation()).getFloors()[0].getRooms();
		
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
				new int[] {R.id.RoomNameValue,
				R.id.RoomIDValue,
				R.id.numOfElementsValue});
		
		///bind the data
		setListAdapter(simpleAdapter);
		 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}			
		});
 
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

}

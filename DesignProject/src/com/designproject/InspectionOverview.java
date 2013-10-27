package com.designproject;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class InspectionOverview extends Activity {

    private TabHost myTabHost;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspection_overview_parent);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Set up the tabs dynamically
		setUpTabs();
		
	}
	
	private void setUpTabs()
	{
		myTabHost = (TabHost)this.findViewById(android.R.id.tabhost);
		myTabHost.setup();
		
		
		
		for(int i = 0 ; i<3; i++)
		{
			TabSpec ts1 = myTabHost.newTabSpec("TAB_TAG_1");
			ts1.setIndicator("Tab1");
			ts1.setContent(new TabHost.TabContentFactory(){
				public View createTabContent(String tag)
				{     
					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.inspection_overview_layout, null);
					
					return view;
					}       
				});
			myTabHost.addTab(ts1);
			if(i%2==0)
			{
				TextView textView = (TextView)findViewById(R.id.textViewAddress);
				textView.setText("115 Edgevalley Circle");
				}        
			}
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
	
	public void inspectListener(View view)
	{
		Intent openEquipmentInspectionList = new Intent(InspectionOverview.this, EquipmentInspectionList.class);
		startActivity(openEquipmentInspectionList);
	}

}

package com.designproject.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.designproject.R;
import com.designproject.models.HelperMethods;

public class NavigationDrawerActivity extends SherlockActivity {

	private String[] mDrawerListTitles;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private boolean useLogo = true;
    private boolean showHomeUp = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated constructor stub
		super.onCreate(savedInstanceState);

		
		//getSupportActionBar().setIcon(R.drawable.ic_nav_drawer);
		setupNavigationDrawer();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	 if (item.getItemId() == android.R.id.home) {

    	        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
    	        	mDrawerLayout.closeDrawer(mDrawerList);
    	        } else {
    	        	mDrawerLayout.openDrawer(mDrawerList);
    	        }
    	    }
    	
            return super.onOptionsItemSelected(item);
    }

	private void setupNavigationDrawer()
	{
         mDrawerListTitles = getResources().getStringArray(R.array.drawer_list_options);
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         mDrawerList = (ListView) findViewById(R.id.left_drawer);
         
         mDrawerToggle = new ActionBarDrawerToggle(
                 this,                  /* host Activity */
                 mDrawerLayout,         /* DrawerLayout object */
                 R.drawable.ic_nav_drawer,  /* nav drawer icon to replace 'Up' caret */
                 R.string.drawer_open,  /* "open drawer" description */
                 R.string.drawer_close  /* "close drawer" description */
                 ) {
        	 
        	 public void onDrawerClosed(View view) {
        		 super.onDrawerClosed(view);
        	    }

        	    public void onDrawerOpened(View drawerView) {
        	        super.onDrawerOpened(drawerView);
        	    }
         };
                 
         //Set the drawer toggle as the DrawerListener
         mDrawerLayout.setDrawerListener(mDrawerToggle);
                
         final ActionBar ab = getSupportActionBar();
 		
 		ab.setDisplayHomeAsUpEnabled(showHomeUp);
 		ab.setDisplayUseLogoEnabled(useLogo);
         
         // Set the adapater for the list view through custom adapter
         AdapterClass adpClass = new AdapterClass(this, mDrawerListTitles);
         mDrawerList.setAdapter(adpClass);
         
         // Bind a listener to the drawer list
         mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
         
	}
	
	   @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }
	   
	   @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }
	
    private void loadSettingsPage() {
		// TODO Auto-generated method stub
    	
    	Intent openSettingsPage = new Intent (NavigationDrawerActivity.this, SettingsController.class);
    	startActivity(openSettingsPage);
	}
	
    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    public class AdapterClass  extends ArrayAdapter<String> {
    	Context context;
    	private String[] TextValue;

    	public AdapterClass(Context context, String[] mDrawerListTitles) {
    	    super(context, R.layout.drawer_list_item, mDrawerListTitles);
    	    this.context = context;
    	    this.TextValue= mDrawerListTitles;

    	}

    	@Override
    	public View getView(int position, View coverView, ViewGroup parent) {
    	    // TODO Auto-generated method stub

    	    LayoutInflater inflater = (LayoutInflater) context
    	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    View rowView = inflater.inflate(R.layout.drawer_list_item,
    	            parent, false);

    	    TextView text1 = (TextView)rowView.findViewById(R.id.txtNavDrawer);
    	    
    	    //the position corresponds to the text that is outputted
    	    text1.setText(TextValue[position]);
    	    ImageView image = (ImageView)rowView.findViewById(R.id.imageViewNavDrawer);
    	    
    	    if(position == 0)
    	    	image.setImageResource(R.drawable.ic_client_light);
    	    else if(position == 1)
    	    	image.setImageResource(R.drawable.ic_tab_spec_selected);
    	    else if(position == 2)
    	    	image.setImageResource(R.drawable.ic_inspection_light);
    	    else if(position == 3)
    	    	image.setImageResource(R.drawable.ic_settings_light);
    	    else if(position == 4)
    	    	image.setImageResource(R.drawable.ic_logout_light);

    	    return rowView;
    	    }
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
			loadSettingsPage();
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
}

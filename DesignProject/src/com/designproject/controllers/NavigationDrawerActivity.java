package com.designproject.controllers;

import com.designproject.R;
import com.designproject.models.HelperMethods;

import android.app.Activity;
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
	
    private void selectItem(int position) {
        // update the main content by replacing fragments
        // position is the 0-based placement in the toolbox
    	switch(position){
    	case 4:
    		//logout
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



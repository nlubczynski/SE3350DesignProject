package com.designproject.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.designproject.R;
import com.designproject.models.HelperMethods;

public class NavigationDrawerActivity extends Activity {

	private String[] mDrawerListTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated constructor stub
        super.onCreate(savedInstanceState);
        
        setupNavigationDrawer();
	}
	
	private void setupNavigationDrawer()
    {
         mDrawerListTitles = getResources().getStringArray(R.array.drawer_list_options);
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         mDrawerList = (ListView) findViewById(R.id.left_drawer);
         
         // Set the adapater for the list view through custom adapter
         AdapterClass adpClass = new AdapterClass(this, mDrawerListTitles);
         mDrawerList.setAdapter(adpClass);
         
         // Bind a listener to the drawer list
         mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }
	
    private void selectItem(int position) {
        // update the main content by replacing fragments
        // position is the 0-based placement in the toolbox
    	switch(position){
    	
    	case 2:
    		loadSettingsPage();
    		break;
    	case 3:
    		//logout
    		signOut();
    		break;    	
    	}
    }
    
    private void loadSettingsPage() {
		// TODO Auto-generated method stub
    	
    	Intent openSettingsPage = new Intent (NavigationDrawerActivity.this, SettingsController.class);
    	startActivity(openSettingsPage);
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

    	    return rowView;
    	    }
    	}
}



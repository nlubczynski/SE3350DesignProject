package com.designproject;

import java.io.IOException;

import com.designproject.FireAlertApplication;

import org.xmlpull.v1.XmlPullParserException;

import com.designproject.R;
import com.designproject.R.layout;
import com.designproject.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainMenu extends Activity {

	private String[] mDrawerListTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		try {
			XMLReaderWriter reader = new XMLReaderWriter( this.getApplicationContext() );
			FireAlertApplication a = (FireAlertApplication)getApplication();
			a.setFranchise( reader.parseXML() );
			Franchise franchise = a.getFranchise();
			franchise.getId();
		}catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end");
        
        setContentView(R.layout.activity_main_menu);
        
        
   
        mDrawerListTitles = getResources().getStringArray(R.array.drawer_list_options);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        // Set the adapater for the list view
        mDrawerList.setAdapter((new ArrayAdapter<String>(this,
        		R.layout.drawer_list_item, mDrawerListTitles)));
        
        // Set the adapter for this list view
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		  MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.activity_main_menu, menu);
	        return super.onCreateOptionsMenu(menu);
    }
    
    public void signOut(View view)
    {
    	SharedPreferences preferences = getSharedPreferences("Login",0);
    	preferences.edit().remove("Username");
    	preferences.edit().remove("Password");
    	
    	Intent loginScreen = new Intent(MainMenu.this, LoginScreen.class);
    	startActivity(loginScreen);
    }
    
    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    private void selectItem(int position) {
        // update the main content by replacing fragments

    }
    
    public void inspectionClickListener(View view)
    {
    	
    	Intent openInspectionView = new Intent(MainMenu.this, InspectionView.class);
    	//openInspectionView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    	startActivity(openInspectionView);
    }
    
    
}

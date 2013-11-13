package com.designproject;

import java.io.IOException;
import java.util.Calendar;

import com.designproject.FireAlertApplication;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.xmlpull.v1.XmlPullParserException;

import com.designproject.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainMenu extends Activity {

    private String[] mDrawerListTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Franchise mFranchise;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
        
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        
        try {
        	//Create the reader writer
        	XMLReaderWriter reader = new XMLReaderWriter( this.getApplicationContext() );
			// Get out applciation
        	FireAlertApplication a = (FireAlertApplication)getApplication();
			// Parse the xml and set the franchise to our application
        	a.setFranchise( reader.parseXML() );
			//We're currently in the franchise
        	a.setLocation( a.getFranchise() );
        	//Set the local variable for franchise
			mFranchise = (Franchise) a.getLocation();
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

        
        setupNavigationDrawer();
        // Set the adapater for the list view
        mDrawerList.setAdapter((new ArrayAdapter<String>(this,
        		R.layout.drawer_list_item, mDrawerListTitles)));
        
        // Set the adapter for this list view
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        calculateDates();
    }
    
    @Override
    public void onResume(){
    	// Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
    	super.onResume();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
          MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_main_menu, menu);
            return super.onCreateOptionsMenu(menu);
    }
    
    public void signOut()
    {
    	// Replace current code with LogOutHelper code
    	HelperMethods.logOutHandler( HelperMethods.LOGOUT, this);
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
    
    public void inspectionClickListener(View view)
    {
        
        Intent openInspectionView = new Intent(MainMenu.this, InspectionView.class);
        //openInspectionView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(openInspectionView);
    }
    
    public void imageButtonLogoListener(View view)
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }
    
    private void calculateDates()
    {
        int dueToday = 0, doneToday = 0, dueNextWeek = 0, doneNextWeek = 0, dueNextMonth = 0, doneNextMonth = 0, totalContractsCompleted = 0;
                
        /*TODO: Want to convert the last progress bar into a total contracts */
        
        for(Client client : mFranchise.getClients())
        {
            totalContractsCompleted = client.getContracts().length;
            
            
            for(Contract contract : client.getContracts())
            {
                Calendar startDateCalendar = contract.getStartDate();
                Calendar endDateCalendar = contract.getEndDate();
                String contractTerms = contract.getTerms();
                
                Interval nextInspectionPeriod = HelperMethods.returnNextInspectionInterval(startDateCalendar, endDateCalendar, contractTerms);
                
                DateTime nextInspectionDate = nextInspectionPeriod.getEnd();
                
                LocalDate localDateToday = DateTime.now().toLocalDate();
                LocalDate nextInspectionDateLocal = nextInspectionDate.toLocalDate();
                
                if(nextInspectionDateLocal.isEqual(localDateToday))
                {
                    dueToday++;
                    
                    if(HelperMethods.checkIfInspectionCompleted(nextInspectionPeriod, contract))
                    {
                        doneToday++;
                    }
                    
                    break;
                }
                else if (nextInspectionDate == DateTime.now().plusWeeks(1))
                {
                    dueNextWeek++;
                    
                    if(HelperMethods.checkIfInspectionCompleted(nextInspectionPeriod, contract))
                    {
                        doneNextWeek++;
                    }
                }
                else if (nextInspectionDate.isBefore(DateTime.now().plusMonths(1)))
                {
                    dueNextMonth++;
                    
                    if(HelperMethods.checkIfInspectionCompleted(nextInspectionPeriod, contract))
                    {
                        doneNextMonth++;
                    }
                }
            }
        }
        
        updateProgressBars(dueToday, doneToday, dueNextWeek, doneNextWeek, dueNextMonth, doneNextMonth, totalContractsCompleted);
    }
    
    private void updateProgressBars(int dueToday, int doneToday, int dueNextWeek, int doneNextWeek, 
            int dueNextMonth, int doneNextMonth, int totalContractsCompleted)
    {
        ProgressBar progressBarToday = (ProgressBar) findViewById(R.id.progressBarToday);
        ProgressBar progressBarWeek = (ProgressBar)findViewById(R.id.progressBarWeek);
        ProgressBar progressBarMonth = (ProgressBar)findViewById(R.id.progressBarMonth);
        
        TextView textViewTodayProgress = (TextView) findViewById(R.id.textViewTodayValue);
        TextView textViewWeekProgress = (TextView) findViewById(R.id.textViewWeekValue);
        TextView textViewMonthProgress = (TextView) findViewById(R.id.textViewMonthValue);
        
        if(dueToday != 0)
            progressBarToday.setProgress(doneToday/dueToday*100);
        else
            progressBarToday.setProgress(0);

        if(dueNextWeek != 0)
            progressBarWeek.setProgress(doneNextWeek/dueNextWeek*100);
        else
            progressBarWeek.setProgress(0);

        if(dueNextMonth != 0)
            progressBarMonth.setProgress(doneNextMonth/dueNextMonth*100);
        else
            progressBarMonth.setProgress(0);
        
        textViewWeekProgress.setText(doneNextWeek + "/" + dueNextWeek);
        textViewTodayProgress.setText(doneToday + "/" + dueToday);
        textViewMonthProgress.setText(doneNextMonth + "/" + dueNextMonth);
    }
    
    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    
}

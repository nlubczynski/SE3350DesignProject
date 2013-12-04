package com.designproject.controllers;

import java.io.IOException;
import java.util.Calendar;

import com.designproject.FireAlertApplication;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.xmlpull.v1.XmlPullParserException;

import com.designproject.R;
import com.designproject.models.Client;
import com.designproject.models.Contract;
import com.designproject.models.Franchise;
import com.designproject.models.HelperMethods;
import com.designproject.models.XMLReaderWriter;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainMenu extends NavigationDrawerActivity {

    private Franchise mFranchise;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
        //super.(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        
        
        
        try {
        	//Create the reader writer
        	XMLReaderWriter reader = new XMLReaderWriter( this.getApplicationContext() );
			// Get out application
        	FireAlertApplication a = (FireAlertApplication)getApplication();
			// Parse the xml and set the franchise to our application
        	a.setFranchise( reader.parseXML() );
			//We're currently in the franchise
        	a.setLocation( a.getFranchise() );
        	//Set the local variable for franchise
			mFranchise = (Franchise) a.getLocation();
			
			reader.writeXML(mFranchise);
			
        }catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        calculateDates();
    }
    
    @Override
    public void onResume(){
    	// Check if Logged in
        HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
    	super.onResume();
    }

    public void inspectionClickListener(View view)
    {
        
        Intent openInspectionView = new Intent(MainMenu.this, InspectionController.class);
        //openInspectionView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(openInspectionView);
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
    
    
}

package com.designproject.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.joda.time.Interval;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.designproject.FireAlertApplication;
import com.designproject.R;
import com.designproject.models.Client;
import com.designproject.models.Contract;
import com.designproject.models.Franchise;
import com.designproject.models.HelperMethods;
import com.designproject.models.XMLReaderWriter;

public class InspectionController extends NavigationDrawerActivity {

	// for storing the information.
	private ArrayList<String[]> inspectionInformationArray;
	// Adapters are used to bind to data. I want 2d so create my own
	private SimpleAdapter simpleAdapter;

	// Create arraylist which will hold the string data
	ArrayList<HashMap<String, String>> listInformationString = new ArrayList<HashMap<String, String>>();
	Franchise mFranchise;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Check if Logged in
		HelperMethods.logOutHandler(HelperMethods.CHECK_IF_LOGGED_IN, this);
		setContentView(R.layout.activity_inspection_view);
		super.onCreate(savedInstanceState);

		try {
			// Create the reader writer
			XMLReaderWriter reader = new XMLReaderWriter(
					this.getApplicationContext());
			// Get out application
			FireAlertApplication a = (FireAlertApplication) getApplication();
			// Parse the xml and set the franchise to our application
			a.setFranchise(reader.parseXML());
			// We're currently in the franchise
			a.setLocation(a.getFranchise());
			// Set the local variable for franchise
			mFranchise = (Franchise) a.getLocation();

			reader.writeXML(mFranchise);

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			onPause();
			new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("XML File Error")
			.setMessage("Please save XML file as /FireAlertApp/FireAlertData.xml on the SD card.")
			.setPositiveButton("Okay",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {

							//LOGOUT
							HelperMethods.logOutHandler(HelperMethods.LOGOUT, getApplicationContext());

						}

					}).show();
			return;
		}

		FireAlertApplication a = (FireAlertApplication) getApplication();
		mFranchise = (Franchise) a.getLocation();

		inspectionInformationArray = new ArrayList<String[]>();

		populateListInformation();
	}

	/**
	 * Retrieve the data from the XML and save to member variable
	 */
	private void populateListInformation() {
		// TODO: take in data from model classes and write to the member
		// variables
		String clientName;
		String contractDueDate;
		String contractId;

		for (Client client : mFranchise.getClients()) {
			clientName = client.getName();

			for (Contract contract : client.getContracts()) {
				Calendar startDateCalendar = contract.getStartDate();
				Calendar endDateCalendar = contract.getEndDate();
				String terms = contract.getTerms();

				Interval nextInspectionInterval = HelperMethods
						.returnNextInspectionInterval(startDateCalendar,
								endDateCalendar, terms);

				contractDueDate = nextInspectionInterval.getEnd().toLocalDate()
						.toString();

				contractId = contract.getId();

				String[] s = { clientName, contractDueDate, contractId };
				inspectionInformationArray.add(s);
			}
		}

		bindListView();
	}

	/**
	 * Builds the data (3 values per line) and binds it to adapter
	 */
	private void bindListView() {
		HashMap<String, String> item;

		for (String[] inspectionInformation : inspectionInformationArray) {
			item = new HashMap<String, String>();
			item.put("line1", inspectionInformation[0]);
			item.put("line2", inspectionInformation[1]);
			item.put("line3", inspectionInformation[2]);
			listInformationString.add(item);
		}

		simpleAdapter = new SimpleAdapter(this, listInformationString,
				R.layout.custom_two_lines, new String[] { "line1", "line2",
						"line3" }, new int[] { R.id.clientName, R.id.dueDate,
						R.id.textViewIdValue });
		ListView myList = (ListView) findViewById(android.R.id.list);
		// /bind the data
		myList.setAdapter(simpleAdapter);

	}

	public void inspectionItemListener(View view) {
		Client[] clientList = mFranchise.getClients();
		TextView clientNameChild = (TextView) ((ViewGroup) view).getChildAt(0);
		String clientName = clientNameChild.getText().toString();

		// The view is a listView and its second child element is a relative
		// layout.
		// So cast that relativeLayout as a ViewGroup and get its child.
		TextView contractIdChild = (TextView) ((ViewGroup) ((ViewGroup) view)
				.getChildAt(1)).getChildAt(0);
		String contractId = contractIdChild.getText().toString();

		for (Client client : clientList) {
			if (client.getName() == clientName) {
				for (Contract contract : client.getContracts()) {
					if (contract.getId() == contractId) {
						FireAlertApplication a = (FireAlertApplication) getApplication();
						a.setLocation(contract);
					}
				}
			}
		}

		Intent openInspectionOverview = new Intent(InspectionController.this,
				ContractController.class);
		startActivity(openInspectionOverview);

	}

	@Override
	public void onResume() {
		// Check if Logged in
		HelperMethods.logOutHandler(HelperMethods.CHECK_IF_LOGGED_IN, this);

		super.onResume(); // Always call the superclass method first

		FireAlertApplication a = (FireAlertApplication) getApplication();
		a.setLocation(mFranchise);

	}

}

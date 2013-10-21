package com.designproject;

import com.designproject.Equipment.node;
import com.designproject.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InspectionForm extends Activity {
	private int pageNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspection_form);
		
final Activity activity = this;
		
		// Initialize equipment (for testing purposes only)
		final Equipment equipment = new Equipment("Fire Extinguisher");
		/*
		equipment.setID("2");
		equipment.setLocation("First floor under staircase");
		equipment.addAttribute("model", "Model7899");
		equipment.addAttribute("make", "199");
		equipment.addAttribute("size", "large");
		equipment.addAttribute("notes", "Bent");
		equipment.addInspectionElement(new InspectionElement("Hydro Test"));
		equipment.addInspectionElement(new InspectionElement("6 Year Insp"));
		equipment.addInspectionElement(new InspectionElement("Weight"));
		equipment.addInspectionElement(new InspectionElement("Bracket"));
		equipment.addInspectionElement(new InspectionElement("Gauge"));
		equipment.addInspectionElement(new InspectionElement("Pull Pin"));
		equipment.addInspectionElement(new InspectionElement("Signage"));
		equipment.addInspectionElement(new InspectionElement("Collar"));
		equipment.addInspectionElement(new InspectionElement("Hose"));
		equipment.addInspectionElement(new InspectionElement("Hydro Test"));
		equipment.addInspectionElement(new InspectionElement("6 Year Insp"));
		equipment.addInspectionElement(new InspectionElement("Weight"));
		equipment.addInspectionElement(new InspectionElement("Bracket"));
		equipment.addInspectionElement(new InspectionElement("Gauge"));
		equipment.addInspectionElement(new InspectionElement("Pull Pin"));
		equipment.addInspectionElement(new InspectionElement("Signage"));
		equipment.addInspectionElement(new InspectionElement("Collar"));
		equipment.addInspectionElement(new InspectionElement("Hose"));*/
		
		
		// Set title, id and page number
		setTitle(equipment.getName());
		final InspectionElement[] elements = equipment.getInspectionElements();
		final LinearLayout header = (LinearLayout) findViewById(R.id.inspect_form_header);
		final int numPages = elements.length / 8 + (elements.length % 8 == 0 ? 0 : 1);
		pageNum = 1;
	
		TextView idView = new TextView(activity);
		String id = equipment.getID();
		if (id != null)
		{
			idView.setText("ID: "+id);
			LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
	                0, LayoutParams.MATCH_PARENT, 1);
			header.addView(idView, lparams);
		}
		
		final Button submit = new Button(activity);
		final TextView pageView = new TextView(activity);
		if (numPages > 1)
		{
			pageView.setText("Page "+pageNum+" of "+numPages);
			
			LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
	                0, LayoutParams.MATCH_PARENT, 1);
			pageView.setGravity(Gravity.RIGHT);
			header.addView(pageView, lparams);
			submit.setText("Next");
		}
		else
			submit.setText("Submit");
		
		// Set location value and more info button
		final LinearLayout info = (LinearLayout) findViewById(R.id.inspect_form_info);
		TextView locationView = new TextView(activity);
		String location = equipment.getLocation();
		if (location != null)
		{
			locationView.setText("Location: "+location);
			info.addView(locationView);
		}
		
		final node[] attributes = equipment.getAttributes();
		if(attributes.length > 2)
		{
			final Button moreInfo = new Button(activity);
			moreInfo.setText("More Info");
			info.addView(moreInfo);
	        moreInfo.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	            	if (moreInfo.getText().equals("More Info"))
	            	{
		            	for(int i = 0; i < attributes.length; i++)
		            	{
		            		TextView attributeView = new TextView(activity);
			            	attributeView.setText(attributes[i].getName()+": "+attributes[i].getValue());
			            	info.addView(attributeView, i+2);
		            	}
		            	moreInfo.setText("Less Info");
	            	}
	            	
	            	else
	            	{
		            	for(int i = attributes.length - 1; i >= 0; i--)
		            	{
		            		if(attributes[i].getName() != "id" && attributes[i].getName() != "location")
		            		{
			            		View attributeView = info.getChildAt(i + 2);
			            		info.removeView(attributeView);
		            		}
		            	}
		            	moreInfo.setText("More Info");
	            	}
	            }
	        });
		}
		
		
		// Populate form
		final LinearLayout content = (LinearLayout) findViewById(R.id.inspect_form_content);
		
		for(int i = 0; i < Math.min(8,elements.length); i++)
		{
			CheckBox cb = new CheckBox(activity);
	     	cb.setText(elements[i].getName());
	     	content.addView(cb);
		}
	
		LinearLayout form = (LinearLayout) findViewById(R.id.inspect_form);
        submit.setGravity(Gravity.BOTTOM);
		form.addView(submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//Get and save data
            	for(int i = content.getChildCount() - 1; i >= 0; i--)
            	{
            		View cbView = content.getChildAt(i);
	            	content.removeView(cbView);
            	}
            	
            	if (submit.getText().toString() == "Submit")
            	{
            		content.removeView(submit);
	            	Intent intent = new Intent(activity, FireAlertSplash.class);
	            	startActivity(intent);
            	}
            	else
            	{
            		pageNum++;
            		pageView.setText("Page "+pageNum+" of "+numPages);
            		if (pageNum == numPages)
            			submit.setText("Submit");
            		
            		// Populate next form
            		for(int i = (pageNum - 1) * 8; i < Math.min((pageNum * 8), elements.length); i++)
            		{
            			CheckBox cb = new CheckBox(activity);
            	     	cb.setText(elements[i].getName());
            	     	content.addView(cb, (i - (8 * (pageNum - 1))));
            		}
            	}
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inspection_form, menu);
		return true;
	}

}
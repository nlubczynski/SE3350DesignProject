package com.designproject;

import com.designproject.Equipment.node;
import com.designproject.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InspectFormActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspect_form);
		final Activity activity = this;
		
		// Initialize equipment (for testing purposes only)
		final Equipment equipment = new Equipment("Fire Extinguisher");
		equipment.addAttribute("id", "2");
		equipment.addAttribute("location", "First floor under staircase");
		equipment.addAttribute("model", "Model7899");
		equipment.addInspectionElement(new InspectionElement("Hydro Test"));
		equipment.addInspectionElement(new InspectionElement("6 Year Insp"));
		equipment.addInspectionElement(new InspectionElement("Weight"));
		equipment.addInspectionElement(new InspectionElement("Bracket"));
		equipment.addInspectionElement(new InspectionElement("Gauge"));
		equipment.addInspectionElement(new InspectionElement("Pull Pin"));
		equipment.addInspectionElement(new InspectionElement("Signage"));
		equipment.addInspectionElement(new InspectionElement("Collar"));
		equipment.addInspectionElement(new InspectionElement("Hose"));
		
		
		// Set title and header
		setTitle(equipment.getName());
		
		final LinearLayout header = (LinearLayout) findViewById(R.id.inspect_form_header);
		TextView idView = new TextView(activity);
		String id = equipment.getValue("id");
		if (id != null)
		{
			idView.setText("ID: "+id);
			header.addView(idView);
		}
		
		TextView locationView = new TextView(activity);
		String location = equipment.getValue("location");
		if (location != null)
		{
			locationView.setText("Location: "+location);
			header.addView(locationView);
		}
		
		final Button moreInfo = new Button(activity);
		moreInfo.setText("More Info");
		header.addView(moreInfo);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	node[] attributes = equipment.getAttributes();
            	if (moreInfo.getText().equals("More Info"))
            	{
	            	for(int i = 0; i < attributes.length; i++)
	            	{
	            		if(attributes[i].getName() != "id" && attributes[i].getName() != "location")
	            		{
		            		TextView attributeView = new TextView(activity);
		            		attributeView.setText(attributes[i].getName()+": "+attributes[i].getValue());
		            		header.addView(attributeView, i);
	            		}
	            	}
	            	moreInfo.setText("Less Info");
            	}
            	
            	else
            	{
	            	for(int i = 0; i < attributes.length; i++)
	            	{
	            		if(attributes[i].getName() != "id" && attributes[i].getName() != "location")
	            		{
		            		View attributeView = header.getChildAt(i);
		            		header.removeView(attributeView);
	            		}
	            	}
	            	moreInfo.setText("More Info");
            	}
            }
        });
		
		
		// Populate form
		LinearLayout content = (LinearLayout) findViewById(R.id.inspect_form);
		InspectionElement[] elements = equipment.getInspectionElements();
		for(int i = 0; i < elements.length; i++)
		{
			CheckBox cb = new CheckBox(activity);
	     	cb.setText(elements[i].getName());
	     	content.addView(cb);
		}
		
		Button submit = new Button(activity);
		submit.setText("Submit");
		content.addView(submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(activity, MainActivity.class);
            	startActivity(intent);
            }
        });
	}

}

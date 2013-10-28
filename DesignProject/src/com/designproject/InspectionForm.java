package com.designproject;

import com.designproject.Equipment.node;
import com.designproject.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InspectionForm extends Activity implements OnGestureListener {
        private int pageNum, numPages;
        private GestureDetector gDetector;
        
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_inspection_form);
                gDetector = new GestureDetector(this);
                
                final Activity activity = this;
                
                Intent mIntent = getIntent();
                pageNum = mIntent.getIntExtra("Page Number", 0);
                 
                FireAlertApplication app = (FireAlertApplication) getApplication();
                Equipment equipment = (Equipment) app.getLocation();
                
                final LinearLayout content = (LinearLayout) findViewById(R.id.inspect_form_content);
                final InspectionElement[] elements = equipment.getInspectionElements();
                populateContent(equipment, content, activity);
                
                // Set title, id and page number
                setTitle(equipment.getName());
                final LinearLayout header = (LinearLayout) findViewById(R.id.inspect_form_header);
                
                final Button submit = new Button(activity);
                final TextView pageView = new TextView(activity);
                final LinearLayout form = (LinearLayout) findViewById(R.id.inspect_form);
                
                final TextView typeView = new TextView(activity);
                typeView.setText(equipment.getName());
                header.addView(typeView);
                
                //TODO
                submit.setGravity(Gravity.BOTTOM);
                if (numPages > 1)
                {
                        pageView.setText("Page "+pageNum+" of "+numPages);
                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    0, LayoutParams.MATCH_PARENT, 1);
                        pageView.setGravity(Gravity.RIGHT);
                        header.addView(pageView, lparams);
                }
                if (pageNum == numPages)
                {
                        submit.setText("Submit");
                        form.addView(submit);
                }
                
                // Set location value and more info button
                final LinearLayout info = (LinearLayout) findViewById(R.id.inspect_form_info);
                final LinearLayout header2 = (LinearLayout) findViewById(R.id.inspect_form_header2);
                TextView locationView = new TextView(activity);
                String location = equipment.getLocation();
                if (location != null)
                {
                        locationView.setText(location);
                        header2.addView(locationView);
                }
                
                TextView idView = new TextView(activity);
                String id = equipment.getID();
                if (id != null)
                {
                        idView.setText("ID: "+id);
                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    0, LayoutParams.MATCH_PARENT, 1);
                        idView.setGravity(Gravity.RIGHT);
                        header2.addView(idView, lparams);
                }
                
                final node[] attributes = equipment.getAttributes();
                if(attributes.length > 2)
                {
                	final Button moreInfo = new Button(activity);
                    moreInfo.setText("More Info");
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
                    		LayoutParams.WRAP_CONTENT, 1);
                    moreInfo.setTextSize(10);
                    moreInfo.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    lparams.height = 40;
                    lparams.gravity = Gravity.RIGHT;
                    info.addView(moreInfo, lparams);
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
                            content.setBackgroundColor(getResources().getColor(R.color.white));
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
                
            submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                    //Get and save data
                    for(int i = content.getChildCount() - 1; i >= 0; i--)
                    {
                            View cbView = content.getChildAt(i);
                            content.removeView(cbView);
                    }
                    
                    content.removeView(submit);
                    Intent intent = new Intent(activity, EquipmentInspectionList.class);
                    startActivity(intent);
            }
        });
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.inspection_form, menu);
                return true;
        }

        public boolean onDown(MotionEvent arg0) {
                // TODO Auto-generated method stub
                return false;
        }

        public boolean onFling(MotionEvent start, MotionEvent finish, float xVelocity, float yVelocity) {
                // Left swipe
                if (start.getRawX() < finish.getRawX()) {
                        if (pageNum > 1)
                        {
                             Intent previousPage = new Intent(InspectionForm.this, InspectionForm.class);
                             previousPage.putExtra("Page Number", --pageNum);
                             startActivity(previousPage);
                        }
                } 
                // Right swipe
                else {
                        if (pageNum < numPages)
                        {
                             Intent nextPage = new Intent(InspectionForm.this, InspectionForm.class);
                             nextPage.putExtra("Page Number", ++pageNum);
                             startActivity(nextPage);
                        }
                }
                return true;
        }

        public void onLongPress(MotionEvent arg0) {
                // TODO Auto-generated method stub
                
        }

        public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
                        float arg3) {
                // TODO Auto-generated method stub
                return false;
        }

        public void onShowPress(MotionEvent arg0) {
                // TODO Auto-generated method stub
                
        }

        public boolean onSingleTapUp(MotionEvent arg0) {
                // TODO Auto-generated method stub
                return false;
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent me) {
                return gDetector.onTouchEvent(me);
        }
        
        public void populateContent(Equipment equipment, LinearLayout content, Activity activity) {
        	String type = equipment.getName();
        	if (type.equals("Extinguisher")) {
        		if(equipment.getInspectionElements().length == 0)
        		{
	        		equipment.addInspectionElement(new InspectionElement("Hydro Test"));
	                equipment.addInspectionElement(new InspectionElement("6 Year Insp"));
	                equipment.addInspectionElement(new InspectionElement("Weight"));
	                equipment.addInspectionElement(new InspectionElement("Bracket"));
	                equipment.addInspectionElement(new InspectionElement("Gauge"));
	                equipment.addInspectionElement(new InspectionElement("Pull Pin"));
	                equipment.addInspectionElement(new InspectionElement("Signage"));
	                equipment.addInspectionElement(new InspectionElement("Collar"));
	                equipment.addInspectionElement(new InspectionElement("Hose"));
        		}
                
                final InspectionElement[] elements = equipment.getInspectionElements();
                numPages = elements.length / 8 + (elements.length % 8 == 0 ? 0 : 1);
            	// Populate form
                for(int i = (pageNum - 1) * 8; i < Math.min((pageNum * 8), elements.length); i++)
                {
                     CheckBox cb = new CheckBox(activity);
                     cb.setText(elements[i].getName());
                     content.addView(cb, (i - (8 * (pageNum - 1))));
                }
               
                if (pageNum == numPages)
                {
                	TextView commentsText = new TextView(activity);
                	commentsText.setText("Comments: ");
                	content.addView(commentsText);
                	EditText comments = new EditText(activity);
                	content.addView(comments);
                }
        	}
        	else if (type.equals("FireHoseCabinet"))
        	{
        		equipment.addInspectionElement(new InspectionElement("Cabinet Condition"));
                equipment.addInspectionElement(new InspectionElement("Valve Condition"));
                equipment.addInspectionElement(new InspectionElement("Hose Re-Rack"));
                equipment.addInspectionElement(new InspectionElement("HT Due"));
                
                final InspectionElement[] elements = equipment.getInspectionElements();
                numPages = elements.length / 8 + (elements.length % 8 == 0 ? 0 : 1);
            	// Populate form
                for(int i = (pageNum - 1) * 8; i < Math.min((pageNum * 8), elements.length); i++)
                {
                     CheckBox cb = new CheckBox(activity);
                     cb.setText(elements[i].getName());
                     content.addView(cb, (i - (8 * (pageNum - 1))));
                }
               
                TextView commentsText = new TextView(activity);
            	commentsText.setText("Comments: ");
            	content.addView(commentsText);
             	EditText comments = new EditText(activity);
               	content.addView(comments);
        	}
        	else if (type.equals("EmergencyLight"))
        	{
        		equipment.addInspectionElement(new InspectionElement("Requires Service or Repair"));
                equipment.addInspectionElement(new InspectionElement("Operation Confirmed"));
                equipment.addInspectionElement(new InspectionElement("Number of Heads"));
                equipment.addInspectionElement(new InspectionElement("Total Power"));
                equipment.addInspectionElement(new InspectionElement("Voltage"));
                equipment.addInspectionElement(new InspectionElement("Comments"));
                
                final InspectionElement[] elements = equipment.getInspectionElements();
                numPages = elements.length / 8 + (elements.length % 8 == 0 ? 0 : 1);
            	// Populate form
                for(int i = 0; i < 2; i++)
                {
                     CheckBox cb = new CheckBox(activity);
                     cb.setText(elements[i].getName());
                     content.addView(cb);
                }
                for (int i = 2; i < 5; i++)
                {
                	TextView tv = new TextView(activity);
                    tv.setText(elements[i].getName());
                    content.addView(tv);
                    EditText et = new EditText(activity);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    content.addView(et);
                }
               
                if (pageNum == numPages)
                {
                	TextView commentsText = new TextView(activity);
                	commentsText.setText("Comments: ");
                	content.addView(commentsText);
                	EditText comments = new EditText(activity);
                	content.addView(comments);
                }
        	}
        	else if (type.equals("KitchenSuppressionSystem"))
        	{
        		
        	}
        }

}
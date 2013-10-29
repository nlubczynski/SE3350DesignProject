package com.designproject;

import android.os.Bundle;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class EquipmentInspectionList extends ListActivity {

private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
private DataReceiver dataScanner = new DataReceiver();
private EditText editText;
private String IDvalue = "";
private Room mRoom;
private Equipment[] equipment;
private ListView listView;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	// Check if Logged in
            HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
        	
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_equipment_inspection_list);
                setupActionBar();
                
                FireAlertApplication a = (FireAlertApplication)getApplication();
                a = (FireAlertApplication)getApplication();
                mRoom = (Room)a.getLocation();
                equipment = mRoom.getEquipment();
                setTitle("Room: "+mRoom.getRoomNo());
                
                setListAdapter(new ArrayAdapter<Equipment>(this, R.layout.equipment_list_item, equipment));
                 
                listView = getListView();
                listView.setTextFilterEnabled(true);
 
                listView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                         
                            FireAlertApplication app = (FireAlertApplication) getApplication();
                            app.setLocation(equipment[position]);
                            Intent inspectionForm = new Intent(EquipmentInspectionList.this, InspectionForm.class);
                            inspectionForm.putExtra("Page Number", 1);
                            startActivity(inspectionForm);
                        }
                });
                
                //how many pieces of equipment in the room
                final int length = equipment.length;
                
                //create button to select equipment for inspection
                Button okButton = (Button) findViewById(R.id.OK);

        		//set action listener for 'Select' button
        		okButton.setOnClickListener(new View.OnClickListener() 
        		{
        			@Override
        			public void onClick(View v) 
        			{
        				//assign String from editText to IDvalue
        				IDvalue = editText.getText().toString();
        				
        				//find which equipment has an id that matches IDvalue
        				//move to appropriate inspection form
        				for (int i = 0; i < length; i++)
        				{
        					if (IDvalue.equals(equipment[i].getID()))
        					{
        						FireAlertApplication a = (FireAlertApplication) getApplication();
                                a.setLocation(equipment[i]);
                                Intent inspectionForm = new Intent(EquipmentInspectionList.this, InspectionForm.class);
                                inspectionForm.putExtra("Page Number", 1);
                                startActivity(inspectionForm);
                                break;
        					}
        				}
        			}
        		});
        }
        
        /**
         * Set up the {@link android.app.ActionBar}, if the API is available.
         */
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private void setupActionBar() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        getActionBar().setDisplayHomeAsUpEnabled(true);
                }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.equipment_list, menu);
                return true;
        }
        
        //given Scanner code
        protected void onResume() {
        	// Check if Logged in
            HelperMethods.logOutHandler( HelperMethods.CHECK_IF_LOGGED_IN , this);
        	
        	registerScanner();
        	initialComponent();
    		super.onResume();
    		FireAlertApplication a = (FireAlertApplication)getApplication();
	    	a.setLocation(mRoom);
    	}

        //given Scanner code
    	@Override
    	protected void onDestroy() {
    		unregisterReceiver();
    		super.onResume();
    		FireAlertApplication a = (FireAlertApplication)getApplication();
    		a.setLocation(mRoom);
    		super.onDestroy();
    	}
    	
    	//given Scanner code
    	private void initialComponent() {
    		//tv_getdata_from_scanner = (TextView)findViewById(R.id.tv_getdata_from_scanner);
    		//tv_getdata_from_edittext  = (TextView)findViewById(R.id.tv_getdata_from_edittext);
    		editText = (EditText)findViewById(R.id.editText);
    		editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    		editText.addTextChangedListener(textWatcher);
    	}
    	
    	//given Scanner code
    	private void registerScanner() {
    		dataScanner = new DataReceiver();
    		IntentFilter intentFilter = new IntentFilter();
    		intentFilter.addAction(ACTION_CONTENT_NOTIFY);
    		registerReceiver(dataScanner, intentFilter);
    	}
    	
    	//given Scanner code
    	private void unregisterReceiver() {
    		if (dataScanner != null) unregisterReceiver(dataScanner);
    	}
    	
    	//given Scanner code
    	//catches if text is entered (i.e. from scanner)
    	private TextWatcher textWatcher =  new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
        	public void afterTextChanged(Editable s){
        		//tv_getdata_from_edittext.setText("Get data from EditText : " + editText.getText().toString());
        		
        		/*
    			//check if entered ID is valid 
    			if ( editText.getText().toString() != null && !(editText.getText().toString().isEmpty()))
    			{
    				//assign entered value to ID value
    				IDvalue = editText.getText().toString();
    			}*/
        	}
        }; 
        
        //given Scanner code
    	private class DataReceiver extends BroadcastReceiver {
    		String content = "";
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			if (intent.getAction().equals(ACTION_CONTENT_NOTIFY)) {
    				Bundle bundle = new Bundle();
    				bundle  = intent.getExtras();
    				
    				//data that Scanner picks up is assigned to "content"
    				//EditText that has <requestFocus/> tag will be updated with "content" value
    				content = bundle.getString("CONTENT");
    				
    				//tv_getdata_from_scanner.setText("Get data from Scanner : " + content);
    			}		
    		}
    	}

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                case android.R.id.home:
                        // This ID represents the Home or Up button. In the case of this
                        // activity, the Up button is shown. Use NavUtils to allow users
                        // to navigate up one level in the application structure. For
                        // more details, see the Navigation pattern on Android Design:
                        //
                        // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                        //
                        NavUtils.navigateUpFromSameTask(this);
                        return true;
                }
                return super.onOptionsItemSelected(item);
        }
        
     // from the link above
        @Override
        public void onConfigurationChanged(Configuration newConfig) {
        	Log.i("debugger", "Onconfigurationchanged called");
            super.onConfigurationChanged(newConfig);
        }
}  
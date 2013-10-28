package com.designproject;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;



public class EquipmentInspectionList extends ListActivity {

private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
private DataReceiver dataScanner = new DataReceiver();
private EditText editText;
private String IDvalue;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_equipment_inspection_list);
                setupActionBar();
                
                FireAlertApplication a = (FireAlertApplication)getApplication();
                Equipment[] equipment = ((Room)a.getLocation()).getEquipment();
                
                setListAdapter(new ArrayAdapter<Equipment>(this, R.layout.equipment_list_item, equipment));
                 
                ListView listView = getListView();
                listView.setTextFilterEnabled(true);
 
                listView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                            // When clicked, show a toast with the TextView text
                            Toast.makeText(getApplicationContext(),
                                ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
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
        protected void onResume() {
        	registerScanner();
        	initialComponent();
    		super.onResume();
    	}

    	@Override
    	protected void onDestroy() {
    		unregisterReceiver();
    		super.onDestroy();
    	}
    	
    	private void initialComponent() {
    		//tv_getdata_from_scanner = (TextView)findViewById(R.id.tv_getdata_from_scanner);
    		//tv_getdata_from_edittext  = (TextView)findViewById(R.id.tv_getdata_from_edittext);
    		editText = (EditText)findViewById(R.id.editText);
    		editText.addTextChangedListener(textWatcher);
    	}
    	
    	private void registerScanner() {
    		dataScanner = new DataReceiver();
    		IntentFilter intentFilter = new IntentFilter();
    		intentFilter.addAction(ACTION_CONTENT_NOTIFY);
    		registerReceiver(dataScanner, intentFilter);
    	}
    	
    	private void unregisterReceiver() {
    		if (dataScanner != null) unregisterReceiver(dataScanner);
    	}
    	
    	private TextWatcher textWatcher =  new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
        	public void afterTextChanged(Editable s){
        		//tv_getdata_from_edittext.setText("Get data from EditText : " + editText.getText().toString());
    		
    			//check if entered ID is valid 
    			if ( editText.getText().toString() != null && !(editText.getText().toString().isEmpty()))
    			{
    				//assign entered value to ID value
    				IDvalue = editText.getText().toString();
    			
    				//move to appropriate inspection form
    			}
    			else
    			{	
    			}
        	}
        }; 
        
    	private class DataReceiver extends BroadcastReceiver {
    		String content = "";
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			if (intent.getAction().equals(ACTION_CONTENT_NOTIFY)) {
    				Bundle bundle = new Bundle();
    				bundle  = intent.getExtras();
    				content = bundle.getString("CONTENT");
    				//tv_getdata_from_scanner.setText("Get data from Scanner : " + content);
    				
    				/*
    				//check if scanned data is valid
    				if ( content != null && !(content.isEmpty()))
    				{
    					//assign scanned value to ID value
    					IDvalue = content;
    					
    					//move to appropriate inspection form
    				}
    				else
    				{	
    				}*/
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
        
}  
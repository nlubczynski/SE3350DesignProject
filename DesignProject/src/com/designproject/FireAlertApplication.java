package com.designproject;

import android.app.Application;

public class FireAlertApplication extends Application {

	private Object location;
	public Object franchise;
	
	public void setLocation( Object o ){
		this.location = o;
	}
	public Object getLocation(){
		return this.location;
	}
	
}

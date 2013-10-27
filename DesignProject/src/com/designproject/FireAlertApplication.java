package com.designproject;

import android.app.Application;

public class FireAlertApplication extends Application {

	private Franchise franchise;
	private Object location;
	
	public void setFranchise(Franchise f){
		this.franchise = f;
	}
	public Franchise getFranchise(){
		return franchise;
	}
	public void setLocation( Object o ){
		this.location = o;
	}
	public Object getLocation(){
		return this.location;
	}
	
}

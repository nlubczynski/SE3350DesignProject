package com.designproject;

import com.designproject.models.Franchise;

import android.app.Application;

public class FireAlertApplication extends Application {

	private Object location;
	private Franchise franchise;
	
	public void setLocation( Object o ){
		this.location = o;
	}
	public Object getLocation(){
		return this.location;
	}
	public Franchise getFranchise(){
		return this.franchise;
	}
	public void setFranchise(Franchise f){
		this.franchise = f;
	}
	
}

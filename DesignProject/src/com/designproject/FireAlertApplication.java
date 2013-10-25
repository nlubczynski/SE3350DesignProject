package com.designproject;

import android.app.Application;

public class FireAlertApplication extends Application {

	private Franchise franchise;
	
	public void setFranchise(Franchise f){
		this.franchise = f;
	}
	public Franchise getFranchise(){
		return franchise;
	}
	
}

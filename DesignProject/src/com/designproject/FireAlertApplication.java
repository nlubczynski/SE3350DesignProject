package com.designproject;

import com.designproject.models.Client;
import com.designproject.models.Franchise;

import android.app.Application;

public class FireAlertApplication extends Application {

	private Object location;
	private Franchise franchise;
	private Client client;
	
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
	public void setClient(Client c){
		this.client = c;
	}
	public Client getClient(){
		return this.client;
	}
}

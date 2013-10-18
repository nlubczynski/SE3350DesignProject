package com.designproject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author NikLubz
 *
 */
public class Inspector {
	
	private String userName;
	private String password;
	private ArrayList<Building> inspectedBuildings;
	
	public Inspector(String userName, String password){
		
		this.userName = userName;
		this.password = password;
		this.inspectedBuildings = new ArrayList<Building>();
		
	}

	/**
	 * @return String userName - Returns the inspector's UserName
	 */
	public String getUserName() {
		return userName;
		
	}

	/**
	 * @param String userName - Set the inspector's UserName
	 * @return Boolean result - whether or not the userName was changed
	 */
	public boolean setUserName(String userName) {
		try{
			this.userName = userName;
		}
		catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return String password - Get the user's password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param String password - Set the user's password
	 * @return Boolean result - whether or not the password was changed
	 */
	public boolean setPassword(String password) {
		try{
			this.password = password;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return Building[] - The buildings an inspector has investigated
	 */
	public Building[] getInspectedBuildings() {
		return Arrays.copyOf(inspectedBuildings.toArray(), inspectedBuildings.toArray().length, Building[].class);
	}

	/**
	 * @param Building inspectedBuilding - the inspectedBuilding to set
	 * @return Boolean result - Whether or not the building was added.
	 */
	public boolean addInspectedBuilding(Building inspectedBuilding) {
		return this.inspectedBuildings.add(inspectedBuilding);
	}

}

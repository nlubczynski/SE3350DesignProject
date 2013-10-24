package com.designproject;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * 
 * @author NikLubz
 *
 */
public class Contract {
	
	private String id;
	private String no;
	private GregorianCalendar startDate;
	private GregorianCalendar endDate;
	private String terms;
	private ArrayList<Building> buildings;
	
	public Contract(String id, String no, GregorianCalendar start, GregorianCalendar end, String terms){
		
		this.id = id;
		this.no = no;
		this.startDate = start;
		this.endDate = end;
		this.terms = terms;
		this.buildings = new ArrayList<Building>();
		
	}
	
	/**
	 * @return String id - returns the contract id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param String id - the new id for the contract
	 * @return Boolean result - whether or not the id was updated
	 */
	public boolean setId(String id) {
		try{
			this.id = id;
		}catch(Exception e){
			return false;
		}
		return true;
	}
	/**
	 * @return String contractNo - returns the contract no
	 */
	public String getNo() {
		return no;
	}
	/**
	 * @param String contractNo - the new contract number
	 * @return Boolean result - whether or not the contractDo was changed
	 */
	public boolean setNo(String no) {
		try{
			this.no = no;
		}catch(Exception e){
			return false;
		}
		return true;
	}
	/**
	 * @return Date startDate - returns the start date of the contract
	 */
	public GregorianCalendar getStartDate() {
		return startDate;
	}
	/**
	 * @param Date starDate - Set the starDate
	 * @return Boolean result - whether or not the changed was successful
	 */
	public boolean setStartDate(GregorianCalendar starDate) {
		try{
			this.startDate = starDate;
		}catch(Exception e){
			return false;
		}
		return true;
	}
	/**
	 * @return Date endDate - the end date of the contract
	 */
	public GregorianCalendar getEndDate() {
		return endDate;
	}
	/**
	 * @param Date endDate - set the endDate for the contract
	 */
	public boolean setEndDate(GregorianCalendar endDate) {
		try{
			this.endDate = endDate;
		}catch(Exception e){
			return false;
		}
		return true;
	}
	/**
	 * @return String terms - return the terms of the agreement
	 */
	public String getTerms() {
		return terms;
	}
	/**
	 * @param String terms  - the terms to set
	 * @return Boolean result - whether or not the terms were set
	 */
	public boolean setTerms(String terms) {
		try{
			this.terms = terms;
		}catch(Exception e){
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @return Building[] buidlings - return the buildings in a contract
	 */
	public Building[] getBuildings(){
		Building[] returnArray = new Building[ buildings.size() ];
		
		for(int i = 0; i < buildings.size(); i++)
			returnArray[i] = buildings.get(i);
		
		return returnArray;
	}
	/**
	 * 
	 * @param Building b - the building to add to the Contract
	 * @return Boolean result - whether or not the building was added
	 */
	public boolean addBuilding(Building b){
		return buildings.add(b);
	}
	
}

package com.designproject;

import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author NikLubz
 *
 */
public class Building {
	
	private String id;
	private String address;
	private String postalCode;
	private String city;
	private String province;
	private String country;
	private Date timeStamp;
	private ArrayList<Floor> floors;
	
	public Building(String id, String address, String postalCode, String city, String province, String country, Date timeStamp){
		this.id = id;
		this.address = address;
		this.postalCode = postalCode;
		this.city = city;
		this.province = province;
		this.country = country;
		this.timeStamp = timeStamp;
		this.floors = new ArrayList<Floor>();
	}

	/**
	 * @return String id - returns the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param String id - the id to set
	 * @return Boolean result - whether or not the id was changed.
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
	 * @return String address - the building's address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param String address - set the building's address
	 * @return Boolean result - whether or not the address was changed
	 */
	public boolean setAddress(String address) {
		try{
			this.address = address;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return String postalCode - returns the building's postal code
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param String postalCode - postalCode to for the building
	 * @result Boolean result - whether or not the postal code was changed
	 */
	public boolean setPostalCode(String postalCode) {
		try{
			this.postalCode = postalCode;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return String city - the City the building belongs too
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param String city - the city to set a building to
	 * @result Boolean result - whether or not the city was changed
	 */
	public boolean setCity(String city) {
		try{
			this.city = city;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return String province - the building's province
	 */
	public String getProvince() {	
		return province;
	}

	/**
	 * @param String province - province to set for the building
	 * @return Boolean result - whether or not the province was changed
	 */
	public boolean setProvince(String province) {
		try{
			this.province = province;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return String country - the country a building is in
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param String country - the country to set a building in
	 * @return Boolean result - whether or not the country was set
	 */
	public boolean setCountry(String country) {
		try{
			this.country = country;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return Date timeStamp - The timestamp of a building's last inspection
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param Date timeStamp - the new timeStamp a building was inspected
	 * @return Boolean result - whether or not the new timestamp was set
	 */
	public boolean setTimeStamp(Date timeStamp) {
		try{
			this.timeStamp = timeStamp;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return Floor[] floors - returns the floors in a building
	 */
	public Floor[] getFloors() {
		return (Floor[]) floors.toArray();
	}

	/**
	 * @param Floor floor - the floor to add
	 * @return Boolean result - whether or not a floor was added
	 */
	public boolean addFloor(Floor floor) {
		return this.floors.add( floor );
	}

}

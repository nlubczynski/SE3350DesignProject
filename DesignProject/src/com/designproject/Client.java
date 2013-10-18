package com.designproject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author NikLubz
 *
 */
public class Client {

	private String name;
	private int id;
	private String address;
	private ArrayList<Contract> contracts;
	
	/**
	 * Just your basic constructor
	 * @param String name - the name of the client
	 * @param Int id - the unique id of the client
	 * @param String address - The address of head office for the client. No address verification is done in the constructor.
	 */
	public Client(String name, int id, String address){
		
		this.name = name;
		this.id = id;
		this.address = address;
		this.contracts = new ArrayList<Contract>();
		
	}

	/**
	 * Get the Contracts a client has
	 * @return Contract[] - An array of the contracts
	 */
	public Contract[] getContracts(){
		return Arrays.copyOf(contracts.toArray(), contracts.toArray().length, Contract[].class);
	}
	/**
	 * Add a contract to the client
	 * @param Contract c - the contract to add
	 * @return Boolean - whether or not the contract was added
	 */
	public boolean addContract(Contract c){
		return this.contracts.add(c);
	}
	/**
	 * @return String - the name of the client
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param String name - the name of the client
	 * @return Boolean result - whether or not the name was set
	 */
	public boolean setName(String name) {
		try{
			this.name = name;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return Int id - the client's ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param Int id - Set the client's id
	 * @return Boolean result - whether or not the ID was set.
	 */
	public boolean setId(int id) {
		try{
			this.id = id;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return String address - Return the client's address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param String address - Set the client's address
	 * @return Boolean result - whether or not the Address was set
	 */
	public boolean setAddress(String address) {
		try{
			this.address = address;
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
}

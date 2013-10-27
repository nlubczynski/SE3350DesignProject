package com.designproject;

import java.util.ArrayList;

/**
 * 
 * @author NikLubz
 * 
 */
public class Franchise {

	/**
	 * Member variables
	 * id: The unique id for the franchise
	 * ownerName: The owner's name for the franchise
	 * clients: A list of the clients that a franchise currently has
	 * inspectors: A list of the inspectors a franchise has at it's disposal
	 */
	private	String id;
	private String ownerName;
	private ArrayList<Client> clients;
	private ArrayList<Inspector> inspectors;
	
	/**
	 * Constructor
	 * @param String id - The franchise id
	 * @param String name - The franchise owner's name
	 */
	public Franchise(String id, String name){
		this.id = id;
		this.ownerName = name;
		this.clients = new ArrayList<Client>();
		this.inspectors = new ArrayList<Inspector>();
	}
	
	/**
	 * Get the franchise owner's name - non modifiable
	 * @return String ownerName - the owner's name
	 */
	public String getOwnerName(){
		return ownerName;
	}
	/**
	 * Set the owner's name
	 * @param String newName - the new name for the franchise owner
	 * @return boolean - whether or not the change was successful
	 */
	public boolean setOwnerName(String newName){
		try{
			ownerName = newName;
		}
		catch(Exception e){
			return false;
		}
		
		return true;		
	}
	/**
	 * 
	 * @return String ID - the id for the Franchise
	 */
	public String getId(){
		return id;
	}
	/**
	 * 
	 * @param String newId - the new id
	 * @return Boolean result - whether the id was changed or not
	 */
	public boolean setId(String newId){
		try{
			id = newId;
		}
		catch(Exception e){
			return false;
		}
		
		return true;
	}
	/**
	 * Adds a client to a franchise
	 * @param Client c - The client to add
	 * @return boolean - Whether or not the client was successfully added
	 */
	public boolean addClient(Client c){
		return clients.add(c);
	}
	
	/**
	 * Gets an array of all a Franchise's clients
	 * @return Client [] - This clients from a franchise
	 */
	public Client[] getClients(){

		Client[] returnArray = new Client[ clients.size() ];
		
		for(int i = 0; i < clients.size(); i++)
			returnArray[i] = clients.get(i);
		
		return returnArray;
	}
	
	/**
	 * Adds an inspector to a franchise
	 * @param Inspector i - The inspector to add
	 * @return
	 */
	public boolean addInspector(Inspector i){
		return inspectors.add(i);
	}
	
}

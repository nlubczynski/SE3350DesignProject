package com.designproject;

import java.util.ArrayList;

/**
 * 
 * @author NikLubz
 *
 */
public class Floor {

	private String name;
	private ArrayList<Room> rooms;
	private boolean completed;
	
	public Floor(String name){
		this.name = name;
		this.rooms = new ArrayList<Room>();
		this.completed = false;
	}

	/**
	 * Overriding the toString method
	 */
	public String toString(){
		return this.getName();
	}
	/**
	 * @return String name - return the Floor name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param String name - set the Floor name
	 * @return Boolean result - whether the floor name change was successful
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
	 * @return Room[] rooms - the rooms on a floor
	 */
	public Room[] getRooms() {
		Room[] returnArray = new Room[ rooms.size() ];
		
		for(int i = 0; i < rooms.size(); i++)
			returnArray[i] = rooms.get(i);
		
		return returnArray;
	}

	/**
	 * @param Room room - the room to add to a floor
	 * @return 
	 * @return Boolean result - whether the room was added or not
	 */
	public boolean addRoom(Room room) {
		return this.rooms.add( room );
	}
	
	/**
	 * @return boolean completed - returns whether a floor has been completely inspected
	 */
	public boolean isCompleted() {
		return completed;		
	}

	/**
	 * @param boolean completed - whether a floor has been completely inspected
	 */
	public void complete(boolean completed) {
		this.completed = completed;
	}
}

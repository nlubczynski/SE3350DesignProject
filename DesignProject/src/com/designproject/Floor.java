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
	
	public Floor(String name){
		this.name = name;
		this.rooms = new ArrayList<Room>();
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
		return (Room[]) rooms.toArray();
	}

	/**
	 * @param Room room - the room to add to a floor
	 * @return 
	 * @return Boolean result - whether the room was added or not
	 */
	public boolean addRoom(Room room) {
		return this.rooms.add( room );
	}
}

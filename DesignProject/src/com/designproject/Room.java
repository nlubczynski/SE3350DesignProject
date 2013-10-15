package com.designproject;

import java.util.ArrayList;

/**
 * 
 * @author NikLubz
 *
 */
public class Room {
	
	private String id;
	private int roomNo;
	private ArrayList<Equipment> equipment;
	
	public Room(String id, int roomNo){
		this.id = id;
		this.roomNo = roomNo;
		this.equipment = new ArrayList<Equipment>();
	}

	/**
	 * @return String id - the room's id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param String id - the id to set for the floor
	 * @return Boolean result - whether or not the id was changed
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
	 * @return Int roomNo - returns the room number
	 */
	public int getRoomNo() {
		return roomNo;
	}

	/**
	 * @param Int roomNo - sets the room number
	 * @return Boolean result - whether or not the room number was changed
	 */
	public boolean setRoomNo(int roomNo) {
		try{
			this.roomNo = roomNo;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * @return Equipment[] equipment - returns a rooms equipment
	 */
	public Equipment[] getEquipment() {
		return (Equipment[]) equipment.toArray();
	}

	/**
	 * @param Equipment e - the piece of equipment to add
	 * @return Boolean result - whether or not the equipment was added
	 */
	public boolean addEquipment(Equipment equipment) {
		return this.equipment.add( equipment );
	}

}

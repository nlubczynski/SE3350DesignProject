package com.designproject.models;

import java.util.ArrayList;

/**
 * 
 * @author NikLubz
 *
 */
public class Room {
	
	private String id;
	private String roomNo;
	private ArrayList<Equipment> equipment;
	private boolean completed;
	
	public Room(String id, String roomNo){
		this.id = id;
		this.roomNo = roomNo;
		this.equipment = new ArrayList<Equipment>();
		this.completed = false;
	}

	/**
	 * Overriding the toString method
	 */
	@Override
	public String toString(){
		return this.getId();
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
	 * @return String roomNo - returns the room number
	 */
	public String getRoomNo() {
		return roomNo;
	}

	/**
	 * @param String roomNo - sets the room number
	 * @return Boolean result - whether or not the room number was changed
	 */
	public boolean setRoomNo(String roomNo) {
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
		Equipment[] returnArray = new Equipment[ equipment.size() ];
		
		for(int i = 0; i < equipment.size(); i++)
			returnArray[i] = equipment.get(i);
		
		return returnArray;
	}

	/**
	 * @param Equipment e - the piece of equipment to add
	 * @return Boolean result - whether or not the equipment was added
	 */
	public boolean addEquipment(Equipment equipment) {
		return this.equipment.add( equipment );
	}
	
	/**
	 * @return boolean completed - returns whether a room has been completely inspected
	 */
	public boolean isCompleted() {
		if(!completed)
		{
			boolean allEquipmentComplete = true;
			for (Equipment e : equipment)
			{
				if(!e.isCompleted())
					allEquipmentComplete = false;
			}
			completed = allEquipmentComplete;
		}
		return completed;	
	}

}

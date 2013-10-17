package com.designproject;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author NikLubz
 *
 */
public class Equipment {

	private String name;
	private ArrayList<node> attributes;
	private ArrayList<InspectionElement> elements;
	
	/**
	 * A node class for equipment
	 * <p>
	 * This node class is visible to other classes, but the constructor is not. 
	 * Therefore, classes can use nodes for their values, but they cannot be modified
	 * outside of this class.
	 * <p>
	 * @author NikLubz
	 *
	 */
	public class node{
		private String name;
		private String value;
		
		/**
		 * Constructor, only accessible in the equipment class
		 * @param String name - The name of the attribute
		 * @param String value - The value of the attribute
		 */
		protected node(String name, String value){
			this.name = name;
			this.value = value;
		}
		/**
		 * Public get method
		 * @return String name - the name of the attribute
		 */
		public String getName(){
			return name;
		}
		
		/**
		 * Public get method
		 * @return String value - the value of the attribute
		 */
		public String getValue(){
			return value;
		}
		
		/**
		 * Protected setter, only accessible in Equipment.
		 * @param String name - the name to set for the attribute
		 * @return Boolean result - whether the name change worked or not
		 */
		protected boolean setName(String name){
			try{
				this.name = name;
			}catch(Exception e){
				return false;
			}
			return true;
		}
		/**
		 * Protected setter, only accessible in Equipment.
		 * @param String value - the value to set for the attribute
		 * @return Boolean result - whether the value change worked or not
		 */
		protected boolean setValue(String value){
			try{
				this.value = value;
			}catch(Exception e){
				return false;
			}
			return true;
		}
		
	}
	
	/**
	 * Just a constructor
	 * @param String name - the name of the piece of equipment
	 */
	public Equipment(String name){
		this.name = name;
		this.attributes = new ArrayList<node>();
		this.elements = new ArrayList<InspectionElement>();
	}
	
	/**
	 * 
	 * @param InspectionElement e - the inspection element to add
	 * @return Boolean result - whether or not the inspection element was added.
	 */
	public boolean addInspectionElement(InspectionElement ie){
		try{
			this.elements.add(ie);
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @return InspectionElement[] elements - the inspection elements for a piece of equipment
	 */
	@SuppressLint("NewApi")
	public InspectionElement[] getInspectionElements(){
		return Arrays.copyOf(this.elements.toArray(), this.elements.toArray().length, InspectionElement[].class);
	}
	/**
	 * @return String name - return the name of the equipment
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param String name - set the name of the equipment
	 * @return Boolean result - whether or not the name was changed
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
	 * 
	 * @param String name - the name of the attribute to add
	 * @param String value - the value of the attribute to add
	 * @return Boolean result - whether or not the (name, value) tuple was added
	 */
	public boolean addAttribute(String name, String value){
		try{
			node temp = new node(name,value);
			attributes.add(temp);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * Get all the attributes for a piece of equipment
	 * @return node[] - An array of all the attributes
	 */
	@SuppressLint("NewApi")
	public node[] getAttributes(){
		return Arrays.copyOf(this.attributes.toArray(), this.attributes.toArray().length, node[].class);
	}
	
	/**
	 * Returns the value of a certain attribute
	 * <p>
	 * Example usage: node.getValue("id") returns the id for a piece of equipment
	 * <p>
	 * @param String key - the key to look for for a piece of equipment
	 * @return String value - returns the value of the key, or null if it doesn't exist
	 */
	public String getValue(String key){
		for(node test: attributes)
			if(test.getName() == key)
				return test.getValue();
		return null;
	}
	
}

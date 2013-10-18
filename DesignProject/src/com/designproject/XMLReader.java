package com.designproject;

import java.io.IOException;
import java.util.GregorianCalendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.content.res.XmlResourceParser;

/**
 * 
 * @author NikLubz and Jessica
 *
 */
public class XMLReader {

	private Franchise franchise;
	private Context context;
	
	/**
	 * Constructor
	 * @param String pathToXMLFile - the path to the XML file being used
	 * @throws XmlPullParserException
	 */
	public XMLReader( Context context ) throws XmlPullParserException{
		// The application context
		this.context = context;
	}
	
	public void parseXML() throws XmlPullParserException, IOException{
		
		// Set the parser
		XmlResourceParser parser = context.getResources().getXml(R.xml.inspection_data);	
		
		// Prepare for the while loop
 		int eventType = parser.getEventType();
 		
 		// store the more recently created element
 		Client lastClient = null;
 		Contract lastContract = null;
 		Building lastBuilding = null;
 		Floor lastFloor = null;
 		Room lastRoom = null;
 		Equipment lastEquipment = null;
 		
 		// Booleans inRoom and inEquipment
 		// if inRoom, but not inEquipment - the next startTag is a piece of equipment
 		// if not inRoom, then then next starTag is anything
 		//if inRoom and inEquipment then next startTag is inspectionElement
 		boolean inRoom = false;
 		boolean inEquipment = false;
		
		// Loop through the XML document
		while(eventType != XmlPullParser.END_DOCUMENT){
			
			switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					System.out.println("Starting parsing of document");
					break;
				case XmlPullParser.START_TAG:
					if( parser.getName().equals( "Franchisee" ) ) 
						this.franchise = franchiseParser( parser );
					else if( parser.getName().equals("Client" ) ) 
						lastClient = clientParser( parser );
					else if( parser.getName().equals( "clientContract" ) ) 
						lastContract = contractParser( parser, lastClient );
					else if( parser.getName().equals( "ServiceAddress" ) ) 
						lastBuilding = buildingParser( parser, lastContract );
					else if( parser.getName().equals( "Floor" ) ) 
						lastFloor = floorParser( parser, lastBuilding );
					else if( parser.getName().equals( "Room" ) ){ 
						lastRoom = roomParser( parser, lastFloor );
						inRoom = true;
					}
					else if( inRoom == true && inRoom != inEquipment ){ 
						lastEquipment = equipmentParser( parser, lastRoom );
						inEquipment = true;
					}
					else if( parser.getName().matches( "inspectionElement_[0-9]+" ) ) 
						inspectionElementParser( parser, lastEquipment );
					break;
				case XmlPullParser.END_TAG:
					if( parser.getName().equals( "Room" ) )
						inRoom = false;
					else if( inRoom = true && inRoom == inEquipment && !parser.getName().matches( "inspectionElement_[0-9]+" ))
						inEquipment = false;
					break;
				case XmlPullParser.TEXT:
					break;			
			}	
			eventType = parser.next();			
		}
		
		System.out.println("Done parsing document");
	}
	
	/**
	 * Parses the franchise tag
	 * <p>
	 * Sets this class's franchise element to a newly created franchise element
	 * with the proper name and id
	 * <p>
	 * @param XmlPullParser parser - the parser (located at a franchise tag)
	 */
	private Franchise franchiseParser( XmlPullParser parser ){
		
		// Error checking
		if( !parser.getName().equals( "Franchisee" ) )
			return null;
		
		// Initialize the variables
		int id = 0;
		String ownerName = "John Smith";
		
		// find the number of attributes (should be 2)
		int numOfAttributes = parser.getAttributeCount();
		
		// Loop through these attributes and set the id and owner name values
		for(int i = 0; i < numOfAttributes; i++)
			if( parser.getAttributeName( i ).equals( "id" ) )
				id = Integer.valueOf( parser.getAttributeValue(i) );
			else if( parser.getAttributeName( i ).equals( "name" ) )
				ownerName = parser.getAttributeValue(i);
		
		// Create franchise, and add the correct information.
		return new Franchise( id, ownerName );
	}
	
	/**
	 * Parses the client tag
	 * <p>
	 * Adds a client to this class's franchise element and 
	 * sets the "lastClient" to this new client as well.
	 * <p>
	 * @param XmlPullParser parser - the parser (located at a client tag)
	 */
	private Client clientParser( XmlPullParser parser ){
		
		// Error Checking
		if( !parser.getName().equals( "Client" ) )
			return null;
		
		// Initialize variables
		String name = "ACME Inc.";
		String address = "123 Fake Street";
		int id = 0;
		
		// find the number of attributes (should be 3)
		int numOfAttributes = parser.getAttributeCount();
				
		// Loop through these attributes and set the id, name, and address values
		for(int i = 0; i < numOfAttributes; i++)
			if( parser.getAttributeName( i ) == "id" )
				id = Integer.valueOf( parser.getAttributeValue(i) );
			else if( parser.getAttributeName( i ).equals( "name" ) )
				name = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ).equals( "address" ) )
				address = parser.getAttributeValue( i );
		
		// Create new client
		Client lastClient = new Client( name, id, address );
		
		// Add the client to the Franchise
		this.franchise.addClient( lastClient );
		
		//return client
		return lastClient;
	}
	
	/**
	 * Parses the contract tag
	 * <p>
	 * Adds a contract to this class's last client element and 
	 * sets the "lastContract" to this new contract as well.
	 * <p>
	 * @param XmlPullParser parser - the parser (located at a contract tag)
	 * @return 
	 */
	private Contract contractParser( XmlPullParser parser, Client lastClient ){
		
		// Error Checking
		if( !parser.getName().equals( "clientContract" ) )
			return null;
		
		// Initialize variables
		String terms = "ACME Inc.";
		String id = "0";
		int no = 0;
		GregorianCalendar startDate = null;
		GregorianCalendar endDate = null;
		
		// find the number of attributes (should be 5)
		int numOfAttributes = parser.getAttributeCount();
				
		// Loop through these attributes and set the id, no, terms, startDate and endDate attributes
		for(int i = 0; i < numOfAttributes; i++)
			if( parser.getAttributeName( i ).equals( "id"  ) )
				id = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ).equals( "No" ) )
				no = Integer.valueOf( parser.getAttributeValue(i) );
			else if( parser.getAttributeName( i ).equals( "terms" ) )
				terms = parser.getAttributeValue( i );
			else if( parser.getAttributeValue( i ).equals( "StartDate" ) ){
				
				String [] temp = parser.getAttributeValue( i ).split("/");
				int year = Integer.valueOf( temp[2] );
				int month = Integer.valueOf( temp[1] ) - 1;
				int day = Integer.valueOf( temp[0] ) ;
				
				startDate = new GregorianCalendar(year, month, day);
			}
			else if( parser.getAttributeValue( i ).equals( "EndDate" ) ){
				
				String [] temp = parser.getAttributeValue( i ).split("/");
				int year = Integer.valueOf( temp[2] );
				int month = Integer.valueOf( temp[1] ) - 1;
				int day = Integer.valueOf( temp[0] ) ;
				
				endDate = new GregorianCalendar(year, month, day);
			}
		
		// Create a new contract
		Contract lastContract = new Contract( id, no, startDate, endDate, terms );
		
		// Add the contract to the lastClient
		lastClient.addContract( lastContract );
		
		return lastContract;
	}
	
	/**
	 * Parses the building (serviceAddress) tag
	 * <p>
	 * Adds a building to this class's last contract element and 
	 * sets the "lastBuilding" to this new building as well.
	 * <p>
	 * @param XmlPullParser parser - the parser (located at a building tag)
	 */
	private Building buildingParser( XmlPullParser parser, Contract lastContract){
		// Error Checking
		if( !parser.getName().equals( "ServiceAddress" ) )
			return null;
		
		// Initialize variables
		String id = "B1";
		String address = "123 ABC St";
		String postalCode = "N6G 2P4";
		String contact = "John Smith";
		String city = "London";
		String province = "Ontario";
		String country = "Canada";
		String inspectorId = "ID001";
		GregorianCalendar timeStamp = null;
		
		// find the number of attributes (should be 9)
		int numOfAttributes = parser.getAttributeCount();
				
		// Loop through these attributes and set the id, name, and address values
		for(int i = 0; i < numOfAttributes; i++)
			if( parser.getAttributeName( i ).equals( "id" ) )
				id = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ).equals(  "address" ) )
				address = parser.getAttributeValue( i );
			else if( parser.getAttributeName( i ).equals( "postalCode" ) )
				postalCode = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ).equals( "contact" ) )
				contact = parser.getAttributeValue( i );
			else if( parser.getAttributeName( i ).equals( "city" ) )
				city = parser.getAttributeValue( i );
			else if( parser.getAttributeName( i ).equals(  "province" ) )
				province = parser.getAttributeValue( i );
			else if( parser.getAttributeName( i ).equals( "country" ) )
				country = parser.getAttributeValue( i );
			else if( parser.getAttributeName( i ).equals(  "InspectorID" ) )
				inspectorId = parser.getAttributeValue( i );
			else if( parser.getAttributeValue( i ).equals(  "testTimeStamp" ) ){
				
				String [] temp = parser.getAttributeValue( i ).split(" ");
				int year = Integer.valueOf( temp[0].substring(0, 4) );
				int month = Integer.valueOf( temp[0].substring(4, 6) ) - 1;
				int day = Integer.valueOf( temp[0].substring(6, 8) ) ;
				
				int hour = 	Integer.valueOf( temp[1].substring(0,5).split(":")[0] );
				int minute = Integer.valueOf( temp[1].substring(0,5).split(":")[1] );
				
				timeStamp = new GregorianCalendar(year, month, day, hour, minute);
			}
		
		// Create new building
		Building lastBuilding = new Building( id, address, postalCode, city, province, country, contact, inspectorId, timeStamp );
		
		// Add the building to the last contract
		lastContract.addBuilding( lastBuilding );
		
		return lastBuilding;
	}
	
	private Floor floorParser( XmlPullParser flrParser, Building lastBuilding ){
		// Create Floor Object
		Floor floorObject = new Floor("");
		
		// Expected - 1 attribute (name)
		int counter = flrParser.getAttributeCount();
		
		for ( int i = 0; i < counter; i++ )
		{
			if (flrParser.getAttributeName( i ).equals(  "name" ) )
				floorObject.setName( flrParser.getAttributeValue( i ) );
		}
		
		// Add Floor to Above Building
		lastBuilding.addFloor( floorObject );
		return floorObject;
	}
	
	private Room roomParser( XmlPullParser rmParser, Floor lastFloor ){		
		// Create Room Object
		Room roomObject = new Room("", 0);
		
		// Expected - 2 attributes (id, No)
		int counter = rmParser.getAttributeCount();
		
		for ( int i = 0; i < counter; i++ )
		{
			if ( rmParser.getAttributeName( i ).equals( "id" ) )
				roomObject.setId( rmParser.getAttributeValue( i ) );
			else if ( rmParser.getAttributeName( i ).equals( "No" ) )				
				//TODO: what if room no is null?
				roomObject.setRoomNo( Integer.parseInt( rmParser.getAttributeValue( i ) ) );
		}

		// Add Room to Above Floor
		lastFloor.addRoom( roomObject );
		return roomObject;
	}
	private Equipment equipmentParser( XmlPullParser equipParser, Room lastRoom ){
		// Create Equipment Object
		Equipment equipObject = new Equipment( equipParser.getName() );
		
		// Varying number of expected attributes
		int counter = equipParser.getAttributeCount();
		
		for ( int i = 1; i < counter; i++ )
			equipObject.addAttribute( equipParser.getAttributeName( i ), equipParser.getAttributeValue( i ) );

		// Equipment equipObject = new Equipment(equipParser.getAttributeValue(0));
		
		// Add Equipment to Above Room
		lastRoom.addEquipment( equipObject );
		return equipObject;
	}
	private void inspectionElementParser( XmlPullParser elementParser, Equipment lastEquipment ){	
		
		// Error checking
		if( !elementParser.getName().matches("inspectionElement_[0-9]+") )
			return;
		
		// Expected - 3 attributes (name, testResult, testNote)
		int counter = elementParser.getAttributeCount();
		
		// Initialize variables 
		String name = "testingElement";
		
		for ( int i = 0; i < counter; i++ )
			if (elementParser.getAttributeName( i ).equals(  "name" ) )
				name = elementParser.getAttributeValue( i );
		
		//Add Inspection Element to Above Equipment
		lastEquipment.addInspectionElement( new InspectionElement( name ) );
		
	}
}

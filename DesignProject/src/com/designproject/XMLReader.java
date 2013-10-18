package com.designproject;

import java.io.IOException;
import java.io.StringReader;
import java.util.GregorianCalendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * 
 * @author NikLubz and Jessica
 *
 */
public class XMLReader {

	private String pathToXMLFile;
	private Franchise franchise;
	private XmlPullParser parser;
	
	// store the more recently created element
	private Client lastClient;
	private Contract lastContract;
	private Building lastBuilding;
	private Floor lastFloor;
	private Room lastRoom;
	private Equipment lastEquipment;
	
	/**
	 * Constructor
	 * @param String pathToXMLFile - the path to the XML file being used
	 * @throws XmlPullParserException
	 */
	public XMLReader(String pathToXMLFile) throws XmlPullParserException{
		
		//The path to the XML file
		this.pathToXMLFile = pathToXMLFile;
		
		//Create the factory (temp) to create the parser (member variable)
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		parser = factory.newPullParser();
	}
	
	public void parseXML() throws XmlPullParserException, IOException{
		
		// Set the input
		parser.setInput( new StringReader( this.pathToXMLFile ));
		
		// Prepare for the while loop
		int eventType = parser.getEventType();
		
		// Loop through the XML document
		while(eventType != XmlPullParser.END_DOCUMENT){
			
			switch(eventType){
			case XmlPullParser.START_DOCUMENT:
				System.out.println("Starting parsing of document");
				break;
			case XmlPullParser.START_TAG:
				if( parser.getName() == "Franchisee" ) franchiseParser( parser );
				else if( parser.getName() == "Client" ) clientParser( parser );
				else if( parser.getName() == "clientContract" ) contractParser( parser );
				else if( parser.getName() == "ServiceAddress" ) buildingParser( parser );
				else if( parser.getName() == "Floor" ) floorParser( parser );
				else if( parser.getName() == "Room" ) roomParser( parser );
				else if( parser.getName() == "Extinguisher" ) equipmentParser( parser );
				else if( parser.getName() == "inspectionElement" ) inspectionElementParser( parser );
				break;
			case XmlPullParser.END_TAG:
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
	private void franchiseParser( XmlPullParser parser ){
		
		// Error checking
		if( parser.getName() != "Franchisee" )
			return;
		
		// Initialize the variables
		int id = 0;
		String ownerName = "John Smith";
		
		// find the number of attributes (should be 2)
		int numOfAttributes = parser.getAttributeCount();
		
		// Loop through these attributes and set the id and owner name values
		for(int i = 0; i < numOfAttributes; i++)
			if( parser.getAttributeName( i ) == "id" )
				id = Integer.valueOf( parser.getAttributeValue(i) );
			else if( parser.getAttributeName( i ) == "name" )
				ownerName = parser.getAttributeValue(i);
		
		// Create franchise, and add the correct information.
		this.franchise = new Franchise( id, ownerName );
	}
	
	/**
	 * Parses the client tag
	 * <p>
	 * Adds a client to this class's franchise element and 
	 * sets the "lastClient" to this new client as well.
	 * <p>
	 * @param XmlPullParser parser - the parser (located at a client tag)
	 */
	private void clientParser( XmlPullParser parser ){
		
		// Error Checking
		if( parser.getName() != "Client" )
			return;
		
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
			else if( parser.getAttributeName( i ) == "name" )
				name = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ) == "address" )
				address = parser.getAttributeValue(i);
		
		// Create new client
		this.lastClient = new Client( name, id, address );
		
		// Add the client to the Franchise
		this.franchise.addClient( this.lastClient );
	}
	
	/**
	 * Parses the contract tag
	 * <p>
	 * Adds a contract to this class's last client element and 
	 * sets the "lastContract" to this new contract as well.
	 * <p>
	 * @param XmlPullParser parser - the parser (located at a contract tag)
	 */
	private void contractParser( XmlPullParser parser ){
		
		// Error Checking
		if( parser.getName() == "clientContract" )
			return;
		
		// Initialize variables
		String terms = "ACME Inc.";
		int id = 0;
		int no = 0;
		GregorianCalendar startDate = null;
		GregorianCalendar endDate = null;
		
		// find the number of attributes (should be 5)
		int numOfAttributes = parser.getAttributeCount();
				
		// Loop through these attributes and set the id, no, terms, startDate and endDate attributes
		for(int i = 0; i < numOfAttributes; i++)
			if( parser.getAttributeName( i ) == "id" )
				id = Integer.valueOf( parser.getAttributeValue(i) );
			else if( parser.getAttributeName( i ) == "No" )
				no = Integer.valueOf( parser.getAttributeValue(i) );
			else if( parser.getAttributeName( i ) == "terms" )
				terms = parser.getAttributeValue(i);
			else if( parser.getAttributeValue( i ) == "StartDate" ){
				
				String [] temp = parser.getAttributeValue( i ).split("/");
				int year = Integer.valueOf( temp[2] );
				int month = Integer.valueOf( temp[1] ) - 1;
				int day = Integer.valueOf( temp[0] ) ;
				
				startDate = new GregorianCalendar(year, month, day);
			}
			else if( parser.getAttributeValue( i ) == "EndDate" ){
				
				String [] temp = parser.getAttributeValue( i ).split("/");
				int year = Integer.valueOf( temp[2] );
				int month = Integer.valueOf( temp[1] ) - 1;
				int day = Integer.valueOf( temp[0] ) ;
				
				endDate = new GregorianCalendar(year, month, day);
			}
		
		// Create a new contract
		this.lastContract = new Contract( id, no, startDate, endDate, terms );
		
		// Add the contract to the lastClient
		this.lastClient.addContract( this.lastContract );
	}
	
	/**
	 * Parses the building (serviceAddress) tag
	 * <p>
	 * Adds a building to this class's last contract element and 
	 * sets the "lastBuilding" to this new building as well.
	 * <p>
	 * @param XmlPullParser parser - the parser (located at a building tag)
	 */
	private void buildingParser( XmlPullParser parser ){
		
		// Error Checking
		if( parser.getName() != "serviceAddress" )
			return;
		
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
			if( parser.getAttributeName( i ) == "id" )
				id = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ) == "address" )
				address = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ) == "postalCode" )
				postalCode = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ) == "contact" )
				contact = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ) == "city" )
				city = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ) == "province" )
				province = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ) == "country" )
				country = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ) == "InspectorID" )
				inspectorId = parser.getAttributeValue(i);
			else if( parser.getAttributeValue( i ) == "testTimeStamp" ){
				
				String [] temp = parser.getAttributeValue( i ).split(" ");
				int year = Integer.valueOf( temp[0].substring(0, 4) );
				int month = Integer.valueOf( temp[0].substring(4, 6) ) - 1;
				int day = Integer.valueOf( temp[0].substring(6, 8) ) ;
				
				int hour = 	Integer.valueOf( temp[1].substring(0,5).split(":")[0] );
				int minute = Integer.valueOf( temp[1].substring(0,5).split(":")[1] );
				
				timeStamp = new GregorianCalendar(year, month, day, hour, minute);
			}
		
		// Create new building
		this.lastBuilding = new Building( id, address, postalCode, city, province, country, contact, inspectorId, timeStamp );
		
		// Add the building to the last contract
		this.lastContract.addBuilding( this.lastBuilding );
	}
	
	private void floorParser(XmlPullParser flrParser)
	{
		//Create Floor Object
		Floor floorObject = new Floor("");
		
		//Expected - 1 attribute (name)
		int counter = flrParser.getAttributeCount();
		
		for ( int i = 0; i < counter; i++ )
		{
			if (flrParser.getAttributeName(i) == "name")
				floorObject.setName(flrParser.getAttributeValue(i));
		}
		
		//Add Floor to Above Building
		this.lastBuilding.addFloor(floorObject);
		this.lastFloor = floorObject;
	}
	
	private void roomParser(XmlPullParser rmParser)
	{		
		//Create Room Object
		Room roomObject = new Room("", 0);
		
		//Expected - 2 attributes (id, No)
		int counter = rmParser.getAttributeCount();
		
		for ( int i = 0; i < counter; i++ )
		{
			if (rmParser.getAttributeName(i) =="id")
				roomObject.setId(rmParser.getAttributeValue(i));
			else if (rmParser.getAttributeName(i) == "No")
				roomObject.setRoomNo(Integer.parseInt(rmParser.getAttributeValue(i)));
		}

		//Add Room to Above Floor
		lastFloor.addRoom(roomObject);
		this.lastRoom = roomObject;
	}
	private void equipmentParser(XmlPullParser equipParser)
	{
		//Create Equipment Object
		Equipment equipObject = new Equipment("");
		
		//Varying number of expected attributes
		int counter = equipParser.getAttributeCount();
		
		for ( int i = 1; i < counter; i++ )
			equipObject.addAttribute(equipParser.getAttributeName(i), equipParser.getAttributeValue(i));

		//Equipment equipObject = new Equipment(equipParser.getAttributeValue(0));
		
		//Add Equipment to Above Room
		lastRoom.addEquipment(equipObject);
		this.lastEquipment = equipObject;
	}
	private void inspectionElementParser(XmlPullParser elementParser)
	{	
		//Create Inspection Element
		InspectionElement element = new InspectionElement("");
		
		//Expected - 3 attributes (name, testResult, testNote)
		int counter = elementParser.getAttributeCount();
		
		for ( int i = 0; i < counter; i++ )
		{
			if (elementParser.getAttributeName(i) == "name")
				element.setName(elementParser.getAttributeValue(i));
			else if (elementParser.getAttributeName(i) == "testResult")
				element.setTestResult(Boolean.valueOf(elementParser.getAttributeValue(i)));
			else if (elementParser.getAttributeName(i) == "testNote")
				element.setTestNotes(elementParser.getAttributeValue(i));
		}
		
		//Add Inspection Element to Above Equipment
		lastEquipment.addInspectionElement(element);
		
		/*HARD-CODED
		 * InspectionElement element = new InspectionElement(elementParser.getAttributeValue(0));
		 * element.setTestResult(Boolean.valueOf(elementParser.getAttributeValue(1)));
		 * element.setTestNotes(elementParser.getAttributeValue(2));
		*/
	}
}

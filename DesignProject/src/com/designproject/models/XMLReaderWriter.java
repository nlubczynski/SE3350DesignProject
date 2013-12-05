package com.designproject.models;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.util.Xml;

import com.designproject.R;
import com.designproject.models.Equipment.node;
/**
 * 
 * @author NikLubz and Jessica
 *
 */
public class XMLReaderWriter {

	private Franchise franchise;
	private Context context;
	
	/**
	 * Constructor
	 * @param String pathToXMLFile - the path to the XML file being used
	 * @throws XmlPullParserException
	 */
	public XMLReaderWriter( Context context ) throws XmlPullParserException{
		// The application context
		this.context = context;
	}
	
	public boolean writeXML(Franchise franchise){
		
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		
		try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        
	        // Franchise 
	        // <Franchisee id="1001" name="Darwin Fleming">
	        serializer.startTag( "", "Franchisee");
	        serializer.attribute( "", "id", String.valueOf( franchise.getId() ) );
	        serializer.attribute( "", "name", franchise.getOwnerName() );
	        // Clients 
	        for( Client client: franchise.getClients() ){
		        // <Client id="1001-01" name="North Bay Inc" address="North Bay, Muskoka, Parry Sound, Orillia">
	        	serializer.startTag( "", "Client");
	        	serializer.attribute( "", "id", String.valueOf( client.getId() ) );
	        	serializer.attribute( "", "name", client.getName() );
	        	serializer.attribute( "", "address", client.getAddress() );
	        	//Contracts
	        	for( Contract contract: client.getContracts() ){
	        		// <clientContract id="1001-01-003" No ="1234" startDate ="01/01/2013" endDate ="31/12/2014" terms="">
	        		serializer.startTag("", "clientContract");
	        		serializer.attribute( "", "id", contract.getId() );
	        		serializer.attribute( "", "No", String.valueOf( contract.getNo() ) );
	        		// start date to ints
	        		int day = contract.getStartDate().get( Calendar.DAY_OF_MONTH );
	        		int month = contract.getStartDate().get( Calendar.MONTH ) + 1;
	        		int year = contract.getStartDate().get( Calendar.YEAR );
	        		
	        		// need to make sure that day, month, hour, and minutes are not single digits
	        		// hourString and minute string are used in the next level of loop
	        		String dayString, monthString, hourString, minuteString;
        			if(day < 10)
        				dayString = "0" + day;
        			else
        				dayString = String.valueOf( day );
        			if(month < 10)
        				monthString = "0" + month;
        			else
        				monthString = String.valueOf( month );
	        		
	        		serializer.attribute("", "startDate", dayString + "/" + monthString + "/" + year );
	        		// end date to ints
	        		day = contract.getEndDate().get( Calendar.DAY_OF_MONTH );
	        		month = contract.getEndDate().get( Calendar.MONTH ) + 1;
	        		year = contract.getEndDate().get( Calendar.YEAR );
	        		
	        		// need to make sure that day, month, hour, and minutes are not single digits
        			if(day < 10)
        				dayString = "0" + day;
        			else
        				dayString = String.valueOf( day );
        			if(month < 10)
        				monthString = "0" + month;
        			else
        				monthString = String.valueOf( month );
	        		
	        		serializer.attribute("", "endDate", dayString + "/" + monthString + "/" + year );
	        		serializer.attribute("", "terms", contract.getTerms() );
	        		// Buildings
	        		for( Building building: contract.getBuildings() ){
	        			// <ServiceAddress id="S1" address="123 Sesame Street" postalCode="N6G 2P4" contact="" city="London" 
	        			// province="Ontario" country="Canada" InspectorID="ID001" testTimeStamp="20131009 09:49PM">
	        			serializer.startTag( "", "ServiceAddress" );
	        			serializer.attribute( "", "id", building.getId() );
	        			serializer.attribute( "", "address", building.getAddress() );
	        			serializer.attribute("", "postalCode", building.getPostalCode() );
	        			serializer.attribute( "", "contact", building.getContact() );
	        			serializer.attribute( "", "city", building.getCity() );
	        			serializer.attribute( "", "province", building.getProvince() );
	        			serializer.attribute( "", "country", building.getCountry() );
	        			serializer.attribute( "", "InspectorID", building.getLastInspectedBy() );
	        			// Time Stamp ints
	        			day = building.getTimeStamp().get( Calendar.DAY_OF_MONTH );
        				month = building.getTimeStamp().get( Calendar.MONTH ) + 1;	        				
	        			year = building.getTimeStamp().get( Calendar.YEAR );
	        			int hour = building.getTimeStamp().get( Calendar.HOUR);
	        			int minute = building.getTimeStamp().get( Calendar.MINUTE );
	        			String am_pm = building.getTimeStamp().get( Calendar.AM_PM ) == Calendar.AM ? "AM" : "PM";
	        			
	        			// need to make sure that day, month, hour, and minutes are not single digits
	        			if(day < 10)
	        				dayString = "0" + day;
	        			else
	        				dayString = String.valueOf( day );
	        			if(month < 10)
	        				monthString = "0" + month;
	        			else
	        				monthString = String.valueOf( month );
	        			if(hour < 10)
	        				hourString = "0" + hour;
	        			else
	        				hourString = String.valueOf( hour );
	        			if(minute < 10)
	        				minuteString = "0" + minute;
	        			else
	        				minuteString = String.valueOf( minute );
	        			
	        			serializer.attribute( "", "testTimeStamp", year + monthString + dayString + " " 
	        					+ hourString + ":" + minuteString + am_pm);
	        			// Floors
	        			for(Floor floor: building.getFloors() ){
	        				// <Floor name="First Floor">
	        				serializer.startTag( "", "Floor" );
	        				serializer.attribute( "", "name", floor.getName() );
	        				// Rooms
	        				for(Room room: floor.getRooms() ){
	        					// <Room id="R1" No="1">
	        					serializer.startTag( "", "Room");
	        					serializer.attribute( "", "id", room.getId() );
	        					serializer.attribute( "", "No", String.valueOf( room.getRoomNo() ) );
	        					// Equipment
	        					for(Equipment equipment: room.getEquipment() ){
	        						// <Extinguisher id="33101" location="East Stair" size="10" type="ABC" model="Amerex" serialNo="s123">
	        						serializer.startTag( "", equipment.getName() );
	        						serializer.attribute( "", "id", equipment.getID() );
	        						serializer.attribute( "", "location", equipment.getLocation() );
	        						for( node attribute: equipment.getAttributes() )
	        							serializer.attribute( "", attribute.getName() , attribute.getValue() );
	        						// Inspection elements
	        						for(int i = 0; i < equipment.getInspectionElements().length; i++){
	        							//Break on "comments"
	        							if( equipment.getInspectionElements()[i].getName().equals("Comments") )
	        								continue;
	        							// <inspectionElement_1 name="Hydro Test" testResult="" testNote=""/>
	        							serializer.startTag("", "inspectionElement_" + String.valueOf( i + 1 ) );
	        							serializer.attribute( "", "name", equipment.getInspectionElements()[i].getName() );
	        							//Check for the hardcoded names of elements
	        							if( equipment.getInspectionElements()[i].getTestNotes().length() > 0 )
	        								// There is a comment, and that means that we're dealing with a special case
	        								serializer.attribute( "", "testResult", equipment.getInspectionElements()[i].getTestNotes());
	        							else
		        							serializer.attribute( "", "testResult", equipment.getInspectionElements()[i].hasBeenTested() == false ? "" :
		        									equipment.getInspectionElements()[i].getTestResult() == true ? "PASS" : "FAIL" );
	        							serializer.attribute( "", "testNote", equipment.getInspectionElements()[i].getTestNotes() );
	        							serializer.endTag( "", "inspectionElement" );
	        						}
	        						serializer.endTag( "", equipment.getName() );
	        					}
	        					serializer.endTag( "", "Room" );
	        				}
	        				serializer.endTag( "", "Floor" );
	        			}
	        			serializer.endTag( "", "ServiceAddress" );
	        		}
	        		serializer.endTag( "", "clientContract");
	        	}
	        	serializer.endTag( "", "Client");
	        }
	        serializer.endTag( "", "Franchisee" );        
	        serializer.endDocument();
		} catch (Exception e) {
	        return false;
	    } 
		//Write the file
		if(canWrite()){
			
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File (sdCard.getAbsolutePath() + "/FireAlertApp");
			
			//It doesn't exist, and we can't make it - we don't have the permissions
			if( !(dir.mkdirs() || dir.isDirectory()) )
				return false;
			
			File file = new File(dir, "FireAlertData.xml");
			
			FileOutputStream os;
			BufferedOutputStream out;
			
			try {
				os = new FileOutputStream(file);
				out = new BufferedOutputStream(os);
				out.write( writer.toString().getBytes() );
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		else
			return false;
		
		return true;
	}
	
	public String getXML() throws IOException{		
		
		if(canRead()){
			File sdCard = Environment.getExternalStorageDirectory();
			File file = new File(sdCard.getAbsoluteFile() + "/FireAlertApp/" , "FireAlertData.xml");
			
			if(file.exists()){			
				BufferedReader br = new BufferedReader(new FileReader(file));		
				StringBuilder out = new StringBuilder();
				
				String line = br.readLine();
				while(line != null){
					out.append(line);
					line = br.readLine();
				}
				
				br.close();
				return out.toString();
			}
			else
				throw new IOException();
		}
		throw new IOException();
	}
	
	public Franchise parseXML() throws XmlPullParserException, IOException{
		
		if(canRead()){
			File sdCard = Environment.getExternalStorageDirectory();
			File file = new File(sdCard.getAbsoluteFile(), "FireAlertData.xml");
		
			if(file.exists()){
				
				FileInputStream fis = new FileInputStream(file);
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				
				// Set the parser
				XmlPullParser parser = factory.newPullParser();
				
				parser.setInput(new InputStreamReader(fis));
				
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
							else if( parser.getName().matches( "inspectionElement" ) ) 
								inspectionElementParser( parser, lastEquipment );
							break;
						case XmlPullParser.END_TAG:
							if( parser.getName().equals( "Room" ) )
								inRoom = false;
							else if( inRoom == true && inRoom == inEquipment && !parser.getName().matches( "inspectionElement" ))
								inEquipment = false;
							break;
						case XmlPullParser.TEXT:
							break;			
					}	
					eventType = parser.next();			
				}
				
				return this.franchise;
			}
			else
				throw new IOException();
		}
		else
			throw new IOException();
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
		String id = "0";
		String ownerName = "John Smith";
		
		// find the number of attributes (should be 2)
		int numOfAttributes = parser.getAttributeCount();
		
		// Loop through these attributes and set the id and owner name values
		for(int i = 0; i < numOfAttributes; i++)
			if( parser.getAttributeName( i ).equals( "id" ) )
				id = parser.getAttributeValue( i );
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
		String id = "0";
		
		// find the number of attributes (should be 3)
		int numOfAttributes = parser.getAttributeCount();
				
		// Loop through these attributes and set the id, name, and address values
		for(int i = 0; i < numOfAttributes; i++)
			if( parser.getAttributeName( i ).equals( "id" ) )
				id = parser.getAttributeValue( i );
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
		String no = "0";
		GregorianCalendar startDate = null;
		GregorianCalendar endDate = null;
		
		// find the number of attributes (should be 5)
		int numOfAttributes = parser.getAttributeCount();
				
		// Loop through these attributes and set the id, no, terms, startDate and endDate attributes
		for(int i = 0; i < numOfAttributes; i++)
			if( parser.getAttributeName( i ).equals( "id"  ) )
				id = parser.getAttributeValue(i);
			else if( parser.getAttributeName( i ).equals( "No" ) )
				no = parser.getAttributeValue( i );
			else if( parser.getAttributeName( i ).equals( "terms" ) )
				terms = parser.getAttributeValue( i );
			else if( parser.getAttributeName( i ).equals( "startDate" ) ){
				
				String [] temp = parser.getAttributeValue( i ).split("/");
				int year = Integer.valueOf( temp[2] );
				int month = Integer.valueOf( temp[1] ) - 1;
				int day = Integer.valueOf( temp[0] ) ;
				
				startDate = new GregorianCalendar(year, month, day);
			}
			else if( parser.getAttributeName( i ).equals( "endDate" ) ){
				
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
			else if( parser.getAttributeName( i ).equals(  "testTimeStamp" ) ){
				
				String [] temp = parser.getAttributeValue( i ).split(" ");
				int year = Integer.valueOf( temp[0].substring(0, 4) );
				int month = Integer.valueOf( temp[0].substring(4, 6) ) - 1;
				int day = Integer.valueOf( temp[0].substring(6, 8) ) ;
				
				int hour = 	Integer.valueOf( temp[1].substring(0,5).split(":")[0] );
				int minute = Integer.valueOf( temp[1].substring(0,5).split(":")[1] );
				
				int am_pm = temp[1].substring(5,7).equals("PM") ? Calendar.PM : Calendar.AM;
				
				timeStamp = new GregorianCalendar(year, month, day);
				timeStamp.set(Calendar.HOUR, hour);
				timeStamp.set(Calendar.MINUTE, minute);
				timeStamp.set(Calendar.AM_PM, am_pm);
				
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
		Room roomObject = new Room("", "0");
		
		// Expected - 2 attributes (id, No)
		int counter = rmParser.getAttributeCount();
		
		for ( int i = 0; i < counter; i++ )
		{
			if ( rmParser.getAttributeName( i ).equals( "id" ) )
				roomObject.setId( rmParser.getAttributeValue( i ) );
			else if ( rmParser.getAttributeName( i ).equals( "No" ) )				
				//TODO: what if room no is null?
				roomObject.setRoomNo( rmParser.getAttributeValue( i ) );
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
		
		for ( int i = 0; i < counter; i++ )
			equipObject.addAttribute( equipParser.getAttributeName( i ), equipParser.getAttributeValue( i ) );

		// Equipment equipObject = new Equipment(equipParser.getAttributeValue(0));
		
		// Add Equipment to Above Room
		lastRoom.addEquipment( equipObject );
		return equipObject;
	}
	private void inspectionElementParser( XmlPullParser elementParser, Equipment lastEquipment ){	
		
		// Error checking
		if( !elementParser.getName().matches("inspectionElement") )
			return;
		
		// Expected - 3 attributes (name, testResult, testNote)
		int counter = elementParser.getAttributeCount();
		
		// Initialize variables 
		String name = "testingElement";
		boolean result = false;
		String comment = "";
		
		for ( int i = 0; i < counter; i++ )
			if (elementParser.getAttributeName( i ).equals(  "name" ) )
				name = elementParser.getAttributeValue( i );
			else if (elementParser.getAttributeName( i ).equals(  "testResult" ) )
				if(elementParser.getAttributeValue( i ).equals("PASS"))
					result = true ;
			else if (elementParser.getAttributeName( i ).equals(  "testNote" ) )
				comment = elementParser.getAttributeValue( i );
		
		InspectionElement temp = new InspectionElement( name );
		if(result)
			temp.setHasBeenTested();
		temp.setTestNotes(comment);
		temp.setTestResult(result);
		
		//Add Inspection Element to Above Equipment
		lastEquipment.addInspectionElement( temp );
		
	}
	private boolean canRead(){
		
		boolean mExternalStorageAvailable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = false;
		}
		
		return mExternalStorageAvailable;
	}
	private boolean canWrite(){

		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
			mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
			mExternalStorageWriteable = true;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
			mExternalStorageWriteable = false;
		}
		return mExternalStorageWriteable;
	}
}

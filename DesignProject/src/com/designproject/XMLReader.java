package com.designproject;

import java.io.IOException;

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
		
		int eventType = parser.getEventType();
		
		while(eventType != XmlPullParser.END_DOCUMENT){
			
			switch(eventType){
			case XmlPullParser.START_DOCUMENT:
				System.out.println("Starting parsing of document");
				break;
			case XmlPullParser.START_TAG:
				if( parser.getName() == "Franchisee" ) break;
				else if( parser.getName() == "Client" ) break;
				else if( parser.getName() == "clientContract" ) break;
				else if( parser.getName() == "ServiceAddress" ) break;
				else if( parser.getName() == "Floor" ) break;
				else if( parser.getName() == "Room" ) break;
				else if( parser.getName() == "Extinguisher" ) break;
				else if( parser.getName() == "inspectionElement" ) break;
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
	
}

package com.designproject;

/**
 * 
 * @author NikLubz
 *
 */
public class InspectionElement {
	
	private String name;
	private boolean testResult;
	private String testNotes;
	private boolean hasBeenTested;
	
	/**
	 * 
	 * @param String name - the name of the Inspection Element
	 */
	public InspectionElement(String name){
		this.name = name;
		this.testResult = false;
		this.hasBeenTested = false;
		this.testNotes = "";
	}

	/**
	 * @return String name - return the name of the inspection element
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param String name - the name to set
	 * @return Boolean result - whether the name change was succesful or not
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
	 * @return Boolean testResult - the current result of the test
	 */
	public boolean getTestResult() {
			return testResult;
	}
	
	/**
	 * 
	 * @param Boolean testResult - the result of the test
	 * @return Boolean result - whether or not the test result value was updated
	 */
	public boolean setTestResult(boolean testResult){
		try{
			this.testResult = testResult;

		}catch(Exception e){
			return false;
		}
		//mark that we've tested this element
		this.hasBeenTested = true;
		return true;
	}
	/**
	 * 
	 * @return Boolean hasBeenTested - returns whether or not this element has been inspected
	 */
	public boolean hasBeenTested(){
		return this.hasBeenTested;
	}
	
	/**
	 * 
	 * @return void - sets whether or not this element has been inspected
	 */
	public void setHasBeenTested(){
		this.hasBeenTested = true;
	}
	
	/**
	 * @return String testNotes - returns the current test notes
	 */
	public String getTestNotes() {
		return testNotes;
	}

	/**
	 * @param String testNotes - the testNotes to set
	 * @result Boolean result - returns whether or not the new test notes were saved
	 */
	public boolean setTestNotes(String testNotes) {
		try{
			this.testNotes = testNotes;
		}catch(Exception e){
			return false;
		}
		return true;
	}
	

}

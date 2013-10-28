package com.designproject;

import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class HelperMethods {

/**
	 * 
	 * @param
	 * @boolean
	 * @author Braden
	 */
	public final static	ArrayList<DateTime> getInspectionDueDates(DateTime startDateTime, DateTime endDateTime, String terms)
	{
		/*TODO: should probably make this work off of days and not months */
		ArrayList<DateTime> checkDates = new ArrayList<DateTime>();
		
		//Total days between start of contract and end of contract
		int days = Days.daysBetween(startDateTime, endDateTime).getDays();
		
		//Contract length in months
		int spanInMonths = days/30;
		
		int termsInteger = 1;
		
		if (terms.length() != 0)
			termsInteger = Integer.parseInt(terms);
		
		// Figure out the length in time between inspections
		int inspectionSpan = spanInMonths/termsInteger;
		
		//Get the next inspection day
		DateTime nextInspectionDay = startDateTime.plusMonths(inspectionSpan);

		//If the next day is outside the contract time then just use end date
		if(nextInspectionDay.isAfter(endDateTime))
			checkDates.add(endDateTime);
		
		//Add inspection days to array
		while(nextInspectionDay.isBefore(endDateTime))
		{
			checkDates.add(nextInspectionDay);
			nextInspectionDay = nextInspectionDay.plusMonths(inspectionSpan);
		}
		
		return checkDates;
	}
	
	public final static Interval returnNextInspectionInterval(Calendar startDateCalendar, Calendar endDateCalendar, String terms)
	{
		DateTime today = DateTime.now();
		DateTime startDateTime = new DateTime(startDateCalendar);
		DateTime endDateTime = new DateTime(endDateCalendar);
		
		
		ArrayList<DateTime> inspectionDueDates = HelperMethods.getInspectionDueDates(startDateTime, endDateTime, terms);

		if(inspectionDueDates.size() == 0)
			inspectionDueDates.add(endDateTime);
		
		DateTime nextInspectionPeriodStartDate = startDateTime;
		DateTime nextInspectionPeriodEndDate = inspectionDueDates.get(0);
		
		LocalDate todayLocalDate = today.toLocalDate();

		for(DateTime inspectionDueDate : inspectionDueDates )
		{
			nextInspectionPeriodEndDate = inspectionDueDate;
			
			if(today.isBefore(inspectionDueDate) || todayLocalDate.isEqual(inspectionDueDate.toLocalDate()))
				break;
			
			nextInspectionPeriodStartDate = inspectionDueDate;
		}
		
		Interval inspectionInterval = new Interval(nextInspectionPeriodStartDate, nextInspectionPeriodEndDate);

		return inspectionInterval;
	}
	
	public final static boolean checkIfInspectionCompleted(Interval inspectionInterval, Contract contract)
	{
		boolean isCompleted = true;
		Building[] buildings = contract.getBuildings();
		DateTime lastInspectedDateTime = null;
		
		for(Building building : buildings)
		{
			lastInspectedDateTime = new DateTime(building.getTimeStamp());
			if(inspectionInterval.contains(lastInspectedDateTime))
			{
				isCompleted = false;
				break;
			}
		}
		return isCompleted;
		
	}
}



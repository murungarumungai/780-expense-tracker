package com.example.expensetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility defines some help methods for date calculation/conversion
 */
public class Utility {
	
	//calculate current date
	public static String getDefaultStartDate(){
		
		Calendar c= Calendar.getInstance();
		int year= c.get(Calendar.YEAR);
		int month= c.get(Calendar.MONTH);
		int day= c.get(Calendar.DAY_OF_MONTH);
		
		String sDate = formatDate(month,day,year);
	    return sDate;
	}
	
	//calculate the end date giving the start date and range
	public static String calEndDate(String start,String range){
		
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		Date sDate = null;
		try {
			sDate = format.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(sDate);
		
		if(range.equals("Day")){
			cal.add(Calendar.DATE, 1);
		}
		else if(range.equals("Week")){
			cal.add(Calendar.DATE, 7);
		}
		else if(range.equals("Month")){
			cal.add(Calendar.MONTH, 1);
		}
		else{
			cal.add(Calendar.YEAR, 1);
		}
		
		int year= cal.get(Calendar.YEAR);
		int month= cal.get(Calendar.MONTH);
		int day= cal.get(Calendar.DAY_OF_MONTH);
		
		String eDate = Utility.formatDate(month,day,year);
		
		return eDate;
	    	
    }
	//format the date: add 1 to the raw month value;
	//add '0' to month and date if they are single digit
	public static String formatDate(int month, int day, int year) {
    	// Month is 0 based so add 1
    	month++;
    	String formattedMonth;
    	String formattedDay;
    	if(month < 10)
    		formattedMonth = "0"+ Integer.toString(month);
    	else
    		formattedMonth = Integer.toString(month);
    	
    	if(day < 10)
    		formattedDay = "0"+ Integer.toString(day);
    	else
    		formattedDay = Integer.toString(day);
    	
    	return (new StringBuilder()
        
                .append(formattedMonth).append("-").append(formattedDay).append("-").append(year)).toString();
    }
	
	//convert date format from mm-dd-yyyy to yyyy-mm-dd to satisfy mySQL format
	public static String convertDate(String mDate){
		 return mDate.substring(6)+ "-"+mDate.substring(0,2)+"-"+ mDate.substring(3,5);
	 }


}

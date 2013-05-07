package com.example.expensetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * This demonstrates the use of action bar tabs and how they interact
 * with other action bar features.
 */
public class SettingActivity extends Activity {
	protected static final int DATE_DIALOG_ID = 0;
	private EditText startDate;
	private Spinner rangeSpinner,accountSpinner,chartSpinner;
	
	
	static final int DATE_DIALOG_ID1 = 1;
	private int startYear;
	private int startMonth;
	private int startDay;

	private String tag;
	private static boolean isSettingOn = false;
	private static ArrayList<String> accountList;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSettingOn = true;
        setContentView(R.layout.activity_setting);
        
        rangeSpinner = (Spinner) findViewById(R.id.range_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> rangeAdapter = ArrayAdapter.createFromResource(this,
                R.array.range_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        rangeSpinner.setAdapter(rangeAdapter);
        
        chartSpinner = (Spinner) findViewById(R.id.chart_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> chartAdapter = ArrayAdapter.createFromResource(this,
                R.array.charts_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        chartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        chartSpinner.setAdapter(chartAdapter);
        
        accountSpinner = (Spinner) findViewById(R.id.account_spinner);
        
        Bundle extras = getIntent().getExtras();
        tag = extras.getString("tag");
        new GetAccountNumber().execute(extras.getString("username"));
        
        
        
        startDate = (EditText) findViewById(R.id.showStartDate);
        setDefaultStartDate();
        final Calendar c = Calendar.getInstance();
        startYear = c.get(Calendar.YEAR);
        
        startMonth = c.get(Calendar.MONTH);
        
        startDay = c.get(Calendar.DAY_OF_MONTH);
       
        
        
        startDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        
       
    }
    
    protected Dialog onCreateDialog(int id) {
    	return new DatePickerDialog(this, mDateSetListener, startYear, startMonth,startDay);
    }

    // updates the date in the TextView

    private static String formatDate(int month, int day, int year) {
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
    
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            // TODO Auto-generated method stub
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
            String sDate = formatDate(startMonth,startDay,startYear);
            startDate.setText(sDate);
        }
    };

 
public void setDefaultStartDate(){
    	
		Calendar c= Calendar.getInstance();
		int year= c.get(Calendar.YEAR);
		int month= c.get(Calendar.MONTH);
		int day= c.get(Calendar.DAY_OF_MONTH);
		
		String sDate = formatDate(month,day,year);
        startDate.setText(sDate);
 
    }

public static String getDefaultStartDate(){
	
	Calendar c= Calendar.getInstance();
	int year= c.get(Calendar.YEAR);
	int month= c.get(Calendar.MONTH);
	int day= c.get(Calendar.DAY_OF_MONTH);
	
	String sDate = formatDate(month,day,year);
    return sDate;
}

    
	public String calEndDate(){
		String start = startDate.getText().toString();
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		Date sDate = null;
		try {
			sDate = format.parse(start);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(sDate);
		
		String range = rangeSpinner.getSelectedItem().toString();

		
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
		
		String eDate = formatDate(month,day,year);
		
		return eDate;
	    	
    }

	public void onClickView(View v) {
		
	    Intent intent = new Intent(this, ExpenseActivity.class);
	    intent.putExtra("start_date", startDate.getText().toString());
	    
	    String endDate = calEndDate();
	    intent.putExtra("end_date", endDate);
	    String account = accountSpinner.getSelectedItem().toString();
	    int start = account.indexOf("(")+1;
	    int end = account.indexOf(")");
	    String accountNum = account.substring(start,end);
	    intent.putExtra("account_number",accountNum);
	    intent.putExtra("chart_type",chartSpinner.getSelectedItem().toString());
	   
	    startActivity(intent);
		
		
	}
	
	public void onClickAdd(View v) {
		Intent intent;
			
		intent = new Intent(this, RecordActivity.class);
		
	    intent.putExtra("date", startDate.getText().toString());
	    
	    String account = accountSpinner.getSelectedItem().toString();
	    //int start = account.indexOf("(")+1;
	   // int end = account.indexOf(")");
	    //String accountNum = account.substring(start,end);
	    intent.putExtra("account",account);
	    intent.putExtra("tag",tag);
	   
	    startActivity(intent);
			
			
		}
	
public String getAccount(){
	return accountSpinner.getSelectedItem().toString();
}
	public static ArrayList<String> getAccountList(){
		return accountList;
	}
	
	public static boolean getSettingStatus(){
		return isSettingOn;
	}
	public class GetAccountNumber extends AsyncTask<String, Void, ArrayList<String>> {
    	//TextView text;

		@Override
		protected ArrayList<String> doInBackground(String... args) {
			//this.text = arg[0];
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("username",args[0]));
			return CustomHttpClient.getAccount(CustomHttpClient.getResult("http://thecity.sfsu.edu/~weiw/account.php", nameValuePairs));
			
		}
		protected void onPostExecute(ArrayList<String> list)
		{			
			// Create an ArrayAdapter using the string array and a default spinner layout
	        ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(SettingActivity.this,
	        		android.R.layout.simple_spinner_item, list.toArray(new String[list.size()]));
	        // Specify the layout to use when the list of choices appears
	        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        // Apply the adapter to the spinner
	        accountSpinner.setAdapter(accountAdapter);
	        
	        accountList = list;
	        if(NfcActivity.getTagStatus()){
		        Intent intent = new Intent(SettingActivity.this, NfcActivity.class);
		        
		        //intent.putStringArrayListExtra("list", list);
		        intent.putExtra("tag",tag);
		        //intent.putExtra("date", startDate.getText().toString());
		        //tag = null;
			    startActivity(intent);
			    
	        }
			
	        
		}	
    	
    
    }


}
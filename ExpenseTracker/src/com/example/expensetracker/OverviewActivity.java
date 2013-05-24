
package com.example.expensetracker;


import java.util.ArrayList;
import java.util.Calendar;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * OverviewActivity is the main activity that will be called when the application launches.
 * Upon creation, OverviewActivity will check if the user has been logged in or not. If not, 
 * OverviewActivity will start LoginActivity for login. After login, OverviewActivity will show
 * user's list of accounts, date range and chart type and other options for user to pick/select. 
 * If there is a pending NFC tag request, the OverviewActivity will start NfcActivity to process 
 * the information within the NFC tag.
 */

public class OverviewActivity extends Activity {
	private EditText startDate;
	private Spinner rangeSpinner,accountSpinner,chartSpinner;
	private int startYear;
	private int startMonth;
	private int startDay;

	
	private String username;
	
	private static ArrayList<String> accountList;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //check login status
        SharedPreferences prefs = getSharedPreferences("loginStatus", MODE_PRIVATE); 
		boolean isLoggedIn= prefs.getBoolean("isLoggedIn", false);
        if(!isLoggedIn){
	        Intent i = new Intent(this, LoginActivity.class);
	        startActivityForResult(i, 1);
        }
        else{
        	username = prefs.getString("username", null);
        	launchUI();
        }
       
        
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option, menu);
		return true;
	}
    
    /*
     * This method will be invoked whenever the an option is selected
     * in the menu. As right now there is only the 'logout' option in
     * the menu, this method is used to logout of the application 
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.logout_option) {
        	// SharedPreferences prefs = getSharedPreferences("loginStatus", MODE_PRIVATE); 
     		//boolean startByNfc= prefs.getBoolean("startByNfc", false);
        	//clear all the information in the sharedPreferences
     		SharedPreferences.Editor editor = getSharedPreferences("loginStatus", MODE_PRIVATE).edit(); 
    		editor.clear();
    		editor.commit();
    		editor = getSharedPreferences("tagStatus", MODE_PRIVATE).edit(); 
	        editor.clear();
	        editor.commit();
	        Intent intent = new Intent(this, LoginActivity.class);
	        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        intent.putExtra("EXIT", true);
	        startActivityForResult(intent,1);
     		
        }
        return true;
    }
    
    /*
     * This method will be called whenever LoginActivity terminates. 
     * LoginActivity returns the username used for login.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	if(resultCode == RESULT_OK){	
    		username=data.getStringExtra("username");
    		launchUI();
    	}
    	else{
    		finish();
    	}
    }
    
    /*
     * This method initializes UI for OverviewActivity.
     * rangeSpinner and chartSpinner is populated using the string array stored in the 
     * string resource file. The accountSpinner is populated by the account information
     * retrieved from the database. The AsyncTask GetAccountNumber is used to retrieve
     * the account information from the database
     * 
     */
    public void launchUI(){
    	 setContentView(R.layout.activity_overview);
         
         rangeSpinner = (Spinner) findViewById(R.id.range_spinner);
         // Create an ArrayAdapter using the string array and a default spinner layout
         ArrayAdapter<CharSequence> rangeAdapter = ArrayAdapter.createFromResource(this,
                 R.array.range_array, android.R.layout.simple_spinner_item);
         
         rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         rangeSpinner.setAdapter(rangeAdapter);
         
         chartSpinner = (Spinner) findViewById(R.id.chart_spinner);
         ArrayAdapter<CharSequence> chartAdapter = ArrayAdapter.createFromResource(this,
                 R.array.charts_array, android.R.layout.simple_spinner_item);
         
         chartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         chartSpinner.setAdapter(chartAdapter);
         
         accountSpinner = (Spinner) findViewById(R.id.account_spinner);
         new GetAccountNumber().execute(username);
         
         startDate = (EditText) findViewById(R.id.showStartDate);
         setDefaultStartDate();
     
         startDate.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 showDialog(0);
             }
         });
    }
    protected Dialog onCreateDialog(int id) {
    	return new DatePickerDialog(this, mDateSetListener, startYear, startMonth,startDay);
    }
 
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
            String sDate = Utility.formatDate(startMonth,startDay,startYear);
            startDate.setText(sDate);
        }
    };
    
    /*
     * this method uses the current date as the default start date  
     * to initialize the EditText startDate
     */
    public void setDefaultStartDate(){
    	
		Calendar c= Calendar.getInstance();
		startYear= c.get(Calendar.YEAR);
		startMonth= c.get(Calendar.MONTH);
		startDay= c.get(Calendar.DAY_OF_MONTH);
		
		String sDate = Utility.formatDate(startMonth,startDay,startYear);
        startDate.setText(sDate);
 
    }
    
    /*
     * This method will be invoked whenever the 'View Expense' button is clicked in the
     * OverviewActivity. This will start ExpenseActivity to show user's expense.
     */
	public void onClickView(View v) {
		
	    Intent intent = new Intent(this, ExpenseActivity.class);
	    intent.putExtra("start_date", startDate.getText().toString());
	    
	    String endDate = Utility.calEndDate(startDate.getText().toString(),rangeSpinner.getSelectedItem().toString());
	    intent.putExtra("end_date", endDate);
	    String account = accountSpinner.getSelectedItem().toString();
	    int start = account.indexOf("(")+1;
	    int end = account.indexOf(")");
	    String accountNum = account.substring(start,end);
	    intent.putExtra("account_number",accountNum);
	    intent.putExtra("chart_type",chartSpinner.getSelectedItem().toString());
	   
	    startActivity(intent);
		
		
	}
	
	/*
     * This method will be invoked whenever the 'Add Record' button is clicked in the
     * OverviewActivity. This will start RecordActivity to add user's spending record.
     */
	public void onClickAdd(View v) {
		Intent intent;	
		intent = new Intent(this, RecordActivity.class);
	    intent.putExtra("date", startDate.getText().toString()); 
	    String account = accountSpinner.getSelectedItem().toString();
	    intent.putExtra("account",account);
	    startActivity(intent);
	    
	}
	
	
	public static ArrayList<String> getAccountList(){
		return accountList;
	}
	
	/**
	 * GetAccountNumber defines the AsyncTask to post the username to the account.php, 
	 * which retrieve the account information from the database, and populate the accountSpinner 
	 * and start NfcActivity to process the NFC tag information if available.
	 *
	 */
	public class GetAccountNumber extends AsyncTask<String, Void, ArrayList<String>> {

		@Override
		//get account information from the database
		protected ArrayList<String> doInBackground(String... args) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("username",args[0]));
			return CustomHttpClient.getAccount(CustomHttpClient.getResult("http://thecity.sfsu.edu/~weiw/account.php", nameValuePairs));
			
		}
		//populate accountSpinner and start NfcActivity if a NFC tag is available
		protected void onPostExecute(ArrayList<String> list)
		{			
			// Create an ArrayAdapter using the string array and a default spinner layout
	        ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(OverviewActivity.this,
	        		android.R.layout.simple_spinner_item, list.toArray(new String[list.size()]));
	      
	        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        accountSpinner.setAdapter(accountAdapter);
	        accountList = list;
	        
	        if(ControlActivity.getTagStatus()){
	        	
		        Intent intent = new Intent(OverviewActivity.this, NfcActivity.class);
			    startActivity(intent);
			    
	        }
	        
		}	
    	
    
    }


}
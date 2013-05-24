package com.example.expensetracker;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * NfcActivity processes the information retrieved from the NFC tag
 * and insert the purchase record into the database
 *
 */
public class NfcActivity extends Activity implements OnTaskCompleted{
	private String date;
	private String type;
	private String description;
	private String price;
	private Spinner accountSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		
	    date = Utility.getDefaultStartDate();
	    TextView text = (TextView)findViewById(R.id.nfc_text);
	    
	    // get the type, description, price information from shared preference
	    SharedPreferences prefs = getSharedPreferences("tagStatus", MODE_PRIVATE); 
	    type = prefs.getString("type", null);
	    description=prefs.getString("description", null);
	    price=prefs.getString("price", null);
	    text.setText("Date: "+ date+"\n"
 						+"Category: "+type+"\n"
 						+"Description: "+description+"\n"
 						+"price: "+price+"\n"
 						+"adds to account ?");
	    ArrayList<String> accountList = OverviewActivity.getAccountList();
	    accountSpinner = (Spinner) findViewById(R.id.select_account);
	    ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(this,
	    		android.R.layout.simple_spinner_item, accountList.toArray(new String[accountList.size()]));
	    accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    accountSpinner.setAdapter(accountAdapter);
		
	}
	
	/*
     * This method will be invoked whenever the 'cancel' button is clicked
     * to terminate NfcActivity.
     */
	public void onCancel(View v){
		ControlActivity.setTagProcessed(true);
		finish();
		
	}
	/*
     * This method will be invoked whenever the 'submit' button is clicked
     * to submit the NFC information as the purchase record to the database
     */
	public void onSubmit(View v){
		//use InsertRecord AsyncTask to submit information to the database
		String[] param =  new String[5];
        param[0] = Utility.convertDate(date);
        param[1] = accountSpinner.getSelectedItem().toString();
        param[2] = type;
        param[3] = description;
        param[4] = price;
        
        new InsertRecord(this).execute(param);
        ControlActivity.setTagProcessed(true);
        finish();
		
	}
	
	/*
	 * This method will be called if the the InsertRecord AsyncTask finished
	 * inserting record into the database.Toast will be used to show 
	 * the account balance
	 */
	public void onTaskCompleted(){
		Toast.makeText(getApplicationContext(), accountSpinner.getSelectedItem().toString() + " balance: "+InsertRecord.getBalance(), Toast.LENGTH_LONG).show();
	}
	
}

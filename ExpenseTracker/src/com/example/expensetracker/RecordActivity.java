package com.example.expensetracker;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RecordActivity extends Activity {
	Spinner categorySpinner;
	EditText descriptionText;
	EditText amountText;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(categoryAdapter);
        
        descriptionText = (EditText)findViewById(R.id.description_text);
        amountText = (EditText)findViewById(R.id.amount_text);
	}
	
	public void onClickSubmit(View v){
		Bundle extras = getIntent().getExtras();
		final String date = extras.getString("date");
		final String category = categorySpinner.getSelectedItem().toString();
		final String description = descriptionText.getText().toString();
		final String amount = amountText.getText().toString();
		final String account = extras.getString("account");
		
		if(description.length()!= 0 && amount.length()!= 0){
			String message = "Date: "+ date+"\n"
			+"Category: "+category+"\n"
			+"Description: "+description+"\n"
			+"Amount: "+amount+"\n"
			+"adds to account "+account+"?";
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
			// set title
			alertDialogBuilder.setTitle("Confirm record");
 
			// set dialog message
			alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("submit",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						String[] param =  new String[5];
				        param[0] = ExpenseActivity.convertDate(date);
				        param[1] = account;
				        param[2] = category;
				        param[3] = description;
				        param[4] = amount;
				        //Toast.makeText(getApplicationContext(), accountNum, Toast.LENGTH_LONG).show(); 	
				        new InsertRecord().execute(param);
					    
					   
					}
				  })
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			}
		else
			Toast.makeText(getApplicationContext(), "please fill the description and amount", Toast.LENGTH_SHORT).show(); 	
	
	}
	private class InsertRecord extends AsyncTask<String, Void, String> {
    	//TextView text;
		String account;

		@Override
		protected String doInBackground(String... args) {
			//this.text = arg[0];
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("date",args[0]));
		    account = args[1];
		    int start = account.indexOf("(")+1;
			int end = account.indexOf(")");
			String accountNum = account.substring(start,end);
		    nameValuePairs.add(new BasicNameValuePair("account",accountNum));
		    nameValuePairs.add(new BasicNameValuePair("type",args[2]));
		    nameValuePairs.add(new BasicNameValuePair("description",args[3]));
		    nameValuePairs.add(new BasicNameValuePair("amount",args[4]));
		    //Toast.makeText(getApplicationContext(), args[1], Toast.LENGTH_LONG).show(); 	
			return CustomHttpClient.getBalance(CustomHttpClient.getResult("http://thecity.sfsu.edu/~weiw/insertRecord.php", nameValuePairs));
		}
		protected void onPostExecute(String balance)
		{
			 Toast.makeText(getApplicationContext(), account + " balance: "+balance, Toast.LENGTH_LONG).show(); 	
		}	
    	
    
    }

}

package com.example.expensetracker;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * RecordActivity takes the user manual input as the purchasing record
 * and insert the record into the database
 *
 */
public class RecordActivity extends Activity implements OnTaskCompleted{
	Spinner categorySpinner;
	EditText descriptionText;
	EditText amountText;
	private String account;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        
        descriptionText = (EditText)findViewById(R.id.description_text);
        amountText = (EditText)findViewById(R.id.amount_text);
	}
	
	/*
     * This method will be invoked when the 'submit' button is clicked.
     * A dialog will be popped to let user double check the purchase record
     * ant then the purchase record will be inserted into the database
     */
	public void onClickSubmit(View v){
		Bundle extras = getIntent().getExtras();
		final String date = extras.getString("date");
		final String category = categorySpinner.getSelectedItem().toString();
		final String description = descriptionText.getText().toString();
		final String amount = amountText.getText().toString();
		account = extras.getString("account");
		
		if(description.length()!= 0 && amount.length()!= 0){
			String message = "Date: "+ date+"\n"
			+"Category: "+category+"\n"
			+"Description: "+description+"\n"
			+"Amount: "+amount+"\n"
			+"adds to account "+account+"?";
			
			//use the dialog to display user's input
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
				        param[0] = Utility.convertDate(date);
				        param[1] = account;
				        param[2] = category;
				        param[3] = description;
				        param[4] = amount;
				        
				        new InsertRecord(RecordActivity.this).execute(param);
					    
					   
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
	
	/*
	 * This method will be called if the the InsertRecord AsyncTask finished
	 * inserting record into the database.Toast is used to show 
	 * the account balance
	 */
	public void onTaskCompleted(){
		finish();
		Toast.makeText(getApplicationContext(), account + " balance: "+InsertRecord.getBalance(), Toast.LENGTH_LONG).show();
	}
	

}

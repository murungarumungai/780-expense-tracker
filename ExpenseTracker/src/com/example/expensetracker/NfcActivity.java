package com.example.expensetracker;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NfcActivity extends Activity {
	private String[] content;
	private String date;
	//private String account;
	private Spinner accountSpinner;
	private TextView text;
	private String tag;
	private static boolean tagDetected,tagProcessed;
	private static boolean isNfcActivityOn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		
		
		
		//ArrayList<String> account = intent.getStringArrayListExtra("list");
	    //Toast.makeText(this, account.toString(), Toast.LENGTH_SHORT).show();
		
		
	    //date = extras.getString("date");
		tagProcessed = false;
		tagDetected = false;
		isNfcActivityOn = true;
	    date = SettingActivity.getDefaultStartDate();
	    text = (TextView)findViewById(R.id.nfc_text);
	    
		if(LoginActivity.getLoginStatus() && SettingActivity.getSettingStatus()){
			Bundle extras = getIntent().getExtras();
		    tag = extras.getString("tag");
		    
			if(tag != null){
				 content = tag.split(",");
                 text.setText("Date: "+ date+"\n"
 						+"Category: "+content[0]+"\n"
 						+"Description: "+content[1]+"\n"
 						+"Amount: "+content[2]+"\n"
 						+"adds to account ?");
			}
		
			ArrayList<String> account= SettingActivity.getAccountList();
			 accountSpinner = (Spinner) findViewById(R.id.select_account);
			 // Create an ArrayAdapter using the string array and a default spinner layout
		        ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(this,
		        		android.R.layout.simple_spinner_item, account.toArray(new String[account.size()]));
		        // Specify the layout to use when the list of choices appears
		        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        // Apply the adapter to the spinner
		        accountSpinner.setAdapter(accountAdapter);
		}       
		
		/*else{
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra("tag",tag);
			startActivity(intent);
		}*/
		
		
	    
	    
	   
	    
	   
	}
	
	@Override
    public void onResume() {
        super.onResume();
        handleIntent(getIntent());
    }
	
	 public void onNewIntent(Intent intent) {
	        setIntent(intent);
	    }

		    
	public void handleIntent(Intent intent) {
		 super.onNewIntent(intent);
		 
         if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {  
        	 tagDetected = true;
              NdefMessage message = null; 
              Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);  
              if (rawMsgs != null) 
            	  message = (NdefMessage) rawMsgs[0];   
              
              if(message != null) {
            	  tag="";
            	  byte[] payload = message.getRecords()[0].getPayload();  
                     
            	  for (int b = 1; b<payload.length; b++) { // skip SOH  
                        tag += (char) payload[b];  
                   }  
                   Toast.makeText(this, "NFC tag found", Toast.LENGTH_LONG).show(); 
                   //used = true;
                   content = tag.split(",");
                   text.setText("Date: "+ date+"\n"
   						+"Category: "+content[0]+"\n"
   						+"Description: "+content[1]+"\n"
   						+"Amount: "+content[2]+"\n"
   						+"adds to account ?");
                   
                   if((!LoginActivity.getLoginStatus() || !SettingActivity.getSettingStatus())){
                	   Intent intent1 = new Intent(this, LoginActivity.class);
                	   intent1.putExtra("tag",tag);
                	   startActivity(intent1);
                	   
                   }
                   
              }  
         }  
     }
	
	public static boolean getTagStatus(){
		return (tagDetected==true) && (tagProcessed ==false);
	}
	
	public static boolean getNfcActivityStatus(){
		return isNfcActivityOn;
	}
	public void onCancel(View v){
		finish();
		
	}
	
	public void onSubmit(View v){
		
		String[] param =  new String[5];
        param[0] = ExpenseActivity.convertDate(date);
        param[1] = accountSpinner.getSelectedItem().toString();
        param[2] = content[0];
        param[3] = content[1];
        param[4] = content[2];
        //Toast.makeText(getApplicationContext(), accountNum, Toast.LENGTH_LONG).show(); 	
        new InsertRecord().execute(param);
        finish();
		
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
			tagProcessed = true;
			 Toast.makeText(getApplicationContext(), account + " balance: "+balance, Toast.LENGTH_LONG).show();
			 //text.setText("Record added for "+ account+".\n" + "Current balance: "+balance);
		}	
    	
    
    }
	
}

package com.example.expensetracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * ControlActivity is invoked whenever a NFC tag of "application/tracker" type
 * has been discovered. ControlActivity reads the tag information and start
 * OverviewActivity or NfcActivity to process the tag.
 *
 */

public class ControlActivity extends Activity{

	private static boolean tagDetected,tagProcessed;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    tagProcessed = false;
		tagDetected = false;
	    
	}
	
	@Override
    public void onResume() {
        super.onResume();
        handleIntent(getIntent());
        Intent intent;
        //check the login status and how the application is started
        SharedPreferences prefs = getSharedPreferences("loginStatus", MODE_PRIVATE); 
		boolean isLoggedIn= prefs.getBoolean("isLoggedIn", false);
		boolean startByNfc= prefs.getBoolean("startByNfc", false);
		
		//if the user haven't logged in or the application is invoked by NFC tag
		//start OverviewActivity
        if ((!isLoggedIn)||startByNfc){
		       intent = new Intent(this, OverviewActivity.class);
		       SharedPreferences.Editor editor = getSharedPreferences("loginStatus", MODE_PRIVATE).edit(); 
		       editor.putBoolean("startByNfc", true);
		       editor.commit();
        }
        //if the user already manually logged in, only start NfcActivity
        else
        	intent = new Intent(this, NfcActivity.class);   
		      
	    startActivity(intent);
	    finish();
    }
	
	 public void onNewIntent(Intent intent) {
	        setIntent(intent);
	 }
	/*
	 * This method is invoked whenever an NFC tag of type "application/tracker" is discovered.
	 * It reads the type, description and price information from the tag and stores in the
	 * shared preference.
	 */
	public void handleIntent(Intent intent) {
		 super.onNewIntent(intent);
		 
         if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {  
        	 tagDetected = true;
              
        	 Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
              if (rawMsgs != null){
            	  NdefRecord relayRecord1 = ((NdefMessage)rawMsgs[0]).getRecords()[0];
            	  String type = new String(relayRecord1.getPayload());
            	  NdefRecord relayRecord2 = ((NdefMessage)rawMsgs[0]).getRecords()[1];
            	  String description= new String(relayRecord2.getPayload());
            	  NdefRecord relayRecord3 = ((NdefMessage)rawMsgs[0]).getRecords()[2];
            	  String price = new String(relayRecord3.getPayload());
            	  SharedPreferences.Editor editor = getSharedPreferences("tagStatus", MODE_PRIVATE).edit(); 
            	  editor.clear();
            	  editor.putString("type", type);
            	  editor.putString("description", description);
            	  editor.putString("price", price);
            	  editor.commit();
			       
              }
            	 
         }    
	}
	public static boolean getTagStatus(){
		return (tagDetected==true) && (tagProcessed ==false);
	}
	public static void setTagProcessed(boolean status){
		tagProcessed = status;
	}
	
}

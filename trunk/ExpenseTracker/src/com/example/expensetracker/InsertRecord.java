package com.example.expensetracker;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;

/**
 * InsertRecord defines the AsyncTask to post the information to insertRecord.php, 
 * which insert the corresponding information into the database
 */
public class InsertRecord extends AsyncTask<String, Void, String> {
	private OnTaskCompleted listener;
	private String account;
	private static String balance;
	
	public InsertRecord(OnTaskCompleted listener){
        this.listener=listener;
    }

	@Override
	protected String doInBackground(String... args) {
		
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
	    
		return CustomHttpClient.getBalance(CustomHttpClient.getResult("http://thecity.sfsu.edu/~weiw/insertRecord.php", nameValuePairs));
	}
	protected void onPostExecute(String result){
		balance = result;
		listener.onTaskCompleted(); 	
	}	
	
	public static String getBalance(){
		return balance;
	}
	

}

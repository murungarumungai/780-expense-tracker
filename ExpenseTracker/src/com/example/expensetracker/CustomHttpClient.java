package com.example.expensetracker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

/**
 * CustomHttpClient defines the connection between the android application
 * and the service side php code and parse the json format output
 * Adapted from code in
 * http://www.helloandroid.com/tutorials/connecting-mysql-database
 */
public class CustomHttpClient {
	
	/*
	 * getResult method posts the parameters from android application
	 * to server side php and return the resulting output as string
	 */
	public static String getResult(String url,ArrayList<NameValuePair> postParameters){
		InputStream is = null;
		String result= null;
		//post parameters to the specific url
		try{
			HttpClient httpclient = new DefaultHttpClient();  
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(postParameters)); 
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();       
	    
		}catch(Exception e){
			result = "No internet connection";
			return result;
	    } 
		//convert the resulting output to string
		 try{
			 BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			 StringBuilder sb = new StringBuilder();
			 String line = null;
			 while ((line = reader.readLine()) != null) {
				 sb.append(line + "\n");
				 }
			 is.close();
			 result=sb.toString();
	    }catch(Exception e){
	    	result = "Error converting result";
            return result;
	    }

		
		return result;
	}
	
	/*
	 * parseDetailResult method parse the result from detail.php
	 * and return the detailed spending records in string
	 */
	public static String parseDetailResult(String result){
		String returnString = "";
		
		try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    
                    //Get an output to the screen
                    returnString += json_data.getString("date") +"\t"+ json_data.getString("type") +"\t "+ json_data.getString("description") +"\t "+ json_data.getDouble("amount")+"\n";
            }
    }catch(JSONException e){
            
    }
    return returnString;
	
	}
	
	/*
	 * getBalance method parse the result from insertRecord.php
	 * and return the account balance in string
	 */
	public static String getBalance(String result){
		double balance=0;
		String type;

		try{
            JSONArray jArray = new JSONArray(result);
            JSONObject json_data = jArray.getJSONObject(0);
            type = json_data.getString("category");
            balance = json_data.getDouble("total");
            
            //the record table in the database is set up according to 
            //the credit card standards: expense is in positive number and earning
            //is in negative number.Therefore, for checking accounts and
            //saving accounts, need to negate the result
            if((type.equals("c") || type.equals("s"))){
            	balance = (-1)*balance;
            }
        }
		catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
			}
		return Double.toString(balance);
	
	}
	/*
	 * getAccount method parse the result from account.php
	 * and return the account numbers associated with a user
	 */
	public static ArrayList<String> getAccount(String result){
		
		ArrayList<String> accountList = new ArrayList<String>();
		
		if(result == null)
			return accountList;
		else if(result.equals("No internet connection") || result.equals("Error converting result") ){
			accountList.add(result);
			return accountList;
		}
		
		else{
			try{
				
				JSONArray jArray = new JSONArray(result);
				
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					String type= json_data.getString("type");
					String accountNum = json_data.getString("account_number");
					//convert account types
					if(type.equals("s"))
						type = "Saving account";
					else if(type.equals("c"))
						type = "Checking account";
					else if(type.equals("r"))
						type = "Credit card";
					else
						type = "Other";
					
					accountList.add(type+" ("+accountNum+")");
					}
	            }
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
				
				}
		}
		return accountList;
	
	}
	
}

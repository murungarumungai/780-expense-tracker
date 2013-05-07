package com.example.expensetracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class CustomHttpClient {
	
	
	public static String getResult(String url,ArrayList<NameValuePair> postParameters)  {
		InputStream is = null;
		String result= null;
		 try{
	            HttpClient httpclient = new DefaultHttpClient();  
	            HttpPost httppost = new HttpPost(url);
	            httppost.setEntity(new UrlEncodedFormEntity(postParameters)); 
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();       

	    }catch(Exception e){
	            Log.e("log_tag", "Error in http connection "+e.toString());
	    }
		 
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
	            Log.e("log_tag", "Error converting result "+e.toString());
	    }

		
		return result;
	}
	
	public static String parseResult(String result){
		String returnString = "";
		
		try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    
                    //Get an output to the screen
                    returnString += json_data.getString("date") +"\t"+ json_data.getString("type") +"\t "+ json_data.getString("description") +"\t "+ json_data.getDouble("amount")+"\n";
            }
    }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
    }
    return returnString;
	
	}
	
	public static String getBalance(String result){
		double balance=0;
		String type;

		try{
            JSONArray jArray = new JSONArray(result);
            JSONObject json_data = jArray.getJSONObject(0);
            type = json_data.getString("category");
            balance = json_data.getDouble("total");
            if((type.equals("c") || type.equals("s"))){
            	balance = (-1)*balance;
            }
        }
		catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
			}
		return Double.toString(balance);
	
	}
	
	public static ArrayList<String> getAccount(String result){
		
		ArrayList<String> accountList = new ArrayList<String>();
		
		try{
			
			JSONArray jArray = new JSONArray(result);
            
			for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				String type= json_data.getString("type");
				String accountNum = json_data.getString("account_number");
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
    
		return accountList;
	
	}
	
	public static HashMap<String,String> getCategoryAmount(String result){
		
		HashMap<String,String> category =  new HashMap<String,String>();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    category.put(json_data.getString("type"), json_data.getString("total"));
                    nameValuePairs.add(new BasicNameValuePair(json_data.getString("type"),json_data.getString("total")));
                    
            }
            
            
    }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
    }
    return category;
	
	}


}

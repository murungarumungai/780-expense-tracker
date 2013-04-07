package com.example.expensetracker;

import java.io.BufferedReader;
import java.io.IOException;
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
	
	public static ArrayList<String> getAccount(String result){
		
		ArrayList<String> accountList = new ArrayList<String>();
		
		try{
			
			JSONArray jArray = new JSONArray(result);
            
			for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				accountList.add(json_data.getString("account_number"));
				}
            }
		catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
			}
    
		return accountList;
	
	}
	
	public static String getCategoryAmount(String result){
		String returnString = "";
		
		try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    
                    //Get an output to the screen
                    returnString += "Total amount for "+ json_data.getString("type")+":\t" +json_data.getString("total") +"\n";
            }
    }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
    }
    return returnString;
	
	}


}

package com.example.expensetracker;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText username;
	private EditText password;
	private CheckBox cBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		cBox = (CheckBox) findViewById(R.id.rememberBox);
		
		SharedPreferences prefs = getPreferences(MODE_PRIVATE); 
		String uname= prefs.getString("username", null);
		if (uname != null) 
		{
		  int checked = prefs.getInt("checkBoxStatus", -1);
		  if (checked == 1 )
		  {
		     username.setText(uname);
		     cBox.setChecked(true);
		  }
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void onLogin(View v) {
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

		if(cBox.isChecked()){
			
			 editor.putString("username", username.getText().toString());
			 editor.putInt("checkBoxStatus", 1);
			 editor.commit();
		}
		else{
			editor.clear();
			editor.commit();
		}
		String[] param =  new String[2];
        param[0] = username.getText().toString();
        param[1] = password.getText().toString();
		new VerifyLogin().execute(param);
		
	}
	
	
	private class VerifyLogin extends AsyncTask<String, Void, String> {
    	//TextView text;

		@Override
		protected String doInBackground(String... args) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("username",args[0]));
		    nameValuePairs.add(new BasicNameValuePair("password",args[1]));
			return CustomHttpClient.getResult("http://thecity.sfsu.edu/~weiw/login.php", nameValuePairs);
		}
		protected void onPostExecute(String result)
		{
			if(result.length()==15){
				Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
				intent.putExtra("username", username.getText().toString());
			    startActivity(intent);
				
			}
			else{
				Toast.makeText(getApplicationContext(), "wrong username/password", Toast.LENGTH_SHORT).show();
			}
				
	    	  	
		}	
    	
    
    }

}

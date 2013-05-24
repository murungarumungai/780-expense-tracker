package com.example.expensetracker;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * LoginActivity takes user's input for username and password, verify in the database.
 * If the username and password is correct, LoginActivity returns back to OverviewActivity.
 */
public class LoginActivity extends Activity implements OnTaskCompleted{
	private static EditText username;
	private EditText password;
	private CheckBox cBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		cBox = (CheckBox) findViewById(R.id.rememberBox);
		
		//show the username if the 'remember username' is checked
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

	 /*
     * This method will be invoked whenever the 'Login' button is clicked in the
     * LoginActivity. This method uses VerifyLogin AsyncTask to verify username
     * and password in the database
     */
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
		new VerifyLogin(this).execute(param);
		
	}
	
	/*
	 * This method will be called if the the VerifyLogin AsyncTask verifies that the username
	 * and password are correct. This method also stores the login status and username in the 
	 * shared preference and returns back to the OverviewActivity. 
	 */
	public void onTaskCompleted(){
		SharedPreferences.Editor editor = getSharedPreferences("loginStatus", MODE_PRIVATE).edit(); 
		editor.putBoolean("isLoggedIn", true);
		editor.putString("username", username.getText().toString());
		editor.commit();
		Intent returnIntent = new Intent();
		returnIntent.putExtra("username",username.getText().toString());
		setResult(RESULT_OK,returnIntent);    
		finish();
	}
	
	//when press the back button, finish the LoginActivity
	public void onBackPressed(){
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED,returnIntent);    
		finish();
	}
	/**
	 * VerifyLogin defines the AsyncTask to post the username and password to login.php,
	 * which verifies the username and password in the database
	 */
	public class VerifyLogin extends AsyncTask<String, Void, String > {
		private OnTaskCompleted listener;
		public VerifyLogin(OnTaskCompleted listener){
	        this.listener=listener;
	    }


		@Override
		//verify username and password in the database
		protected String  doInBackground(String... args) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("username",args[0]));
		    nameValuePairs.add(new BasicNameValuePair("password",args[1]));
			return CustomHttpClient.getResult("http://thecity.sfsu.edu/~weiw/login.php", nameValuePairs);
		}
		
		protected void onPostExecute(String result){
			if(result.substring(0,1).equals("0")){
				Toast.makeText(getApplicationContext(), "wrong username/password", Toast.LENGTH_SHORT).show();
			}
			
			else if(result.equals("No internet connection") || result.equals("Error converting result") ){
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
			}
			else
				listener.onTaskCompleted();
			
		}	
    	
    
    }

}

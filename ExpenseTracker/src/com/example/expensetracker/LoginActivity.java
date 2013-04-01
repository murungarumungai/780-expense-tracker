package com.example.expensetracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button b = (Button) findViewById(R.id.main_signup);
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(LoginActivity.this,SignupActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void onLogin(View v) {
	    //resultField.setText("");
		
	    Intent intent = new Intent(this, SettingActivity.class);
	    startActivity(intent);
		
		
	}

}

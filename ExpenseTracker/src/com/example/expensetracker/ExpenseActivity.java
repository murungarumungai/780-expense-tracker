package com.example.expensetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class ExpenseActivity extends Activity{
	String startDate;
	String endDate;
	String accountNumber;
	String chartType;
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Bundle extras = getIntent().getExtras();
	        startDate = extras.getString("start_date");
	        endDate = extras.getString("end_date");
	        accountNumber = extras.getString("account_number");
	        chartType = extras.getString("chart_type");

	        final ActionBar bar = getActionBar();
	        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        

	        bar.addTab(bar.newTab()
	                .setText(R.string.categories)
	                .setTabListener(new TabListener(new CategoryFragment())));
	        
	        bar.addTab(bar.newTab()
	                .setText(R.string.details)
	                .setTabListener(new TabListener(new DetailFragment())));
	        
	        /*String text3 = "Mobile Wallet";
	        bar.addTab(bar.newTab()
	                .setText(R.string.mobileWallet)
	                .setTabListener(new TabListener(new MobileWalletFragment(text3))));*/
	       
	        setContentView(R.layout.activity_expense);
	 }
	 
	 public String getStartDate(){
		 return convertDate(startDate);
	 }
	 
	 public String getEndDate(){
		 return convertDate(endDate);
	 }
	 public String getAccountNumber(){
		 return accountNumber;
	 }
	 
	 public String getChartType(){
		 return chartType;
	 }
	 public String convertDate(String mDate){
		 return mDate.substring(6)+ "-"+mDate.substring(0,2)+"-"+ mDate.substring(3,5);
	 }

}

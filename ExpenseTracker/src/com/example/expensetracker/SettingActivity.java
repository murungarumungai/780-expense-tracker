package com.example.expensetracker;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * This demonstrates the use of action bar tabs and how they interact
 * with other action bar features.
 */
public class SettingActivity extends Activity {
	protected static final int DATE_DIALOG_ID = 0;
	EditText startDate;
	EditText endDate;
	static final int DATE_DIALOG_ID1 = 1;
	private int startYear;
	private int startMonth;
	private int startDay;

	private int endYear;
	private int endMonth;
	private int endDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_setting);
        Spinner spinner = (Spinner) findViewById(R.id.chart_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.charts_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        
        
final RadioButton monthButton = (RadioButton) findViewById(R.id.month);
        
        
        if(monthButton.isChecked()){
        	 setStartDate((EditText) findViewById(R.id.showStartDate), 1);
            setEndDate((EditText) findViewById(R.id.showEndDate), 1);
        }
        
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);        
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) 
            {
                
            	if(checkedId == R.id.month){
            		setStartDate((EditText) findViewById(R.id.showStartDate), 1);
                    setEndDate((EditText) findViewById(R.id.showEndDate), 1);
            	}
            	
            	else if (checkedId == R.id.week){
            		setStartDate((EditText) findViewById(R.id.showStartDate), 2);
                    setEndDate((EditText) findViewById(R.id.showEndDate), 2);
            	}
            	else{
            		TextView text = (TextView) findViewById(R.id.showStartDate);
                    text.setText("");
                    TextView text2 = (TextView) findViewById(R.id.showEndDate);
                    text2.setText("");
            		
            	}
            		
            }
        });
        final Calendar c = Calendar.getInstance();
        startYear = c.get(Calendar.YEAR);
        endYear = startYear;
        startMonth = c.get(Calendar.MONTH);
        endMonth = startMonth;
        startDay = c.get(Calendar.DAY_OF_MONTH);
        endDay = startDay;
        startDate = (EditText) findViewById(R.id.showStartDate);
        
        startDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        
        
        endDate = (EditText) findViewById(R.id.showEndDate);
        
        endDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID1);
            }
        });

    }
    
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this, mDateSetListener, startYear, startMonth,
                    startDay);

        case DATE_DIALOG_ID1:
            return new DatePickerDialog(this, mDateSetListener1, endYear, endMonth,
                    endDay);
        }
        return null;
    }

    // updates the date in the TextView

    private void updateDisplay(EditText text,int month, int day, int year) {
    	month++;
    	String formattedMonth;
    	String formattedDay;
    	if(month < 10)
    		formattedMonth = "0"+ Integer.toString(month);
    	else
    		formattedMonth = Integer.toString(month);
    	
    	if(day < 10)
    		formattedDay = "0"+ Integer.toString(day);
    	else
    		formattedDay = Integer.toString(day);
    	
    	text.setText(new StringBuilder()
        // Month is 0 based so add 1
                .append(formattedMonth).append("-").append(formattedDay).append("-").append(year));
    }
    
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            // TODO Auto-generated method stub
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
            updateDisplay(startDate,startMonth,startDay,startYear);
        }
    };

    private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year1, int monthOfYear1,
                int dayOfMonth1) {
            // TODO Auto-generated method stub
            endYear = year1;
            endMonth = monthOfYear1;
            endDay = dayOfMonth1;
            updateDisplay(endDate,endMonth,endDay,endYear);
        }
    };   
public void setStartDate(EditText text, int mode){
    	
		Calendar c= Calendar.getInstance();
		int year;
		int month;
		int day;
		
		if(mode ==1){
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = 1;
			
		}
			
		else{
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			year = c.get(Calendar.YEAR);
			day = c.get(Calendar.DAY_OF_MONTH);
			
		}
 
		updateDisplay(text,month,day,year);
 
    	
    }
    
public void setEndDate(EditText text, int mode){
 
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day;
		
		if(mode ==1){
			switch(month){
			case 0:case 2:case 4:case 6:case 7:case 9:case 11:
				day = 31;
				break;
			case 1:
				if(year % 4 == 0)
					day = 29;
				else
					day = 28;
				break;	
			default:
				day = 30;
				break;
				}
		}
		else{
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			year = c.get(Calendar.YEAR);
			day = c.get(Calendar.DAY_OF_MONTH);
			
		}
			
		updateDisplay(text,month,day,year);
 
    	
    }

public void onClick(View v) {
    //resultField.setText("");
	
    Intent intent = new Intent(this, ExpenseActivity.class);
    intent.putExtra("start_date", startDate.getText().toString());
    intent.putExtra("end_date", endDate.getText().toString());
    /**
     * Start YellowActivity and wait for the result.
     */
    startActivity(intent);
	
	
}

}
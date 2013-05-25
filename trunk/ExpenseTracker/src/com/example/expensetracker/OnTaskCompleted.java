package com.example.expensetracker;

/**
 * OnTaskCompleted is a simple interface that LoginActivity,RecordActivity 
 * and NfcActivity use to indicate the completion of the AsyncTask.
 */
public interface OnTaskCompleted {
	void onTaskCompleted();

}

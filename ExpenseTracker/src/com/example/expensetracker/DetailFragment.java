package com.example.expensetracker;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * CategoryFragment shows the user's expense records within
 * the specified date range 
 *
 */
public class DetailFragment extends Fragment {
    private TextView text;
    private String startDate;
    private String endDate;
    private String account;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View fragView = inflater.inflate(R.layout.fragment_detail, container,false);

        text = (TextView) fragView.findViewById(R.id.detail_result);
        
        startDate = ((ExpenseActivity) this.getActivity()).getStartDate();
        endDate = ((ExpenseActivity) this.getActivity()).getEndDate();
        account = ((ExpenseActivity) this.getActivity()).getAccountNumber();
        //use GetDetail AsyncTask to display user's expense records
        new GetDetail().execute();

        return fragView;
    }
    
    /**
	 * GetDetail defines the AsyncTask to post the date range and account number
	 * to detail.php, which retrieves user's expense records into the database,
	 * and show the result in the TextView
	 */
    private class GetDetail extends AsyncTask<String, Void, String> {
    	
		@Override
		protected String doInBackground(String... args) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("start",startDate));
		    nameValuePairs.add(new BasicNameValuePair("end",endDate));
		    nameValuePairs.add(new BasicNameValuePair("account",account));
			return CustomHttpClient.parseDetailResult(CustomHttpClient.getResult("http://thecity.sfsu.edu/~weiw/detail.php", nameValuePairs));
		}
		protected void onPostExecute(String page)
		{    	
			if(page.equals (""))
				text.setText("No expense record found from "+startDate+" to "+endDate);
			else
				text.setText(page);
		}	
    	
    
    }

}


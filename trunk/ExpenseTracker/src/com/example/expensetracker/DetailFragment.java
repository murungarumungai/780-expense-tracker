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

public class DetailFragment extends Fragment {
    private TextView text;
    
    public DetailFragment() {
        
    }

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_detail, container, false);

        text = (TextView) fragView.findViewById(R.id.detail_result);
        String[] param =  new String[2];
        param[0] = ((ExpenseActivity) this.getActivity()).getStartDate();
        param[1] = ((ExpenseActivity) this.getActivity()).getEndDate();
        new GetDetail().execute(param);

        return fragView;
    }
    
    
    private class GetDetail extends AsyncTask<String[], Void, String> {
    	//TextView text;

		@Override
		protected String doInBackground(String[]... args) {
			//this.text = arg[0];
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("start",args[0][0]));
		    nameValuePairs.add(new BasicNameValuePair("end",args[0][1]));
			return CustomHttpClient.parseResult(CustomHttpClient.getResult("http://thecity.sfsu.edu/~weiw/detail.php", nameValuePairs));
		}
		protected void onPostExecute(String page)
		{    	
	    	  text.setText(page);    	
		}	
    	
    
    }

}


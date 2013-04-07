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

public class CategoryFragment extends Fragment {
	private TextView text;
    
    public CategoryFragment() {
        
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_category, container, false);

        text = (TextView) fragView.findViewById(R.id.category_result);
        String[] param =  new String[3];
        param[0] = ((ExpenseActivity) this.getActivity()).getStartDate();
        param[1] = ((ExpenseActivity) this.getActivity()).getEndDate();
        param[2] = ((ExpenseActivity) this.getActivity()).getAccountNumber();
        new GetCategory().execute(param);

        return fragView;
    }
    
    
    private class GetCategory extends AsyncTask<String, Void, String> {
    	//TextView text;

		@Override
		protected String doInBackground(String... args) {
			//this.text = arg[0];
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("start",args[0]));
		    nameValuePairs.add(new BasicNameValuePair("end",args[1]));
		    nameValuePairs.add(new BasicNameValuePair("account",args[2]));
			return CustomHttpClient.getCategoryAmount(CustomHttpClient.getResult("http://thecity.sfsu.edu/~weiw/category.php", nameValuePairs));
		}
		protected void onPostExecute(String page)
		{    	
	    	  text.setText(page);    	
		}	
    	
    
    }

}


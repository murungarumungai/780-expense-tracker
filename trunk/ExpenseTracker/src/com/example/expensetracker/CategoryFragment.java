package com.example.expensetracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;



import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryFragment extends Fragment {
	private WebView wv;
	private String type;
    
    public CategoryFragment() {
        
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_category, container, false);

        //text = (TextView) fragView.findViewById(R.id.category_result);
        wv = (WebView) fragView.findViewById(R.id.chartView);
        String[] param =  new String[3];
        param[0] = ((ExpenseActivity) this.getActivity()).getStartDate();
        param[1] = ((ExpenseActivity) this.getActivity()).getEndDate();
        param[2] = ((ExpenseActivity) this.getActivity()).getAccountNumber();
        type = ((ExpenseActivity) this.getActivity()).getChartType();
        new GetCategory().execute(param);

        return fragView;
    }
    
    
    private class GetCategory extends AsyncTask<String, Void, HashMap<String,String>> {
    	//TextView text;

		@Override
		protected HashMap<String,String> doInBackground(String... args) {
			
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("start",args[0]));
		    nameValuePairs.add(new BasicNameValuePair("end",args[1]));
		    nameValuePairs.add(new BasicNameValuePair("account",args[2]));
			return CustomHttpClient.getCategoryAmount(CustomHttpClient.getResult("http://thecity.sfsu.edu/~weiw/category.php", nameValuePairs));
			
		}
		protected void onPostExecute(HashMap<String,String> result)
		{    	
	    	
			wv.getSettings().setJavaScriptEnabled(true);
			String url = "http://thecity.sfsu.edu/~weiw/chart.php?";
			String[ ] category = {"Grocery","Housing", "Clothes", "Transportation", "Entertainment","Health Care","Other" };
			
			for (String s: category){
				if(result.get(s)!= null)
					url+= s+"="+result.get(s).toString();
				else
					url+= s+"=0";
				url+="&";
				
			}
			url += "mode="+type;
			url = url.replaceAll("\\s","");
			
			//Toast.makeText(getActivity().getApplicationContext(), url, Toast.LENGTH_LONG).show();
			wv.loadUrl(url);
		}	
    	
    
    }

}


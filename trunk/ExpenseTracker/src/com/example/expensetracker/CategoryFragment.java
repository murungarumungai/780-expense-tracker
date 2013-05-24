package com.example.expensetracker;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * CategoryFragment shows user's expense in 7 categories by charts
 *
 */
public class CategoryFragment extends Fragment {
	private WebView wv;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View fragView = inflater.inflate(R.layout.fragment_category, container,false);

        wv = (WebView) fragView.findViewById(R.id.chartView);
        String[] param =  new String[4];
        param[0] = ((ExpenseActivity) this.getActivity()).getStartDate();
        param[1] = ((ExpenseActivity) this.getActivity()).getEndDate();
        param[2] = ((ExpenseActivity) this.getActivity()).getAccountNumber();
        param[3] = ((ExpenseActivity) this.getActivity()).getChartType();
        //use GetCategory AsyncTask to display chart to show the spending in categories
        new GetCategory().execute(param);

        return fragView;
    }
    
    /**
	 * GetCategory defines the AsyncTask to connect to categoryCart.php,which shows
	 * the spending in 7 categories using google chart tool.
	 */
    private class GetCategory extends AsyncTask<String, Void, String> {
    	
		@Override
		protected String doInBackground(String... args) {
			wv.getSettings().setJavaScriptEnabled(true);
			String url = "http://thecity.sfsu.edu/~weiw/categoryChart.php?start="+args[0]+"&end="+args[1]+"&account="+args[2]+"&mode="+args[3];
			url = url.replaceAll("\\s","");
			wv.loadUrl(url);
			return url;
		}
		
    	
    
    }

}


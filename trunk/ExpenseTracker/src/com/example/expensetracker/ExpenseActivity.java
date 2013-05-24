package com.example.expensetracker;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * ExpenseActivity populates the action bar and with 2 tabs
 * managed by 2 fragments: CategoryFragment and DetailFragment.
 * CategoryFragment shows user's expense in 7 categories by charts
 * DetailFragment shows all the user's expense records that satisfies
 * the search criteria 
 */
public class ExpenseActivity extends Activity{
	
	private String startDate;
	private String endDate;
	private String accountNumber;
	private String chartType;
	
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        final ActionBar bar = getActionBar();
	       
	        Bundle extras = getIntent().getExtras();
	        startDate = extras.getString("start_date");
	        endDate = extras.getString("end_date");
	        accountNumber = extras.getString("account_number");
	        chartType = extras.getString("chart_type");
	        
	        
	        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        
	       
	        bar.addTab(bar.newTab()
	                .setText(R.string.categories)
	                .setTabListener(new TabListener<CategoryFragment>(this, "category", CategoryFragment.class)));
	        
	        bar.addTab(bar.newTab()
	                .setText(R.string.details)
	                .setTabListener(new TabListener<DetailFragment>(this, "detail", DetailFragment.class)));
	        
	        
	       
	        setContentView(R.layout.activity_expense);
	 }
	 

	
	 public String getStartDate(){
		 return Utility.convertDate(startDate);
	 }
	 
	 public String getEndDate(){
		 return Utility.convertDate(endDate);
	 }
	 public String getAccountNumber(){
		 return accountNumber;
	 }
	 
	 public String getChartType(){
		 return chartType;
	 }
	 /*
	  * TabListener class defines the action of two tabs within the action bar.
	  * This class is adapted from the tutorial code in
	  * http://stackoverflow.com/questions/15817127/android-googlemap-api-v2-and-tabs
	  */
	 public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
		    private final Activity mActivity;
		    private final String mTag;
		    private final Class<T> mClass;
		    private final Bundle mArgs;
		    private Fragment mFragment;

		    public TabListener(Activity activity, String tag, Class<T> clz) {
		        this(activity, tag, clz, null);
		    }

		    public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
		        mActivity = activity;
		        mTag = tag;
		        mClass = clz;
		        mArgs = args;

		        // Check to see if we already have a fragment for this tab, probably
		        // from a previously saved state.  If so, deactivate it, because our
		        // initial state is that a tab isn't shown.
		        mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
		        if (mFragment != null && !mFragment.isDetached()) {
		            FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
		            ft.detach(mFragment);
		            ft.commit();
		        }
		    }

		    public void onTabSelected(Tab tab, FragmentTransaction ft) {
		        if (mFragment == null) {
		            mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
		            ft.add(android.R.id.content, mFragment, mTag);
		        } else {
		            ft.attach(mFragment);
		        }
		    }

		    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		        if (mFragment != null) {
		            ft.detach(mFragment);
		        }
		    }

		    public void onTabReselected(Tab tab, FragmentTransaction ft) {
		    }
		}


}

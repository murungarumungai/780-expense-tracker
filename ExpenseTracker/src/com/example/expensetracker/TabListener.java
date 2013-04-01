package com.example.expensetracker;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;


public class TabListener implements ActionBar.TabListener {
    private Fragment mFragment;

    public TabListener(Fragment fragment) {
        mFragment = fragment;
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    	ft.add(R.id.fragment_content, mFragment, mFragment.getTag());
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        ft.remove(mFragment);
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        
    }

}
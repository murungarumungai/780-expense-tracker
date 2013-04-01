package com.example.expensetracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CategoryFragment extends Fragment {
    private String mText;
    
    public CategoryFragment() {
        mText = null;
    }
    
    public CategoryFragment(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_category, container, false);

        TextView text = (TextView) fragView.findViewById(R.id.category_result);
        text.setText(((ExpenseActivity) this.getActivity()).getStartDate() + "~"+((ExpenseActivity) this.getActivity()).getEndDate());

        return fragView;
    }

}


package com.example.expensetracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MobileWalletFragment extends Fragment {
    private String mText;
    
    public MobileWalletFragment() {
        mText = null;
    }

    public MobileWalletFragment(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_mobile_wallet, container, false);

        TextView text = (TextView) fragView.findViewById(R.id.text3);
        text.setText(mText);

        return fragView;
    }

}


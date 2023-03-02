package com.example.costcalculator30;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AppPage
{
    private final View mPage;
    //private final Help mHelp;

    AppPage(LayoutInflater inflater, ViewGroup container)
    {
        mPage = inflater.inflate(R.layout.page_template, container, false);


    }

    public View getPage()
    {
        return mPage;
    }
}

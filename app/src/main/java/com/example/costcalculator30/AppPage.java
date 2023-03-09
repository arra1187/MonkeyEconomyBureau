package com.example.costcalculator30;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AppPage
{
    private final View mOverView;
    private final View mPage;
    private final View mCustomView;

    AppPage(LayoutInflater inflater, ViewGroup container, int customViewLayout, String pageHeader)
    {
        mOverView = inflater.inflate(R.layout.invisible_page_template, container, false);
        mPage = inflater.inflate(R.layout.page_template, container, false);
        mCustomView = inflater.inflate(customViewLayout, container, false);

        ((FrameLayout) mOverView.findViewById(R.id.page_frame)).addView(mPage);
        ((FrameLayout) mPage.findViewById(R.id.page_frame)).addView(mCustomView);
        ((TextView) mPage.findViewById(R.id.page_header)).setText(pageHeader);
}

    public View getOverView()
    {
        return mOverView;
    }

    public View getPage()
    {
        return mPage;
    }

    public View getCustomView()
    {
        return mCustomView;
    }
}

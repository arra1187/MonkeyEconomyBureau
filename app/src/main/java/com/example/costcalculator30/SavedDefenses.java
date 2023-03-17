package com.example.costcalculator30;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SavedDefenses extends Fragment
{
    private DatabaseRepository mRepository;
    private Executor mExecutor;
    private AppPage mAppPage;
    private RecyclerView mDefenseRecycler;
    private DefenseRecyclerViewAdapter mDefenseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final String pageHeader = "Saved\nDefenses";
        final int fragmentLayout = R.layout.fragment_saved_defenses;

        mRepository = new DatabaseRepository();
        mExecutor = Executors.newSingleThreadExecutor();

        mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

        mDefenseRecycler = mAppPage.getCustomView().findViewById(R.id.defense_recyclerView);
        mDefenseRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mDefenseAdapter = new DefenseRecyclerViewAdapter(getContext());
        mDefenseRecycler.setAdapter(mDefenseAdapter);

        return mAppPage.getOverView();
    }
}
package com.example.costcalculator30;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SavedDefenses extends Fragment
{
    private DefenseViewModel mViewModel;
    private Executor mExecutor;
    private AppPage mAppPage;
    private RecyclerView mDefenseRecycler;
    private DefenseRecyclerViewAdapter mDefenseAdapter;
    private ArrayList<Defense> mDefenses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final String pageHeader = "Saved\nDefenses";
        final int fragmentLayout = R.layout.fragment_saved_defenses;

        mExecutor = Executors.newSingleThreadExecutor();

        mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

        mDefenses = new ArrayList<>();

        mDefenseRecycler = mAppPage.getCustomView().findViewById(R.id.defense_recyclerView);
        mDefenseRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewModel = new ViewModelProvider(this).get(DefenseViewModel.class);

        mDefenseAdapter = new DefenseRecyclerViewAdapter(mDefenses, getContext());
        mDefenseRecycler.setAdapter(mDefenseAdapter);

        //bind to Livedata
        mViewModel.getAllData().observe(getViewLifecycleOwner(), new Observer<List<Defense>>()
        {
            @Override
            public void onChanged(@Nullable List<Defense> defenses)
            {
                if(defenses != null)
                {
                    setListData(defenses);
                }
            }
        });

        //setListData(mViewModel.getAllData().getValue());

        /*mExecutor.execute(() ->
        {
            ArrayList<Defense> defenses = (ArrayList<Defense>) mRepository.getDefenseDao(getContext()).getAll();

            mDefenses.addAll(defenses);

            mAppPage.getCustomView().post (() ->
                    mDefenseAdapter.notifyItemRangeInserted(0, mDefenses.size())
            );
        });*/

        //updateDefense();

        mExecutor.execute(() ->
        {
            int numDefenses;

            numDefenses = mViewModel.getSize();

            mAppPage.getCustomView().post(() ->
                    mDefenseAdapter.notifyItemRangeInserted(0, numDefenses));
        });

        return mAppPage.getOverView();
    }

    public void setListData(List<Defense> defenses)
    {
        //if data changed, set new list to adapter of recyclerview

        if (mDefenses == null)
        {
            mDefenses = new ArrayList<>();
        }

        mDefenses.clear();
        mDefenses.addAll(defenses);

        if (mDefenseAdapter != null)
        {
            mDefenseAdapter.setListData(mDefenses);
            //mDefenseAdapter.notifyItemRangeInserted(0, mDefenses.size());
        }
    }

    public void updateDefense()
    {
        mExecutor.execute(() ->
        {
            /*ArrayList<Defense> defenses = (ArrayList<Defense>) mRepository.getDefenseDao(getContext()).getAll();

            mDefenses.addAll(defenses);

            mAppPage.getCustomView().post (() ->
                    mDefenseAdapter.notifyItemRangeInserted(0, mDefenses.size())
            );*/
        });
    }

    public void onResume()
    {
        super.onResume();


    }
}
package com.example.costcalculator30;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavedDefenses extends Fragment
{
    private DefenseViewModel mDefenseViewModel;
    private ExecutorService mDatabaseExecutor;
    private Executor mExecutor;
    private AppPage mAppPage;
    private RecyclerView mDefenseRecycler;
    private DefenseRecyclerViewAdapter mDefenseAdapter;
    private ArrayList<Defense> mDefenses;
    private Defense mCurrentDefense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final String pageHeader = "Saved\nDefenses";
        final int fragmentLayout = R.layout.fragment_saved_defenses;
        final LifecycleOwner lifecycleOwner = getViewLifecycleOwner();

        mExecutor = Executors.newSingleThreadExecutor();

        mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

        mDefenses = new ArrayList<>();

        mDefenseRecycler = mAppPage.getCustomView().findViewById(R.id.defense_recyclerView);
        mDefenseRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mDefenseRecycler.setItemAnimator(null);

        mDefenseViewModel = new ViewModelProvider(this).get(DefenseViewModel.class);

        setupCurrentDisplay(inflater, container);

        mDefenseAdapter = new DefenseRecyclerViewAdapter(mDefenseViewModel, mDefenses, getContext(), lifecycleOwner);
        mDefenseRecycler.setAdapter(mDefenseAdapter);

        mDefenseViewModel.getAllLiveData().observe(lifecycleOwner, new Observer<List<Defense>>()
        {
            @Override
            public void onChanged(@Nullable List<Defense> defenses)
            {
                if(defenses != null)
                {
                    mDefenseAdapter.notifyDataSetChanged();
                }
            }
        });

        mExecutor.execute(() ->
        {
            int numDefenses;

            numDefenses = mDefenseViewModel.getSize();

            mAppPage.getCustomView().post(() ->
                    mDefenseAdapter.notifyItemRangeInserted(0, numDefenses));
        });

        return mAppPage.getOverView();
    }

    private void setupCurrentDisplay(LayoutInflater inflater, ViewGroup container)
    {
        Utility utility = new Utility();
        View currentView = inflater.inflate(R.layout.defense_display, container, false);
        String costText;

        ((FrameLayout) mAppPage.getCustomView().findViewById(R.id.page_frame)).addView(currentView);

        mDatabaseExecutor = Executors.newSingleThreadExecutor();

        mDatabaseExecutor.execute (() ->
        {
            mCurrentDefense = mDefenseViewModel.getCurrent().get(0);
        });

        utility.joinExecutor(mDatabaseExecutor);

        costText = "$" + mCurrentDefense.getCost();

        ((TextView) currentView.findViewById(R.id.defense_id)).setText("Current");
        ((TextView) currentView.findViewById(R.id.defense_cost)).setText(costText);
    }
}
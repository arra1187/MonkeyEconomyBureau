package com.example.costcalculator30;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    private Defense mCurrentDefense;
    private boolean mTowerListShowing;
    private TextView mTowerListView;
    private StringBuilder mTowerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final String pageHeader = "Saved\nDefenses";
        final int fragmentLayout = R.layout.fragment_saved_defenses;
        final LifecycleOwner lifecycleOwner = getViewLifecycleOwner();

        mExecutor = Executors.newSingleThreadExecutor();

        mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

        mDefenseRecycler = mAppPage.getCustomView().findViewById(R.id.defense_recyclerView);
        mDefenseRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mDefenseRecycler.setItemAnimator(null);

        mDefenseViewModel = new ViewModelProvider(this).get(DefenseViewModel.class);

        setupCurrentDisplay(inflater, container);

        mDefenseAdapter = new DefenseRecyclerViewAdapter(mDefenseViewModel, getContext(), lifecycleOwner);
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
        Button dropDownButton;

        ((FrameLayout) mAppPage.getCustomView().findViewById(R.id.current_frame)).addView(currentView);

        mDatabaseExecutor = Executors.newSingleThreadExecutor();

        mDatabaseExecutor.execute (() ->
        {
            mCurrentDefense = mDefenseViewModel.getCurrent().get(0);
        });

        utility.joinExecutor(mDatabaseExecutor);

        costText = "$" + mCurrentDefense.getCost();

        ((TextView) currentView.findViewById(R.id.defense_id)).setText("Current");
        ((TextView) currentView.findViewById(R.id.defense_cost)).setText(costText);

        mTowerListShowing = false;
        mTowerListView = (TextView) currentView.findViewById(R.id.tower_list);

        mTowerList = utility.setTowerList(mCurrentDefense.getTowers());

        dropDownButton = currentView.findViewById(R.id.drop_down_button);

        dropDownButton.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.asset_triangle_down, null));

        dropDownButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!mTowerListShowing)
                {
                    mTowerListView.setText(mTowerList.toString());
                    view.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.asset_triangle_up, null));
                    mTowerListShowing = true;
                }
                else
                {
                    mTowerListView.setText("");
                    view.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.asset_triangle_down, null));
                    mTowerListShowing = false;
                }
            }
        });

        currentView.findViewById(R.id.clear_defense_button).setVisibility(View.INVISIBLE);
    }
}
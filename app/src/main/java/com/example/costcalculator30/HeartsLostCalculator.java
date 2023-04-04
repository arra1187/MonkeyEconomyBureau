package com.example.costcalculator30;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HeartsLostCalculator extends Fragment
{
  private Executor mExecutor;

  private BloonRepository mBloonRepository;

  private ArrayList<BloonItem> mBloons;

  private BloonRecyclerViewAdapter mAdapter;

  private AppPage mAppPage;

  private RecyclerView mRecyclerView;
  private Button mAddBloonButton;
  private Button mClearBloonsButton;
  private Spinner mBloonDropdown;

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final String pageHeader = "Hearts Lost\nCalculator";
    final int fragmentLayout = R.layout.fragment_hearts_lost_calculator;

    ArrayList<BloonItem> bloons = new ArrayList<>();

    mExecutor = Executors.newSingleThreadExecutor();

    mBloonRepository = new BloonRepository(getActivity().getApplication());
    
    mBloons = new ArrayList<>();

    mAdapter = new BloonRecyclerViewAdapter(mBloons, getContext());

    mAdapter.getLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<BloonItem>>()
    {
      @Override
      public void onChanged(ArrayList<BloonItem> bloonItems)
      {
        if(bloonItems != null)
        {
          //Calculate hearts lost / RBE

          mAdapter.notifyItemRangeInserted(0, bloonItems.size());
        }
      }
    });

    mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

    initializeViews();

    return mAppPage.getOverView();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
  {
    mAddBloonButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        String title = mBloonDropdown.getSelectedItem().toString();

        if(title.equals("Select Bloon"))
        {
          Toast towerToast = Toast.makeText(getActivity(), "Select a bloon", Toast.LENGTH_LONG);
          towerToast.show();
          return;
        }

        BloonItem bloonItem = new BloonItem
        (
          title,
          getActivity().getApplication()
        );

        mBloons.add(bloonItem);

        mAdapter.setBloons(mBloons);
      }
    });
  }

  public void initializeViews()
  {
    mRecyclerView = mAppPage.getCustomView().findViewById(R.id.bloon_recyclerView);
    mAddBloonButton = mAppPage.getCustomView().findViewById(R.id.add_bloon_button);
    mClearBloonsButton = mAppPage.getCustomView().findViewById(R.id.clear_bloons_button);

    mBloonDropdown = mAppPage.getCustomView().findViewById(R.id.bloon_dropdown);
    ArrayAdapter<CharSequence> towerAdapter = ArrayAdapter.createFromResource(getActivity(),
            R.array.RBE_bloons, android.R.layout.simple_spinner_item);
    towerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
    mBloonDropdown.setAdapter(towerAdapter);
  }
}
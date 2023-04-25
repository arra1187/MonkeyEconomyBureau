package com.example.costcalculator30;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HeartsLostCalculator extends Fragment
{
  private Executor mExecutor;

  private BloonRepository mBloonRepository;
  private RoundRepository mRoundRepository;

  private ArrayList<BloonItem> mBloons;

  private MutableLiveData<Boolean> mbListener;

  private BloonRecyclerViewAdapter mAdapter;

  private AppPage mAppPage;

  private boolean mbConsiderRounds;
  private boolean mbAlternateBloonsRounds;

  private RecyclerView mRecyclerView;
  private Button mAddBloonButton;
  private Button mClearBloonsButton;
  private Spinner mBloonDropdown;
  private TextView mHeartsLost;
  private EditText mStartRound;
  private EditText mEndRound;
  private Switch mConsiderationSwitch;
  private Switch mRoundTypeSwitch;

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final String pageHeader = "Hearts Lost\nCalculator";
    final int fragmentLayout = R.layout.fragment_hearts_lost_calculator;

    mbListener = new MutableLiveData<>();

    mbListener.setValue(false);

    mExecutor = Executors.newSingleThreadExecutor();

    mBloonRepository = new BloonRepository(getActivity().getApplication());
    mRoundRepository = new RoundRepository(getActivity().getApplication());
    
    mBloons = new ArrayList<>();

    mAdapter = new BloonRecyclerViewAdapter(mBloons, getContext(), mbListener, getViewLifecycleOwner());

    mbConsiderRounds = true;

    mbListener.observe(getViewLifecycleOwner(), new Observer<Boolean>()
    {
      @Override
      public void onChanged(Boolean bBoolean)
      {
        mAdapter.notifyDataSetChanged();

        calculateHeartsLostBloons();
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

        mAdapter.notifyItemInserted(mBloons.size());
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

        if(!mbConsiderRounds)
        {
          calculateHeartsLostBloons();
        }
      }
    });

    mClearBloonsButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        if(mBloons.size() == 0)
        {
          Toast towerToast = Toast.makeText(getActivity(), "There are no bloons to clear", Toast.LENGTH_LONG);
          towerToast.show();
          return;
        }

        int size = mBloons.size();

        mBloons.clear();

        mAdapter.notifyItemRangeRemoved(0, size);

        if(!mbConsiderRounds)
        {
          calculateHeartsLostBloons();
        }
      }
    });

    mConsiderationSwitch.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        mbConsiderRounds = mConsiderationSwitch.isChecked();

        chooseConsideration();
      }
    });

    mRoundTypeSwitch.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        mbAlternateBloonsRounds = mRoundTypeSwitch.isChecked();

        chooseConsideration();
      }
    });

    mStartRound.setOnEditorActionListener(new TextView.OnEditorActionListener()
    {
      @Override
      public boolean onEditorAction (TextView textView, int i,
                                     KeyEvent keyEvent)
      {
        chooseConsideration();

        return false;
      }
    });

    mEndRound.setOnEditorActionListener(new TextView.OnEditorActionListener()
    {
      @Override
      public boolean onEditorAction (TextView textView, int i,
                                     KeyEvent keyEvent)
      {
        chooseConsideration();

        return false;
      }
    });
  }

  public void initializeViews()
  {
    mRecyclerView = mAppPage.getCustomView().findViewById(R.id.bloon_recyclerView);
    mAddBloonButton = mAppPage.getCustomView().findViewById(R.id.add_bloon_button);
    mClearBloonsButton = mAppPage.getCustomView().findViewById(R.id.clear_bloons_button);
    mHeartsLost = mAppPage.getCustomView().findViewById(R.id.hearts_lost);
    mStartRound = mAppPage.getCustomView().findViewById(R.id.start_round_entry);
    mEndRound = mAppPage.getCustomView().findViewById(R.id.end_round_entry);
    mConsiderationSwitch = mAppPage.getCustomView().findViewById(R.id.consideration_switch);
    mRoundTypeSwitch = mAppPage.getCustomView().findViewById(R.id.round_type_switch);

    mBloonDropdown = mAppPage.getCustomView().findViewById(R.id.bloon_dropdown);
    ArrayAdapter<CharSequence> towerAdapter = ArrayAdapter.createFromResource(getActivity(),
            R.array.RBE_bloons, android.R.layout.simple_spinner_item);
    towerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
    mBloonDropdown.setAdapter(towerAdapter);

    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.setAdapter(mAdapter);
  }

  private void chooseConsideration()
  {
    if(mbConsiderRounds)
    {
      calculateHeartsLostRounds();
    }
    else
    {
      calculateHeartsLostBloons();
    }
  }

  private void calculateHeartsLostBloons()
  {
    mExecutor.execute(() ->
    {
      int heartsLost = 0;
      final int SUPER_ROUND = 80;
      final Integer finalHeartsLost;
      String superText = "", inputText, title, temp;

      temp = mStartRound.getText().toString();

      if(!temp.equals(""))
      {
        if(Integer.parseInt(mStartRound.getText().toString()) > SUPER_ROUND)
        {
          superText = "Super ";
        }
      }

      for(BloonItem bloonItem : mBloons)
      {
        title = bloonItem.getTitle();

        if(Objects.equals(bloonItem.getType(), "Blimp") || Objects.equals(title, "Ceramic Bloon"))
        {
          inputText = superText + title;
        }
        else
        {
          inputText = title;
        }

        heartsLost += bloonItem.getNumBloons() * mBloonRepository.getHeartsLost(inputText, bloonItem.getFortified());
      }

      finalHeartsLost = heartsLost;

      mAppPage.getCustomView().post (() -> mHeartsLost.setText(finalHeartsLost.toString()));
    });
  }

  private void calculateHeartsLostRounds()
  {
    String startRoundText, endRoundText;

    startRoundText = mStartRound.getText().toString();
    endRoundText = mEndRound.getText().toString();

    if(startRoundText.equals(""))
    {
      Toast towerToast = Toast.makeText(getActivity(), "Enter a starting round first", Toast.LENGTH_LONG);
      towerToast.show();

      return;
    }

    if(!endRoundText.equals(""))
    {
      if(Integer.parseInt(startRoundText) > Integer.parseInt(endRoundText))
      {
        Toast towerToast = Toast.makeText(getActivity(), "The end round larger than the starting round", Toast.LENGTH_LONG);
        towerToast.show();

        mHeartsLost.setText("0");

        return;
      }
    }

    mExecutor.execute(() ->
    {
      int heartsLost = 0, startRound, endRound;
      String type, temp;
      final Integer finalHeartsLost;

      startRound = Integer.parseInt(mStartRound.getText().toString());

      temp = mEndRound.getText().toString();

      if(!temp.equals(""))
      {
        endRound = Integer.parseInt(temp);
      }
      else
      {
        endRound = startRound;
      }

      if(mbAlternateBloonsRounds)
      {
        type = "abr";
      }
      else
      {
        type = "normal";
      }

      for(int round = startRound; round <= endRound; round++)
      {
        heartsLost += mRoundRepository.getHeartsLost(round, type);
      }

      finalHeartsLost = heartsLost;

      mAppPage.getCustomView().post (() -> mHeartsLost.setText(finalHeartsLost.toString()));
    });
  }
}
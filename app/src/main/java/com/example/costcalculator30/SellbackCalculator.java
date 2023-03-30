package com.example.costcalculator30;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SellbackCalculator extends Fragment
{
  private final double SELLBACK_RATE = 0.7;
  private final String DOLLAR_SIGN = "$";

  private final int DEACTIVATE_CODE = -1;
  private final int NEUTRAL_CODE = 0;
  private final int ACTIVATE_CODE = 1;

  private Executor mExecutor;
  private AppPage mAppPage;

  private UpgradeRepository mUpgradeRepository;
  private DefenseRepository mDefenseRepository;

  private TextView mDefenseSellbackView;

  private Spinner mTowerDropdown;
  private Spinner mTopPath;
  private Spinner mMiddlePath;
  private Spinner mBottomPath;

  private Button mEnterStartingCash;
  private EditText mStartingCashView;

  private Button mSellButton;
  private Button mRebuyButton;

  private TextView mRemainingCashView;

  private int mTowerCost;
  private int mRemainingCash;
  private boolean mBuy;

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final String pageHeader = "Sellback\nCalculator";
    final int fragmentLayout = R.layout.fragment_saved_defenses;

    mExecutor = Executors.newSingleThreadExecutor();

    mUpgradeRepository = new UpgradeRepository(getActivity().getApplication());
    mDefenseRepository = new DefenseRepository(getActivity().getApplication());

    mExecutor = Executors.newSingleThreadExecutor();
    mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

    mBuy = true;

    initializeViews();

    setupPage();

    return mAppPage.getOverView();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
  {
    mEnterStartingCash.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        String startingCash = mStartingCashView.getText().toString(), output;

        if(startingCash.equals("0") || startingCash.equals(""))
        {
          Toast towerToast = Toast.makeText(getActivity(), "Enter your starting cash", Toast.LENGTH_LONG);
          towerToast.show();
          return;
        }

        mRemainingCash = Integer.parseInt(startingCash);

        output = DOLLAR_SIGN + startingCash;

        mRemainingCashView.setText(output);

        toggleButtons(ACTIVATE_CODE, NEUTRAL_CODE);

        setTowerCost();

        mRebuyButton.setText(R.string.rebuy_button);
      }
    });

    mSellButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        if(!mSellButton.isActivated())
        {
          Toast towerToast = Toast.makeText(getActivity(), "Tower has not yet been purchased", Toast.LENGTH_LONG);
          towerToast.show();
          return;
        }

        String sellbackText;

        mRemainingCash += mTowerCost + SELLBACK_RATE;

        sellbackText = DOLLAR_SIGN + mTowerCost;

        mRemainingCashView.setText(sellbackText);

        toggleButtons(DEACTIVATE_CODE, ACTIVATE_CODE);
      }
    });

    mRebuyButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        if(!mRebuyButton.isActivated())
        {
          Toast towerToast = Toast.makeText(getActivity(), "Tower has already been purchased.", Toast.LENGTH_LONG);
          towerToast.show();
          return;
        }

        String sellbackText;

        mRemainingCash += mTowerCost + SELLBACK_RATE;

        sellbackText = DOLLAR_SIGN + mTowerCost;

        mRemainingCashView.setText(sellbackText);

        toggleButtons(ACTIVATE_CODE, DEACTIVATE_CODE);

        if(mBuy)
        {
          mRebuyButton.setText(R.string.rebuy_button);
        }
      }
    });
  }

  private void initializeViews()
  {
    mDefenseSellbackView = mAppPage.getCustomView().findViewById(R.id.defense_sellback_price);

    mTowerDropdown = mAppPage.getCustomView().findViewById(R.id.tower_spinner);
    ArrayAdapter<CharSequence> towerAdapter = ArrayAdapter.createFromResource(getActivity(),
        R.array.towers, android.R.layout.simple_spinner_item);
    towerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
    mTowerDropdown.setAdapter(towerAdapter);

    ArrayAdapter<CharSequence> upgradeAdapter = ArrayAdapter.createFromResource(getActivity(),
        R.array.upgrades, android.R.layout.simple_spinner_item);
    upgradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

    mTopPath = mAppPage.getCustomView().findViewById(R.id.top_path);
    mTopPath.setAdapter(upgradeAdapter);

    mMiddlePath = mAppPage.getCustomView().findViewById(R.id.middle_path);
    mMiddlePath.setAdapter(upgradeAdapter);

    mBottomPath = mAppPage.getCustomView().findViewById(R.id.bottom_path);
    mBottomPath.setAdapter(upgradeAdapter);

    mEnterStartingCash = mAppPage.getCustomView().findViewById(R.id.enter_starting_cash_button);
    mStartingCashView = mAppPage.getCustomView().findViewById(R.id.starting_cash);

    mSellButton = mAppPage.getCustomView().findViewById(R.id.sell_button);;
    mRebuyButton = mAppPage.getCustomView().findViewById(R.id.rebuy_button);

    mRemainingCashView = mAppPage.getCustomView().findViewById(R.id.remaining_cash);;
  }

  private void setupPage()
  {
    mExecutor.execute(() ->
    {
      final int CURRENT_DEFENESE_ID = 1;

      String sellbackText;
      int defenseCost = mDefenseRepository.getCost(CURRENT_DEFENESE_ID);

      sellbackText = DOLLAR_SIGN + (defenseCost * SELLBACK_RATE);

      mDefenseSellbackView.setText(sellbackText);
    });

    toggleButtons(DEACTIVATE_CODE, DEACTIVATE_CODE);
  }

  private void toggleButtons(int sellCode, int rebuyCode)
  {
    if(sellCode == DEACTIVATE_CODE)
    {
      mSellButton.setActivated(false);
      mSellButton.setBackground(ResourcesCompat.getDrawable
      (
          getContext ().getResources(),
          R.drawable.gray_rectangle_button_template, null
      ));
    }
    else if(sellCode == ACTIVATE_CODE)
    {
      mSellButton.setActivated(true);
      mSellButton.setBackground(ResourcesCompat.getDrawable
      (
          getContext ().getResources(),
          R.drawable.red_rectangle_button_template, null
      ));
    }

    if(rebuyCode == DEACTIVATE_CODE)
    {
      mRebuyButton.setActivated(false);
      mRebuyButton.setBackground(ResourcesCompat.getDrawable
      (
          getContext ().getResources(),
          R.drawable.gray_rectangle_button_template, null
      ));
    }
    else if(rebuyCode == ACTIVATE_CODE)
    {
      mRebuyButton.setActivated(true);
      mRebuyButton.setBackground(ResourcesCompat.getDrawable
      (
          getContext ().getResources(),
          R.drawable.green_rectangle_button_template, null
      ));
    }
  }

  private void setTowerCost()
  {
    mExecutor.execute(() ->
    {
      Tower tower = new Tower
          (
              mTowerDropdown.getSelectedItem().toString(),
              Integer.parseInt(mTopPath.getSelectedItem().toString()),
              Integer.parseInt(mMiddlePath.getSelectedItem().toString()),
              Integer.parseInt(mBottomPath.getSelectedItem().toString()),
              mUpgradeRepository
          );

      mTowerCost = tower.getTowerCost();
    });
  }
}
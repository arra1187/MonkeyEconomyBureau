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

  private Button mHelpButton;
  private Button mEnterStartingCash;
  private EditText mStartingCashView;

  private Button mSellButton;
  private Button mRebuyButton;

  private TextView mRemainingCashView;
  private TextView mSellCountView;
  private TextView mRebuyCountView;

  private Tower mTower;
  private int mTowerCost;
  private int mRemainingCash;
  private boolean mbBuy;

  private int mNumSells;
  private int mNumRebuys;

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final String pageHeader = "Sellback\nCalculator";
    final int fragmentLayout = R.layout.fragment_sellback_calculator;

    mExecutor = Executors.newSingleThreadExecutor();

    mUpgradeRepository = new UpgradeRepository(getActivity().getApplication());
    mDefenseRepository = new DefenseRepository(getActivity().getApplication());

    mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

    mbBuy = true;
    mNumSells = 0;
    mNumRebuys = 0;

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

        if(startingCash.equals("0")
            || startingCash.equals("")
            || mTowerDropdown.getSelectedItem().equals("Select Tower"))
        {
          Toast towerToast = Toast.makeText(getActivity(), "Enter your sellback data (tower, upgrade(s), and starting cash)", Toast.LENGTH_LONG);
          towerToast.show();
          return;
        }

        mRemainingCash = Integer.parseInt(startingCash);

        output = DOLLAR_SIGN + startingCash;

        mRemainingCashView.setText(output);

        toggleButtons(DEACTIVATE_CODE, ACTIVATE_CODE);

        setTowerCost();

        mbBuy = true;
        mRebuyButton.setText(R.string.buy_button);

        mNumSells = 0;
        mNumRebuys = 0;

        mSellCountView.setText("Sells: 0");
        mRebuyCountView.setText("Rebuys: 0");
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

        String sellbackText, sellCountText;

        if(mTowerCost == 0)
        {
          mTowerCost = mTower.getTowerCost();
        }

        mRemainingCash += mTowerCost * SELLBACK_RATE;

        sellbackText = DOLLAR_SIGN + mRemainingCash;

        mRemainingCashView.setText(sellbackText);

        toggleButtons(DEACTIVATE_CODE, ACTIVATE_CODE);

        mNumSells++;

        sellCountText = "Sells: " + mNumSells;

        mSellCountView.setText(sellCountText);
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

        if(mbBuy)
        {
          mRebuyButton.setText(R.string.rebuy_button);
          mbBuy = false;
        }

        String rebuyText, rebuyCountText;

        if(mTowerCost == 0)
        {
          mTowerCost = mTower.getTowerCost();
        }

        mRemainingCash -= mTowerCost;

        if(mRemainingCash < 0)
        {
          mRemainingCash += mTowerCost;

          Toast towerToast = Toast.makeText(getActivity(), "You don't have enough money to buy this tower", Toast.LENGTH_LONG);
          towerToast.show();

          return;
        }

        rebuyText = DOLLAR_SIGN + mRemainingCash;

        mRemainingCashView.setText(rebuyText);

        toggleButtons(ACTIVATE_CODE, DEACTIVATE_CODE);

        if(mbBuy)
        {
          mRebuyButton.setText(R.string.rebuy_button);
        }

        mNumRebuys++;

        rebuyCountText = "Rebuys: " + mNumRebuys;

        mRebuyCountView.setText(rebuyCountText);
      }
    });

    mHelpButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        Toast towerToast = Toast.makeText(getActivity(), "Tower Cost: " + mTowerCost, Toast.LENGTH_LONG);
        towerToast.show();
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
    mSellCountView = mAppPage.getCustomView().findViewById(R.id.sell_count_display);
    mRebuyCountView = mAppPage.getCustomView().findViewById(R.id.rebuy_count_display);
    mHelpButton = mAppPage.getPage().findViewById(R.id.help_button);

    mSellButton = mAppPage.getCustomView().findViewById(R.id.sell_button);;
    mRebuyButton = mAppPage.getCustomView().findViewById(R.id.rebuy_button);

    mRemainingCashView = mAppPage.getCustomView().findViewById(R.id.hearts_lost);;
  }

  private void setupPage()
  {
    mExecutor.execute(() ->
    {
      String sellbackText;
      int defenseCost = mDefenseRepository.getCurrentCost();

      sellbackText = DOLLAR_SIGN + (int) (defenseCost * SELLBACK_RATE);

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
          getContext().getResources(),
          R.drawable.gray_rectangle_button_template, null
      ));
    }
    else if(sellCode == ACTIVATE_CODE)
    {
      mSellButton.setActivated(true);
      mSellButton.setBackground(ResourcesCompat.getDrawable
      (
          getContext().getResources(),
          R.drawable.red_rectangle_button_template, null
      ));
    }

    if(rebuyCode == DEACTIVATE_CODE)
    {
      mRebuyButton.setActivated(false);
      mRebuyButton.setBackground(ResourcesCompat.getDrawable
      (
          getContext().getResources(),
          R.drawable.gray_rectangle_button_template, null
      ));
    }
    else if(rebuyCode == ACTIVATE_CODE)
    {
      mRebuyButton.setActivated(true);
      mRebuyButton.setBackground(ResourcesCompat.getDrawable
      (
          getContext().getResources(),
          R.drawable.green_rectangle_button_template, null
      ));
    }
  }

  private void setTowerCost()
  {
    mTower = new Tower
    (
        mTowerDropdown.getSelectedItem().toString(),
        Integer.parseInt(mTopPath.getSelectedItem().toString()),
        Integer.parseInt(mMiddlePath.getSelectedItem().toString()),
        Integer.parseInt(mBottomPath.getSelectedItem().toString()),
        mUpgradeRepository
    );

    mTowerCost = mTower.getTowerCost();
  }
}
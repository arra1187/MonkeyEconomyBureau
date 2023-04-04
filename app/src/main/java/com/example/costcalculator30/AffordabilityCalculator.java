package com.example.costcalculator30;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.costcalculator30.databinding.FragmentAffordabilityCalculatorBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AffordabilityCalculator extends Fragment
{
    private FragmentAffordabilityCalculatorBinding binding;
    private AppPage mAppPage;

    private EditText mStartRoundEntry;
    private EditText mEndRoundEntry;
    private EditText mMyCash;

    private Spinner mCashMultiplierDropdown;
    private Spinner mRoundTypeDropdown;

    private TextView mMoneyDisplay;
    private TextView mRoundDisplay;
    private TextView mDefenseCostView;

    private DatabaseRepository mRepository;
    private RoundDao mRoundDao;
    //private DefenseDao mDefenseDao;

    private RoundRepository mRoundRepository;
    private DefenseViewModel mDefenseViewModel;

    private String mDefenseCost;

    private final int END_OF_ROUND_CASH_CONSTANT = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final String pageHeader = "Affordability\nCalculator";
        final int fragmentLayout = R.layout.fragment_affordability_calculator;

        mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

        mDefenseViewModel = new ViewModelProvider(this).get(DefenseViewModel.class);

        mStartRoundEntry = mAppPage.getCustomView().findViewById(R.id.start_round_entry);
        mEndRoundEntry = mAppPage.getCustomView().findViewById(R.id.end_round_entry);
        mMyCash = mAppPage.getCustomView().findViewById(R.id.starting_cash);

        mMoneyDisplay = mAppPage.getCustomView().findViewById(R.id.money_display);
        mRoundDisplay = mAppPage.getCustomView().findViewById(R.id.round_display);
        mDefenseCostView = mAppPage.getCustomView().findViewById(R.id.hearts_lost);

        mCashMultiplierDropdown = mAppPage.getCustomView().findViewById(R.id.cash_multiplier_spinner);
        ArrayAdapter<CharSequence> cashMultiplierAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.cash_multipliers, android.R.layout.simple_spinner_item);
        cashMultiplierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mCashMultiplierDropdown.setAdapter(cashMultiplierAdapter);

        mRoundTypeDropdown = mAppPage.getCustomView().findViewById(R.id.rounds_type_spinner);
        ArrayAdapter<CharSequence> roundTypeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.round_types, android.R.layout.simple_spinner_item);
        roundTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mRoundTypeDropdown.setAdapter(roundTypeAdapter);

        mCashMultiplierDropdown.setSelection(1);

        //mRepository = new DatabaseRepository();
        //mRoundDao = mRepository.getRoundDao(getContext());
        //mDefenseDao = mRepository.getDefenseDao(getContext());

        mRoundRepository = new RoundRepository(getActivity().getApplication());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
        {
            //String finalPriceDisplay = "$" + mDefenseViewModel.getCost(1);
            mDefenseCost = "$" + mDefenseViewModel.getCost(1);

            mAppPage.getCustomView().post (() -> mDefenseCostView.setText(mDefenseCost));
        });

        return mAppPage.getOverView();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.enter_for_money_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mStartRoundEntry.getText().toString().equals("") || mEndRoundEntry.getText().toString().equals(""))
                {
                    Toast towerToast = Toast.makeText(getActivity(), "Enter a range of rounds first", Toast.LENGTH_LONG);
                    towerToast.show();

                    return;
                }

                ExecutorService mExecutor= Executors.newSingleThreadExecutor();
                mExecutor.execute(() ->
                {
                    int money = 0;
                    String roundType = mRoundTypeDropdown.getSelectedItem().toString();
                    String output;

                    for(int i = Integer.parseInt(mStartRoundEntry.getText().toString());
                        i < Integer.parseInt(mEndRoundEntry.getText().toString()) + 1; i++)
                    {
                        money += mRoundRepository.getRoundCash(i, roundType);
                        money += END_OF_ROUND_CASH_CONSTANT + i;
                    }

                    switch(mCashMultiplierDropdown.getSelectedItem().toString())
                    {
                        case "Half Cash":
                            money = money / 2;
                            break;
                        case "Double Cash":
                            money = money * 2;
                            break;
                    }

                    output = "$" + money;

                    mMoneyDisplay.setText(output);
                });
            }
        });

        view.findViewById(R.id.enter_for_round_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mStartRoundEntry.getText().toString().equals(""))
                {
                    Toast towerToast = Toast.makeText(getActivity(), "Enter a starting round first", Toast.LENGTH_LONG);
                    towerToast.show();

                    return;
                }

                ExecutorService mExecutor= Executors.newSingleThreadExecutor();
                mExecutor.execute(() ->
                {
                    int round = Integer.parseInt(mStartRoundEntry.getText().toString());
                    double defenseCost = Double.parseDouble(mDefenseCost), multiplier = 1;
                    String roundType = mRoundTypeDropdown.getSelectedItem().toString();
                    String output = null;

                    defenseCost -= Double.parseDouble(mMyCash.getText().toString());

                    switch(mCashMultiplierDropdown.getSelectedItem().toString())
                    {
                        case "Half Cash":
                            multiplier = 0.5;
                            break;
                        case "Double Cash":
                            multiplier = 2;
                            break;
                    }

                    while(defenseCost > 0)
                    {
                        defenseCost -= mRoundRepository.getRoundCash(round, roundType) * multiplier;

                        if(defenseCost <= 0)
                        {
                            output = "Middle of Round " + round;
                        }
                        else
                        {
                            defenseCost -= (END_OF_ROUND_CASH_CONSTANT + round) * multiplier;

                            if(defenseCost <= 0)
                            {
                                output = "End of Round " + round;
                            }
                            else
                            {
                                round++;
                            }

                        }
                    }

                    mRoundDisplay.setText(output);
                });
            }
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}
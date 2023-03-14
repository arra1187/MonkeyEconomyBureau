package com.example.costcalculator30;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.costcalculator30.databinding.FragmentAffordabilityCalculatorBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AffordabilityCalculator extends Fragment
{

    private FragmentAffordabilityCalculatorBinding binding;
    private AppPage mAppPage;

    private EditText mStartRoundEntry;
    private EditText mEndRoundEntry;

    private Spinner mCashMultiplierDropdown;
    private Spinner mRoundTypeDropdown;

    private TextView mMoneyDisplay;

    private RoundDao mRoundDao;
    private DefenseDao mDefenseDao;

    private final int END_OF_ROUND_CASH_CONSTANT = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final String pageHeader = "Affordability\nCalculator";
        final String initialCost = "$0";
        final int fragmentLayout = R.layout.fragment_affordability_calculator;

        mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

        mStartRoundEntry = mAppPage.getCustomView().findViewById(R.id.start_round_entry);
        mEndRoundEntry = mAppPage.getCustomView().findViewById(R.id.end_round_entry);

        mMoneyDisplay = mAppPage.getCustomView().findViewById(R.id.money_display);

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

        ExecutorService mExecutor= Executors.newSingleThreadExecutor();
        mExecutor.execute(() ->
        {
            RoundDatabase roundDatabase = Room.databaseBuilder(getContext(),
                    RoundDatabase.class, "Round-db").build();
            DefenseDatabase defenseDatabase = Room.databaseBuilder(getContext(),
                    DefenseDatabase.class, "Defense-db").build();

            mRoundDao = roundDatabase.mRoundDao();
            mDefenseDao = defenseDatabase.mDefenseDao();
        });

        //binding = FragmentAffordabilityCalculatorBinding.inflate(inflater, container,
        //  false);
        //return binding.getRoot();

        String finalPriceDisplay = "$" + mDefenseDao.getCost(0);

        mMoneyDisplay.setText(finalPriceDisplay);

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
                ExecutorService mExecutor= Executors.newSingleThreadExecutor();
                mExecutor.execute(() ->
                {
                    Integer money = 0;

                    for(int i = Integer.parseInt(mStartRoundEntry.getText().toString());
                        i < Integer.parseInt(mEndRoundEntry.getText().toString()); i++)
                    {
                        money += mRoundDao.getCash(i);
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

                    mMoneyDisplay.setText(money.toString());
                });
            }
        });


        /*binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AffordabilityCalculator.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });*/

        /*view.findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(AffordabilityCalculator.this).navigate(AffordabilityCalculatorDirections.moveToCC());
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
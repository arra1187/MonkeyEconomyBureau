package com.example.costcalculator30;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private TextView mMoneyDisplay;

    private RoundDao mRoundDao;

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

        ExecutorService mExecutor= Executors.newSingleThreadExecutor();
        mExecutor.execute(() ->
        {
            RoundDatabase roundDatabase = Room.databaseBuilder(getContext(),
                    RoundDatabase.class, "Round-db").build();

            mRoundDao = roundDatabase.mRoundDao();
        });

        //binding = FragmentAffordabilityCalculatorBinding.inflate(inflater, container,
        //  false);
        //return binding.getRoot();

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
                int money = 0;

                for(int i = Integer.parseInt(mStartRoundEntry.getText().toString());
                    i < Integer.parseInt(mEndRoundEntry.getText().toString()); i++)
                {
                    money += mRoundDao.getCash(i);
                }

                mMoneyDisplay.setText(money);
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
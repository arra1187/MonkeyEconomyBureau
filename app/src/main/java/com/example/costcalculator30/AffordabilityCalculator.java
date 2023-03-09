package com.example.costcalculator30;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.costcalculator30.databinding.FragmentAffordabilityCalculatorBinding;

public class AffordabilityCalculator extends Fragment {

    private FragmentAffordabilityCalculatorBinding binding;
    private AppPage mAppPage;

    private EditText mMoneyCount;
    private EditText mCurrentRound;
    private Spinner mTargetTower;
    private Spinner mTopPath;
    private Spinner mMiddlePath;
    private Spinner mBottomPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final String pageHeader = "Affordability\nCalculator";
        final String initialCost = "$0";
        final int fragmentLayout = R.layout.fragment_affordability_calculator;

        mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

        //binding = FragmentAffordabilityCalculatorBinding.inflate(inflater, container,
        //  false);
        //return binding.getRoot();

        return mAppPage.getOverView();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mTargetTower = view.findViewById(R.id.target_tower_dropdown);
        mTopPath = view.findViewById(R.id.top_path);
        mMiddlePath = view.findViewById(R.id.middle_path);
        mBottomPath = view.findViewById(R.id.bottom_path);

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
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
        binding = FragmentAffordabilityCalculatorBinding.inflate(inflater, container,
          false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mMoneyCount = view.findViewById(R.id.enter_current_round);
        mCurrentRound = view.findViewById(R.id.enter_money_count);
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

        view.findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(AffordabilityCalculator.this).navigate(AffordabilityCalculatorDirections.moveToCC());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
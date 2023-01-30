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

import com.example.costcalculator30.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

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
        binding = FragmentSecondBinding.inflate(inflater, container,
          false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mMoneyCount = view.findViewById(R.id.enter_current_round);
        mCurrentRound = view.findViewById(R.id.enter_money_count);
        mTargetTower = view.findViewById(R.id.target_tower_dropdown);
        mTopPath = view.findViewById(R.id.target_tower_top_path);
        mMiddlePath = view.findViewById(R.id.target_tower_middle_path);
        mBottomPath = view.findViewById(R.id.target_tower_bottom_path);

        /*binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
package com.example.costcalculator30;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.costcalculator30.databinding.FragmentCostCalculatorBinding;

import java.util.ArrayList;

public class CostCalculator extends Fragment
{

    private FragmentCostCalculatorBinding binding;
    private RecyclerView mTowerRecycler;
    private RecyclerView.Adapter mTowerTypeAdapter;
    private ArrayList<String> mTowers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentCostCalculatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        /*binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View background_view) {
                NavHostFragment.findNavController(CostCalculator.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/

        mTowerRecycler = view.findViewById(R.id.tower_recyclerView);
        mTowerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mTowers = new ArrayList<>();
        mTowers.add("Select Tower");

        mTowerTypeAdapter = new TowerTypeRecyclerViewAdapter(mTowers, getContext());

        Spinner difficulty_dropDown = view.findViewById(R.id.difficulty_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.difficulties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        difficulty_dropDown.setAdapter(adapter);

        view.findViewById(R.id.tower_tab_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
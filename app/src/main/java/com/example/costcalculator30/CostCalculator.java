package com.example.costcalculator30;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.costcalculator30.databinding.FragmentCostCalculatorBinding;

import java.util.ArrayList;
import java.util.Objects;

public class CostCalculator extends Fragment
{
    //private FragmentCostCalculatorBinding binding;
    //private RecyclerView.Adapter mTowerTypeAdapter;
    private TowerRecyclerViewAdapter mTowerTypeAdapter;
    private UpgradeDao mUpgradeDao;
    private DatabaseViewModel viewModel;

    private RecyclerView mTowerRecycler;
    private TextView mFinalPrice;

    private Spinner mTowerDropdown;
    private Spinner mDifficultyDropdown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        //binding = FragmentCostCalculatorBinding.inflate(inflater, container, false);

        View view = inflater.inflate(R.layout.fragment_cost_calculator, container, false);

        //return fragmentFirstLayout;

        viewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        mFinalPrice = view.findViewById(R.id.final_price);
        mFinalPrice.setText("$0");

        mTowerRecycler = view.findViewById(R.id.tower_recyclerView);
        mTowerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mUpgradeDao = viewModel.getUpgradeDao();

        mTowerTypeAdapter = new TowerRecyclerViewAdapter(getContext(), mUpgradeDao);
        mTowerRecycler.setAdapter(mTowerTypeAdapter);

        mTowerDropdown = view.findViewById(R.id.target_tower_dropdown);
        ArrayAdapter<CharSequence> towerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.towers, android.R.layout.simple_spinner_item);
        towerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mTowerDropdown.setAdapter(towerAdapter);

        mDifficultyDropdown = view.findViewById(R.id.difficulty_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.difficulties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mDifficultyDropdown.setAdapter(adapter);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.add_tower_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String newTowerTitle = mTowerDropdown.getSelectedItem().toString();

                if(newTowerTitle.equals("Select Tower"))
                {
                    Toast errorToast = Toast.makeText(getActivity(), "Select tower before pressing the Add Tower button", Toast.LENGTH_LONG);
                    errorToast.show();

                    return;
                }

                Tower newTower = new Tower(newTowerTitle, mUpgradeDao);

                ConnectTowerList.getTowers().add(newTower);
                mTowerTypeAdapter.notifyItemInserted(ConnectTowerList.getTowers().size() - 1);
            }
        });

        view.findViewById(R.id.help_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast towerToast = Toast.makeText(getActivity(), "There are " + ConnectTowerList.getTowers().size() + " towers.", Toast.LENGTH_LONG);
                towerToast.show();
            }
        });

        ConnectTowerList.addMyArrayListListener(new ConnectionArrayListChangedListener() {
            @Override
            public void OnMyArrayListChanged()
            {
                int finalPrice = 0;
                String finalPriceDisplay;

                for(Tower tower : ConnectTowerList.getTowers())
                {
                    finalPrice += tower.getTowerCost();
                }

                finalPriceDisplay = "$" + finalPrice;

                mFinalPrice.setText(finalPriceDisplay);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}
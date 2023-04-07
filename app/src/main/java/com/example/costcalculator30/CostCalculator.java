package com.example.costcalculator30;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CostCalculator extends Fragment
{
    private TowerRecyclerViewAdapter mTowerTypeAdapter;
    private AppPage mAppPage;

    private UpgradeRepository mUpgradeRepository;
    private DefenseViewModel mDefenseViewModel;

    private RecyclerView mTowerRecycler;

    private TextView mFinalPriceView;

    private Spinner mTowerDropdown;
    private Spinner mDifficultyDropdown;

    private ExecutorService mExecutor;

    private int mDefenseCost;
    private final int mCurrent;
    private Defense mCurrentDefense;

    public CostCalculator()
    {
        mCurrent = 1;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final String pageHeader = "Cost\nCalculator";
        final String initialCost = "$0";
        final int fragmentLayout = R.layout.fragment_cost_calculator;

        mUpgradeRepository = new UpgradeRepository(getActivity().getApplication());
        mDefenseViewModel = new ViewModelProvider(this).get(DefenseViewModel.class);

        mDefenseViewModel.getAllLiveData().observe(getViewLifecycleOwner(), new Observer<List<Defense>>()
        {
            @Override
            public void onChanged(@Nullable List<Defense> defenses)
            {
                if(defenses != null)
                {
                    int doNothing = 1;
                }
            }
        });

        mExecutor = Executors.newSingleThreadExecutor();

        mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

        mFinalPriceView = mAppPage.getCustomView().findViewById(R.id.hearts_lost);
        mFinalPriceView.setText(initialCost);

        mTowerRecycler = mAppPage.getCustomView().findViewById(R.id.bloon_recyclerView);
        mTowerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mTowerTypeAdapter = new TowerRecyclerViewAdapter(getContext());
        mTowerRecycler.setAdapter(mTowerTypeAdapter);

        mTowerDropdown = mAppPage.getCustomView().findViewById(R.id.bloon_dropdown);
        ArrayAdapter<CharSequence> towerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.towers, android.R.layout.simple_spinner_item);
        towerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mTowerDropdown.setAdapter(towerAdapter);

        mDifficultyDropdown = mAppPage.getCustomView().findViewById(R.id.difficulty_dropdown);
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.difficulties, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mDifficultyDropdown.setAdapter(difficultyAdapter);

        mDifficultyDropdown.setSelection(1);

        mDefenseCost = 0;

        updateTowers();

        return mAppPage.getOverView();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.help_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast towerToast = Toast.makeText(getActivity(), "There are " + ConnectTowerList.getTowers().size() + " towers.", Toast.LENGTH_LONG);
                towerToast.show();
            }
        });

        mAppPage.getCustomView().findViewById(R.id.add_bloon_button).setOnClickListener(new View.OnClickListener()
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

                Tower newTower = new Tower(newTowerTitle, 0, 0, 0, mUpgradeRepository);

                ConnectTowerList.getTowers().add(newTower);
                mTowerTypeAdapter.notifyItemInserted(ConnectTowerList.getTowers().size() - 1);
                mTowerRecycler.scrollToPosition(mTowerTypeAdapter.getItemCount() - 1);

                mExecutor.execute(() ->
                {
                    //mDefenseDao.setTowers(ConnectTowerList.getTowers(), 0);
                    mDefenseViewModel.setTowers(ConnectTowerList.getTowers(), mCurrent);
                });
            }
        });

        mAppPage.getCustomView().findViewById(R.id.clear_bloons_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int size = ConnectTowerList.getTowers().size();

                ConnectTowerList.clearTowers();
                mTowerTypeAdapter.notifyItemRangeRemoved(0, size);

                mTowerTypeAdapter.updateDatabase();
            }
        });

        mAppPage.getCustomView().findViewById(R.id.save_defense_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mExecutor.execute(() ->
                {
                    Defense newDefense = new Defense(ConnectTowerList.getTowers(), mDefenseCost, mDifficultyDropdown.getSelectedItem().toString(), mCurrent);
                    mDefenseViewModel.insertItem(newDefense);

                    Looper.prepare();
                    Toast saveToast = Toast.makeText(getActivity(), "Defense saved", Toast.LENGTH_LONG);
                    view.post (() -> saveToast.show());
                });
            }
        });

        mDifficultyDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                updateFinalPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        ConnectTowerList.addMyArrayListListener(new ConnectionArrayListChangedListener()
        {
            @Override
            public void OnMyArrayListChanged()
            {
                updateFinalPrice();
            }
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        //binding = null;
    }

    private void updateFinalPrice()
    {
        final double EASY_MULTIPLIER = 0.85;
        final double HARD_MULTIPLIER = 1.075;
        final double IMPOPPABLE_MULTIPLIER = 1.2;

        int finalPrice = 0;
        String finalPriceDisplay;
        String difficulty;

        for(Tower tower : ConnectTowerList.getTowers())
        {
            finalPrice += tower.getTowerCost();
        }

        difficulty = mDifficultyDropdown.getSelectedItem().toString();

        switch(difficulty)
        {
            case "Easy":
                finalPrice *= EASY_MULTIPLIER;
                break;
            case "Hard":
                finalPrice *= HARD_MULTIPLIER;
                break;
            case "Impoppable":
                finalPrice *= IMPOPPABLE_MULTIPLIER;
                break;
        }

        finalPriceDisplay = "$" + finalPrice;

        mFinalPriceView.setText(finalPriceDisplay);

        mDefenseCost = finalPrice;

        mExecutor.execute(() ->
        {
            mDefenseViewModel.setCost(mDefenseCost, mCurrent);
        });
    }

    public void setCost(int defenseCost)
    {
        String finalPriceDisplay = "$" + defenseCost;

        mFinalPriceView.setText(finalPriceDisplay);

        mDefenseCost = defenseCost;
    }

    private void updateTowers()
    {
        mExecutor.execute(() ->
        {
            int position = 1;

            mCurrentDefense = mDefenseViewModel.getCurrent().get(0);

            ConnectTowerList.setTowers(mCurrentDefense.getTowers());
            setCost(mCurrentDefense.getCost());

            switch(mCurrentDefense.getDifficulty())
            {
                case "Easy":
                    position = 0;
                    break;
                case "Medium":
                    position = 1;
                    break;
                case "Hard":
                    position = 2;
                    break;
                case "Impoppable":
                    position = 3;
                    break;
            }

            mDifficultyDropdown.setSelection(position);
        });
    }
}
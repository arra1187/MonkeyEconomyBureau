package com.example.costcalculator30;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CostCalculator extends Fragment
{
    private TowerRecyclerViewAdapter mTowerTypeAdapter;
    private UpgradeDatabase mDatabase;
    private UpgradeDao mUpgradeDao;
    private AppPage mAppPage;

    private RecyclerView mTowerRecycler;

    private TextView mPageHeader;
    private TextView mFinalPrice;

    private Spinner mTowerDropdown;
    private Spinner mDifficultyDropdown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final String pageHeader = "Cost\nCalculator";
        final String initialCost = "$0";
        final int fragmentLayout = R.layout.fragment_cost_calculator;

        mAppPage = new AppPage(inflater, container, fragmentLayout, pageHeader);

        getDatabase();

        //mPageHeader = mAppPage.getPage().findViewById(R.id.page_header);
        //mPageHeader.setText(pageHeader);

        mFinalPrice = mAppPage.getCustomView().findViewById(R.id.final_price);
        mFinalPrice.setText(initialCost);

        mTowerRecycler = mAppPage.getCustomView().findViewById(R.id.tower_recyclerView);
        mTowerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mTowerTypeAdapter = new TowerRecyclerViewAdapter(getContext(), mUpgradeDao);
        mTowerRecycler.setAdapter(mTowerTypeAdapter);

        mTowerDropdown = mAppPage.getCustomView().findViewById(R.id.target_tower_dropdown);
        ArrayAdapter<CharSequence> towerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.towers, android.R.layout.simple_spinner_item);
        towerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mTowerDropdown.setAdapter(towerAdapter);

        mDifficultyDropdown = mAppPage.getCustomView().findViewById(R.id.difficulty_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.difficulties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mDifficultyDropdown.setAdapter(adapter);

        mDifficultyDropdown.setSelection(1);

        return mAppPage.getOverView();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        /*view.findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(CostCalculator.this).navigate(CostCalculatorDirections.moveToAC());
            }
        });*/

        view.findViewById(R.id.help_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast towerToast = Toast.makeText(getActivity(), "There are " + ConnectTowerList.getTowers().size() + " towers.", Toast.LENGTH_LONG);
                towerToast.show();
            }
        });

        mAppPage.getCustomView().findViewById(R.id.add_tower_button).setOnClickListener(new View.OnClickListener()
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

                Tower newTower = new Tower(newTowerTitle, 0, 0, 0, 0, mUpgradeDao);

                ConnectTowerList.getTowers().add(newTower);
                mTowerTypeAdapter.notifyItemInserted(ConnectTowerList.getTowers().size() - 1);
                mTowerRecycler.scrollToPosition(mTowerTypeAdapter.getItemCount() - 1);
            }
        });

        mAppPage.getCustomView().findViewById(R.id.clear_towers_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ConnectTowerList.clearTowers();
                mTowerTypeAdapter.notifyDataSetChanged();
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

        mFinalPrice.setText(finalPriceDisplay);
    }

    private void getDatabase()
    {
        ExecutorService mExecutor= Executors.newSingleThreadExecutor ();
        mExecutor.execute(() ->
        {
            UpgradeDatabase upgradeDatabase = Room.databaseBuilder (getContext(),
                    UpgradeDatabase.class, "Upgrade-db").build();

            mUpgradeDao = upgradeDatabase.mUpgradeDao();
        });
    }

    private void getDatabaseOld()
    {
        ExecutorService mExecutor= Executors.newSingleThreadExecutor ();
        mExecutor.execute(() ->
        {
            String jsonString, aFileArray[];
            JSONArray jsonArray;
            AssetManager towerFiles;

            String title, tower;
            int upgradeID, cost;

            mDatabase = Room.databaseBuilder (getContext(),
                    UpgradeDatabase.class, "Upgrade-db").build();

            mUpgradeDao = mDatabase.mUpgradeDao();

            mUpgradeDao.deleteAll();

            towerFiles = getContext().getAssets();

            try
            {
                aFileArray = towerFiles.list("");

                for(String fileName : aFileArray)
                {
                    if(!(fileName.equals("images")))
                    {
                        if(!(fileName.equals("webkit")))
                        {
                            InputStream inputStream = towerFiles.open(fileName);
                            int size = inputStream.available();
                            byte[] buffer = new byte[size];

                            inputStream.read(buffer);
                            inputStream.close();

                            jsonString = new String(buffer, "UTF-8");

                            jsonArray = new JSONArray(jsonString);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonItem = jsonArray.getJSONObject(i);

                                title = jsonItem.getString("mTitle");
                                upgradeID = Integer.parseInt(jsonItem.getString("mUpgradeID"));
                                tower = jsonItem.getString("mTower");
                                cost = Integer.parseInt(jsonItem.getString("mCost"));

                                Upgrade newUpgrade = new Upgrade(title, upgradeID, tower, cost);

                                mUpgradeDao.insert(newUpgrade);
                            }
                        }
                    }
                }
            }
            catch(JSONException exception)
            {
                exception.printStackTrace();
            }
            catch(IOException exception)
            {
                exception.printStackTrace();
            }
        });
    }
}
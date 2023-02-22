package com.example.costcalculator30;

import android.content.Context;
import android.content.res.AssetManager;
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
    //private FragmentCostCalculatorBinding binding;
    //private RecyclerView.Adapter mTowerTypeAdapter;
    private TowerRecyclerViewAdapter mTowerTypeAdapter;
    private UpgradeDatabase mDatabase;
    private UpgradeDao mUpgradeDao;

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

        //mDatabase = UpgradeDatabase.getDatabase(getContext());
        //mUpgradeDao = mDatabase.mUpgradeDao();

        getDatabase();

        mFinalPrice = view.findViewById(R.id.final_price);
        mFinalPrice.setText("$0");

        mTowerRecycler = view.findViewById(R.id.tower_recyclerView);
        mTowerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

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

        view.findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(CostCalculator.this).navigate(CostCalculatorDirections.moveToAC());
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

    private String loadJSONFromAsset(Context context)
    {
        String jsonString = null;
        try
        {
            InputStream inputStream = context.getAssets().open("dart.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            jsonString = new String(buffer, "UTF-8");
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
            return null;
        }

        return jsonString;
    }

    private void getDatabase()
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

            //jsonString = loadJSONFromAsset(getContext());

            try
            {
                aFileArray = towerFiles.list("");

                for(String fileName : aFileArray)
                {
                    InputStream inputStream = towerFiles.open(fileName);
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];

                    inputStream.read(buffer);
                    inputStream.close();

                    jsonString = new String(buffer, "UTF-8");

                    jsonArray = new JSONArray(jsonString);

                    for(int i = 0; i < jsonArray.length(); i++)
                    {
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
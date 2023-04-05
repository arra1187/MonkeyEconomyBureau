package com.example.costcalculator30;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.costcalculator30.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
{
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private ExecutorService mExecutor;
    private UpgradeDatabase mDatabase;
    private UpgradeDao mUpgradeDao;

    private FragmentManager fragmentManager;

    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final String appName = getApplicationContext().getResources().getString(R.string.app_name);
        ActionBar mActionBar;
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        mActionBar = getSupportActionBar();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        setNavigationDrawer();

        if(mActionBar != null)
        {
            mActionBar.setTitle(appName);
        }

        fillDatabases();
    }

    private void setNavigationDrawer()
    {
        mDrawerLayout = findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = findViewById(R.id.navigation); // initiate a Navigation View

        // implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                Fragment fragment = null;               // create a Fragment Object
                int itemId = menuItem.getItemId();      // get selected menu item's id

                // check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.welcome_page)
                {
                    fragment = new WelcomePage();
                }
                else if (itemId == R.id.cost_calculator_page)
                {
                    fragment = new CostCalculator();
                }
                else if (itemId == R.id.affordability_calculator_page)
                {
                    fragment = new AffordabilityCalculator();
                }
                else if (itemId == R.id.saved_defenses_page)
                {
                    fragment = new SavedDefenses();
                }
                else if (itemId == R.id.sellback_calculator_page)
                {
                    fragment = new SellbackCalculator();
                }
                else if (itemId == R.id.hearts_lost_calculator_page)
                {
                    fragment = new HeartsLostCalculator();
                }

                if (fragment != null)
                {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.page_frame, fragment); // replace a Fragment with Frame Layout
                    transaction.commit(); // commit the changes
                    mDrawerLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Fragment fragment = null;

        switch(item.getItemId())
        {
            case R.id.welcome_page:
                fragment = new WelcomePage();
                break;
            case R.id.cost_calculator_page:
                fragment = new CostCalculator();
                break;
            case R.id.affordability_calculator_page:
                fragment = new AffordabilityCalculator();
                break;
            case R.id.saved_defenses_page:
                fragment = new SavedDefenses();
                break;
            case R.id.sellback_calculator_page:
                fragment = new SellbackCalculator();
                break;
            case R.id.hearts_lost_calculator_page:
                fragment = new HeartsLostCalculator();
                break;
        }

        if (fragment != null)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.page_frame, fragment); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes
            return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public UpgradeDao getUpgradeDao()
    {
        return mUpgradeDao;
    }

    private void fillDatabases()
    {
        ExecutorService mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(() ->
        {
            String jsonString, aFileArray[];
            JSONArray jsonArray;
            AssetManager towerFiles;

            String title, tower, type;
            int upgradeID, cost, roundNumber, RBE, cash, heartsLost, health;
            boolean bFortified;

            UpgradeRepository upgradeRepository = new UpgradeRepository(getApplication());
            RoundRepository roundRepository = new RoundRepository(getApplication());
            BloonRepository bloonRepository = new BloonRepository(getApplication());
            DefenseViewModel defenseViewModel = new ViewModelProvider(this).get(DefenseViewModel.class);

            upgradeRepository.deleteAll();
            roundRepository.deleteAll();
            bloonRepository.deleteAll();

            if(defenseViewModel.getSize() == 0)
            {
                defenseViewModel.insertItem(new Defense(new ArrayList<>(), 0));
            }

            towerFiles = getApplicationContext().getAssets();

            try
            {
                aFileArray = towerFiles.list("towers/");

                for (String fileName : aFileArray)
                {
                    InputStream inputStream = towerFiles.open("towers/" + fileName);
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];

                    inputStream.read(buffer);
                    inputStream.close();

                    jsonString = new String(buffer, "UTF-8");

                    jsonArray = new JSONArray(jsonString);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);

                        title = jsonItem.getString("mTitle");
                        upgradeID = Integer.parseInt(jsonItem.getString("mUpgradeID"));
                        tower = jsonItem.getString("mTower");
                        cost = Integer.parseInt(jsonItem.getString("mCost"));

                        Upgrade newUpgrade = new Upgrade(title, upgradeID, tower, cost);

                        //upgradeDao.insert(newUpgrade);
                        upgradeRepository.insert(newUpgrade);
                    }
                }

                aFileArray = towerFiles.list("rounds/");

                for (String fileName : aFileArray)
                {
                    InputStream inputStream = towerFiles.open("rounds/" + fileName);
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];

                    inputStream.read(buffer);
                    inputStream.close();

                    jsonString = new String(buffer, "UTF-8");

                    jsonArray = new JSONArray(jsonString);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);

                        roundNumber = Integer.parseInt(jsonItem.getString("mRoundNumber"));
                        RBE = Integer.parseInt(jsonItem.getString("mRBE"));
                        cash = Integer.parseInt(jsonItem.getString("mCash"));
                        type = jsonItem.getString("mType");

                        Round newRound = new Round(roundNumber, RBE, cash, type);

                        roundRepository.insert(newRound);
                    }
                }

                aFileArray = towerFiles.list("bloons/");

                for (String fileName : aFileArray)
                {
                    InputStream inputStream = towerFiles.open("bloons/" + fileName);
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];

                    inputStream.read(buffer);
                    inputStream.close();

                    jsonString = new String(buffer, "UTF-8");

                    jsonArray = new JSONArray(jsonString);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);

                        title = jsonItem.getString("mTitle");
                        type = jsonItem.getString("mType");
                        bFortified = Boolean.parseBoolean(jsonItem.getString("mbFortified"));
                        RBE = Integer.parseInt(jsonItem.getString("mRBE"));
                        heartsLost = Integer.parseInt(jsonItem.getString("mHeartsLost"));
                        health = Integer.parseInt(jsonItem.getString("mHealth"));

                        Bloon newBloon = new Bloon(title, type, bFortified, RBE, heartsLost, health);

                        bloonRepository.insert(newBloon);
                    }
                }
            }
            catch(JSONException | IOException exception)
            {
                exception.printStackTrace();
            }
        });
    }
}
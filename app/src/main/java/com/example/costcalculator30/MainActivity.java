package com.example.costcalculator30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.costcalculator30.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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

    //public View currentView;
    //public NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final String appName = getApplicationContext().getResources().getString(R.string.app_name);
        ActionBar mActionBar;
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_graph);
        //mNavCon = navHostFragment.getNavController();
        setSupportActionBar(binding.toolbar);

        mActionBar = getSupportActionBar();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //getActionBar().setTitle(appName);

        setNavigationDrawer();

        if(mActionBar != null)
        {
            mActionBar.setTitle(appName);
            //mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        fillUpgradeDatabase();


        //fragmentManager = getSupportFragmentManager();

        //mDrawerLayout = findViewById(R.id.my_drawer_layout);
        //actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        //mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        //actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        //getDatabase();

        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                Intent intent = new Intent(getApplicationContext(), DatabaseTest.class);

                startActivity(intent);
            }
        });*/
    }

    private void setNavigationDrawer()
    {

        //View customView = findViewById(R.id.page_frame).getRootView();
        //View customView = findLayoutById(R.layout.page_template);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
// implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                Fragment fragment = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.welcome_page)
                {
                    fragment = new WelcomePage();
                } else if (itemId == R.id.cost_calculator_page)
                {
                    fragment = new CostCalculator();
                } else if (itemId == R.id.affordability_calculator_page)
                {
                    fragment = new AffordabilityCalculator();
                }
// display a toast message with menu item's title
                //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();

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
        //Fragment fragment = new Fragment();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //int destination = 0;
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
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

    private boolean navigate()
    {
        return true;
    }

    public UpgradeDao getUpgradeDao()
    {
        return mUpgradeDao;
    }

    private void fillUpgradeDatabase() {
        ExecutorService mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(() ->
        {
            String jsonString, aFileArray[];
            JSONArray jsonArray;
            AssetManager towerFiles;

            String title, tower;
            int upgradeID, cost;

            mDatabase = Room.databaseBuilder(getApplicationContext(),
                    UpgradeDatabase.class, "Upgrade-db").build();

            mUpgradeDao = mDatabase.mUpgradeDao();

            mUpgradeDao.deleteAll();

            towerFiles = getApplicationContext().getAssets();

            try
            {
                aFileArray = towerFiles.list("");

                for (String fileName : aFileArray)
                {
                    if (!(fileName.equals("images")))
                    {
                        if (!(fileName.equals("webkit")))
                        {
                            InputStream inputStream = towerFiles.open(fileName);
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
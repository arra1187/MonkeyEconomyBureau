package com.example.costcalculator30;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.costcalculator30.databinding.ActivityMainBinding;

import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        getDatabase();

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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up help_button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public UpgradeDao getUpgradeDao()
    {
        return mUpgradeDao;
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
        mExecutor = Executors.newSingleThreadExecutor ();
        mExecutor.execute(() ->
        {
            String jsonString;
            JSONArray jsonArray;

            String title, tower;
            int upgradeID, cost;

            mDatabase = Room.databaseBuilder (getApplicationContext(),
                    UpgradeDatabase.class, "Upgrade-db").build();

            mUpgradeDao = mDatabase.mUpgradeDao();

            mUpgradeDao.deleteAll();

            jsonString = loadJSONFromAsset(getApplicationContext());

            try
            {
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
            catch(JSONException exception)
            {
                exception.printStackTrace();
            }
        });
    }
}
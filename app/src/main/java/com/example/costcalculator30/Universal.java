package com.example.costcalculator30;

import android.content.res.AssetManager;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Universal extends Fragment
{
    private UpgradeDatabase mDatabase;
    private UpgradeDao mUpgradeDao;

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

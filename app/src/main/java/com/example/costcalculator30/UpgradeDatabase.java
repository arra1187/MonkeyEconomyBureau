package com.example.costcalculator30;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database (entities = {Upgrade.class}, version = 1, exportSchema = false)
public abstract class UpgradeDatabase extends RoomDatabase
{
    public abstract UpgradeDao mUpgradeDao();
    public static UpgradeDatabase mInstance;

    public static UpgradeDatabase getDatabase(final Context context)
    {
        ExecutorService mExecutor = Executors.newSingleThreadExecutor ();
        mExecutor.execute(() ->
        {
            String jsonString;
            JSONArray jsonArray;

            String title, tower;
            int upgradeID, cost;

            mInstance = Room.databaseBuilder (context, UpgradeDatabase.class, "Upgrade-db").build();

            mInstance.mUpgradeDao().deleteAll();

            jsonString = loadJSONFromAsset(context);

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

                    mInstance.mUpgradeDao().insert(newUpgrade);
                }
            }
            catch(JSONException exception)
            {
                exception.printStackTrace();
            }
        });

        return mInstance;
    }

    private static String loadJSONFromAsset(Context context)
    {
        String jsonString = null;
        try
        {
            InputStream inputStream = context.getAssets().open("upgrade.json");
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
}
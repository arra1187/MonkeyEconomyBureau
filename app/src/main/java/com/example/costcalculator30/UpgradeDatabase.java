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
    private static volatile UpgradeDatabase INSTANCE;

    public abstract UpgradeDao mUpgradeDao();

    public static UpgradeDatabase getDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            synchronized(DefenseDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UpgradeDatabase.class, "upgrade-db")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance()
    {
        INSTANCE = null;
    }
}
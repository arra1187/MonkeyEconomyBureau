package com.example.costcalculator30;

import android.content.Context;

import androidx.room.Room;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseRepository
{
    /*public DefenseDao getDefenseDao(Context context)
    {
        DefenseDatabase defenseDatabase = Room.databaseBuilder(context,
                DefenseDatabase.class, "Defense-db").build();

        return defenseDatabase.mDefenseDao();
    }*/

    public RoundDao getRoundDao(Context context)
    {
        RoundDatabase roundDatabase = Room.databaseBuilder(context,
                RoundDatabase.class, "Round-db").build();
        return roundDatabase.mRoundDao();
    }

    public UpgradeDao getUpgradeDao(Context context)
    {
        UpgradeDatabase upgradeDatabase = Room.databaseBuilder(context,
                UpgradeDatabase.class, "Upgrade-db").build();
        return upgradeDatabase.mUpgradeDao();
    }
}
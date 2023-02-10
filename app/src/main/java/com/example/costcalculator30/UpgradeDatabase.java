package com.example.costcalculator30;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database (entities = {Upgrade.class}, version = 1, exportSchema = false)
public abstract class UpgradeDatabase extends RoomDatabase
{
    public abstract UpgradeDao upgradeDao();
}
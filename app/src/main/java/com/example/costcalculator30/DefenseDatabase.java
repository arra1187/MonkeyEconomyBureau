package com.example.costcalculator30;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Defense.class}, version = 1, exportSchema = false)
public abstract class DefenseDatabase extends RoomDatabase
{
    public abstract DefenseDao mDefenseDao();
}

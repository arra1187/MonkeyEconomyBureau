package com.example.costcalculator30;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Defense.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DefenseDatabase extends RoomDatabase
{
    public abstract DefenseDao mDefenseDao();
}

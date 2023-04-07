package com.example.costcalculator30;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Defense.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DefenseDatabase extends RoomDatabase
{
    private static volatile DefenseDatabase INSTANCE;

    public abstract DefenseDao mDefenseDao();

    public static DefenseDatabase getDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            synchronized(DefenseDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DefenseDatabase.class, "defense-db")
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

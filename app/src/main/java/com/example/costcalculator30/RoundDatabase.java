package com.example.costcalculator30;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Round.class}, version = 1, exportSchema = false)
public abstract class RoundDatabase extends RoomDatabase
{
    private static volatile RoundDatabase INSTANCE;

    public abstract RoundDao mRoundDao();

    public static RoundDatabase getDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            synchronized(DefenseDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RoundDatabase.class, "round-db")
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

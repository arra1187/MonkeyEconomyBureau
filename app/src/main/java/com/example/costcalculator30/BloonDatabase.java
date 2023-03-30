package com.example.costcalculator30;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database (entities = {Bloon.class}, version = 1, exportSchema = false)
public abstract class BloonDatabase extends RoomDatabase
{
  private static volatile BloonDatabase INSTANCE;

  public abstract BloonDao mBloonDao();

  public static BloonDatabase getDatabase(Context context)
  {
    if (INSTANCE == null)
    {
      synchronized(BloonDatabase.class)
      {
        if (INSTANCE == null)
        {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                  BloonDatabase.class, "bloon-db")
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

package com.example.costcalculator30;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Round.class}, version = 1, exportSchema = false)
public abstract class RoundDatabase extends RoomDatabase
{
    public abstract RoundDao mRoundDao();
}

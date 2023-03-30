package com.example.costcalculator30;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RoundDao
{
    @Insert
    void insert(Round round);

    @Query("Delete from Round")
    void deleteAll();

    @Query("Select cash from Round WHERE round = :round AND type = :type")
    int getCash(int round, String type);
}

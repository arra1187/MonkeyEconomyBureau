package com.example.costcalculator30;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface RoundDao
{
    @Query("Select cash from Round WHERE round = :round")
    int getCash(int round);
}

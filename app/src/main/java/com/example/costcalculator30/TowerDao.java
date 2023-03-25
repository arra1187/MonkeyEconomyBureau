package com.example.costcalculator30;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TowerDao
{
    /*@Query("Select title from Tower WHERE nid = :id")
    String getTitle(int id);

    @Query("Select towerCost from Tower WHERE nid = :id")
    int getTowerCost(int id);

    @Query("Update Tower SET topPath = :topPath WHERE nid = :id")
    void setTopPath(int topPath, int id);

    @Query("Update Tower SET middlePath = :middlePath WHERE nid = :id")
    void setMiddlePath(int middlePath, int id);

    @Query("Update Tower SET bottomPath = :bottomPath WHERE nid = :id")
    void setBottomPath(int bottomPath, int id);*/
}

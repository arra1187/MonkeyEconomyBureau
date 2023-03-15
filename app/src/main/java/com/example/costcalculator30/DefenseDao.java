package com.example.costcalculator30;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DefenseDao
{
    @Query("SELECT * FROM Defense")
    List<Defense> getAll();

    //@Query("SELECT towers FROM Defense WHERE id = :id")
    //ArrayList<Tower> getTowers(int id);

    @Query("SELECT cost FROM Defense WHERE id = :id")
    int getCost(int id);

    @Query("SELECT count(*) FROM Defense")
    int getSize();

    @Insert
    void insert(Defense defense);

    @Query("UPDATE Defense SET id = :newID WHERE id = :oldID")
    void setID(int oldID, int newID);

    @Query("UPDATE Defense SET towers = :towers WHERE id = :id")
    void setTowers(ArrayList<Tower> towers, int id);

    @Query("UPDATE Defense SET cost = :cost WHERE id = :id")
    void setCost(int cost, int id);
}
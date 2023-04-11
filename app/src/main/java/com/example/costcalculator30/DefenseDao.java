package com.example.costcalculator30;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DefenseDao
{
    @Query("SELECT * FROM Defense WHERE current = 0")
    LiveData<List<Defense>> getAllLive();

    @Query("SELECT * FROM Defense WHERE current = 0")
    List<Defense> getAll();

    @Query("SELECT * FROM Defense WHERE current = 1")
    List<Defense> getCurrent();

    @Query("Select * FROM Defense WHERE nid = :id")
    Defense getDefense(int id);

    @Query("SELECT cost FROM Defense WHERE nid = :id")
    int getCost(int id);

    @Query("SELECT count(*) FROM Defense")
    int getSize();

    @Insert
    void insert(Defense defense);

    @Delete
    void delete(Defense defense);

    @Query("Delete from Defense")
    void deleteAll();

    @Query("UPDATE Defense SET nid = :newID WHERE nid = :oldID")
    void setID(int oldID, int newID);

    @Query("UPDATE Defense SET towers = :towers WHERE current = :current")
    void setTowers(ArrayList<Tower> towers, int current);

    @Query("UPDATE Defense SET cost = :cost WHERE current = :current")
    void setCost(int cost, int current);

    @Query("UPDATE Defense SET difficulty = :difficulty WHERE current = :current")
    void setDifficulty(String difficulty, int current);

    @Query("UPDATE Defense SET current = :current WHERE nid = :id")
    void setCurrent(int current, int id);
}
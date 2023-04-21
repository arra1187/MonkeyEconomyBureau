package com.example.costcalculator30;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BloonDao
{
  @Insert
  void insert(Bloon bloon);

  @Delete
  void delete(Bloon bloon);

  @Query("Delete from Bloon")
  void deleteAll();

  @Query("SELECT * FROM Bloon WHERE title = :title AND fortified = :bFortified")
  Bloon getBloon(String title, boolean bFortified);

  @Query("SELECT RBE FROM Bloon WHERE title = :title AND fortified = :bFortified")
  int getRBE(String title, boolean bFortified);

  @Query("SELECT hearts_lost FROM Bloon WHERE title = :title AND fortified = :bFortified")
  int getHeartsLost(String title, boolean bFortified);

  @Query("SELECT health FROM Bloon WHERE title = :title AND fortified = :bFortified")
  int getHealth(String title, boolean bFortified);

}

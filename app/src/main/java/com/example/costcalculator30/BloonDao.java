package com.example.costcalculator30;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BloonDao
{
  @Insert
  void insert(Bloon bloon);

  @Delete
  void delete(Bloon bloon);

  @Delete
  void deleteAll();

  @Query("SELECT * FROM Bloon WHERE title = :title AND fortified = :bFortified")
  int getBloon(String title, boolean bFortified);

  @Query("SELECT RBE FROM Bloon WHERE title = :title")
  int getRBE(String title);

  @Query("SELECT health FROM Bloon WHERE title = :title")
  int getHealth(String title);
}

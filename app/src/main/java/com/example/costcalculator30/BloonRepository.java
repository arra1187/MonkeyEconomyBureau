package com.example.costcalculator30;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BloonRepository
{
  private final BloonDao mBloonDao;

  public BloonRepository(Application application)
  {
    BloonDatabase bloonDatabase = BloonDatabase.getDatabase(application);
    mBloonDao = bloonDatabase.mBloonDao();
  }

  public void insert(Bloon bloon)
  {
    mBloonDao.insert(bloon);
  }

  public void delete(Bloon bloon)
  {
    mBloonDao.delete(bloon);
  }

  public void deleteAll()
  {
    mBloonDao.deleteAll();
  }

  public Bloon getBLoon(String title, boolean fortified)
  {
    return mBloonDao.getBloon(title, fortified);
  }

  public int getRBE(String title, boolean fortified)
  {
    return mBloonDao.getRBE(title, fortified);
  }

  public int getHealth(String title, boolean fortified)
  {
    return mBloonDao.getHealth(title, fortified);
  }

  public int getHeartsLost(String title, boolean fortified)
  {
    return mBloonDao.getHeartsLost(title, fortified);
  }
}

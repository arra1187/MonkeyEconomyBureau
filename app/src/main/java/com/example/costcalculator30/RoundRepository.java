package com.example.costcalculator30;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RoundRepository
{
  private final RoundDao mRoundDao;
  //private final LiveData<List<Round>> mRounds;

  public RoundRepository(Application application)
  {
    RoundDatabase roundDatabase = RoundDatabase.getDatabase(application);
    mRoundDao = roundDatabase.mRoundDao();
    //mRounds = mRoundDao.getAll();
  }

  public void insert(Round round)
  {
    mRoundDao.insert(round);
  }

  public void deleteAll()
  {
    mRoundDao.deleteAll();
  }

  public int getRoundCash(int round, String type)
  {
    return mRoundDao.getCash(round, type);
  }
}

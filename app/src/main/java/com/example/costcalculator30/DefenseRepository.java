package com.example.costcalculator30;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefenseRepository
{
    private final DefenseDao mDefenseDao;
    private final LiveData<List<Defense>> mLiveDefenses;
    private List<Defense> mDefenses;

    public DefenseRepository(Application application)
    {
        DefenseDatabase defenseDatabase = DefenseDatabase.getDatabase(application);
        mDefenseDao = defenseDatabase.mDefenseDao();
        mLiveDefenses = mDefenseDao.getAllLive();
    }

    public LiveData<List<Defense>> getAllLive()
    {
        return mLiveDefenses;
    }

    public List<Defense> getAll()
    {
        if(mDefenses == null)
        {
            mDefenses = mDefenseDao.getAll();
        }

        return mDefenses;
    }

    public Defense getDefense(int id)
    {
        return mDefenseDao.getDefense(id);
    }

    public int getSize()
    {
        return mDefenseDao.getSize();
    }

    //You must call these methods on a non-UI thread or your app will crash

    public int getCost(int id)
    {
        return mDefenseDao.getCost(id);
    }

    public void setID(int oldID, int newID)
    {
        mDefenseDao.setID(oldID, newID);
    }

    public void setCost(int cost, int id)
    {
        mDefenseDao.setCost(cost, id);
    }

    public void setTowers(ArrayList<Tower> towers, int id)
    {
        mDefenseDao.setTowers(towers, id);
    }

    public void insert(Defense defense)
    {
        mDefenseDao.insert(defense);
    }

    public void delete(Defense defense)
    {
        mDefenseDao.delete(defense);
    }
}

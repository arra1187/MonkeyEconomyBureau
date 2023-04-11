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

    //You must call these methods on a non-UI thread or your app will crash

    public List<Defense> getAll()
    {
        return mDefenseDao.getAll();
    }

    public List<Defense> getCurrent()
    {
        return mDefenseDao.getCurrent();
    }

    public Defense getDefense(int id)
    {
        return mDefenseDao.getDefense(id);
    }

    public int getSize()
    {
        return mDefenseDao.getAll().size();
    }

    public int getCurrentCost()
    {
        return mDefenseDao.getCurrentCost();
    }

    public int getCost(int id)
    {
        return mDefenseDao.getCost(id);
    }

    public void setID(int oldID, int newID)
    {
        mDefenseDao.setID(oldID, newID);
    }

    public void setCost(int cost, int current)
    {
        mDefenseDao.setCost(cost, current);
    }

    public void setTowers(ArrayList<Tower> towers, int current)
    {
        mDefenseDao.setTowers(towers, current);
    }

    public void setDifficulty(String difficulty, int current)
    {
        mDefenseDao.setDifficulty(difficulty, current);
    }

    public void setCurrent(int current, int id)
    {
        mDefenseDao.setCurrent(current, id);
    }

    public void insert(Defense defense)
    {
        mDefenseDao.insert(defense);
    }

    public void delete(Defense defense)
    {
        mDefenseDao.delete(defense);
    }

    public void deleteAll()
    {
        mDefenseDao.deleteAll();
    }
}

package com.example.costcalculator30;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DefenseRepository
{
    private DefenseDao mDefenseDao;
    private LiveData<List<Defense>> mDefenses;

    public DefenseRepository(Application application)
    {
        DefenseDatabase defenseDatabase = DefenseDatabase.getDatabase(application);
        mDefenseDao = defenseDatabase.mDefenseDao();
        mDefenses = mDefenseDao.getAll();
    }

    LiveData<List<Defense>> getAll()
    {
        return mDefenses;
    }

    //You must call these methods on a non-UI thread or your app will crash

    public void insert(Defense defense)
    {
        new insertAsyncTask(mDefenseDao).execute(defense);
    }

    private static class insertAsyncTask extends AsyncTask<Defense, Void, Void>
    {
        private DefenseDao mAsyncTaskDao;
        insertAsyncTask(DefenseDao defenseDao)
        {
            mAsyncTaskDao = defenseDao;
        }

        @Override
        protected Void doInBackground(final Defense... params)
        {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void delete(Defense defense)
    {
        new deleteAsyncTask(mDefenseDao).execute(defense);
    }

    private static class deleteAsyncTask extends AsyncTask<Defense, Void, Void>
    {
        private DefenseDao mAsyncTaskDao;
        deleteAsyncTask(DefenseDao defenseDao)
        {
            mAsyncTaskDao = defenseDao;
        }

        @Override
        protected Void doInBackground(final Defense... params)
        {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public Defense getDefense(int id)
    {
        for(Defense defense : mDefenses.getValue())
        {
            if(defense.getNid() == id)
            {
                return defense;
            }
        }

        return null;
    }
}

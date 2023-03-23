package com.example.costcalculator30;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Objects;

public class DefenseRepository
{
    private final DefenseDao mDefenseDao;
    private final LiveData<List<Defense>> mDefenses;

    public DefenseRepository(Application application)
    {
        DefenseDatabase defenseDatabase = DefenseDatabase.getDatabase(application);
        mDefenseDao = defenseDatabase.mDefenseDao();
        mDefenses = mDefenseDao.getAll();
    }

    public LiveData<List<Defense>> getAll()
    {
        return mDefenses;
    }

    public int getSize()
    {
        return mDefenseDao.getSize();
    }

    //You must call these methods on a non-UI thread or your app will crash

    public void insert(Defense defense)
    {
        //new insertAsyncTask(mDefenseDao).execute(defense);

        mDefenseDao.insert(defense);
    }

    /*private static class insertAsyncTask extends AsyncTask<Defense, Void, Void>
    {
        private final DefenseDao mAsyncTaskDao;

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
    }*/

    public void delete(Defense defense)
    {
        //new deleteAsyncTask(mDefenseDao).execute(defense);

        mDefenseDao.insert(defense);
    }

    /*private static class deleteAsyncTask extends AsyncTask<Defense, Void, Void>
    {
        private final DefenseDao mAsyncTaskDao;

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
    }*/

    public Defense getDefense(int id)
    {
        for(Defense defense : Objects.requireNonNull(mDefenses.getValue()))
        {
            if(defense.getNid() == id)
            {
                return defense;
            }
        }

        return null;
    }
}

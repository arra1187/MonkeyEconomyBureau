package com.example.costcalculator30;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Objects;

public class UpgradeRepository
{
    private final UpgradeDao mUpgradeDao;
    private final LiveData<List<Upgrade>> mUpgrades;

    public UpgradeRepository(Application application)
    {
        UpgradeDatabase upgradeDatabase = UpgradeDatabase.getDatabase(application);
        mUpgradeDao = upgradeDatabase.mUpgradeDao();
        mUpgrades = mUpgradeDao.getAll();
    }

    public LiveData<List<Upgrade>> getAll()
    {
        return mUpgrades;
    }

    public int getSize()
    {
        return mUpgradeDao.getSize();
    }

    //You must call these methods on a non-UI thread or your app will crash

    public void insert(Upgrade upgrade)
    {
        //new UpgradeRepository.insertAsyncTask(mUpgradeDao).execute(Upgrade);

        mUpgradeDao.insert(upgrade);
    }

    /*private static class insertAsyncTask extends AsyncTask<Upgrade, Void, Void>
    {
        private final UpgradeDao mAsyncTaskDao;

        insertAsyncTask(UpgradeDao UpgradeDao)
        {
            mAsyncTaskDao = UpgradeDao;
        }

        @Override
        protected Void doInBackground(final Upgrade... params)
        {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }*/

    public void delete(Upgrade upgrade)
    {
        //new UpgradeRepository.deleteAsyncTask(mUpgradeDao).execute(Upgrade);

        mUpgradeDao.delete(upgrade);
    }

    /*private static class deleteAsyncTask extends AsyncTask<Upgrade, Void, Void>
    {
        private final UpgradeDao mAsyncTaskDao;

        deleteAsyncTask(UpgradeDao UpgradeDao)
        {
            mAsyncTaskDao = UpgradeDao;
        }

        @Override
        protected Void doInBackground(final Upgrade... params)
        {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }*/

    public void deleteAll()
    {
        mUpgradeDao.deleteAll();
    }


    public Upgrade getUpgrade(int id)
    {
        return getUpgrade(id);
    }

    public int getCost(String tower, int upgradeID)
    {
        return mUpgradeDao.getCost(tower, upgradeID);
    }
}

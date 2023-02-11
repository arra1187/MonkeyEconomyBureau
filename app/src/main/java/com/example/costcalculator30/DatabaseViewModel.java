package com.example.costcalculator30;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DatabaseViewModel extends ViewModel
{
    UpgradeDao mUpgradeDao;

    public void setUpgradeDao(UpgradeDao upgradeDao)
    {
        mUpgradeDao = upgradeDao;
    }

    public UpgradeDao getUpgradeDao()
    {
        return mUpgradeDao;
    }
}

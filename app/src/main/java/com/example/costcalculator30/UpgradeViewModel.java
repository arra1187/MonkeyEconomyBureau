package com.example.costcalculator30;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UpgradeViewModel extends AndroidViewModel
{
    private final UpgradeRepository mDataRepository;
    private final LiveData<List<Upgrade>> mListLiveData;

    public UpgradeViewModel(@NonNull Application application)
    {
        super(application);
        mDataRepository = new UpgradeRepository(application);
        mListLiveData = mDataRepository.getAll();
    }

    public LiveData<List<Upgrade>> getAllData()
    {
        return mListLiveData;
    }

    public int getSize()
    {
        return mDataRepository.getSize();
    }

    public void insertItem(Upgrade upgrade)
    {
        mDataRepository.insert(upgrade);
    }

    public void deleteItem(Upgrade upgrade)
    {
        mDataRepository.delete(upgrade);
    }

    public Upgrade getDefense(int id)
    {
        return mDataRepository.getUpgrade(id);
    }

    public int getCost(Upgrade upgrade)
    {
        return getCost(upgrade);
    }

    public void deleteAll()
    {
        mDataRepository.deleteAll();
    }
}
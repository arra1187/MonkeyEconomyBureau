package com.example.costcalculator30;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class DefenseViewModel extends AndroidViewModel
{
    private final DefenseRepository mDataRepository;
    private final LiveData<List<Defense>> mListLiveData;

    public DefenseViewModel(@NonNull Application application)
    {
        super(application);
        mDataRepository = new DefenseRepository(application);
        mListLiveData = mDataRepository.getAllLive();
    }

    public LiveData<List<Defense>> getAllLiveData()
    {
        return mListLiveData;
    }

    public List<Defense> getAllData()
    {
        return mDataRepository.getAll();
    }

    public List<Defense> getCurrent()
    {
        return mDataRepository.getCurrent();
    }

    public void setCost(int cost, int id)
    {
        mDataRepository.setCost(cost, id);
    }

    public void setTowers(ArrayList<Tower> towers, int id)
    {
        mDataRepository.setTowers(towers, id);
    }

    public int getCost(int id)
    {
        return mDataRepository.getCost(id);
    }

    public void setID(int oldID, int newID)
    {
        mDataRepository.setID(oldID, newID);
    }

    public int getSize()
    {
        return mDataRepository.getSize();
    }

    public void insertItem(Defense defense)
    {
        mDataRepository.insert(defense);
    }

    public void deleteItem(Defense defense)
    {
        mDataRepository.delete(defense);
    }

    public void deleteAll()
    {
        mDataRepository.deleteAll();
    }

    public Defense getDefense(int id)
    {
        return mDataRepository.getDefense(id);
    }
}

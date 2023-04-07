package com.example.costcalculator30;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity
public class Defense
{
    @PrimaryKey(autoGenerate = true)
    private int nid;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "towers")
    @SerializedName("towers")
    private ArrayList<Tower> mTowers;

    @ColumnInfo(name = "cost")
    private int mCost;

    @ColumnInfo(name = "current")
    private int mCurrent;

    @ColumnInfo(name = "difficulty")
    private String mDifficulty;

    public Defense(ArrayList<Tower> towers, int cost, String difficulty, int current)
    {
        mTowers = towers;
        mCost = cost;
        mDifficulty = difficulty;
        mCurrent = current;
    }

    public ArrayList<Tower> getTowers()
    {
        return mTowers;
    }

    public int getCost()
    {
        return mCost;
    }

    public int getCurrent()
    {
        return mCurrent;
    }

    public String getDifficulty()
    {
        return mDifficulty;
    }

    public int getNid()
    {
        return nid;
    }

    public void setTowers(ArrayList<Tower> towers)
    {
        mTowers = towers;
    }

    public void setCost(int cost)
    {
        mCost = cost;
    }

    public void setCurrent(int current)
    {
        mCurrent = current;
    }

    public void setDifficulty(String difficulty)
    {
        mDifficulty = difficulty;
    }

    public void setNid(int value)
    {
        nid = value;
    }
}

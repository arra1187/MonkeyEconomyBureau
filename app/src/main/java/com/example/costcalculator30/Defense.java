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

    /*public Defense(int current)
    {
        mCurrent = current;

        mCost = 0;
        mTowers = new ArrayList<>();
    }*/

    public Defense(ArrayList<Tower> towers, int cost)
    {
        mTowers = towers;
        mCost = cost;
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

    public void setNid(int value)
    {
        nid = value;
    }
}

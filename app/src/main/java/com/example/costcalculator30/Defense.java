package com.example.costcalculator30;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Defense
{
    @PrimaryKey()
    private int nid;

    @ColumnInfo(name = "towers")
    private ArrayList<Tower> mTowers;

    @ColumnInfo(name = "cost")
    private int mCost;

    public Defense(ArrayList<Tower> towers, int cost, int id)
    {
        mTowers = towers;
        mCost = cost;
        nid = id;
    }

    public ArrayList<Tower> getTowers()
    {
        return mTowers;
    }

    public int getCost()
    {
        return mCost;
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

    public void setNid(int value)
    {
        nid = value;
    }
}

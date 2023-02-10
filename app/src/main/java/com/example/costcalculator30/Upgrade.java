package com.example.costcalculator30;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Upgrade
{
    @PrimaryKey (autoGenerate = true)
    private int nid;

    @ColumnInfo (name = "title")
    private String mTitle;
    @ColumnInfo(name = "upgradeID")
    private int mUpgradeID;
    @ColumnInfo (name = "tower")
    private String mTower;
    @ColumnInfo(name = "cost")
    private int mCost;

    public Upgrade(String title, int upgradeID, String tower, int cost)
    {
        mTitle = title;
        mUpgradeID = upgradeID;
        mTower = tower;
        mCost = cost;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public int getUpgradeID()
    {
        return mUpgradeID;
    }

    public String getTower()
    {
        return mTower;
    }

    public int getCost()
    {
        return mCost;
    }

    public int getNid() { return nid; }

    public void setNid(int value) { nid = value; }
}
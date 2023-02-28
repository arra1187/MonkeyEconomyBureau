package com.example.costcalculator30;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Entity
public class Tower
{
    @PrimaryKey(autoGenerate = true)
    private int nid;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo()
    private int mTopPath;

    @ColumnInfo()
    private int mMiddlePath;

    @ColumnInfo()
    private int mBottomPath;

    private double [] mTopPathDiscounts;
    private double [] mMiddlePathDiscounts;
    private double [] mBottomPathDiscounts;
    private double mBaseDiscount;

    private int mBaseCost;
    private int mTopPathCosts[];
    private int mMiddlePathCosts[];
    private int mBottomPathCosts[];

    private final int numTiers = 6;

    Tower(String title, UpgradeDao upgradeDao)
    {
        mTitle = title;
        mTopPath = 0;
        mMiddlePath = 0;
        mBottomPath = 0;
        mBaseDiscount = 0;

        mTopPathCosts = new int[numTiers];
        mMiddlePathCosts = new int[numTiers];
        mBottomPathCosts = new int[numTiers];

        ExecutorService mExecutor = Executors.newSingleThreadExecutor ();
        mExecutor.execute(() ->
        {
            int paragonCost;

            mBaseCost = upgradeDao.getCost(mTitle, 0);

            for (int i = 0; i < numTiers; i++)
            {
                mTopPathCosts[i] = upgradeDao.getCost(mTitle, 10 + i + 1);
            }

            for (int i = 0; i < numTiers; i++)
            {
                mMiddlePathCosts[i] = upgradeDao.getCost(mTitle, 20 + i + 1);
            }

            for (int i = 0; i < numTiers; i++)
            {
                mBottomPathCosts[i] = upgradeDao.getCost(mTitle, 30 + i + 1);
            }

            paragonCost = upgradeDao.getCost(mTitle, 6);

            mTopPathCosts[numTiers - 1] = paragonCost;
            mMiddlePathCosts[numTiers - 1] = paragonCost;
            mBottomPathCosts[numTiers - 1] = paragonCost;
        });
    }

    public String getTitle()
    {
        return mTitle;
    }

    public int getTowerCost()
    {
        int towerCost = mBaseCost;

        for (int i = 0; i < mTopPath; i++)
        {
            towerCost += mTopPathCosts[i];
        }

        for (int i = 0; i < mMiddlePath; i++)
        {
            towerCost += mMiddlePathCosts[i];
        }

        for (int i = 0; i < mBottomPath; i++)
        {
            towerCost += mBottomPathCosts[i];
        }

        return towerCost;
    }

    public int getTopPath()
    {
        return mTopPath;
    }

    public void setTopPath(int topPath)
    {
        mTopPath = topPath;

        if(topPath != 0)
        {
            mTopPathDiscounts = new double[topPath];
        }
    }

    public void setMiddlePath(int middlePath)
    {
        mMiddlePath = middlePath;

        if(middlePath != 0)
        {
            mMiddlePathDiscounts = new double[middlePath];
        }
    }

    public void setBottomPath(int bottomPath)
    {
        mBottomPath = bottomPath;

        if(bottomPath != 0)
        {
            mBottomPathDiscounts = new double[bottomPath];
        }
    }

    public void setUpgrades(int topPath, int middlePath, int bottomPath)
    {
        mTopPath = topPath;
        mMiddlePath = middlePath;
        mBottomPath = bottomPath;

        if(topPath != 0)
        {
            mTopPathDiscounts = new double[topPath];
        }

        if(middlePath != 0)
        {
            mMiddlePathDiscounts = new double[middlePath];
        }

        if(bottomPath != 0)
        {
            mBottomPathDiscounts = new double[bottomPath];
        }
    }

    public void setDiscounts(double [] topPathDiscounts, double [] middlePathDiscounts,
                             double [] bottomPathDiscounts, double baseDiscount)
    {
        int count = 0;

        for(double discount : topPathDiscounts)
        {
            mTopPathDiscounts[count] = discount;
            count++;
        }

        count = 0;

        for(double discount : middlePathDiscounts)
        {
            mMiddlePathDiscounts[count] = discount;
            count++;
        }

        count = 0;

        for(double discount : bottomPathDiscounts)
        {
            mBottomPathDiscounts[count] = discount;
            count++;
        }

        mBaseDiscount = baseDiscount;
    }
}

package com.example.costcalculator30;

import com.google.gson.annotations.SerializedName;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tower
{
    @SerializedName("title")
    private String mTitle;

    @SerializedName("topPath")
    private int mTopPath;

    @SerializedName("middlePath")
    private int mMiddlePath;

    @SerializedName("bottomPath")
    private int mBottomPath;

    @SerializedName("towerCost")
    private int mTowerCost;

    @SerializedName("baseCost")
    private int mBaseCost;

    @SerializedName("topPathCosts")
    private int mTopPathCosts[];

    @SerializedName("middlePathCosts")
    private int mMiddlePathCosts[];

    @SerializedName("bottomPathCosts")
    private int mBottomPathCosts[];

    @SerializedName("numTiers")
    private final int numTiers = 6;

    @SerializedName("numTowers")
    private int mNumTowers;

    Tower(String title, int topPath, int middlePath, int bottomPath, UpgradeRepository upgradeRepository)
    {
        mTitle = title;
        mTopPath = topPath;
        mMiddlePath = middlePath;
        mBottomPath = bottomPath;

        mTopPathCosts = new int[numTiers];
        mMiddlePathCosts = new int[numTiers];
        mBottomPathCosts = new int[numTiers];

        mNumTowers = 1;

        ExecutorService mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(() ->
        {
            int paragonCost;

            mBaseCost = upgradeRepository.getCost(mTitle, 0);

            for (int i = 0; i < numTiers; i++)
            {
                mTopPathCosts[i] = upgradeRepository.getCost(mTitle, 10 + i + 1);
            }

            for (int i = 0; i < numTiers; i++)
            {
                mMiddlePathCosts[i] = upgradeRepository.getCost(mTitle, 20 + i + 1);
            }

            for (int i = 0; i < numTiers; i++)
            {
                mBottomPathCosts[i] = upgradeRepository.getCost(mTitle, 30 + i + 1);
            }

            paragonCost = upgradeRepository.getCost(mTitle, 6);

            mTopPathCosts[numTiers - 1] = paragonCost;
            mMiddlePathCosts[numTiers - 1] = paragonCost;
            mBottomPathCosts[numTiers - 1] = paragonCost;
        });
    }

    public String getTitle()
    {
        return mTitle;
    }

    public int getTowerCost(double multiplier)
    {
        mTowerCost = Utility.convertCost(mBaseCost, multiplier);

        for (int i = 0; i < mTopPath; i++)
        {
            mTowerCost += Utility.convertCost(mTopPathCosts[i], multiplier);
        }

        for (int i = 0; i < mMiddlePath; i++)
        {
            mTowerCost += Utility.convertCost(mMiddlePathCosts[i], multiplier);
        }

        for (int i = 0; i < mBottomPath; i++)
        {
            mTowerCost += Utility.convertCost(mBottomPathCosts[i], multiplier);
        }

        return mTowerCost * mNumTowers;
    }

    public int getNumTowers()
    {
        return mNumTowers;
    }

    public int getTopPath()
    {
        return mTopPath;
    }

    public int getMiddlePath()
    {
        return mMiddlePath;
    }

    public int getBottomPath()
    {
        return mBottomPath;
    }

    public void setNumTowers(int numTowers)
    {
        mNumTowers = numTowers;
    }

    public void setTopPath(int topPath)
    {
        mTopPath = topPath;
    }

    public void setMiddlePath(int middlePath)
    {
        mMiddlePath = middlePath;
    }

    public void setBottomPath(int bottomPath)
    {
        mBottomPath = bottomPath;
    }
}

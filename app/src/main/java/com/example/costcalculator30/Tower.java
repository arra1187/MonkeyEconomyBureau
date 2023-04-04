package com.example.costcalculator30;

import com.google.gson.annotations.SerializedName;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Entity
public class Tower
{
    //@PrimaryKey(autoGenerate = true)
    //private int nid;

    //@ColumnInfo(name = "title")
    @SerializedName("title")
    private String mTitle;

    //@ColumnInfo(name = "topPath")
    @SerializedName("topPath")
    private int mTopPath;

    //@ColumnInfo(name = "middlePath")
    @SerializedName("middlePath")
    private int mMiddlePath;

    //@ColumnInfo(name = "bottomPath")
    @SerializedName("bottomPath")
    private int mBottomPath;

    //@ColumnInfo(name = "towerCost")
    @SerializedName("towerCost")
    private int mTowerCost;

    //@ColumnInfo(name = "upgradeDao")
    //private UpgradeDao mUpgradeDao;

    /*@ColumnInfo(name = "topPathDiscounts")
    private double [] mTopPathDiscounts;

    @ColumnInfo(name = "middlePathDiscounts")
    private double [] mMiddlePathDiscounts;

    @ColumnInfo(name = "bottomPathDiscounts")
    private double [] mBottomPathDiscounts;

    @ColumnInfo(name = "baseDiscount")
    private double mBaseDiscount;*/

    //@ColumnInfo(name = "baseCost")
    //@Ignore
    @SerializedName("baseCost")
    private int mBaseCost;

    //@ColumnInfo(name = "topPathCosts")
    //@Ignore
    @SerializedName("topPathCosts")
    private int mTopPathCosts[];

    //@ColumnInfo(name = "middlePathCosts")
    //@Ignore
    @SerializedName("middlePathCosts")
    private int mMiddlePathCosts[];

    //@ColumnInfo(name = "bottomPathCosts")
    //@Ignore
    @SerializedName("bottomPathCosts")
    private int mBottomPathCosts[];

    //@Ignore
    @SerializedName("numTiers")
    private final int numTiers = 6;

    Tower(String title, int topPath, int middlePath, int bottomPath, UpgradeRepository upgradeRepository)
    {
        mTitle = title;
        mTopPath = topPath;
        mMiddlePath = middlePath;
        mBottomPath = bottomPath;
        //mBaseDiscount = 0;

        mTopPathCosts = new int[numTiers];
        mMiddlePathCosts = new int[numTiers];
        mBottomPathCosts = new int[numTiers];

        ExecutorService mExecutor = Executors.newSingleThreadExecutor ();
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

    public int getTowerCost()
    {
        mTowerCost = mBaseCost;

        for (int i = 0; i < mTopPath; i++)
        {
            mTowerCost += mTopPathCosts[i];
        }

        for (int i = 0; i < mMiddlePath; i++)
        {
            mTowerCost += mMiddlePathCosts[i];
        }

        for (int i = 0; i < mBottomPath; i++)
        {
            mTowerCost += mBottomPathCosts[i];
        }

        return mTowerCost;
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

    public void setTopPath(int topPath)
    {
        mTopPath = topPath;

        if(topPath != 0)
        {
            //mTopPathDiscounts = new double[topPath];
        }
    }

    public void setMiddlePath(int middlePath)
    {
        mMiddlePath = middlePath;

        if(middlePath != 0)
        {
            //mMiddlePathDiscounts = new double[middlePath];
        }
    }

    public void setBottomPath(int bottomPath)
    {
        mBottomPath = bottomPath;

        if(bottomPath != 0)
        {
            //mBottomPathDiscounts = new double[bottomPath];
        }
    }

    /*public void setUpgrades(int topPath, int middlePath, int bottomPath)
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
    }*/
}

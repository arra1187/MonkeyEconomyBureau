package com.example.costcalculator30;

public class Tower
{
    private String mTitle;
    private UpgradeDao mUpgradeDao;

    private int mTopPath;
    private int mMiddlePath;
    private int mBottomPath;

    private int mTopPathCost;
    private int mMiddlePathCost;
    private int mBottomPathCost;

    private double [] mTopPathDiscounts;
    private double [] mMiddlePathDiscounts;
    private double [] mBottomPathDiscounts;
    private double mBaseDiscount;

    Tower(String title, UpgradeDao upgradeDao)
    {
        mTitle = title;
        mTopPath = 0;
        mMiddlePath = 0;
        mBottomPath = 0;
        mBaseDiscount = 0;
        mUpgradeDao = upgradeDao;
    }

    public int getTowerCost()
    {
        int towerCost = 0;

        for(int i = 0; i < mTopPath; i++)
        {
            towerCost += mUpgradeDao.getCost(mTitle, 10 + i);
        }

        for(int i = 0; i < mMiddlePath; i++)
        {
            towerCost += mUpgradeDao.getCost(mTitle, 20 + i);
        }

        for(int i = 0; i < mBottomPath; i++)
        {
            towerCost += mUpgradeDao.getCost(mTitle, 30 + i);
        }

        return towerCost;
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

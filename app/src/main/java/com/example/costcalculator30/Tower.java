package com.example.costcalculator30;

public class Tower
{
    private int mTopPath;
    private int mMiddlePath;
    private int mBottomPath;

    private double [] mTopPathDiscounts;
    private double [] mMiddlePathDiscounts;
    private double [] mBottomPathDiscounts;
    private double mBaseDiscount;

    Tower()
    {
        mTopPath = 0;
        mMiddlePath = 0;
        mBottomPath = 0;
        mBaseDiscount = 0;
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

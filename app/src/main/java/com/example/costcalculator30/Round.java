package com.example.costcalculator30;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Round {
    @PrimaryKey(autoGenerate = true)
    private int nid;

    @ColumnInfo(name = "round")
    private int mRoundNumber;

    @ColumnInfo(name = "rbe")
    private int mRBE;

    @ColumnInfo(name = "cash")
    private int mCash;

    Round()
    {

    }

    Round(int roundNumber, int RBE, int cash)
    {
        mRoundNumber = roundNumber;
        mRBE = RBE;
        mCash = cash;
    }

    public int getRoundNumber()
    {
        return mRoundNumber;
    }

    public int getRBE()
    {
        return mRBE;
    }

    public int getCash()
    {
        return mCash;
    }

    public int getNid() { return nid; }

    public void setRoundNumber(int roundNumber)
    {
        mRoundNumber = roundNumber;
    }

    public void setRBE(int RBE)
    {
        mRBE = RBE;
    }

    public void setCash(int cash)
    {
        mCash = cash;
    }

    public void setNid(int value) { nid = value; }
}

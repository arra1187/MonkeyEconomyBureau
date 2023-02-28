package com.example.costcalculator30;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Defense
{
    @PrimaryKey(autoGenerate = true)
    private int nid;

    @ColumnInfo()
    private ArrayList<Tower> mTowers;

    public Defense(ArrayList<Tower> towers)
    {
        mTowers = towers;
    }

    public ArrayList<Tower> getTowers()
    {
        return mTowers;
    }
}

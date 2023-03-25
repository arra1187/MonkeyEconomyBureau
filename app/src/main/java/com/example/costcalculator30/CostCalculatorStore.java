package com.example.costcalculator30;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CostCalculatorStore implements Parcelable
{
    ArrayList<Tower> mTowers;

    protected CostCalculatorStore(Parcel in)
    {
        mTowers = in.readArrayList(getClass().getClassLoader());
    }

    public static final Creator<CostCalculatorStore> CREATOR = new Creator<CostCalculatorStore>()
    {
        @Override
        public CostCalculatorStore createFromParcel(Parcel in) {
            return new CostCalculatorStore(in);
        }

        @Override
        public CostCalculatorStore[] newArray(int size) {
            return new CostCalculatorStore[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i)
    {
        dest.writeArray(mTowers.toArray());
    }
}

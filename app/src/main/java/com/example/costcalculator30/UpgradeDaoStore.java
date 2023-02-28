package com.example.costcalculator30;

import android.os.Parcel;
import android.os.Parcelable;

public class UpgradeDaoStore implements Parcelable
{
    private final UpgradeDao mUpgradeDao;

    protected UpgradeDaoStore(Parcel in)
    {
        mUpgradeDao = (UpgradeDao) in.readValue(getClass().getClassLoader());
    }

    public static final Creator<UpgradeDaoStore> CREATOR = new Creator<UpgradeDaoStore>()
    {
        @Override
        public UpgradeDaoStore createFromParcel(Parcel in)
        {
            return new UpgradeDaoStore(in);
        }

        @Override
        public UpgradeDaoStore[] newArray(int size)
        {
            return new UpgradeDaoStore[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i)
    {
        dest.writeValue(mUpgradeDao);
    }
}

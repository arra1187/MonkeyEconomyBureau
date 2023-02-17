package com.example.costcalculator30;

import java.util.ArrayList;
import java.util.List;

public class ConnectTowerList
{
    private static ArrayList<Tower> mTowers = new ArrayList<>();
    private static List<ConnectionArrayListChangedListener> listeners = new ArrayList<ConnectionArrayListChangedListener>();

    public static ArrayList<Tower> getTowers()
    {
        return mTowers;
    }

    public static void addTower(Tower newTower)
    {
        mTowers.add(newTower);

        for(ConnectionArrayListChangedListener listener : listeners)
        {
            listener.OnMyArrayListChanged();
        }
    }

    public static void setTowers(ArrayList<Tower> towers)
    {
        mTowers = towers;

        for(ConnectionArrayListChangedListener listener : listeners)
        {
            listener.OnMyArrayListChanged();
        }
    }

    public static void removeTower(int position)
    {
        mTowers.remove(position);

        for (ConnectionArrayListChangedListener listener : listeners)
        {
            listener.OnMyArrayListChanged();
        }
    }

    public static void addMyArrayListListener(ConnectionArrayListChangedListener listener)
    {
        listeners.add(listener);
    }
}

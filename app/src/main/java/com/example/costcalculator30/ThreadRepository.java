package com.example.costcalculator30;

public class ThreadRepository extends Thread
{
    public int runGetDefenseSize(DefenseViewModel defenseViewModel)
    {
        return defenseViewModel.getSize();
    }
}

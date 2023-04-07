package com.example.costcalculator30;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Utility
{
  public void joinExecutor(ExecutorService executorService)
  {
    executorService.shutdown();

    while(!executorService.isTerminated())
    {
      try
      {
        executorService.awaitTermination(1, TimeUnit.SECONDS);
      }
      catch(InterruptedException ignored)
      {

      }
    }
  }

  public StringBuilder setTowerList(ArrayList<Tower> towers)
  {
    StringBuilder towerList = new StringBuilder();
    boolean first = true;

    for(Tower currentTower : towers)
    {
      if(first)
      {
        first = false;
      }
      else
      {
        towerList.append("\n");
      }

      towerList.append(currentTower.getTitle()).append(" ").append(currentTower.getTopPath()).append(" - ").append(currentTower.getMiddlePath()).append(" - ").append(currentTower.getBottomPath());
    }

    return towerList;
  }
}

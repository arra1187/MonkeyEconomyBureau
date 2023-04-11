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

  public StringBuilder setTowerList(Defense defense)
  {
    StringBuilder towerList = new StringBuilder();
    boolean first = true;

    towerList.append("Difficulty: " + defense.getDifficulty() + "\n");

    for(Tower currentTower : defense.getTowers())
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

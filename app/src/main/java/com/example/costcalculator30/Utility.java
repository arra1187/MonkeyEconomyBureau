package com.example.costcalculator30;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Utility
{
  public static void joinExecutor(ExecutorService executorService)
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

  public static StringBuilder setTowerList(Defense defense)
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

  public static double getDifficultyMultiplier(String difficulty)
  {
    final double EASY_MULTIPLIER = 0.85;
    final double MEDIUM_MULTIPLIER = 1;
    final double HARD_MULTIPLIER = 1.08;
    final double IMPOPPABLE_MULTIPLIER = 1.2;

    double multiplier = 1;

    switch(difficulty)
    {
      case "Easy":
        multiplier = EASY_MULTIPLIER;
        break;
      case "Medium":
        multiplier = MEDIUM_MULTIPLIER;
        break;
      case "Hard":
        multiplier = HARD_MULTIPLIER;
        break;
      case "Impoppable":
        multiplier = IMPOPPABLE_MULTIPLIER;
        break;
    }

    return multiplier;
  }

  public static int convertCost(int cost, double multiplier)
  {
    cost = (int) (cost * multiplier);

    while(cost % 5 != 0)
    {
      if(multiplier > 1)
      {
        cost++;
      }
      else if(multiplier < 1)
      {
        cost--;
      }
    }

    return cost;
  }
}

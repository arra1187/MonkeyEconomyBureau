package com.example.costcalculator30;

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
}

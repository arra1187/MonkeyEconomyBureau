package com.example.costcalculator30;

import android.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BloonItem
{
  private final String mTitle;

  private final String mType;   //Bloon, Heavy Bloon (can be fortified), Blimp, Boss, or other

  private boolean mbFortified;

  private int mNumBloons;

  private BloonRepository mBloonRepository;

  public BloonItem(String title, Application application)
  {
    mBloonRepository = new BloonRepository(application);

    mTitle = title;

    if (mTitle.equals("Lead Bloon") || mTitle.equals("Ceramic Bloon"))
    {
      mType = "Heavy Bloon";
    }
    else if (mTitle.equals("MOAB") || mTitle.equals("BFB") || mTitle.equals("ZOMG")
            || mTitle.equals("DDT") || mTitle.equals("BAD"))
    {
      mType = "Blimp";
    } else
    {
      mType = "Bloon";
    }

    mbFortified = false;
    mNumBloons = 1;
  }

  public String getTitle() {
    return mTitle;
  }

  public int getNumBloons()
  {
    return mNumBloons;
  }

  public String getType()
  {
    return mType;
  }

  public boolean getFortified()
  {
    return mbFortified;
  }

  public void setNumBloons(int numBloons)
  {
    mNumBloons = numBloons;
  }

  public void setFortified(boolean bFortified)
  {
    mbFortified = bFortified;
  }
}

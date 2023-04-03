package com.example.costcalculator30;

import android.app.Application;

import androidx.room.ColumnInfo;

public class BloonItem
{
  private String mTitle;

  private String mType;   //Bloon, Heavy Bloon (can be fortified), Blimp, Boss, or other

  private boolean mFortified;

  private int mRBE;

  private int mHealth;

  private int mNumBloons;

  public BloonItem(String title, Application application)
  {
    BloonRepository bloonRepository = new BloonRepository(application);

    mTitle = title;

    if(mTitle.equals("Lead Bloon") || mTitle.equals("Ceramic Bloon"))
    {
      mType = "Heavy Bloon";
    }
    if(mTitle.equals("MOAB") || mTitle.equals("BFB") || mTitle.equals("ZOMG")
                             || mTitle.equals("DDT") || mTitle.equals("BAD"))
    {
      mType = "Blimp";
    }
    else
    {
      mType = "Bloon";
    }

    mFortified = false;


  }

  public String getTitle()
  {
    return mTitle;
  }

  public void setNumBloons(int numBloons)
  {
    mNumBloons = numBloons;
  }
}

package com.example.costcalculator30;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bloon
{
  @PrimaryKey(autoGenerate = true)
  private int nid;

  @ColumnInfo(name = "title")
  private String mTitle;

  @ColumnInfo(name = "type")
  private String mType;   //Bloon, Heavy Bloon (can be fortified), Blimp, Boss, or other

  @ColumnInfo(name = "fortified", defaultValue = "false")
  private boolean mbFortified;

  @ColumnInfo(name = "RBE")
  private int mRBE;

  @ColumnInfo(name = "health")
  private int mHealth;

  public Bloon(String title, String type, boolean bFortified, int RBE, int health)
  {
    mTitle = title;
    mType = type;
    mbFortified = bFortified;
    mRBE = RBE;
    mHealth = health;
  }

  public String getTitle()
  {
    return mTitle;
  }

  public String getType()
  {
    return mType;
  }

  public boolean getFortified()
  {
    return mbFortified;
  }

  public int getRBE()
  {
    return mRBE;
  }

  public int getHealth()
  {
    return mHealth;
  }

  public int getNid()
  {
    return nid;
  }

  public void setTitle(String title)
  {
    mTitle = title;
  }

  public void setType(String type)
  {
    mType = type;
  }

  public void setFortified(boolean bFortified)
  {
    mbFortified = bFortified;
  }

  public void setRBE(int RBE)
  {
    mRBE = RBE;
  }

  public void setHealth(int health)
  {
    mHealth = health;
  }

  public void setNid(int id)
  {
    nid = id;
  }
}

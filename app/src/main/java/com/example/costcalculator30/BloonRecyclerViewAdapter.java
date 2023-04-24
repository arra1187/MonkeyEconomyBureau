package com.example.costcalculator30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BloonRecyclerViewAdapter
    extends RecyclerView.Adapter<BloonRecyclerViewAdapter.ViewHolder>
{
  private ArrayList<BloonItem> mBloons;
  private Context mContext;
  private MutableLiveData<Boolean> mbListener;
  private final LifecycleOwner mLifecycleOwner;

  BloonRecyclerViewAdapter(ArrayList<BloonItem> bloons, Context context, MutableLiveData<Boolean> bListener, LifecycleOwner lifecycleOwner)
  {
    mBloons = bloons;
    mContext = context;
    mbListener = bListener;
    mLifecycleOwner = lifecycleOwner;
  }

  @NonNull
  @Override
  public BloonRecyclerViewAdapter.ViewHolder onCreateViewHolder
      (@NonNull ViewGroup parent, int viewType)
  {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloon_display, parent,
        false);

    return new BloonRecyclerViewAdapter.ViewHolder(view, mContext, mbListener, mLifecycleOwner);
  }

  @Override
  public void onBindViewHolder
      (@NonNull BloonRecyclerViewAdapter.ViewHolder holder, int position)
  {
    holder.setBloon(mBloons.get(position));

    holder.getRemoveButton().setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        mBloons.remove(holder.getAdapterPosition());
        //notifyItemRemoved(holder.getAdapterPosition());

        if(mbListener.getValue())
        {
          mbListener.setValue(false);
        }
        else
        {
          mbListener.setValue(true);
        }
      }
    });

    holder.bindData();
  }

  @Override
  public int getItemCount()
  {
    return mBloons.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder
  {
    private Context mContext;
    private BloonItem mBloon;

    private MutableLiveData<Boolean> mbListener;
    private MutableLiveData<Integer> mPosition;

    private ImageView mSymbol;
    private TextView mTitle;
    private EditText mCount;
    private CheckBox mFortified;
    private ImageButton mClearButton;
    private final LifecycleOwner mLifecycleOwner;

    public ViewHolder(@NonNull View itemView, Context context, MutableLiveData<Boolean> bListener, LifecycleOwner lifecycleOwner)
    {
      super(itemView);

      mContext = context;
      mbListener = bListener;

      mPosition = new MutableLiveData<>(getAdapterPosition());

      mCount = itemView.findViewById(R.id.bloon_count);

      mLifecycleOwner = lifecycleOwner;
    }

    public ImageButton getRemoveButton()
    {
      return (ImageButton) itemView.findViewById(R.id.clear_bloon_button);
    }

    public void setBloon(BloonItem bloon)
    {
      mBloon = bloon;
    }

    @SuppressLint ("UseCompatLoadingForDrawables")
    public void bindData()
    {
      String bloonName = mBloon.getTitle();
      Drawable bloonSymbol = null;

      if(mSymbol == null)
      {
        mSymbol = itemView.findViewById(R.id.bloon_symbol);
      }
      if(mTitle == null)
      {
        mTitle = itemView.findViewById(R.id.bloon_name);
      }
      if(mFortified == null)
      {
        mFortified = itemView.findViewById(R.id.fortified_checkbox);
      }
      if(mClearButton == null)
      {
        mClearButton = itemView.findViewById(R.id.clear_bloon_button);
      }

      mTitle.setText(bloonName);

      switch(bloonName)
      {
        case "Red Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.red_bloon_symbol);
          break;
        case "Blue Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.blue_bloon_symbol);
          break;
        case "Green Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.green_bloon_symbol);
          break;
        case "Yellow Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.yellow_bloon_symbol);
          break;
        case "Pink Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.pink_bloon_symbol);
          break;
        case "Black Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.black_bloon_symbol);
          break;
        case "White Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.white_bloon_symbol);
          break;
        case "Purple Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.purple_bloon_symbol);
          break;
        case "Zebra Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.zebra_bloon_symbol);
          break;
        case "Rainbow Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.rainbow_bloon_symbol);
          break;
        case "Ceramic Bloon":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.ceramic_bloon_symbol);
          break;
        case "MOAB":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.moab_symbol);
          break;
        case "BFB":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.bfb_symbol);
          break;
        case "ZOMG":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.zomg_symbol);
          break;
        case "DDT":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.ddt_symbol);
          break;
        case "BAD":
          bloonSymbol = mContext.getResources().getDrawable(R.drawable.bad_symbol);
          break;
      }

      mSymbol.setImageDrawable(bloonSymbol);

      if(mBloon.getType().equals("Bloon") || mBloon.getType().equals("Extra"))
      {
        mFortified.setVisibility(View.INVISIBLE);
      }
      else
      {
        mFortified.setVisibility(View.VISIBLE);
      }

     //mCount.setText("1");

      mPosition.observe(mLifecycleOwner, new Observer<Integer>()
      {
        @Override
        public void onChanged(Integer integer)
        {
          mCount.setText(Integer.toString(mBloon.getNumBloons()));
        }
      });

      mCount.setOnEditorActionListener(new TextView.OnEditorActionListener()
      {
        @Override
        public boolean onEditorAction (TextView textView, int i, KeyEvent keyEvent)
        {
          String bloonCount = textView.getText().toString();

          if(bloonCount.equals(""))
          {
            return false;
          }

          mBloon.setNumBloons(Integer.parseInt(bloonCount));

          signalListener();

          return true;
        }
      });

      mFortified.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View view)
        {
          if(!mBloon.getFortified())
          {
            mBloon.setFortified(true);
          }
          else
          {
            mBloon.setFortified(false);
          }

          signalListener();
        }
      });
    }

    private void signalListener()
    {
      if(mbListener.getValue())
      {
        mbListener.setValue(false);
      }
      else
      {
        mbListener.setValue(true);
      }
    }
  }
}

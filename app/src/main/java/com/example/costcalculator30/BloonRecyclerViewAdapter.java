package com.example.costcalculator30;

import android.content.Context;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BloonRecyclerViewAdapter
    extends RecyclerView.Adapter<BloonRecyclerViewAdapter.ViewHolder>
{
  //private MutableLiveData<ArrayList<BloonItem>> mBloons;
  private ArrayList<BloonItem> mBloons;
  private Context mContext;
  private MutableLiveData<Boolean> mbListener;

  BloonRecyclerViewAdapter(ArrayList<BloonItem> bloons, Context context, MutableLiveData<Boolean> bListener)
  {
    //mBloons = new MutableLiveData<>();

    //mBloons.setValue(bloons);

    mBloons = bloons;
    mContext = context;
    mbListener = bListener;
  }

  @NonNull
  @Override
  public BloonRecyclerViewAdapter.ViewHolder onCreateViewHolder
      (@NonNull ViewGroup parent, int viewType)
  {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloon_display, parent,
        false);

    return new BloonRecyclerViewAdapter.ViewHolder(view, mContext, mbListener);
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

  /*public MutableLiveData<ArrayList<BloonItem>> getLiveData()
  {
    return mBloons;
  }*/

  public void setBloons(ArrayList<BloonItem> bloons)
  {
    mBloons = bloons;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder
  {
    private Context mContext;
    private BloonItem mBloon;

    private MutableLiveData<Boolean> mbListener;

    private ImageView mSymbol;
    private TextView mTitle;
    private EditText mCount;
    private CheckBox mFortified;
    private ImageButton mClearButton;

    public ViewHolder(@NonNull View itemView, Context context, MutableLiveData<Boolean> bListener)
    {
      super(itemView);

      mContext = context;
      mbListener = bListener;
    }

    public ImageButton getRemoveButton()
    {
      return (ImageButton) itemView.findViewById(R.id.clear_bloon_button);
    }

    public void setBloon(BloonItem bloon)
    {
      mBloon = bloon;
    }

    public void bindData()
    {
      if(mSymbol == null)
      {
        mSymbol = itemView.findViewById(R.id.bloon_symbol);
      }
      if(mTitle == null)
      {
        mTitle = itemView.findViewById(R.id.bloon_name);
      }
      if(mCount == null)
      {
        mCount = itemView.findViewById(R.id.bloon_count);
      }
      if(mFortified == null)
      {
        mFortified = itemView.findViewById(R.id.fortified_checkbox);
      }
      if(mClearButton == null)
      {
        mClearButton = itemView.findViewById(R.id.clear_bloon_button);
      }

      mTitle.setText(mBloon.getTitle());

      if(mBloon.getType().equals("Bloon") || mBloon.getType().equals("Extra"))
      {
        mFortified.setVisibility(View.INVISIBLE);
      }
      else
      {
        mFortified.setVisibility(View.VISIBLE);
      }

      mCount.setOnEditorActionListener(new TextView.OnEditorActionListener()
      {
        @Override
        public boolean onEditorAction (TextView textView, int i,
            KeyEvent keyEvent)
        {
          mBloon.setNumBloons(Integer.parseInt(textView.getText().toString()));

          signalListener();

          return false;
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

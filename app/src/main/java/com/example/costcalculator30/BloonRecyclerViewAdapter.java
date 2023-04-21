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

  public void setBloons(ArrayList<BloonItem> bloons)
  {
    mBloons = bloons;
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

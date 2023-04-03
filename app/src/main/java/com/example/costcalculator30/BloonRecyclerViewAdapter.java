package com.example.costcalculator30;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BloonRecyclerViewAdapter
    extends RecyclerView.Adapter<BloonRecyclerViewAdapter.ViewHolder>
{
  private LiveData<ArrayList<BloonItem>> mBloons;
  private Context mContext;

  BloonRecyclerViewAdapter(LiveData<ArrayList<BloonItem>> bloons, Context context)
  {
    mBloons = bloons;
    mContext = context;
  }

  @NonNull
  @Override
  public BloonRecyclerViewAdapter.ViewHolder onCreateViewHolder
      (@NonNull ViewGroup parent, int viewType)
  {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloon_display, parent,
        false);

    return new BloonRecyclerViewAdapter.ViewHolder(view, mContext);
  }

  @Override
  public void onBindViewHolder
      (@NonNull BloonRecyclerViewAdapter.ViewHolder holder, int position)
  {
    holder.setBloons(mBloons.getValue().get(position));

    holder.bindData();
  }

  @Override
  public int getItemCount()
  {
    return mBloons.getValue().size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder
  {
    private Context mContext;
    private BloonItem mBloon;

    private ImageView mSymbol;
    private TextView mTitle;
    private EditText mCount;
    private CheckBox mFortified;
    private ImageButton mClearButton;

    public ViewHolder(@NonNull View itemView, Context context)
    {
      super(itemView);

      mContext = context;
    }

    public void setBloons(BloonItem bloon)
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

      mCount.setOnEditorActionListener (new TextView.OnEditorActionListener()
      {
        @Override
        public boolean onEditorAction (TextView textView, int i,
            KeyEvent keyEvent)
        {
          mBloon.setNumBloons(Integer.parseInt(textView.getText().toString()));

          return false;
        }
      });
    }
  }
}

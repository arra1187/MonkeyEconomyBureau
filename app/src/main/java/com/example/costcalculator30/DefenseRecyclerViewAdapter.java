package com.example.costcalculator30;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DefenseRecyclerViewAdapter
        extends RecyclerView.Adapter<DefenseRecyclerViewAdapter.ViewHolder>
{
    ArrayList<Defense> mDefenses;

    public DefenseRecyclerViewAdapter(ArrayList<Defense> defenses)
    {
        mDefenses = defenses;
    }

    @NonNull
    @Override
    public DefenseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defense_display, parent,
                false);

        return new DefenseRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DefenseRecyclerViewAdapter.ViewHolder holder, int position)
    {
        holder.setDefense(mDefenses.get(position));

        holder.bindData();
    }

    @Override
    public int getItemCount()
    {
        return mDefenses.size();
    }

    public void setListData(ArrayList<Defense> defenses)
    {
        mDefenses = defenses;
        notifyItemRangeInserted(0, mDefenses.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        Defense mDefense;

        TextView mDefenseID;
        TextView mDefenseCost;
        TextView mTowerList;

        Button mDropDownButton;
        Button mLoadDefenseButton;

        ImageButton mClearDefenseButton;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }

        public void setDefense(Defense defense)
        {
            mDefense = defense;
        }

        public void bindData()
        {
            if(mDefenseID == null)
            {
                mDefenseID = (TextView) itemView.findViewById(R.id.defense_id);
            }

            if(mDefenseCost == null)
            {
                mDefenseCost = (TextView) itemView.findViewById(R.id.defense_cost);
            }

            if(mTowerList == null)
            {
                mTowerList = (TextView) itemView.findViewById(R.id.tower_list);
            }

            if(mDropDownButton == null)
            {
                mDropDownButton = (Button) itemView.findViewById(R.id.drop_down_button);
            }

            if(mLoadDefenseButton == null)
            {
                mLoadDefenseButton = (Button) itemView.findViewById(R.id.load_defense_button);
            }

            if(mClearDefenseButton == null)
            {
                mClearDefenseButton = (ImageButton) itemView.findViewById(R.id.clear_defense_button);
            }

            final int CURRENT_ID = 0;
            String cost = "$" + mDefense.getCost();
            Integer id = mDefense.getNid() - 1;
            String label = id.toString();

            if(id == CURRENT_ID)
            {
                label = "Current";
            }

            mDefenseID.setText(label);
            mDefenseCost.setText(cost);

            mDropDownButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    StringBuilder towerList = new StringBuilder();
                    boolean first = true;

                    for(Tower currentTower : mDefense.getTowers())
                    {
                        if(first)
                        {
                            first = false;
                            towerList.append(", ");
                        }

                        towerList.append(currentTower.getTitle()).append(" ").append(currentTower.getTopPath()).append(" - ").append(currentTower.getMiddlePath()).append(" - ").append(currentTower.getBottomPath());
                    }

                    mTowerList.setText(towerList.toString());
                }
            });
        }
    }
}

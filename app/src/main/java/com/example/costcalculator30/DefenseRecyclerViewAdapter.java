package com.example.costcalculator30;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DefenseRecyclerViewAdapter
        extends RecyclerView.Adapter<DefenseRecyclerViewAdapter.ViewHolder>
{
    private ArrayList<Defense> mDefenses;
    private Context mContext;

    public DefenseRecyclerViewAdapter(ArrayList<Defense> defenses, Context context)
    {
        mDefenses = defenses;
        mContext = context;
    }

    @NonNull
    @Override
    public DefenseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defense_display, parent,
                false);

        return new DefenseRecyclerViewAdapter.ViewHolder(view, mContext);
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
        private Defense mDefense;

        private TextView mDefenseID;
        private TextView mDefenseCost;
        private TextView mTowerListView;

        private Button mDropDownButton;
        private Button mLoadDefenseButton;

        private ImageButton mClearDefenseButton;

        private StringBuilder mTowerList;
        private boolean mTowerListShowing;

        private Context mContext;

        public ViewHolder(@NonNull View itemView, Context context)
        {
            super(itemView);
            mContext = context;
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
                mTowerListView = (TextView) itemView.findViewById(R.id.tower_list);
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

            mTowerList = new StringBuilder();
            boolean first = true;

            for(Tower currentTower : mDefense.getTowers())
            {
                if(first)
                {
                    first = false;
                }
                else
                {
                    mTowerList.append("\n");
                }

                mTowerList.append(currentTower.getTitle()).append(" ").append(currentTower.getTopPath()).append(" - ").append(currentTower.getMiddlePath()).append(" - ").append(currentTower.getBottomPath());
            }

            mTowerListShowing = false;

            mDropDownButton.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.asset_triangle_down, null));

            if(id == CURRENT_ID)
            {
                //mClearDefenseButton.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.clear_button_template, null));
                mClearDefenseButton.setVisibility(View.INVISIBLE);
            }

            mDropDownButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(!mTowerListShowing)
                    {
                        mTowerListView.setText(mTowerList.toString());
                        mDropDownButton.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.asset_triangle_up, null));
                        mTowerListShowing = true;
                    }
                    else
                    {
                        mTowerListView.setText("");
                        mDropDownButton.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.asset_triangle_down, null));
                        mTowerListShowing = false;
                    }

                }
            });
        }
    }
}

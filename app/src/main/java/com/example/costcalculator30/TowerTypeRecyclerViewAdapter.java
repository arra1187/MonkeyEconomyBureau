package com.example.costcalculator30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TowerTypeRecyclerViewAdapter
        extends RecyclerView.Adapter<TowerTypeRecyclerViewAdapter.ViewHolder>
{
    private ArrayList<String> mTowers;
    private Context mContext;
    private ArrayList<Integer> mTowerCosts;

    public TowerTypeRecyclerViewAdapter(ArrayList<String> towers, Context context)
    {
        mTowers = towers;
        mContext = context;
    }

    @NonNull
    @Override
    public TowerTypeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                  int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tower_type_display,
                parent, false);
        TowerTypeRecyclerViewAdapter.ViewHolder holder
                = new TowerTypeRecyclerViewAdapter.ViewHolder(view, mContext);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TowerTypeRecyclerViewAdapter.ViewHolder holder,
                                 int position)
    {
        //holder.setTower(mTowers.get(position));
        mTowers.set(position, holder.getTower());
        //mFinalCost.set(position, holder.getTowerCost())
        holder.bindData();
    }

    public void getFinalCost()
    {
        /*
        int finalCost = 0;

        for(ArrayList<Integer> mTowerCosts : towerCost)
        {
            finalCost += towerCost;
        }

        return finalCost;
        */
    }

    @Override
    public int getItemCount()
    {
        return mTowers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private String mTower;
        private Spinner mSelectTower;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView mTowersRecycler;
        private Context mContext;

        public ViewHolder(@NonNull View itemView, Context context)
        {
            super(itemView);

            mContext = context;
        }

        public void setTower(String tower)
        {
            mTower = tower;
        }

        public String getTower()
        {
            return mTower;
        }

        public int getTowerCost()
        {
            //return mAdapter.getTowerCost()
        }

        public void bindData()
        {
            ArrayAdapter<CharSequence> UpgradeAdapter
                    = ArrayAdapter.createFromResource(itemView.getContext(), R.array.towers,
                    android.R.layout.simple_spinner_item);
            UpgradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            if(mSelectTower == null)
            {
                mSelectTower = (Spinner) itemView.findViewById(R.id.target_tower_dropdown);
            }

            mSelectTower.setAdapter(UpgradeAdapter);
            mTower = mSelectTower.getSelectedItem().toString();

            mAdapter = new TowerRecyclerViewAdapter(mContext);
            mTowersRecycler.setAdapter(mAdapter);
        }
    }
}

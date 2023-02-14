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
    private UpgradeDao mUpgradeDao;

    public TowerTypeRecyclerViewAdapter(ArrayList<String> towers, Context context, UpgradeDao upgradeDao)
    {
        mTowers = towers;
        mContext = context;
        mUpgradeDao = upgradeDao;
    }

    @NonNull
    @Override
    public TowerTypeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                  int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tower_type_display,
                parent, false);
        TowerTypeRecyclerViewAdapter.ViewHolder holder
                = new TowerTypeRecyclerViewAdapter.ViewHolder(view, mContext, mUpgradeDao);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TowerTypeRecyclerViewAdapter.ViewHolder holder,
                                 int position)
    {
        //holder.setTower(mTowers.get(position));
        mTowers.set(position, holder.getTower());
        //mTowerCosts.set(position, holder.getTowerCost());
        holder.bindData();
    }

    public int getFinalCost()
    {
        int finalCost = 0;

        for(int cost : mTowerCosts)
        {
            finalCost += cost;
        }

        return finalCost;
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
        //private RecyclerView.Adapter mAdapter;
        private TowerRecyclerViewAdapter mAdapter;
        private RecyclerView mTowersRecycler;
        private Context mContext;
        private UpgradeDao mUpgradeDao;

        public ViewHolder(@NonNull View itemView, Context context, UpgradeDao upgradeDao)
        {
            super(itemView);

            mContext = context;
            mUpgradeDao = upgradeDao;
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
            //return mAdapter.getTowerCost();

            return 1;
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

            mAdapter = new TowerRecyclerViewAdapter(mContext, mUpgradeDao);
            //mTowersRecycler.setAdapter(mAdapter);
        }
    }
}

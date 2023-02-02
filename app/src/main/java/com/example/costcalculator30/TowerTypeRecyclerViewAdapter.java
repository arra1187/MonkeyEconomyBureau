package com.example.costcalculator30;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TowerTypeRecyclerViewAdapter
        extends RecyclerView.Adapter<TowerTypeRecyclerViewAdapter.ViewHolder>
{
    private ArrayList<String> mTowers;

    public TowerTypeRecyclerViewAdapter(ArrayList<String> towers)
    {
        mTowers = towers;
    }

    @NonNull
    @Override
    public TowerTypeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                  int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tower_type_display,
                parent, false);
        TowerTypeRecyclerViewAdapter.ViewHolder holder
                = new TowerTypeRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TowerTypeRecyclerViewAdapter.ViewHolder holder,
                                 int position)
    {
        holder.setTower(mTowers.get(position));
        holder.bindData();
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
        private RecyclerView mRVTowers;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mAdapter = new TowerRecyclerViewAdapter();
            mRVTowers.setAdapter(mAdapter);
        }

        public void setTower(String tower)
        {
            mTower = tower;
        }

        public String getTower()
        {
            return mTower;
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
        }
    }
}
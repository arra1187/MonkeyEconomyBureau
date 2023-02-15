package com.example.costcalculator30;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TowerRecyclerViewAdapter
        extends RecyclerView.Adapter<TowerRecyclerViewAdapter.ViewHolder>
{
    private ArrayList<Tower> mTowers;
    String mTitle;
    Context mContext;
    UpgradeDao mUpgradeDao;

    public TowerRecyclerViewAdapter(ArrayList<Tower> towers, Context context, UpgradeDao upgradeDao)
    {
        mTowers = towers;
        mContext = context;
        mUpgradeDao = upgradeDao;
        mTitle = "";
    }

    @NonNull
    @Override
    public TowerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tower_display, parent,
                false);


        return new ViewHolder(view, mContext, mTitle, mUpgradeDao);
    }

    @Override
    public void onBindViewHolder(@NonNull TowerRecyclerViewAdapter.ViewHolder holder,
                                 int position)
    {
        holder.setTower(mTowers.get(position));
        //mTowers.set(position, holder.getTower());
        holder.setTitle(mTitle);
        holder.bindData();
    }

    @Override
    public int getItemCount()
    {
        return mTowers.size();
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private Tower mTower;
        private String mTitle;
        private UpgradeDao mUpgradeDao;

        private Spinner mTopPath;
        private Spinner mMiddlePath;
        private Spinner mBottomPath;

        private Button mDiscountButton;
        private ImageButton mRemoveTowerButton;

        private Context mContext;

        public ViewHolder(@NonNull View itemView, Context context, String title, UpgradeDao upgradeDao)
        {
            super(itemView);

            mContext = context;
            mTitle = title;
            mUpgradeDao = upgradeDao;
            mTower = new Tower(title, mUpgradeDao);
        }

        public void setTower(Tower tower)
        {
            mTower = tower;
        }

        public void setTitle(String title)
        {
            mTitle = title;
        }

        public Tower getTower()
        {
            return mTower;
        }

        public void bindData()
        {
            ArrayAdapter<CharSequence> UpgradeAdapter
                    = ArrayAdapter.createFromResource(itemView.getContext(), R.array.upgrades,
                    android.R.layout.simple_spinner_item);
            UpgradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            if(mTopPath == null)
            {
                mTopPath = (Spinner) itemView.findViewById(R.id.top_path);
            }

            if(mMiddlePath == null)
            {
                mMiddlePath = (Spinner) itemView.findViewById(R.id.middle_path);
            }

            if(mBottomPath == null)
            {
                mBottomPath = (Spinner) itemView.findViewById(R.id.bottom_path);
            }

            if(mDiscountButton == null)
            {
                mDiscountButton = (Button) itemView.findViewById(R.id.tower_tab_button);
            }

            if(mRemoveTowerButton == null)
            {
                mRemoveTowerButton = (ImageButton) itemView.findViewById(R.id.remove_tower_button);
            }

            mTopPath.setAdapter(UpgradeAdapter);
            mMiddlePath.setAdapter(UpgradeAdapter);
            mBottomPath.setAdapter(UpgradeAdapter);

            mTower.setUpgrades(Integer.parseInt(mTopPath.getSelectedItem().toString()),
                               Integer.parseInt(mMiddlePath.getSelectedItem().toString()),
                               Integer.parseInt(mBottomPath.getSelectedItem().toString()));

            mDiscountButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    //PopupMenu discountPopup = new PopupMenu(itemView.getContext(), mDiscountButton);
                    //discountPopup.getMenuInflater().inflate(R.layout.discount_popup, discountPopup.getMenu());

                    //Pass in context from the first fragment

                    LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View discountPopup = layoutInflater.inflate(R.layout.discount_popup, null);

                }
            });

            mRemoveTowerButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}

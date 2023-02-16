package com.example.costcalculator30;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TowerRecyclerViewAdapter
        extends RecyclerView.Adapter<TowerRecyclerViewAdapter.ViewHolder>
{
    private ArrayList<Tower> mTowers;
    Context mContext;
    UpgradeDao mUpgradeDao;

    public TowerRecyclerViewAdapter(ArrayList<Tower> towers, Context context, UpgradeDao upgradeDao)
    {
        mTowers = towers;
        mContext = context;
        mUpgradeDao = upgradeDao;
    }

    @NonNull
    @Override
    public TowerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_tower_display, parent,
                false);


        return new ViewHolder(view, mContext, mUpgradeDao);
    }

    @Override
    public void onBindViewHolder(@NonNull TowerRecyclerViewAdapter.ViewHolder holder,
                                 int position)
    {
        holder.setTower(mTowers.get(position));
        //mTowers.set(position, holder.getTower());

        holder.getRemoveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTowers.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        holder.getTopPath().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int newTopPath = Integer.parseInt(parent.getItemAtPosition(position).toString());

                mTowers.get(holder.getAdapterPosition()).setTopPath(newTopPath);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        holder.getMiddlePath().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int newMiddlePath = Integer.parseInt(parent.getItemAtPosition(position).toString());

                mTowers.get(holder.getAdapterPosition()).setMiddlePath(newMiddlePath);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        holder.getBottomPath().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int newBottomPath = Integer.parseInt(parent.getItemAtPosition(position).toString());

                mTowers.get(holder.getAdapterPosition()).setBottomPath(newBottomPath);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        holder.bindData();
    }

    @Override
    public int getItemCount()
    {
        return mTowers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private Tower mTower;
        private String mTitle;
        private UpgradeDao mUpgradeDao;

        private Button mDiscountButton;

        private ArrayAdapter<CharSequence> mUpgradeAdapter;

        private Context mContext;

        public ViewHolder(@NonNull View itemView, Context context, UpgradeDao upgradeDao)
        {
            super(itemView);

            mContext = context;
            mUpgradeDao = upgradeDao;

            mUpgradeAdapter = ArrayAdapter.createFromResource(itemView.getContext(), R.array.upgrades,
                android.R.layout.simple_spinner_item);
            mUpgradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
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

        public ImageButton getRemoveButton()
        {
            return (ImageButton) itemView.findViewById(R.id.remove_tower_button);
        }

        public Spinner getTopPath()
        {
            Spinner topPath = (Spinner) itemView.findViewById(R.id.top_path);

            topPath.setAdapter(mUpgradeAdapter);

            return topPath;
        }

        public Spinner getMiddlePath()
        {
            Spinner middlePath = (Spinner) itemView.findViewById(R.id.middle_path);

            middlePath.setAdapter(mUpgradeAdapter);

            return middlePath;
        }

        public Spinner getBottomPath()
        {
            Spinner bottomPath = (Spinner) itemView.findViewById(R.id.bottom_path);

            bottomPath.setAdapter(mUpgradeAdapter);

            return bottomPath;
        }

        public void bindData()
        {
            if(mDiscountButton == null)
            {
                mDiscountButton = (Button) itemView.findViewById(R.id.tower_tab_button);
            }

            //mTower.setUpgrades(Integer.parseInt(mTopPath.getSelectedItem().toString()),
                               //Integer.parseInt(mMiddlePath.getSelectedItem().toString()),
                               //Integer.parseInt(mBottomPath.getSelectedItem().toString()));

            mDiscountButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //PopupMenu discountPopup = new PopupMenu(itemView.getContext(), mDiscountButton);
                    //discountPopup.getMenuInflater().inflate(R.layout.discount_popup, discountPopup.getMenu());

                    LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View discountPopup = layoutInflater.inflate(R.layout.discount_popup, null);

                }
            });
        }
    }
}

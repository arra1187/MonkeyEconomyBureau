package com.example.costcalculator30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TowerRecyclerViewAdapter
        extends RecyclerView.Adapter<TowerRecyclerViewAdapter.ViewHolder>
{
    private final Context mContext;
    private final UpgradeDao mUpgradeDao;
    //private final DefenseDao mDefenseDao;
    private final Executor mExecutor;

    public TowerRecyclerViewAdapter(Context context)
    {
        mContext = context;

        DatabaseRepository repository = new DatabaseRepository();
        mUpgradeDao = repository.getUpgradeDao(context);
        //mDefenseDao = repository.getDefenseDao(context);

        mExecutor = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public TowerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tower_display, parent,
                false);


        return new ViewHolder(view, mContext, mUpgradeDao);
    }

    @Override
    public void onBindViewHolder(@NonNull TowerRecyclerViewAdapter.ViewHolder holder,
                                 int position)
    {
        holder.setTower(ConnectTowerList.getTowers().get(position));

        /*int topPath = holder.getTower().getTopPath();

        holder.getTopPath().setSelection(topPath);
        holder.getMiddlePath().setSelection(holder.getTower().getMiddlePath());
        holder.getBottomPath().setSelection(holder.getTower().getBottomPath());*/

        holder.getRemoveButton().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ConnectTowerList.removeTower(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

                updateDatabase();
            }
        });

        holder.getTopPath().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int newTopPath = Integer.parseInt(parent.getItemAtPosition(position).toString());
                ArrayList<Tower> towers = ConnectTowerList.getTowers();
                towers.get(holder.getAdapterPosition()).setTopPath(newTopPath);

                ConnectTowerList.setTowers(towers);

                updateDatabase();
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

                ArrayList<Tower> towers = ConnectTowerList.getTowers();
                towers.get(holder.getAdapterPosition()).setMiddlePath(newMiddlePath);

                ConnectTowerList.setTowers(towers);

                updateDatabase();
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

                ArrayList<Tower> towers = ConnectTowerList.getTowers();
                towers.get(holder.getAdapterPosition()).setBottomPath(newBottomPath);

                ConnectTowerList.setTowers(towers);

                updateDatabase();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        holder.bindData();
    }

    public void updateDatabase()
    {
        mExecutor.execute(() ->
        {
            //mDefenseDao.setTowers(ConnectTowerList.getTowers(), 0);
        });
    }

    @Override
    public int getItemCount()
    {
        return ConnectTowerList.getTowers().size();
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
        private ImageView mTowerSymbol;

        private ArrayAdapter<CharSequence> mUpgradeAdapter;

        private Context mContext;

        @SuppressLint("UseCompatLoadingForDrawables")
        public ViewHolder(@NonNull View itemView, Context context, UpgradeDao upgradeDao)
        {
            super(itemView);

            mContext = context;
            mUpgradeDao = upgradeDao;

            mUpgradeAdapter = ArrayAdapter.createFromResource(itemView.getContext(), R.array.upgrades,
                android.R.layout.simple_spinner_item);
            mUpgradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            mTopPath = (Spinner) itemView.findViewById(R.id.top_path);
            mTopPath.setAdapter(mUpgradeAdapter);

            mMiddlePath = (Spinner) itemView.findViewById(R.id.middle_path);
            mMiddlePath.setAdapter(mUpgradeAdapter);

            mBottomPath = (Spinner) itemView.findViewById(R.id.bottom_path);
            mBottomPath.setAdapter(mUpgradeAdapter);
        }

        public void setTower(Tower tower)
        {
            mTower = tower;

            Spinner topPath = (Spinner) itemView.findViewById(R.id.top_path);

            mUpgradeAdapter = ArrayAdapter.createFromResource(itemView.getContext(), R.array.upgrades,
                    android.R.layout.simple_spinner_item);
            mUpgradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            topPath.setAdapter(mUpgradeAdapter);

            int value = tower.getTopPath();

            topPath.setSelection(value);
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

        @SuppressLint("UseCompatLoadingForDrawables")
        public void bindData()
        {
            Drawable towerSymbol = null;

            mTopPath.setSelection(mTower.getTopPath());
            mMiddlePath.setSelection(mTower.getMiddlePath());
            mBottomPath.setSelection(mTower.getBottomPath());

            if(mDiscountButton == null)
            {
                mDiscountButton = (Button) itemView.findViewById(R.id.tower_tab_button);
            }
            if(mTowerSymbol == null)
            {
                mTowerSymbol = (ImageView) itemView.findViewById(R.id.tower_symbol);
            }

            switch(mTower.getTitle())
            {
                case "Dart Monkey":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.new_dart_monkey_symbol);
                    break;
                case "Boomerang Monkey":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.boomerang_monkey_symbol);
                    break;
                case "Bomb Shooter":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.bomb_shooter_symbol);
                    break;
                case "Tack Shooter":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.tack_shooter_symbol);
                    break;
            }

            mTowerSymbol.setImageDrawable(towerSymbol);

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

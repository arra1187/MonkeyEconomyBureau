package com.example.costcalculator30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
    private final DefenseViewModel mDefenseViewModel;
    private final Executor mExecutor;

    public TowerRecyclerViewAdapter(Context context, DefenseViewModel defenseViewModel)
    {
        mContext = context;

        DatabaseRepository repository = new DatabaseRepository();
        mUpgradeDao = repository.getUpgradeDao(context);
        mDefenseViewModel = defenseViewModel;

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

        holder.getTowerCountView().setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
            {
                ArrayList<Tower> towers = ConnectTowerList.getTowers();
                towers.get(holder.getAdapterPosition()).setNumTowers(Integer.parseInt(textView.getText().toString()));

                ConnectTowerList.setTowers(towers);

                updateDatabase();

                return false;
            }
        });

        holder.bindData();
    }

    public void updateDatabase()
    {
        mExecutor.execute(() ->
        {
            mDefenseViewModel.setTowers(ConnectTowerList.getTowers(), 1);
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
        private EditText mTowerCountView;

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

        public EditText getTowerCountView()
        {
            return (EditText) itemView.findViewById(R.id.tower_count);
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
            if(mTowerCountView == null)
            {
                mTowerCountView = (EditText) itemView.findViewById(R.id.tower_count);
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
                case "Ice Monkey":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.ice_monkey_symbol);
                    break;
                case "Glue Gunner":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.glue_gunner_symbol);
                    break;
                case "Sniper Monkey":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.sniper_monkey_symbol);
                    break;
                case "Monkey Buccaneer":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.monkey_bucaneer_symbol);
                    break;
                case "Monkey Sub":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.monkey_sub_symbol);
                    break;
                case "Monkey Ace":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.monkey_ace_symbol);
                    break;
                case "Heli Pilot":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.heli_pilot_symbol);
                    break;
                case "Mortar Monkey":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.mortar_monkey_symbol);
                    break;
                case "Dartling Gunner":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.dartling_gunner_symbol);
                    break;
                case "Wizard Monkey":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.wizard_monkey_symbol);
                    break;
                case "Super Monkey":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.super_monkey_symbol);
                    break;
                case "Ninja Monkey":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.ninja_monkey_symbol);
                    break;
                case "Alchemist":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.alchemist_symbol);
                    break;
                case "Druid":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.druid_symbol);
                    break;
                case "Banana Farm":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.banana_farm_symbol);
                    break;
                case "Spike Factory":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.spike_factory_symbol);
                    break;
                case "Monkey Village":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.monkey_village_symbol);
                    break;
                case "Engineer Monkey":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.engineer_monkey_symbol);
                    break;
                case "Beast Handler":
                    towerSymbol = mContext.getResources().getDrawable(R.drawable.beast_handler_symbol);
                    break;
            }

            mTowerSymbol.setImageDrawable(towerSymbol);
            mTowerCountView.setText(String.format("%d", mTower.getNumTowers()));
            mTowerCountView.setSelected(false);
        }
    }
}

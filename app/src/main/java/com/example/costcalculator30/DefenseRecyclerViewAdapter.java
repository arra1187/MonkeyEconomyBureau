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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DefenseRecyclerViewAdapter
        extends RecyclerView.Adapter<DefenseRecyclerViewAdapter.ViewHolder>
{
    private DefenseViewModel mDefenseViewModel;
    private ArrayList<Defense> mDefenses;
    private Context mContext;
    private Executor mExecutor;

    private int mItemCount;
    private Boolean mLock;

    private ThreadRepository threadRepository;

    private LifecycleOwner mLifecycleOwner;

    public DefenseRecyclerViewAdapter(DefenseViewModel defenseViewModel, ArrayList<Defense> defenses, Context context, LifecycleOwner lifecycleOwner)
    {
        mDefenseViewModel = defenseViewModel;
        mDefenses = defenses;
        mContext = context;

        mLock = false;

        mExecutor = Executors.newSingleThreadExecutor();
        mLifecycleOwner = lifecycleOwner;
        threadRepository = new ThreadRepository();
    }

    @NonNull
    @Override
    public DefenseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defense_display, parent,
                false);

        return new DefenseRecyclerViewAdapter.ViewHolder(view, mContext, mLifecycleOwner);
    }

    @Override
    public void onBindViewHolder(@NonNull DefenseRecyclerViewAdapter.ViewHolder holder, int position)
    {
        mExecutor.execute(() ->
        {
            holder.setDefense(mDefenseViewModel.getAllData().get(position));

            holder.bindData();
        });

        holder.getClearDefenseButton().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mExecutor.execute(() ->
                {
                    mDefenseViewModel.deleteItem(holder.mDefense);
                });
            }
        });
    }

    @Override
    public int getItemCount()
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //mLock = true;

        executorService.execute(() ->
        {
            mItemCount = mDefenseViewModel.getSize();
            //mLock = false;
        });

        //while(mLock = true);

        //mExecutor.join();

        executorService.shutdown();

        while(!executorService.isTerminated())
        {
            try
            {
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            }
            catch(InterruptedException ignored)
            {

            }
        }

        return mDefenseViewModel.getAllLiveData().getValue().size();
    }

    public void setListData(ArrayList<Defense> defenses)
    {
        mDefenses = defenses;
        notifyItemRangeInserted(0, mDefenses.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private MutableLiveData<Integer> mPosition;
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

        public ViewHolder(@NonNull View itemView, Context context, LifecycleOwner lifecycleOwner)
        {
            super(itemView);

            mPosition = new MutableLiveData<>(getAdapterPosition());

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

            mPosition.observe(lifecycleOwner, new Observer<Integer>()
            {
                @Override
                public void onChanged(Integer integer)
                {
                    setLabel();
                }
            });

            mContext = context;
        }

        public ImageButton getClearDefenseButton()
        {
            return (ImageButton) itemView.findViewById(R.id.clear_defense_button);
        }

        public void setDefense(Defense defense)
        {
            mDefense = defense;
        }

        public void setLabel()
        {
            final int CURRENT_ID = 0;
            Integer id = getAdapterPosition();
            String label = id.toString();

            if(id == CURRENT_ID)
            {
                label = "Current";
                mClearDefenseButton.setVisibility(View.INVISIBLE);
            }

            mDefenseID.setText(label);
        }

        public void bindData()
        {
            String cost = "$" + mDefense.getCost();
            mDefenseCost.setText(cost);

            setLabel();

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

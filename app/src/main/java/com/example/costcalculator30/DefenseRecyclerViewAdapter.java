package com.example.costcalculator30;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
    //private ArrayList<Defense> mDefenses;

    private Utility mUtility;
    private final DefenseViewModel mDefenseViewModel;
    private final Context mContext;

    private ExecutorService mDatabaseExecutor;

    private int mItemCount;

    private final LifecycleOwner mLifecycleOwner;
    private final FragmentManager mFragmentManager;

    public DefenseRecyclerViewAdapter(DefenseViewModel defenseViewModel, ArrayList<Defense> defenses, Context context, LifecycleOwner lifecycleOwner, FragmentManager fragmentManager)
    {
        mUtility = new Utility();
        mDefenseViewModel = defenseViewModel;
        //mDefenses = defenses;
        mContext = context;
        mLifecycleOwner = lifecycleOwner;
        mFragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public DefenseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defense_display, parent,
                false);

        return new DefenseRecyclerViewAdapter.ViewHolder(view, mContext, mLifecycleOwner, mDefenseViewModel, mFragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull DefenseRecyclerViewAdapter.ViewHolder holder, int position)
    {
        mDatabaseExecutor = newSingleThreadExecutor();

        mDatabaseExecutor.execute(() ->
        {
            holder.setDefense(mDefenseViewModel.getAllData().get(position));
        });

        mUtility.joinExecutor(mDatabaseExecutor);

        holder.getClearDefenseButton().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                holder.closeListView();

                mDatabaseExecutor = newSingleThreadExecutor();

                mDatabaseExecutor.execute(() ->
                {
                    mDefenseViewModel.deleteItem(holder.mDefense);
                });

                mUtility.joinExecutor(mDatabaseExecutor);
            }
        });

        holder.bindData();
    }

    @Override
    public int getItemCount()
    {
        mDatabaseExecutor = newSingleThreadExecutor();

        mDatabaseExecutor.execute(() ->
        {
            mItemCount = mDefenseViewModel.getSize();
        });

        mUtility.joinExecutor(mDatabaseExecutor);

        return mItemCount;
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
        private DefenseViewModel mDefenseViewModel;
        private Utility mUtility;
        private FragmentManager mFragmentManager;
        private Executor mExecutor;

        public ViewHolder(@NonNull View itemView, Context context, LifecycleOwner lifecycleOwner, DefenseViewModel defenseViewModel, FragmentManager fragmentManager)
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
            mDefenseViewModel = defenseViewModel;
            mUtility = new Utility();
            mFragmentManager = fragmentManager;
            mExecutor = newSingleThreadExecutor();
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
            Integer id = getAdapterPosition() + 1;
            String label = id.toString();

            mDefenseID.setText(label);
        }

        public void closeListView()
        {
            mTowerListView.setText("");
        }

        public void bindData()
        {
            String cost = "$" + mDefense.getCost();
            mDefenseCost.setText(cost);

            setLabel();

            mTowerList = mUtility.setTowerList(mDefense);

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

            mLoadDefenseButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mExecutor.execute(() ->
                    {
                        mDefenseViewModel.setTowers(mDefense.getTowers(), 1);
                        mDefenseViewModel.setDifficulty(mDefense.getDifficulty(), 1);
                        mDefenseViewModel.setCost(mDefense.getCost(), 1);

                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.page_frame, new CostCalculator());
                        transaction.commit();
                    });
                }
            });
        }
    }
}

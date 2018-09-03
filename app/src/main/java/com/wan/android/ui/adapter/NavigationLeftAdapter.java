package com.wan.android.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wan.android.R;
import com.wan.android.ui.navigation.OnRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wzc
 * @date 2018/8/27
 */
public class NavigationLeftAdapter
        extends RecyclerView.Adapter<NavigationLeftAdapter.NavigationLeftHolder> {
    private List<String> mData = new ArrayList<>();
    private OnRecycleViewItemClickListener mOnRecycleViewItemClickListener;
    private int mCheckedPosition;

    public NavigationLeftAdapter() {
    }

    public void setData(List<String> list) {
        mData = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NavigationLeftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.navigation_left_recycle_item, parent, false);
        return new NavigationLeftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationLeftHolder holder, int position) {
        holder.bindItem(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener l) {
        mOnRecycleViewItemClickListener = l;
    }

    public void setCheckedPosition(int position) {
        mCheckedPosition = position;
        notifyDataSetChanged();
    }

    class NavigationLeftHolder extends RecyclerView.ViewHolder {

        private final TextView mTvLeftTitle;

        public NavigationLeftHolder(View itemView) {
            super(itemView);
            mTvLeftTitle = itemView.findViewById(R.id.tv_navigation_left_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecycleViewItemClickListener != null) {
                        mOnRecycleViewItemClickListener.onRecyclerViewItemClicked(getAdapterPosition());
                    }
                }
            });
        }

        public void bindItem(String title) {
            mTvLeftTitle.setText(title);
            if (getAdapterPosition() == mCheckedPosition) {
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.color_background));
            } else {
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.color_f0));
            }
        }
    }
}

package com.wan.android.ui.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wan.android.R;
import com.wan.android.ui.adapter.NavigationLeftAdapter;
import com.wan.android.ui.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wzc
 * @date 2018/8/24
 */
public class NavigationLeftFragment extends BaseFragment implements OnRecycleViewItemClickListener {

    private NavigationLeftAdapter mAdapter;
    private NavigationFragment mNavigationFragment;
    private LinearLayoutManager mLinearLayoutManager;

    public static NavigationLeftFragment newInstance() {
        Bundle args = new Bundle();
        NavigationLeftFragment fragment = new NavigationLeftFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigationFragment = (NavigationFragment) getParentFragment();
    }

    @BindView(R.id.recylcerview_navigation_left_fragment)
    RecyclerView mRecyclerViewLeft;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_left_fragment, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    protected void setUp(View view) {
        mAdapter = new NavigationLeftAdapter();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewLeft.setLayoutManager(mLinearLayoutManager);
        mRecyclerViewLeft.setAdapter(mAdapter);
    }

    public void setData(List<String> titles) {
        mAdapter.setOnRecycleViewItemClickListener(this);
        mAdapter.setData(titles);
    }

    @Override
    public void onRecyclerViewItemClicked(int position) {
        setCheckedPosition(position);
        if (mNavigationFragment != null) {
            mNavigationFragment.onNavigationLeftItemClicked(position);
        }
    }

    public void setCheckedPosition(int position) {
        mAdapter.setCheckedPosition(position);
        // 将当前选中的 item 居中
        View childAt = mRecyclerViewLeft.getChildAt(position - mLinearLayoutManager.findFirstVisibleItemPosition());
        if (childAt != null) {
            int offset = childAt.getTop() - mRecyclerViewLeft.getHeight() / 2;
            mRecyclerViewLeft.smoothScrollBy(0, offset);
        }
    }

    @Override
    public void scrollToTop() {
        mAdapter.setCheckedPosition(0);
        mRecyclerViewLeft.smoothScrollToPosition(0);
    }
}

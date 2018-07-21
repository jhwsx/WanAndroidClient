package com.wan.android.tree;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wan.android.R;
import com.wan.android.adapter.TreeAdapter;
import com.wan.android.base.BaseListFragment;
import com.wan.android.branch.BranchActivity;
import com.wan.android.data.bean.BranchData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class TreeFragment extends BaseListFragment implements TreeContract.View {

    private static final String TAG = TreeFragment.class.getSimpleName();
    private TreeAdapter mTreeAdapter;
    private List<BranchData> mDatasList = new ArrayList<>();
    private TreeContract.Presenter mTreePresenter;

    @Override
    public void onResume() {
        super.onResume();
        mTreePresenter.start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // fixme
        mTreePresenter = new TreePresenter(this);
        initAdapter();
        initRefreshLayout();
        swipeRefresh();
    }

    private void initAdapter() {
        mTreeAdapter = new TreeAdapter(R.layout.tree_recycle_view, mDatasList);
        mTreeAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mTreeAdapter);
        mTreeAdapter.setEnableLoadMore(false);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                BranchData data = mDatasList.get(position);
                String title = data.getName();
                ArrayList<BranchData.Leaf> children = data.getChildren();
                BranchActivity.start(mActivity, title, title, children);
            }
        });
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh();
            }
        });
    }

    @Override
    protected void swipeRefresh() {
        Log.d("haha", "swipeRefresh: mTreePresenter = " + mTreePresenter);
        mTreePresenter.swipeRefresh();
    }

    @Override
    public void showSwipeRefreshSuccess(List<BranchData> data) {
        mDatasList.clear();
        mDatasList.addAll(data);
        mTreeAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showSuccess();
    }

    @Override
    public void showSwipeRefreshFail(CommonException e) {
        Toast.makeText(Utils.getApp(), e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(TreeContract.Presenter presenter) {
        // fixme
//        mTreePresenter = checkNotNull(presenter);
    }
}

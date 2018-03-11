package com.wan.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wan.android.R;
import com.wan.android.activity.BranchActivity;
import com.wan.android.adapter.TreeAdapter;
import com.wan.android.bean.TreeListResponse;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.client.TreeListClient;
import com.wan.android.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/2/1
 */
public class TreeFragment extends BaseListFragment {
    private static final String TAG = TreeFragment.class.getSimpleName();
    private TreeAdapter mTreeAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        initRefreshLayout();
        refresh();
    }

    @Override
    protected void refresh() {
        TreeListClient client = RetrofitClient.create(TreeListClient.class);
        Call<TreeListResponse> call = client.getTree();
        call.enqueue(new Callback<TreeListResponse>() {
            @Override
            public void onResponse(Call<TreeListResponse> call, Response<TreeListResponse> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                mLoadService.showSuccess();

                TreeListResponse body = response.body();
                List<TreeListResponse.Data> data = body.getData();
                mDatasList.clear();
                mDatasList.addAll(data);
                mTreeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TreeListResponse> call, Throwable t) {

                mSwipeRefreshLayout.setRefreshing(false);
                mLoadService.showCallback(EmptyCallback.class);
                mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view,R.id.ll_error, R.id.ll_empty,R.id.ll_loading);

            }
        });
    }
    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }
    private List<TreeListResponse.Data> mDatasList = new ArrayList<>();

    private void initAdapter() {
        mTreeAdapter = new TreeAdapter(R.layout.tree_item_view, mDatasList);
        mTreeAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mTreeAdapter);
        mTreeAdapter.setEnableLoadMore(false);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                TreeListResponse.Data data = mDatasList.get(position);
                String title = data.getName();
                ArrayList<TreeListResponse.Data.Children> children = data.getChildren();
                BranchActivity.start(mActivity, title, children);
            }
        });
    }

}

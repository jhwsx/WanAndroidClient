package com.wan.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.callback.LoadingCallback;

/**
 * @author wzc
 * @date 2018/3/10
 */
public abstract class BaseListFragment extends BaseFragment {
    protected LoadService mLoadService;
    protected RecyclerView mRecyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 获取RecyclerView布局
        View recyclerView = LayoutInflater.from(getContext()).inflate(R.layout.recycler_view, null);
        // 获取LoadService,把RecyclerView添加进去
        mLoadService = LoadSir.getDefault().register(recyclerView, new com.kingja.loadsir.callback.Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
                refresh();
            }
        });

        // 把状态管理页面添加到根布局中
        mRootView.addView(mLoadService.getLoadLayout(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // 设置可下拉刷新的子view
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
        mRecyclerView = (RecyclerView) recyclerView.findViewById(R.id.recyclerview_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
    }

    protected abstract void refresh();
}

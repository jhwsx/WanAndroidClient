package com.wan.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wan.android.R;
import com.wan.android.view.MultiSwipeRefreshLayout;

/**
 * @author wzc
 * @date 2018/2/1
 */
public abstract class BaseFragment extends Fragment {
    protected MultiSwipeRefreshLayout mRootView;
    protected MultiSwipeRefreshLayout mSwipeRefreshLayout;
    protected Activity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取根布局
        mRootView = (MultiSwipeRefreshLayout) inflater.inflate(R.layout.fragment_common, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout_fragment_common);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
    }
}

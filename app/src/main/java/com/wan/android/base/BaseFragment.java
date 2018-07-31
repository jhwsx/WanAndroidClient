package com.wan.android.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wan.android.R;
import com.wan.android.util.NightModeUtils;
import com.wan.android.view.MultiSwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment 基类
 * @author wzc
 * @date 2018/2/1
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    @BindView(R.id.swipe_refresh_layout)
    protected MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setColorSchemeResources(
                NightModeUtils.isNightMode()
                        ? new int[]{R.color.colorPrimaryDark_night, R.color.colorPrimary_night}
                        : new int[]{R.color.colorPrimaryDark, R.color.colorPrimary});
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();

    }

    public Integer resetCurrPage() {
        return 1;
    }
}

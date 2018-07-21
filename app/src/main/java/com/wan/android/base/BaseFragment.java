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

/**
 * @author wzc
 * @date 2018/2/1
 */
public abstract class BaseFragment extends Fragment {
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
        return inflater.inflate(R.layout.base_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(
                NightModeUtils.isNightMode()
                        ? new int[]{R.color.colorPrimaryDark_night, R.color.colorPrimary_night}
                        : new int[]{R.color.colorPrimaryDark, R.color.colorPrimary});
    }

    public Integer resetCurrPage() {
        return 1;
    }
}

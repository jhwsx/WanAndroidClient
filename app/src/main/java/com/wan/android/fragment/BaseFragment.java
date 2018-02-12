package com.wan.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * @author wzc
 * @date 2018/2/1
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
}

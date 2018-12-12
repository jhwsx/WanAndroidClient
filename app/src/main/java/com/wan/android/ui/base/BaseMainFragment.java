package com.wan.android.ui.base;

import com.umeng.analytics.MobclickAgent;

/**
 * @author wzc
 * @date 2018/12/12
 */
public abstract class BaseMainFragment extends BaseFragment {
    private boolean mFirstInit = true;
    private boolean mVisible = false;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mVisible = false;
            MobclickAgent.onPageEnd(getFragmentName());
        } else {
            mVisible = true;
            MobclickAgent.onPageStart(getFragmentName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFirstInit) {
            mFirstInit = false;
            mVisible = true;
            MobclickAgent.onPageStart(getFragmentName());
            return;
        }
        if (mVisible) {
            MobclickAgent.onPageStart(getFragmentName());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVisible) {
            MobclickAgent.onPageEnd(getFragmentName());
        }
    }

    @Override
    protected boolean hasChildFragment() {
        return true;
    }
}

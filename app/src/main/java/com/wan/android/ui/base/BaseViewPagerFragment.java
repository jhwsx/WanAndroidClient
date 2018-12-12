package com.wan.android.ui.base;

import com.umeng.analytics.MobclickAgent;

import timber.log.Timber;

/**
 * @author wzc
 * @date 2018/12/12
 */
public abstract class BaseViewPagerFragment extends BaseFragment {
    private boolean mVisible;
    private boolean hasStarted;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Timber.d("setUserVisibleHint: %s, %s", isVisibleToUser, this);
        mVisible = isVisibleToUser;
        if (isVisibleToUser) {
            hasStarted = true;
            MobclickAgent.onPageStart(getFragmentName());
        } else {
            if (hasStarted) {
                hasStarted = false;
                MobclickAgent.onPageEnd(getFragmentName());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVisible && !hasStarted) {
            MobclickAgent.onPageStart(getFragmentName());

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVisible) {
            hasStarted = false;
            MobclickAgent.onPageEnd(getFragmentName());
        }
    }

    @Override
    protected boolean hasChildFragment() {
        return true;
    }
}

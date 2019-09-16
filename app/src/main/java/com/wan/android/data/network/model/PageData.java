package com.wan.android.data.network.model;


import androidx.fragment.app.Fragment;

/**
 * ViewPager 页面数据
 *
 * @author wzc
 * @date 2018/8/23
 */
public class PageData {
    private String mTitle;
    private Fragment mFragment;

    public PageData(String title, Fragment fragment) {
        mTitle = title;
        mFragment = fragment;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }
}

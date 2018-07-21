package com.wan.android.data.bean;

import android.support.v4.app.Fragment;

/**
 * viewpage页面模型类
 *
 * @author wzc
 * @date 2018/2/12
 */
public class PageModel {

    private String mTabTitle;

    private Fragment mBranchFragment;

    public PageModel(String tabTitle, Fragment branchFragment) {
        mTabTitle = tabTitle;
        mBranchFragment = branchFragment;
    }

    public String getTabTitle() {
        return mTabTitle;
    }

    public void setTabTitle(String tabTitle) {
        mTabTitle = tabTitle;
    }

    public Fragment getFragment() {
        return mBranchFragment;
    }

    public void setBranchFragment(Fragment branchFragment) {
        mBranchFragment = branchFragment;
    }
}

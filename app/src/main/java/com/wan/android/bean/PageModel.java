package com.wan.android.bean;

import com.wan.android.fragment.BranchFragment;

/**
 * viewpage页面模型类
 *
 * @author wzc
 * @date 2018/2/12
 */
public class PageModel {

    private String mTabTitle;

    private BranchFragment mBranchFragment;

    public PageModel(String tabTitle, BranchFragment branchFragment) {
        mTabTitle = tabTitle;
        mBranchFragment = branchFragment;
    }

    public String getTabTitle() {
        return mTabTitle;
    }

    public void setTabTitle(String tabTitle) {
        mTabTitle = tabTitle;
    }

    public BranchFragment getBranchFragment() {
        return mBranchFragment;
    }

    public void setBranchFragment(BranchFragment branchFragment) {
        mBranchFragment = branchFragment;
    }
}

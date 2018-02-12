package com.wan.android.bean;

import com.wan.android.fragment.CommonUseFragment;

/**
 * @author wzc
 * @date 2018/2/12
 */
public class PageModel {

    private String mTabTitle;

    private CommonUseFragment mCommonUseFragment;

    public PageModel(String tabTitle, CommonUseFragment commonUseFragment) {
        mTabTitle = tabTitle;
        mCommonUseFragment = commonUseFragment;
    }

    public String getTabTitle() {
        return mTabTitle;
    }

    public void setTabTitle(String tabTitle) {
        mTabTitle = tabTitle;
    }

    public CommonUseFragment getCommonUseFragment() {
        return mCommonUseFragment;
    }

    public void setCommonUseFragment(CommonUseFragment commonUseFragment) {
        mCommonUseFragment = commonUseFragment;
    }
}

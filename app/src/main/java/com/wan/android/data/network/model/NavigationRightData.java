package com.wan.android.data.network.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * @author wzc
 * @date 2018/8/24
 */
public class NavigationRightData implements Serializable, MultiItemEntity {
    private boolean mIsTitle;
    private String mTitle;
    private ArticleDatas mArticleDatas;
    private int  mGroupId;
    private int mItemType;
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CONTENT = 1;
    public NavigationRightData(boolean isTitle, String title,
                               ArticleDatas articleDatas, int  groupId, int itemType) {
        mIsTitle = isTitle;
        mTitle = title;
        mArticleDatas = articleDatas;
        mGroupId = groupId;
        mItemType = itemType;
    }

    public boolean isTitle() {
        return mIsTitle;
    }

    public void setTitle(boolean title) {
        mIsTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ArticleDatas getArticleDatas() {
        return mArticleDatas;
    }

    public void setArticleDatas(ArticleDatas articleDatas) {
        mArticleDatas = articleDatas;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public int getGroupId() {
        return mGroupId;
    }

    public void setGroupId(int groupId) {
        mGroupId = groupId;
    }
}

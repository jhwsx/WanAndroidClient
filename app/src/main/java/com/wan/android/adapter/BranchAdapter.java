package com.wan.android.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wan.android.R;
import com.wan.android.bean.BranchListResponse;

import java.util.List;

/**
 * @author wzc
 * @date 2018/2/2
 */
public class BranchAdapter extends BaseQuickAdapter<BranchListResponse.Data.Datas,BaseViewHolder> {

    public BranchAdapter(@LayoutRes int layoutResId, @Nullable List<BranchListResponse.Data.Datas> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, BranchListResponse.Data.Datas item) {
        // title
        helper.setText(R.id.tv_home_item_view_title, item.getTitle());
        // chapterName
        helper.setText(R.id.tv_home_item_view_chapter_name, item.getChaptername());
        // niceDate
        helper.setText(R.id.tv_home_item_view_nice_date, item.getNicedate());
        // authorName
        helper.setText(R.id.tv_home_item_view_author, item.getAuthor());
        //
    }
}

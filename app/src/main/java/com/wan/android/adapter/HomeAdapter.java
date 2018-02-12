package com.wan.android.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wan.android.R;
import com.wan.android.bean.HomeListResponse;

import java.util.List;

/**
 * @author wzc
 * @date 2018/2/2
 */
public class HomeAdapter extends BaseQuickAdapter<HomeListResponse.Data.Datas,BaseViewHolder> {

    public HomeAdapter(@LayoutRes int layoutResId, @Nullable List<HomeListResponse.Data.Datas> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, HomeListResponse.Data.Datas item) {
        // title
        helper.setText(R.id.tv_home_item_view_title, item.getTitle());
        // chapterName
        helper.setText(R.id.tv_home_item_view_chapter_name, item.getChapterName());
        // niceDate
        helper.setText(R.id.tv_home_item_view_nice_date, item.getNiceDate());
        // authorName
        helper.setText(R.id.tv_home_item_view_author, item.getAuthor());
        //
    }
}

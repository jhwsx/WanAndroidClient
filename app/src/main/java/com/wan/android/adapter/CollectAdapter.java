package com.wan.android.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wan.android.R;
import com.wan.android.bean.CollectListResponse;

import java.util.List;

/**
 * @author wzc
 * @date 2018/2/2
 */
public class CollectAdapter extends BaseQuickAdapter<CollectListResponse.Data.Datas, BaseViewHolder> {

    public CollectAdapter(@LayoutRes int layoutResId, @Nullable List<CollectListResponse.Data.Datas> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CollectListResponse.Data.Datas item) {
        // title
        helper.setText(R.id.tv_home_item_view_title, item.getTitle());
        // chapterName
        helper.setText(R.id.tv_home_item_view_chapter_name, item.getChaptername());
        // niceDate
        helper.setText(R.id.tv_home_item_view_nice_date, item.getNicedate());
        // authorName
        helper.setText(R.id.tv_home_item_view_author, item.getAuthor());
        helper.addOnClickListener(R.id.iv_home_item_view_collect);
        helper.setImageResource(R.id.iv_home_item_view_collect, R.drawable.ic_favorite);
    }
}

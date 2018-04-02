package com.wan.android.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wan.android.R;
import com.wan.android.data.bean.ArticleDatas;

import java.util.List;

/**
 * @author wzc
 * @date 2018/2/2
 */
public class CommonListAdapter extends BaseQuickAdapter<ArticleDatas,BaseViewHolder> {

    public CommonListAdapter(@LayoutRes int layoutResId, @Nullable List<ArticleDatas> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleDatas item) {
        // title
        helper.setText(R.id.tv_home_item_view_title, Html.fromHtml(item.getTitle()));
        // chapterName
        helper.setText(R.id.tv_home_item_view_chapter_name, item.getChapterName());
        // niceDate
        helper.setText(R.id.tv_home_item_view_nice_date, item.getNiceDate());
        // authorName
        helper.setText(R.id.tv_home_item_view_author, item.getAuthor());
        helper.addOnClickListener(R.id.iv_home_item_view_collect);
        // 类别可以点击
        if (!TextUtils.isEmpty(item.getChapterName())) {
            helper.addOnClickListener(R.id.tv_home_item_view_chapter_name);
        }
        // 作者名字可点击
        if (!TextUtils.isEmpty(item.getAuthor())) {
            helper.addOnClickListener(R.id.tv_home_item_view_author);
        }
        helper.setImageResource(R.id.iv_home_item_view_collect, item.isCollect() ? R.drawable.ic_favorite : R.drawable.ic_favorite_empty);
    }
}

package com.wan.android.ui.adapter;

import android.support.annotation.LayoutRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wan.android.R;
import com.wan.android.data.network.model.ArticleDatas;

import java.util.ArrayList;

/**
 * @author wzc
 * @date 2018/2/2
 */
public class CommonListAdapter extends BaseQuickAdapter<ArticleDatas,BaseViewHolder> {

    public CommonListAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);

    }
    @Override
    protected void convert(BaseViewHolder helper, ArticleDatas item) {
        // title
        helper.setText(R.id.tv_home_item_view_title, Html.fromHtml(item.getTitle()));
        // chapterName
        helper.setText(R.id.tv_home_item_view_chapter_name, item.getSuperChapterName() + "/"+ item.getChapterName());
        // niceDate
        helper.setText(R.id.tv_home_item_view_nice_date, item.getNiceDate());
        // authorName
        helper.setText(R.id.tv_home_item_view_author, item.getAuthor());
        // tag 存在 可以点击
        ArrayList<ArticleDatas.TagsBean> tags = item.getTags();
        if (!tags.isEmpty() && tags.get(0) != null) {
            helper.getView(R.id.tv_home_item_view_tag).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_home_item_view_tag, tags.get(0).getName());
            helper.addOnClickListener(R.id.tv_home_item_view_tag);
        } else {
            helper.getView(R.id.tv_home_item_view_tag).setVisibility(View.GONE);
        }
        // 类别可以点击
        if (!TextUtils.isEmpty(item.getChapterName())) {
            helper.addOnClickListener(R.id.tv_home_item_view_chapter_name);
        }
        // 作者名字可点击
        if (!TextUtils.isEmpty(item.getAuthor())) {
            helper.addOnClickListener(R.id.tv_home_item_view_author);
        }
        // collect
        helper.addOnClickListener(R.id.iv_home_item_view_collect);
        helper.setImageResource(R.id.iv_home_item_view_collect, item.isCollect() ? R.drawable.ic_favorite : R.drawable.ic_favorite_empty);
    }
}

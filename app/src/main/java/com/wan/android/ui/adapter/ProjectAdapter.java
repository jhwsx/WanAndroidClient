package com.wan.android.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wan.android.R;
import com.wan.android.data.network.model.ArticleDatas;

/**
 * @author wzc
 * @date 2018/7/20
 */
public class ProjectAdapter extends BaseQuickAdapter<ArticleDatas, BaseViewHolder> {

    public ProjectAdapter() {
        super(R.layout.project_recycle_item);

    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleDatas item) {
        // title
        helper.setText(R.id.tv_recycle_project_item_title, item.getTitle());
        // desc
        helper.setText(R.id.tv_recycle_project_item_desc, item.getDesc());
        // author
        helper.setText(R.id.tv_recycle_project_item_author, item.getAuthor());
        // nice date
        helper.setText(R.id.tv_recycle_project_item_nice_date, item.getNiceDate());
        // pic
        ImageView imageView = helper.getView(R.id.iv_recycle_project_item);
        if (item.getEnvelopePic() != null && item.getEnvelopePic().endsWith(".gif")) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getEnvelopePic())
                    .asGif()
                    .placeholder(R.drawable.project_itemdefault_bg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            Glide.with(helper.itemView.getContext())
                    .load(item.getEnvelopePic())
                    .placeholder(R.drawable.project_itemdefault_bg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
        helper.addOnClickListener(R.id.iv_recycle_project_item_collect);
        helper.setImageResource(R.id.iv_recycle_project_item_collect,
                item.isCollect() ? R.drawable.ic_favorite : R.drawable.ic_favorite_empty);
    }
}

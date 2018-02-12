package com.wan.android.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wan.android.R;
import com.wan.android.bean.TreeListResponse;

import java.util.List;

/**
 * @author wzc
 * @date 2018/2/11
 */
public class TreeAdapter extends BaseQuickAdapter<TreeListResponse.Data,BaseViewHolder> {
    public TreeAdapter(@LayoutRes int layoutResId, @Nullable List<TreeListResponse.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TreeListResponse.Data item) {
        // title
        helper.setText(R.id.tv_knowledge_item_title, item.getName());
        // subtitle
       StringBuffer stringBuffer = new StringBuffer();
        List<TreeListResponse.Data.Children> children = item.getChildren();
        if (children != null && children.size() >0) {
            for (int i = 0; i < children.size(); i++) {
                TreeListResponse.Data.Children child = children.get(i);
                if (i == children.size() -1) {
                    stringBuffer.append(child.getName());
                } else {
                    stringBuffer.append(child.getName()).append("  ");
                }
            }
        }
        helper.setText(R.id.tv_knowledge_item_subtitle, stringBuffer.toString());
    }
}

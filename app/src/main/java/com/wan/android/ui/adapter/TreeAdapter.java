package com.wan.android.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wan.android.R;
import com.wan.android.data.network.model.BranchData;

import java.util.List;

/**
 * 知识体系数据 Adapter
 * @author wzc
 * @date 2018/8/23
 */
public class TreeAdapter extends BaseQuickAdapter<BranchData,BaseViewHolder> {
    public TreeAdapter() {
        super(R.layout.tree_recycle_view);
    }

    @Override
    protected void convert(BaseViewHolder helper, BranchData item) {
        // title
        helper.setText(R.id.tv_knowledge_item_title, item.getName());
        // subtitle
       StringBuffer stringBuffer = new StringBuffer();
        List<BranchData.Leaf> children = item.getChildren();
        if (children != null && children.size() >0) {
            for (int i = 0; i < children.size(); i++) {
                BranchData.Leaf child = children.get(i);
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

package com.wan.android.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wan.android.R;
import com.wan.android.data.network.model.NavigationRightData;

import java.util.List;

/**
 * @author wzc
 * @date 2018/8/24
 */
public class NavigationRightAdapter extends BaseMultiItemQuickAdapter<NavigationRightData, BaseViewHolder> {

    public NavigationRightAdapter(List<NavigationRightData> data) {
        super(data);
        addItemType(NavigationRightData.TYPE_TITLE, R.layout.navigation_right_title_recycle_item);
        addItemType(NavigationRightData.TYPE_CONTENT, R.layout.navigation_right_content_recycle_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationRightData item) {
        int itemViewType = helper.getItemViewType();
        if (itemViewType == NavigationRightData.TYPE_TITLE) {
            helper.setText(R.id.tv_navigation_right_title, item.getTitle());
        } else if (itemViewType == NavigationRightData.TYPE_CONTENT) {
            helper.setText(R.id.tv_navigation_right_content, item.getArticleDatas().getTitle());
        }
    }
}

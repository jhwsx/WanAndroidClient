package com.wan.android.ui.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wan.android.R;
import com.wan.android.data.network.model.NavigationRightData;

import java.util.List;

import timber.log.Timber;

/**
 * @author wzc
 * @date 2018/8/27
 */
public class ItemHeaderDecoration extends RecyclerView.ItemDecoration {
    private List<NavigationRightData> mData;
    private LayoutInflater mLayoutInflater;
    private int mTitleHeight;
    private static int sRecordedGroupId = 0;
    private OnNavigationRightGroupIdChangeListener mOnNavigationRightGroupIdChangeListener;

    public ItemHeaderDecoration(Context context, List<NavigationRightData> data) {
        mData = data;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.dp_16));
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        mTitleHeight = (int) (fontMetrics.bottom - fontMetrics.top + 2 * context.getResources().getDimensionPixelSize(R.dimen.dp_8));
        mLayoutInflater = LayoutInflater.from(context);
    }

    public static int getRecordedGroupId() {
        return sRecordedGroupId;
    }

    public static void setRecordedGroupId(int recordedGroupId) {
        sRecordedGroupId = recordedGroupId;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
        // 获取第一个可见的 item 的索引
        int firstVisibleItemPosition =
                ((LinearLayoutManager) gridLayoutManager).findFirstVisibleItemPosition();
        // 第一个可见的 item 的 spanSize
        int firstVisibleItemSpanSize = spanSizeLookup.getSpanSize(firstVisibleItemPosition);
        // 获取第一个可见的 item 的 groupId
        int newGroupId = getTagByPosition(firstVisibleItemPosition);
        // 获取第一个可见的 item 所对应的 view
        View itemView = parent.findViewHolderForAdapterPosition(firstVisibleItemPosition).itemView;
        boolean isTranslate = false;
        int bottomMargin = ((ViewGroup.MarginLayoutParams) itemView.getLayoutParams()).bottomMargin;
        int offset = itemView.getHeight() + itemView.getTop() + bottomMargin - mTitleHeight;
        Timber.d("onDrawOver offset=%s, itemView.getHeight()=%s, itemView.getTop()=%s, mTitleHeight=%s",
                offset, itemView.getHeight(), itemView.getTop(), mTitleHeight);
        if (canFindNewTag(firstVisibleItemPosition) && firstVisibleItemSpanSize == 1) {
            if (offset < 0) {
                canvas.save();
                isTranslate = true;
                canvas.translate(0, offset);
            }
        }
        drawHeader(canvas, parent, firstVisibleItemPosition);
        if (isTranslate) {
            canvas.restore();
        }
        // 右侧顶部的 tag 和 记录的 tag 不相等
        if (newGroupId != getRecordedGroupId()) {
            mOnNavigationRightGroupIdChangeListener.onNavigationRightGroupIdChanged(newGroupId);
            setRecordedGroupId(newGroupId);
        }
    }

    private void drawHeader(Canvas canvas, RecyclerView parent, int firstVisibleItemPosition) {
        View header = mLayoutInflater.inflate(R.layout.navigation_right_title_recycle_item,
                parent, false);
        TextView tvTitle = header.findViewById(R.id.tv_navigation_right_title);
        tvTitle.setText(mData.get(firstVisibleItemPosition).getTitle());
        // 设置 header 的布局参数, 没有布局参数是无法测量的
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) header.getLayoutParams();
        if (lp == null) {
            lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        header.setLayoutParams(lp);

        int headerWidthMeasureSpec = getMeasureSpec(lp.width, parent.getWidth(),
                parent.getPaddingLeft() + parent.getPaddingRight()
                        + lp.leftMargin + lp.rightMargin, lp.width);
        int headerHeightMeasureSpec = getMeasureSpec(lp.height, parent.getHeight(),
                parent.getPaddingTop() + parent.getPaddingBottom(), mTitleHeight);

        header.measure(headerWidthMeasureSpec, headerHeightMeasureSpec);
        header.layout(parent.getPaddingLeft() + lp.leftMargin, parent.getPaddingTop(),
                parent.getPaddingLeft() + lp.leftMargin + header.getMeasuredWidth(),
                parent.getPaddingTop() + header.getMeasuredHeight());
        canvas.translate(parent.getPaddingLeft() + lp.leftMargin, 0);
        header.draw(canvas);
    }

    private boolean canFindNewTag(int pos) {
        return getTagByPosition(pos) != getTagByPosition(pos + 1)
                || getTagByPosition(pos) != getTagByPosition(pos + 2);
    }

    private int getTagByPosition(int position) {
        return mData.get(position).getGroupId();
    }

    private int getMeasureSpec(int childDimension, int parentDimension, int padding, int exactDimension) {
        int size = Math.max(0, parentDimension - padding);
        int resultSize;
        int resultMode;
        if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
            resultSize = size;
            resultMode = View.MeasureSpec.EXACTLY;
        } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
            resultSize = size;
            resultMode = View.MeasureSpec.AT_MOST;
        } else {
            resultSize = exactDimension;
            resultMode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

    public void setOnNavigationRightGroupIdChangeListener(OnNavigationRightGroupIdChangeListener l) {
        mOnNavigationRightGroupIdChangeListener = l;
    }
}

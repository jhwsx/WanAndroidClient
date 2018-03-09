package com.wan.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.wan.android.R;

/**
 * @author wzc
 * @date 2018/3/5
 */
public class MyItemView extends ConstraintLayout {

    private ImageView mIvIcon;
    private TextView mTvTitle;

    public MyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.my_item_view, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyItemView);
        String title = ta.getString(R.styleable.MyItemView_android_text);
        int resourceId = ta.getResourceId(R.styleable.MyItemView_android_src, -1);
        ta.recycle();
        initViews();
        mTvTitle.setText(title);
        mIvIcon.setImageResource(resourceId);
    }

    private void initViews() {
        mIvIcon = (ImageView) findViewById(R.id.iv_my_item_view_icon);
        mTvTitle = (TextView) findViewById(R.id.tv_my_item_view_title);
    }


    public ImageView getIvIcon() {
        return mIvIcon;
    }

    public void setIvIcon(ImageView ivIcon) {
        mIvIcon = ivIcon;
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        mTvTitle = tvTitle;
    }
}

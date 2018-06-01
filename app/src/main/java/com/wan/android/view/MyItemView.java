package com.wan.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wan.android.R;

import skin.support.constraint.SkinCompatConstraintLayout;

/**
 * @author wzc
 * @date 2018/3/5
 */
public class MyItemView extends SkinCompatConstraintLayout {

    private ImageView mIvIcon;
    private TextView mTvTitle;
    private final boolean mShowSwitch;
    private final String mTitle;
    private final int mResourceId;
    private SwitchCompat mSwitch;
    private ImageView mIvArrow;

    public MyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.my_item_view, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyItemView);
        mTitle = ta.getString(R.styleable.MyItemView_android_text);
        mResourceId = ta.getResourceId(R.styleable.MyItemView_android_src, -1);
        mShowSwitch = ta.getBoolean(R.styleable.MyItemView_is_show_switch, false);
        ta.recycle();
        initViews();
        initData();
    }

    private void initData() {
        mTvTitle.setText(mTitle);
        mSwitch.setClickable(false);
        if (mShowSwitch) {
            mSwitch.setVisibility(View.VISIBLE);
            mIvArrow.setVisibility(View.GONE);
        } else {
            mSwitch.setVisibility(View.GONE);
            mIvArrow.setVisibility(View.VISIBLE);
        }
        mIvIcon.setImageResource(mResourceId);

    }

    private void initViews() {
        mIvIcon = (ImageView) findViewById(R.id.iv_my_item_view_icon);
        mTvTitle = (TextView) findViewById(R.id.tv_my_item_view_title);
        mIvArrow = (ImageView) findViewById(R.id.iv_my_item_view_arrow);
        mSwitch = (SwitchCompat) findViewById(R.id.switch_my_item_view);
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

    public boolean isToggleOn() {
        return mSwitch.isChecked();
    }

    public void setToggle(boolean isChecked) {
        mSwitch.setChecked(isChecked);
    }

    public void toggle() {
        mSwitch.toggle();
    }
}

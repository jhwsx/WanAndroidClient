package com.wan.android.skin;

import android.content.Context;
import android.util.AttributeSet;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

public class VerticalTabLayoutCompat extends VerticalTabLayout implements SkinCompatSupportable {

    private final SkinCompatBackgroundHelper mBackgroundTintHelper;

    public VerticalTabLayoutCompat(Context context) {
        this(context, null);
    }

    public VerticalTabLayoutCompat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTabLayoutCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }
}

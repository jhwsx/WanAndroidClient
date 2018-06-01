package com.wan.android.skin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.wan.android.R;

import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatHelper;
import skin.support.widget.SkinCompatSupportable;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

public class SkinMaterialBottomNavigationView2 extends BottomNavigationView implements SkinCompatSupportable {
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] DISABLED_STATE_SET = {-android.R.attr.state_enabled};

    private int mTextColorResId = INVALID_ID;
    private int mIconTintResId = INVALID_ID;
    private int mDefaultTintResId = INVALID_ID;

    public SkinMaterialBottomNavigationView2(@NonNull Context context) {
        this(context, null);
    }

    public SkinMaterialBottomNavigationView2(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinMaterialBottomNavigationView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationView, defStyleAttr,
                R.style.Widget_Design_BottomNavigationView);

        if (a.hasValue(R.styleable.BottomNavigationView_itemIconTint)) {
            mIconTintResId = a.getResourceId(R.styleable.BottomNavigationView_itemIconTint, INVALID_ID);
        } else {
            mDefaultTintResId = resolveColorPrimary();
        }
        if (a.hasValue(R.styleable.BottomNavigationView_itemTextColor)) {
            mTextColorResId = a.getResourceId(R.styleable.BottomNavigationView_itemTextColor, INVALID_ID);
        } else {
            mDefaultTintResId = resolveColorPrimary();
        }
        a.recycle();
        applyItemIconTintResource();
        applyItemTextColorResource();

        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    private void applyItemTextColorResource() {
        mTextColorResId = SkinCompatHelper.checkResourceId(mTextColorResId);
        if (mTextColorResId != INVALID_ID) {
            setItemTextColor(SkinCompatResources.getColorStateList(getContext(), mTextColorResId));
        } else {
            mDefaultTintResId = SkinCompatHelper.checkResourceId(mDefaultTintResId);
            if (mDefaultTintResId != INVALID_ID) {
                setItemTextColor(createDefaultColorStateList(android.R.attr.textColorSecondary));
            }
        }
    }

    private void applyItemIconTintResource() {
        mIconTintResId = SkinCompatHelper.checkResourceId(mIconTintResId);
        if (mIconTintResId != INVALID_ID) {
            setItemIconTintList(SkinCompatResources.getColorStateList(getContext(), mIconTintResId));
        } else {
            mDefaultTintResId = SkinCompatHelper.checkResourceId(mDefaultTintResId);
            if (mDefaultTintResId != INVALID_ID) {
                setItemIconTintList(createDefaultColorStateList(android.R.attr.textColorSecondary));
            }
        }
    }

    private ColorStateList createDefaultColorStateList(int baseColorThemeAttr) {
        final TypedValue value = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(baseColorThemeAttr, value, true)) {
            return null;
        }
        ColorStateList baseColor = SkinCompatResources.getColorStateList(getContext(), value.resourceId);

        int colorPrimary = SkinCompatResources.getColor(getContext(), mDefaultTintResId);
        int defaultColor = baseColor.getDefaultColor();
        return new ColorStateList(new int[][]{
                DISABLED_STATE_SET,
                CHECKED_STATE_SET,
                EMPTY_STATE_SET
        }, new int[]{
                baseColor.getColorForState(DISABLED_STATE_SET, defaultColor),
                colorPrimary,
                defaultColor
        });
    }

    private int resolveColorPrimary() {
        final TypedValue value = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(
                R.attr.colorPrimary, value, true)) {
            return INVALID_ID;
        }
        return value.resourceId;
    }

    @Override
    public void applySkin() {
        applyItemIconTintResource();
        applyItemTextColorResource();
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }
}

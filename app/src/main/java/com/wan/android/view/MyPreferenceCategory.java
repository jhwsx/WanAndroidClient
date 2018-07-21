package com.wan.android.view;

import android.content.Context;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wan.android.R;
import com.wan.android.util.NightModeUtils;
import com.wan.android.util.Utils;

/**
 * @author wzc
 * @date 2018/7/19
 */
public class MyPreferenceCategory extends PreferenceCategory {
    public MyPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPreferenceCategory(Context context) {
        this(context, null);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        if (holder.itemView instanceof TextView) {
            TextView textView = (TextView) holder.itemView;
            textView.setTextColor(Utils.getApp().getResources().getColor(
                    NightModeUtils.isNightMode()
                            ? R.color.colorPrimary1_night
                            :R.color.colorPrimary1));
        }
    }
}

package com.wan.android.ui.setting;

import android.content.Context;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wan.android.R;
import com.wan.android.util.Utils;

/**
 * @author wzc
 * @date 2018/7/19
 */
public class MyPreference extends Preference {

    public MyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        View itemView = holder.itemView;
        TextView title = itemView.findViewById(android.R.id.title);
        title.setTextColor(Utils.getApp().getResources().getColor( R.color.color_3e));
    }
}

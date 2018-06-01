package com.wan.android.skin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import skin.support.app.SkinLayoutInflater;

public class SkinRecyclerViewInflater implements SkinLayoutInflater {
    @Override
    public View createView(@NonNull Context context, final String name, @NonNull AttributeSet attrs) {
        View view = null;
        switch (name) {
            case "android.support.v7.widget.RecyclerView":
                view = new SkinCompatRecyclerView(context, attrs);
                break;
            default:
                break;
        }
        return view;
    }
}
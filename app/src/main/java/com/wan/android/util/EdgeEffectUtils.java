package com.wan.android.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.EdgeEffect;

import com.wan.android.R;

import java.lang.reflect.Field;

/**
 * @author wzc
 * @date 2018/5/21
 */
public class EdgeEffectUtils {

    private EdgeEffectUtils() {
        //no instance
    }

    public static void setEdgeGlowColor(ViewPager viewPager, int color) {
        try {
            Class<?> clazz = ViewPager.class;
            for (String name : new String[]{"mLeftEdge", "mRightEdge"}) {
                Field field = clazz.getDeclaredField(name); // 获取 Field 对象
                field.setAccessible(true);
                Object edge = field.get(viewPager); // android.support.v4.widget.EdgeEffectCompat 获取 mLeftEdge 对象
//                Field fEdgeEffect = edge.getClass().getDeclaredField("mEdgeEffect");
//                fEdgeEffect.setAccessible(true);
                setEdgeEffectColor((EdgeEffect) edge, color);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static void setEdgeGlowColor(RecyclerView recyclerView, int color) {
        try {
            Class<RecyclerView> recyclerViewClass = RecyclerView.class;
            for (String name : new String[]{"mLeftGlow", "mTopGlow", "mRightGlow", "mBottomGlow"}) {
                Field edgeEffectField = recyclerViewClass.getDeclaredField(name);
                edgeEffectField.setAccessible(true);
                Object edgeEffectObject = edgeEffectField.get(recyclerView);
                setEdgeEffectColor((EdgeEffect) edgeEffectObject, color);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private static void setEdgeEffectColor(EdgeEffect edgeEffect, int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                edgeEffect.setColor(color);
                return;
            }

            for (String name : new String[]{"mEdge", "mGlow"}) {
                final Field field = EdgeEffect.class.getDeclaredField(name);
                field.setAccessible(true);
                final Drawable drawable = (Drawable) field.get(edgeEffect);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                // free up any references
                drawable.setCallback(null);
            }

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static  void setViewPagerEdgeEffect(ViewPager viewPager) {
        EdgeEffectUtils.setEdgeGlowColor(viewPager,
                NightModeUtils.isNightMode()
                        ? Utils.getApp().getResources().getColor(R.color.color_ef_night)
                        : Utils.getApp().getResources().getColor(R.color.colorPrimary2));
    }

    public static void setRecyclerViewEdgeEffect(RecyclerView recyclerView) {
        EdgeEffectUtils.setEdgeGlowColor(recyclerView,
                NightModeUtils.isNightMode()
                        ? Utils.getApp().getResources().getColor(R.color.color_ef_night)
                        : Utils.getApp().getResources().getColor(R.color.colorPrimary2));
    }
}

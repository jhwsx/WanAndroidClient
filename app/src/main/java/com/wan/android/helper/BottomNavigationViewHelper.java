package com.wan.android.helper;

import android.support.compat.BuildConfig;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * 该类来自stackoverflow,解决了BottomNavigationView使用的时候 item 数大于 3 个时会有位移
 * https://stackoverflow.com/questions/40176244/how-to-disable-bottomnavigationview-shift-mode
 */
public class BottomNavigationViewHelper {
    private static final String TAG = BottomNavigationViewHelper.class.getSimpleName();

    private BottomNavigationViewHelper() {
        //no instance
    }
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Unable to get shift mode field", e);
            }
        } catch (IllegalAccessException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Unable to change value of shift mode", e);
            }
        }
    }
}

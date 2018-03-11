package com.wan.android.util;

import android.content.Context;

import java.util.HashSet;

/**
 * Created by wzc on 2017/10/13.
 * <p>
 * SharedPreferences工具类
 */

public class PreferenceUtils {
    private PreferenceUtils() {
    }

    private static final String FILE_SP = "file_sp";

    public static void putInt(Context context, String key, int value) {
        if (context == null) {
            return;
        }
        context.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        if (context == null) {
            return defaultValue;
        }
        return context.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE).getInt(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        if (context == null) {
            return;
        }
        context.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        if (context == null) {
            return defaultValue;
        }
        return context.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        if (context == null) {
            return;
        }
        context.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        if (context == null) {
            return defaultValue;
        }
        return context.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE).getString(key, defaultValue);
    }

    public static void putLong(Context context, String key, long value) {
        if (context == null) {
            return;
        }
        context.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE)
                .edit()
                .putLong(key, value)
                .apply();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        if (context == null) {
            return defaultValue;
        }
        return context.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE).getLong(key, defaultValue);
    }

    public static void putFloat(Context context, String key, float value) {
        if (context == null) {
            return;
        }
        context.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE)
                .edit()
                .putFloat(key, value)
                .apply();
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        if (context == null) {
            return defaultValue;
        }
        return context.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE).getFloat(key, defaultValue);
    }


    public static HashSet<String> getStringSet(Context ctx, String key) {
        HashSet<String> set = (HashSet<String>) ctx.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE).getStringSet(key, new HashSet<String>());
        return new HashSet<>(set);
    }

    public static void putStringSet(Context ctx, String key, HashSet<String> set) {
        ctx.getSharedPreferences(FILE_SP, Context.MODE_PRIVATE).edit().putStringSet(key, set).apply();
    }
}

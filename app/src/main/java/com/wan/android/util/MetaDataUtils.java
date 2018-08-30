package com.wan.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * @author wzc
 * @date 2018/5/3
 */
public class MetaDataUtils {

    private MetaDataUtils() {
        //no instance
    }

    /**
     * 获取String类型的元数据
     *
     * @param key 元数据的key
     * @return 元数据的value
     */
    public static String getStringMetaData(Context context, String key) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (applicationInfo.metaData.containsKey(key)) {
                return applicationInfo.metaData.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

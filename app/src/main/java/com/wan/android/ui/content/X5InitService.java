package com.wan.android.ui.content;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;

import timber.log.Timber;

/**
 * @author wzc
 * @date 2018/8/18
 */
public class X5InitService extends IntentService {
    private static final String TAG = X5InitService.class.getSimpleName();

    public X5InitService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        initX5WebView(getApplicationContext());
    }

    public static void initX5WebView(final Context context) {
        // 在调用TBS初始化、创建WebView之前进行如下配置，以开启优化方案
        HashMap<String, Object> map = new HashMap<>(1);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                if (!QbSdk.isTbsCoreInited()) {
                    QbSdk.preInit(context, null);
                }
            }
        });

        QbSdk.PreInitCallback callback = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Timber.d("onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean isX5Core) {
                Timber.d("onViewInitFinished: isX5Core = %s", isX5Core);
            }
        };
        QbSdk.initX5Environment(context, callback);
    }
}

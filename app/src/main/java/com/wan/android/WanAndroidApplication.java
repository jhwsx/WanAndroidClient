package com.wan.android;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.kingja.loadsir.core.LoadSir;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.umeng.commonsdk.UMConfigure;
import com.wan.android.callback.CustomCallback;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.ErrorCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.callback.TimeoutCallback;
import com.wan.android.util.CrashHandler;
import com.wan.android.util.ProcessUtils;
import com.wan.android.util.UmengUtils;

import skin.support.SkinCompatManager;

/**
 * @author wzc
 * @date 2018/2/11
 */
public class WanAndroidApplication extends Application {
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        CrashHandler.getInstance().init(this);

        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();

        UmengUtils.initUmengAnalytics(this);
        // 友盟+设置组件化的Log开关
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        // 友盟+设置日志加密
        UMConfigure.setEncryptEnabled(!BuildConfig.DEBUG);

        if (ProcessUtils.isMainProcess(this)) {
            SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
//                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
//                    .addInflater(new SkinMaterialViewInflater2())            // material design 控件换肤初始化[可选]
//                    .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
//                    .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
//                    .addInflater(new SkinRecyclerViewInflater())                // RecyclerView v7 控件换肤初始化[可选]
                    .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                    .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                    .loadSkin();
        }

        FileDownloadLog.NEED_LOG = BuildConfig.DEBUG;
        FileDownloader.setup(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

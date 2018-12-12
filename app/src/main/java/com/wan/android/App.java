package com.wan.android;

import android.app.Application;
import android.content.Context;

import com.kingja.loadsir.core.LoadSir;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.wan.android.di.component.ApplicationComponent;
import com.wan.android.di.component.DaggerApplicationComponent;
import com.wan.android.di.module.ApplicationModule;
import com.wan.android.ui.loadcallback.EmptyCallback;
import com.wan.android.ui.loadcallback.LoadingCallback;
import com.wan.android.ui.loadcallback.NetworkErrorCallback;
import com.wan.android.util.MetaDataUtils;
import com.wan.android.util.MyDebugTree;

import timber.log.Timber;

/**
 * @author wzc
 * @date 2018/8/2
 */
public class App extends Application {

    private static Context sContext;
    private static boolean sIsColdStart = false;
    private ApplicationComponent mApplicationComponent;

    public static Context getContext() {
        return sContext;
    }

    public static boolean isColdStart() {
        return sIsColdStart;
    }

    public static void setColdStart(boolean isColdStart) {
        sIsColdStart = isColdStart;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setColdStart(true);
        sContext = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new MyDebugTree());
        }

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        initLoaderSir();
        initUmeng();
    }

    private void initLoaderSir() {
        LoadSir.beginBuilder()
                .addCallback(new NetworkErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }

    private void initUmeng() {
        /**
         * 初始化common库
         * 注意：一定要在主进程进行该项操作，如果您使用到PUSH，还需在推送进程（channel进程）同样进行该项操作`
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,
                MetaDataUtils.getStringMetaData(this, "UMENG_MESSAGE_SECRET"));
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        /**
         * 设置日志加密
         * 参数：boolean 默认为false（不加密）
         */
        UMConfigure.setEncryptEnabled(BuildConfig.DEBUG);

        MobclickAgent.openActivityDurationTrack(false);
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}

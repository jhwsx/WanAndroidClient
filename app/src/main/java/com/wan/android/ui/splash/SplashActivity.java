package com.wan.android.ui.splash;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wan.android.App;
import com.wan.android.BuildConfig;
import com.wan.android.ui.base.BaseActivity;
import com.wan.android.ui.main.MainActivity;
import com.wan.android.util.BuglyUtil;
import com.wan.android.util.CrashHandler;
import com.wan.android.util.UmengUtils;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * 闪屏页
 *
 * @author wzc
 * @date 2018/8/2
 */
public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    @Inject
    CompositeDisposable mCompositeDisposable;
    private boolean mIsReadPhoneStatePermissionGranted = false;
    private boolean mIsWriteExternalStoragePermissionGranted = false;
    private int mCallbackCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        RxPermissions rxPermissions = new RxPermissions(this);
        mCompositeDisposable.add(rxPermissions
                .requestEach(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        mCallbackCount++;
                        Timber.d("%s, count=%s", permission.toString(), mCallbackCount);
                        if (permission.granted) {
                            // 用户已经同意授权
                            if (TextUtils.equals(permission.name, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                mIsWriteExternalStoragePermissionGranted = true;
                                if (BuildConfig.DEBUG && App.isColdStart()) {
                                    Timber.d("init crashhander");
                                    CrashHandler.getInstance().init(SplashActivity.this);
                                }
                            }
                            if (TextUtils.equals(permission.name, Manifest.permission.READ_PHONE_STATE)) {
                                mIsReadPhoneStatePermissionGranted = true;
                                if (App.isColdStart()) {
                                    Timber.d("init umeng");
                                    UmengUtils.initUmengAnalytics(SplashActivity.this);
                                }
                            }

                            if (mIsReadPhoneStatePermissionGranted
                                    && mIsWriteExternalStoragePermissionGranted) {
                                if (App.isColdStart()) {
                                    Timber.d("init bugly");
                                    BuglyUtil.initBugly(SplashActivity.this);
                                }
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框

                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                        }
                        if (mCallbackCount == 2) {
                            Timber.d("setColdStart false");
                            App.setColdStart(false);
                            MainActivity.start(SplashActivity.this);
                            finish();
                        }
                    }
                }));

    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected boolean hasFragment() {
        return false;
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void setUp() {

    }
}

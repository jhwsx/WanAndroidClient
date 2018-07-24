package com.wan.android.setting;

import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.VersionUpdateData;
import com.wan.android.data.client.VersionUpdateClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.AppUtils;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.NetworkUtils;
import com.wan.android.util.Utils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wzc
 * @date 2018/5/29
 */
public class SettingsPresenter implements SettingsContract.Presenter {
    private final SettingsContract.View mSettingsView;

    public SettingsPresenter(SettingsContract.View settingsView) {
        mSettingsView = settingsView;
        mSettingsView.setPresenter(this);
    }
    @Override
    public void checkVersion() {
        if (!NetworkUtils.isConnected()) {
            mSettingsView.showBadNetwork();
            return;
        }
        mSettingsView.showProgressDialog();

        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(VersionUpdateClient.class)
                .checkVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<VersionUpdateData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<VersionUpdateData> response) {
                        VersionUpdateData data = response.getData();
                        int versionCodeServer = data.getVersionCode();
                        if (AppUtils.getAppVersionCode() < versionCodeServer) {
                            mSettingsView.showNewVersionDialog(data);
                        } else {
                            mSettingsView.showHadLatestVersion();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSettingsView.showCheckVersionFail(new CommonException(-1, e.getMessage()));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }

    @Override
    public void startDownloadNewVersion(VersionUpdateData data) {
        VersionUpdateHelper versionUpdateHelper = new VersionUpdateHelper(Utils.getApp(), data);
        versionUpdateHelper.bindDownloadService();
    }

    @Override
    public void start() {

    }
}

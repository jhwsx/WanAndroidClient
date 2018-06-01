package com.wan.android.setting;

import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.annotation.NotProguard;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.VersionUpdateData;

/**
 * @author wzc
 * @date 2018/5/29
 */
public interface SettingsContract {
    @NotProguard
    interface View extends BaseView<Presenter> {
        // 显示无网络
        void showBadNetwork();

        // 显示正在获取服务端版本信息的进度框
        void showProgressDialog();

        // 获取服务端版本信息成功,检测到有新版本
        void showNewVersionDialog(VersionUpdateData data);

        // 当前已经是最新版本
        void showHadLatestVersion();

        // 获取服务端版本信息失败
        void showCheckVersionFail(CommonException e);
    }

    @NotProguard
    interface Presenter extends BasePresenter {
        // 检查版本
        void checkVersion();

        // 开始下载新版本
        void startDownloadNewVersion(VersionUpdateData data);
    }
}

package com.wan.android.setting;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.VersionUpdateData;
import com.wan.android.util.AppUtils;
import com.wan.android.util.EventReporterFactory;
import com.wan.android.util.Utils;

import java.io.File;

/**
 * @author wzc
 * @date 2018/5/29
 */
public class VersionUpdateService extends Service {

    private static final String TAG = VersionUpdateService.class.getSimpleName();
    private IBinder mBinder = new LocalBinder();
    private NotificationCompat.Builder mBuilder;
    private static final int NOTIFICATION_ID = 0x1000;
    private NotificationManager mNotificationManager;
    private DownloadListener mDownloadListener;
    private boolean mIsDownloading;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopDownloadForeground();
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    public boolean isDownloading() {
        return mIsDownloading;
    }

    public void setDownloading(boolean downloading) {
        mIsDownloading = downloading;
    }

    public class LocalBinder extends Binder {
        public VersionUpdateService getService() {
            return VersionUpdateService.this;
        }
    }

    public void doDownloadTask(VersionUpdateData versionUpdateData) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        setDownloading(true);

        starDownLoadForeground();
        final String wanAndroidApkFilePath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "tmpdir1" + File.separator +
                "com.wan.android_" + versionUpdateData.getVersionName() + ".apk";

        FileDownloader.getImpl()
                .create(versionUpdateData.getDownloadurl())
                .setPath(wanAndroidApkFilePath, false)
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        int progress = (int) (soFarBytes * 100f / totalBytes);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "progress = " + progress);
                        }

                        mBuilder.setContentText(getString(R.string.download_progress,progress)).setProgress(100, progress, false);
                        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "completed: ");
                        }
                        EventReporterFactory.getReporter().report("download_complete");
                        mBuilder.setContentText(getString(R.string.download_completed))
                                .setProgress(0, 0, false);
                        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                        AppUtils.installApk(Utils.getContext(), wanAndroidApkFilePath);
                        setDownloading(false);
                        mDownloadListener.onDownloadSuccess();
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        setDownloading(false);
                        mDownloadListener.onDownloadFail(e);
                    }
                }).start();
    }

    private void starDownLoadForeground() {
        mBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_small_icon)
                .setColor(Color.parseColor("#0F9D58"))
                .setTicker(getString(R.string.new_version_downloading, getString(R.string.app_name)))
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_update, getString(R.string.app_name))) // 通知标题
                .setContentText(getString(R.string.downloading)) // 通知内容
                .setAutoCancel(false);
        Notification notification = mBuilder.getNotification();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void stopDownloadForeground() {
        stopForeground(true);
    }

    public interface DownloadListener {
        void onDownloadSuccess();

        void onDownloadFail(Throwable e);
    }
}

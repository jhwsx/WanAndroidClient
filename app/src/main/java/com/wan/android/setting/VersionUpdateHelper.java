package com.wan.android.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.wan.android.data.bean.VersionUpdateData;

/**
 * @author wzc
 * @date 2018/5/29
 */
public class VersionUpdateHelper implements ServiceConnection {

    private Context mContext;
    private VersionUpdateService mService;
    private VersionUpdateData mVersionUpdateData;
    public VersionUpdateHelper(Context context, VersionUpdateData data) {
        mContext = context;
        mVersionUpdateData = data;
    }

    public void bindDownloadService() {
        if (mService == null && mContext != null) {
            mContext.bindService(new Intent(mContext, VersionUpdateService.class),
                    this, Context.BIND_AUTO_CREATE);
        }
    }

    public void unbindDownloadService() {
        if (mService != null && !mService.isDownloading()) {
            mContext.unbindService(this);
            mService = null;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((VersionUpdateService.LocalBinder) service).getService();
        mService.setDownloadListener(new VersionUpdateService.DownloadListener() {
            @Override
            public void onDownloadSuccess() {
                unbindDownloadService();
            }

            @Override
            public void onDownloadFail(Throwable e) {
                unbindDownloadService();
            }
        });
        mService.doDownloadTask(mVersionUpdateData);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}

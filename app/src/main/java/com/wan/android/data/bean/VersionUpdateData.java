package com.wan.android.data.bean;

import com.wan.android.annotation.NotProguard;

/**
 * @author wzc
 * @date 2018/5/29
 */
@NotProguard
public class VersionUpdateData {
    private String downloadUrl;
    private String info;
    private int versionCode;
    private String versionName;


    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    public String getDownloadurl() {
        return downloadUrl;
    }


    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo() {
        return info;
    }


    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
    public int getVersionCode() {
        return versionCode;
    }


    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    public String getVersionName() {
        return versionName;
    }
}

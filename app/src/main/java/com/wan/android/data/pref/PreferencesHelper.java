package com.wan.android.data.pref;

/**
 * @author wzc
 * @date 2018/8/3
 */
public interface PreferencesHelper {
    String getUsername();

    void setUsername(String username);

    boolean getLoginStatus();

    void setLoginStatus(boolean isLogin);

    String getRoastOpenid();

    void setRoastOpenid(String openid);

    int getRoastHeadPicId();

    void setRoastHeadPicId(int headPicId);
}

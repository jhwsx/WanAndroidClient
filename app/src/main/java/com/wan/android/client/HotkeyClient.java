package com.wan.android.client;

import com.wan.android.bean.HotkeyResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author wzc
 * @date 2018/3/6
 */
public interface HotkeyClient {
    // http://www.wanandroid.com/hotkey/json
    @GET("/hotkey/json")
    Call<HotkeyResponse> getHotkey();
}

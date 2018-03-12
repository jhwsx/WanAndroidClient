package com.wan.android.client;

import com.wan.android.bean.CommonResponse;
import com.wan.android.bean.HotkeyData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 搜索热词
 *
 * @author wzc
 * @date 2018/3/6
 */
public interface HotkeyClient {
    // http://www.wanandroid.com/hotkey/json
    @GET("/hotkey/json")
    Call<CommonResponse<List<HotkeyData>>> getHotkey();
}

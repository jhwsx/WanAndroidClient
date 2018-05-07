package com.wan.android.data.client;

import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.HotkeyData;

import java.util.List;

import io.reactivex.Observable;
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
    Observable<CommonResponse<List<HotkeyData>>> getHotkey();
}

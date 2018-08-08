package com.wan.android.ui.loadcallback;

import com.kingja.loadsir.callback.Callback;
import com.wan.android.R;

/**
 * 网络错误
 * @author wzc
 * @date 2018/8/7
 */
public class NetworkErrorCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.load_network_error;
    }
}

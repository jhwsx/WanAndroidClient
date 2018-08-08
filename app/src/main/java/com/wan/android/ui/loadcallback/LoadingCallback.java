package com.wan.android.ui.loadcallback;

import com.kingja.loadsir.callback.Callback;
import com.wan.android.R;

/**
 * @author wzc
 * @date 2018/8/7
 */
public class LoadingCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.load_loading;
    }
}

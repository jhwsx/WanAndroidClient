package com.wan.android.callback;

import android.content.Context;
import android.view.View;

import com.kingja.loadsir.callback.Callback;
import com.wan.android.R;
import com.wan.android.util.ToastUtils;


public class TimeoutCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_timeout;
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        ToastUtils.showShort(R.string.loadsir_connection_overtime);
        return false;
    }

}

package com.wan.android.callback;

import com.kingja.loadsir.callback.Callback;
import com.wan.android.R;



public class EmptyCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_empty;
    }

}

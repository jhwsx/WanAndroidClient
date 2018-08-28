package com.wan.android.ui.collect;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.wan.android.R;
import com.wan.android.ui.base.BaseSingleFragmentActivity;

/**
 * 我的收藏页面
 *
 * @author wzc
 * @date 2018/8/28
 */
public class MyCollectionActivity extends BaseSingleFragmentActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, MyCollectionActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected Fragment createFragment() {
        return MyCollectionFragment.newInstance();
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void initTitle() {
        mTvTitle.setText(R.string.my_collection);
    }
}

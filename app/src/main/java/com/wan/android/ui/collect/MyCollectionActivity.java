package com.wan.android.ui.collect;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.wan.android.R;
import com.wan.android.ui.base.BaseSingleFragmentActivity;

/**
 * 我的收藏页面
 *
 * @author wzc
 * @date 2018/8/28
 */
public class MyCollectionActivity extends BaseSingleFragmentActivity {

    private static final String TAG = MyCollectionActivity.class.getSimpleName();
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
    protected boolean hasFragment() {
        return true;
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void initTitle() {
        mTvTitle.setText(R.string.my_collection);
    }
}

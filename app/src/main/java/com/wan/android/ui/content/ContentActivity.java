package com.wan.android.ui.content;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.wan.android.R;
import com.wan.android.data.network.model.ContentData;
import com.wan.android.ui.base.BaseSingleFragmentActivity;

/**
 * 详情页面
 * @author wzc
 * @date 2018/8/17
 */
public class ContentActivity extends BaseSingleFragmentActivity {
    private static final String EXTRA_CONTENT_DATA = "com.wan.android.extra_content_data";
    private ContentFragment mContentFragment;

    public static void start(Context context, ContentData data) {
        Intent starter = new Intent(context, ContentActivity.class);
        starter.putExtra(EXTRA_CONTENT_DATA, data);
        context.startActivity(starter);
    }
    @Override
    protected void inject() {
        getActivityComponent().inject(ContentActivity.this);
    }

    @Override
    protected Fragment createFragment() {
        // 启用硬件加速
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }
        mContentFragment = ContentFragment.newInstance((ContentData) getIntent().getSerializableExtra(EXTRA_CONTENT_DATA));
        return mContentFragment;
    }

    @Override
    protected void setUp() {
        // do nothing
    }

    @Override
    protected void initTitle() {
        mToolbar.setTitle(R.string.loading);
    }

    @Override
    public void onBackPressed() {
        if (!mContentFragment.goBack()) {
            super.onBackPressed();
        }
    }
}

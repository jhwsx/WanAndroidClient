package com.wan.android.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.wan.android.fragment.CollectionFragement;

/**
 * 我的收藏
 *
 * @author wzc
 * @date 2018/3/5
 */
public class MyCollectionActivity extends BaseSingleFragmentActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, MyCollectionActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected Fragment createFragment() {
        return CollectionFragement.newInstance();
    }
}

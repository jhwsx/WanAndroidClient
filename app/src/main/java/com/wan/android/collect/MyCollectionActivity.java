package com.wan.android.collect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wan.android.base.BaseSingleFragmentActivity;
import com.wan.android.data.source.RetrofitClient;

/**
 * 我的收藏
 *
 * @author wzc
 * @date 2018/3/5
 */
public class MyCollectionActivity extends BaseSingleFragmentActivity {

    private CollectionFragement mCollectionFragement;
    private CollectPresenter mCollectPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, MyCollectionActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the presenter
        mCollectPresenter = new CollectPresenter(RetrofitClient.getInstance(), mCollectionFragement);
    }

    @Override
    protected Fragment createFragment() {
        mCollectionFragement = CollectionFragement.newInstance();
        return mCollectionFragement;
    }
}

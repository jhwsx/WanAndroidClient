package com.wan.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.bean.FriendResponse;
import com.wan.android.client.FriendClient;
import com.wan.android.retrofit.RetrofitClient;
import com.wan.android.util.Colors;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 常用页面
 *
 * @author wzc
 * @date 2018/2/24
 */
public class FuActivity extends BaseActivity {
    private static final String TAG = FuActivity.class.getSimpleName();
    private TagFlowLayout mTagFlowLayout;

    public static void start(Context context) {
        Intent starter = new Intent(context, FuActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_fu);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        FriendClient client = RetrofitClient.create(FriendClient.class);
        Call<FriendResponse> call = client.getFriend();
        call.enqueue(new Callback<FriendResponse>() {
            @Override
            public void onResponse(Call<FriendResponse> call, Response<FriendResponse> response) {
                FriendResponse body = response.body();
                if (body == null) {
                    return;
                }
                if (body.getErrorcode() != 0) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "getFriend onResponse errorMsg: " + body.getErrormsg());
                    }
                    return;
                }
                final List<FriendResponse.Data> data = body.getData();
                final ArrayList<Integer> colors = Colors.randomList(data.size());
                mTagFlowLayout.setAdapter(new TagAdapter<FriendResponse.Data>(data) {
                    @Override
                    public View getView(FlowLayout parent, int position, FriendResponse.Data d) {
                        TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tv, mTagFlowLayout, false);
                        textView.setText(d.getName());
                        textView.setTextColor(colors.get(position));
                        return textView;
                    }
                });
                mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        FriendResponse.Data d = data.get(position);
                        ContentActivity.start(mContext, d.getName(),d.getLink(),d.getId());
                        return true;
                    }
                });
            }

            @Override
            public void onFailure(Call<FriendResponse> call, Throwable t) {

            }
        });


    }
}

package com.wan.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.wan.android.R;
import com.wan.android.helper.CollectHelper;

/**
 * 内容页面
 * @author wzc
 * @date 2018/2/1
 */
public class ContentActivity extends BaseActivity {

    private LinearLayout mLinearLayoutContainer;
    private TextView mTvTitle;
    private static final String EXTRA_CONTENT_URL = "extra_content_url";
    private static final String EXTRA_ID = "extra_id";
    private static final String EXTRA_TITLE = "extra_title";
    private AgentWeb mAgentWeb;

    public static void start(Context context, String title, String url, int id) {
        Intent starter = new Intent(context, ContentActivity.class);
        starter.putExtra(EXTRA_CONTENT_URL, url);
        starter.putExtra(EXTRA_TITLE, title);
        starter.putExtra(EXTRA_ID, id);
        context.startActivity(starter);
    }

    private String mUrl;
    private int mId;
    private String mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        if (getIntent() != null) {
            mUrl = getIntent().getStringExtra(EXTRA_CONTENT_URL);
            mId = getIntent().getIntExtra(EXTRA_ID, -1);
            mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        }
        mLinearLayoutContainer = (LinearLayout) findViewById(R.id.linearlayout_activity_content_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_content);
        mTvTitle = (TextView) findViewById(R.id.tv_activity_content_title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mTvTitle.setText(R.string.loading);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAgentWeb = AgentWeb.with(this)
                // 设置父容器
                .setAgentWebParent(mLinearLayoutContainer, new ViewGroup.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()
                .ready()
                .go(mUrl);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_activity_content_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mTitle + ": " + mUrl);
                startActivity(Intent.createChooser(shareIntent, "Share"));
                return true;
            case R.id.action_activity_content_collect:
                CollectHelper.collect(mId, null);
                return true;
            case R.id.action_activity_content_open_with_system_browser:
                final Uri uri = Uri.parse(mUrl);
                final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void collect() {
//
//        CollectClient collectClient = RetrofitClient.create(CollectClient.class);
//        Call<CommonResponse<String>> call = collectClient.collect(mId);
//        call.enqueue(new Callback<CommonResponse<String>>() {
//            @Override
//            public void onResponse(Call<CommonResponse<String>> call, Response<CommonResponse<String>> response) {
//                CommonResponse<String> body = response.body();
//                if (body.getErrorcode() != 0) {
//                    Toast.makeText(mContext, body.getErrormsg(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
//                Toast.makeText(mContext, "收藏失败 " + t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mTvTitle != null) {
                mTvTitle.setText(title != null ? title : "");
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}

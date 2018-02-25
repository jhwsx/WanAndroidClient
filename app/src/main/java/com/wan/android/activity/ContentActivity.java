package com.wan.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

/**
 * @author wzc
 * @date 2018/2/1
 */
public class ContentActivity extends BaseActivity {

    private LinearLayout mLinearLayoutContainer;
    private TextView mTitle;
    private static final String EXTRA_CONTENT_URL = "extra_content_url";
    private AgentWeb mAgentWeb;

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, ContentActivity.class);
        starter.putExtra(EXTRA_CONTENT_URL, url);
        context.startActivity(starter);
    }
    private String mUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        if (getIntent()!=null) {
            mUrl = getIntent().getStringExtra(EXTRA_CONTENT_URL);
        }
        mLinearLayoutContainer = (LinearLayout) findViewById(R.id.linearlayout_activity_content_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_content);
        mTitle = (TextView) findViewById(R.id.tv_activity_content_title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mTitle.setText(R.string.loading);
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

        return super.onOptionsItemSelected(item);
    }

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
            if (mTitle != null) {
                mTitle.setText(title != null ? title : "");
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

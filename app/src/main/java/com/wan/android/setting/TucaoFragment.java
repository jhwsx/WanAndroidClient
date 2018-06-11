package com.wan.android.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.base.BaseFragment;
import com.wan.android.constant.SpConstants;
import com.wan.android.constant.UrlConstants;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.util.Utils;

import java.util.Random;
import java.util.UUID;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class TucaoFragment extends BaseFragment {
    private static final String TAG = TucaoFragment.class.getSimpleName();
    private LinearLayout mLinearLayoutContainer;
    private AgentWeb mAgentWeb;

    public static TucaoFragment newInstance() {
        Bundle args = new Bundle();
        TucaoFragment fragment = new TucaoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View root = LayoutInflater.from(mActivity).inflate(R.layout.content_frag, null);
        mSwipeRefreshLayout.addView(root);
        // 设置可下拉刷新的子view
        mSwipeRefreshLayout.setSwipeableChildren(R.id.root_content_frag);
        mLinearLayoutContainer = (LinearLayout) view.findViewById(R.id.root_content_frag);
        mAgentWeb = AgentWeb.with(this)
                // 设置父容器
                .setAgentWebParent(mLinearLayoutContainer, new ViewGroup.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setWebView(new WebView(mActivity) {
                    // 这里是为了解决webview下滑时和下拉刷新冲突的问题
                    @Override
                    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
                        super.onScrollChanged(l, t, oldl, oldt);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "onScrollChanged: l = " + l + ", t = " + t + ", oldl = " + oldl + ", oldt = " + oldt + ", getScrollY() = " + getScrollY());
                        }
                        if (this.getScrollY() == 0) {
                            mSwipeRefreshLayout.setEnabled(true);
                        } else {
                            mSwipeRefreshLayout.setEnabled(false);
                        }
                    }
                })

                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()
                .ready()

                .go(UrlConstants.URL_TUCAO);
        String nickname = PreferenceUtils.getString(getContext(), SpConstants.KEY_USERNAME, "");                        // 用户的nickname
        String headimgurl = "https://tucao.qq.com/static/v2/img/avatar/"+getHeadPicId()+".svg";  // 用户的头像url
        String openid = getOpenId();  // 用户的openid
        String postData = "nickname=" + nickname + "&avatar=" + headimgurl + "&openid=" + openid;

        mAgentWeb.getLoader().postUrl(UrlConstants.URL_TUCAO, postData.getBytes());

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    mAgentWeb.getLoader().reload();
                }
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);

            if (url == null) {
                return  false;
            }
            try {
                if (url.startsWith("weixin://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
            view.loadUrl(url);
            return true;
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

        }
    };

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    public boolean handleKeyEvent(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event);
    }

    public String getOpenId() {
        String openId = PreferenceUtils.getString(Utils.getContext(), SpConstants.KEY_OPEN_ID, "");
        if (TextUtils.isEmpty(openId)) {
            openId = UUID.randomUUID().toString();
            PreferenceUtils.putString(Utils.getContext(), SpConstants.KEY_OPEN_ID, openId);
        }
        return openId;

    }

    public int getHeadPicId() {
        int headPicId = PreferenceUtils.getInt(Utils.getContext(), SpConstants.KEY_HEAD_PIC_ID, 0);
        if (headPicId == 0) {
            headPicId = new Random().nextInt(100) + 1;
            PreferenceUtils.putInt(Utils.getContext(), SpConstants.KEY_HEAD_PIC_ID, headPicId);
        }
        return headPicId;

    }
}

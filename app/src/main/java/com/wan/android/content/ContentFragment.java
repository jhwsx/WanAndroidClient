package com.wan.android.content;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.just.agentweb.AgentWeb;
import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.base.BaseFragment;
import com.wan.android.constant.SpConstants;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ContentCollectEvent;
import com.wan.android.loginregister.LoginActivity;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.util.Utils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class ContentFragment extends BaseFragment implements ContentContract.View{
    private static final String TAG = ContentFragment.class.getSimpleName();
    private static final String ARG_CONTENT_URL = "arg_content_url";
    private static final String ARG_ID = "arg_id";
    private static final String ARG_TITLE = "arg_title";
    private LinearLayout mLinearLayoutContainer;
    private AgentWeb mAgentWeb;
    private ContentContract.Presenter mPresenter;
    public static ContentFragment newInstance(String title, String url, int id) {
        Bundle args = new Bundle();
        args.putString(ARG_CONTENT_URL, url);
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_ID, id);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String mUrl;
    private int mId;
    private String mTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_CONTENT_URL);
            mTitle = getArguments().getString(ARG_TITLE);
            mId = getArguments().getInt(ARG_ID);
        }
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
                .setWebView(new WebView(mActivity){
                    // 这里是为了解决webview下滑时和下拉刷新冲突的问题
                    @Override
                    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
                        super.onScrollChanged(l, t, oldl, oldt);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "onScrollChanged: l = " + l + ", t = " + t + ", oldl = " + oldl + ", oldt = " + oldt + ", getScrollY() = " + getScrollY() );
                        }
                        if (this.getScrollY() == 0){
                            mSwipeRefreshLayout.setEnabled(true);
                        }else {
                            mSwipeRefreshLayout.setEnabled(false);
                        }
                    }
                })
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()
                .ready()
                .go(mUrl);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_content, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.finish();
                return true;
            case R.id.action_activity_content_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                // 添加要分享的内容
                shareIntent.putExtra(Intent.EXTRA_TEXT, mTitle + ": " + mUrl);
                // Activity 间通过隐式 Intent 的跳转，在发出 Intent 之前必须通过 resolveActivity
                // 检查，避免找不到合适的调用组件，造成 ActivityNotFoundException 的异常
                if (mActivity.getPackageManager().resolveActivity(shareIntent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    try {
                        startActivity(Intent.createChooser(shareIntent, mActivity.getString(R.string.share)));
                    } catch (ActivityNotFoundException e) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "activity not found", e);
                        }
                    }

                }

                return true;
            case R.id.action_activity_content_collect:
                if (TextUtils.isEmpty(PreferenceUtils.getString(Utils.getContext(), SpConstants.KEY_USERNAME, ""))) {
                    Toast.makeText(Utils.getContext(), R.string.login_first, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                    return true;
                }
                mPresenter.collect(mId);
                return true;
            case R.id.action_activity_content_open_with_system_browser:
                final Uri uri = Uri.parse(mUrl);
                final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                if (mActivity.getPackageManager().resolveActivity(it, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    try {
                        startActivity(it);
                    } catch (ActivityNotFoundException e) {
                        Log.e(TAG, "activity not found", e);
                    }
                }
                return true;
            default:
                break;
        }
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
            if (mActivity != null) {
                ((OnReceivedTitleListener)mActivity).onReceiveTitle(title != null ? title : "");
            }
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

    @Override
    public void showCollectSuccess() {
        EventBus.getDefault().post(new ContentCollectEvent());
        Toast.makeText(Utils.getContext(), R.string.collect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCollectFail(CommonException e) {
        Toast.makeText(Utils.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ContentContract.Presenter presenter) {
        mPresenter = presenter;
    }
}

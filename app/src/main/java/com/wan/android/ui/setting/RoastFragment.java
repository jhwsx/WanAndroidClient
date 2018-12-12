package com.wan.android.ui.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;
import com.wan.android.R;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.util.constant.AppConstants;

import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 吐个槽 Fragment
 *
 * @author wzc
 * @date 2018/8/29
 */
public class RoastFragment extends BaseFragment {

    private static final String TAG = RoastFragment.class.getSimpleName();
    public static RoastFragment newInstance() {
        Bundle args = new Bundle();
        RoastFragment fragment = new RoastFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AgentWeb mAgentWeb;
    @BindView(R.id.fl_roast_container)
    FrameLayout mFlContainer;
    @Inject
    RoastPresenter<RoastContract.View> mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.roast_fragment, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
        }
        return view;
    }

    @Override
    protected void setUp(View view) {
        mAgentWeb = AgentWeb.with(this)
                // 设置父容器
                .setAgentWebParent(mFlContainer, new ViewGroup.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .createAgentWeb()
                .ready()
                .go(AppConstants.URL_TUCAO);
        String nickname = mPresenter.getUsername();  // 用户的nickname
        String headimgurl = "https://tucao.qq.com/static/v2/img/avatar/" + getHeadPicId() + ".svg";  // 用户的头像url
        String openid = getOpenId(nickname);  // 用户的openid
        String postData = "nickname=" + nickname + "&avatar=" + headimgurl + "&openid=" + openid;

        mAgentWeb.getUrlLoader().postUrl(AppConstants.URL_TUCAO, postData.getBytes());
    }

    @Override
    protected String getFragmentName() {
        return TAG;
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);

            if (url == null) {
                return false;
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

    public String getOpenId(String username) {
        String openId = mPresenter.getRoastOpenid();
        if (TextUtils.isEmpty(openId)) {
            openId = String.valueOf(username.hashCode());
            mPresenter.setRoastOpenid(openId);
        }
        return openId;

    }

    public int getHeadPicId() {
        int headPicId = mPresenter.getRoastHeadPicId();
        if (headPicId == 0) {
            headPicId = new Random().nextInt(100) + 1;
            mPresenter.setRoastHeadPicId(headPicId);
        }
        return headPicId;

    }
}

package com.wan.android.ui.content;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;
import com.wan.android.R;
import com.wan.android.data.network.model.ContentData;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.ui.login.LoginActivity;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wzc
 * @date 2018/8/17
 */
public class ContentFragment extends BaseFragment
        implements ContentContract.View {
    private static final String ARGS_CONTENT_DATA = "com.wan.android.args_content_data";
    private MenuItem mCollectItem;
    private AgentWeb mAgentWeb;

    public static ContentFragment newInstance(ContentData data) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CONTENT_DATA, data);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.fl_content_container)
    FrameLayout mFlContainer;
    private ContentData mContentData;
    @BindString(R.string.share)
    String mShareStr;
    @Inject
    ContentPresenter<ContentContract.View> mPresenter;
    private OnTitleReceiveListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnTitleReceiveListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mContentData = (ContentData) getArguments().getSerializable(ARGS_CONTENT_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
        return view;
    }

    @Override
    protected void setUp(View view) {

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mFlContainer,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()
                .ready()
                .go(mContentData.getUrl());


    }

    public boolean handleKeyEvent(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mAgentWeb.getWebLifeCycle().onPause();

    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void showCollectInSiteArticleSuccess() {
        showMessage(R.string.collect_successfully);
        setCollectState(true);
    }

    @Override
    public void showUncollectArticleListArticleSuccess() {
        showMessage(R.string.uncollect_successfully);
        setCollectState(false);
    }

    private void setCollectState(boolean isCollect) {
        mContentData.setCollect(isCollect);
        mCollectItem.setTitle(isCollect ? R.string.uncollect : R.string.collect);

    }

    private WebViewClient mWebViewClient = new WebViewClient() {
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mListener != null) {
                mListener.onTitleReceived(title);
            }
        }
    };

    public interface OnTitleReceiveListener {
        void onTitleReceived(String title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mCollectItem = menu.findItem(R.id.action_activity_content_collect);
        mCollectItem.setVisible(mContentData.getCollect() != null);
        if (mContentData.getCollect() != null) {
            setCollectState(mContentData.getCollect());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_activity_content_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, mContentData.getTitle() + ":" + mContentData.getUrl());
            if (getBaseActivity().getPackageManager()
                    .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                try {
                    getBaseActivity().startActivity(Intent.createChooser(intent, mShareStr));
                } catch (Exception e) {
                    // do nothing
                }
            }
            return true;
        } else if (itemId == R.id.action_activity_content_collect) {
            int id = (int) mContentData.getId().longValue();
            if (!mPresenter.getLoginStaus()) {
                LoginActivity.start(getBaseActivity());
                return false;
            }
            if (mContentData.getCollect()) {
                mPresenter.uncollectArticleListArticle(id);
            } else {
                mPresenter.collectInSiteArticle(id);
            }
            return true;
        } else if (itemId == R.id.action_activity_content_open_with_system_browser) {
            final Uri uri = Uri.parse(mContentData.getUrl());
            final Intent it = new Intent(Intent.ACTION_VIEW, uri);
            if (getBaseActivity().getPackageManager()
                    .resolveActivity(it, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                try {
                    startActivity(it);
                } catch (ActivityNotFoundException e) {
                    // do nothing
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.wan.android.ui.content;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tencent.smtt.sdk.CookieSyncManager;
import com.wan.android.R;
import com.wan.android.data.network.model.ContentData;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wzc
 * @date 2018/8/17
 */
public class ContentFragment extends BaseFragment implements X5WebView.OnTitleReceiveListener {
    private static final String ARGS_CONTENT_DATA = "com.wan.android.args_content_data";
    public static ContentFragment newInstance(ContentData data) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CONTENT_DATA, data);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @BindView(R.id.fl_content_container)
    FrameLayout mFlContainer;
    @Inject
    X5WebView mX5WebView;
    private ContentData mContentData;

    private OnTitleReceiveListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnTitleReceiveListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        }
        return view;
    }

    @Override
    protected void setUp(View view) {
        mX5WebView.setOnTitleReceiveListener(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mFlContainer.addView(mX5WebView, layoutParams);
        mX5WebView.loadUrl(mContentData.getUrl());
        CookieSyncManager.createInstance(getContext());
        CookieSyncManager.getInstance().sync();
    }


    public boolean goBack() {
        if (mX5WebView != null && mX5WebView.canGoBack()) {
            mX5WebView.goBack();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mX5WebView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mX5WebView.onResume();
    }

    @Override
    public void onDestroy() {
        if (mX5WebView != null) {
            mX5WebView.destroy();
            mX5WebView = null;
        }
        super.onDestroy();

    }

    @Override
    public void onTitleReceived(String title) {
        if (mListener != null) {
            mListener.onTitleReceived(title);
        }
    }

    public interface OnTitleReceiveListener {
        void onTitleReceived(String title);
    }

}

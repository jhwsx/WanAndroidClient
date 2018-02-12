package com.wan.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wan.android.R;
import com.wan.android.view.MultiSwipeRefreshLayout;

/**
 * @author wzc
 * @date 2018/2/12
 */
public class CommonUseFragment extends BaseFragment {

    public static CommonUseFragment newInstance(int cid) {
        
        Bundle args = new Bundle();
        
        CommonUseFragment fragment = new CommonUseFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MultiSwipeRefreshLayout rootView = (MultiSwipeRefreshLayout) inflater.inflate(R.layout.fragment_common_use, container, false);
        return rootView;
    }
}

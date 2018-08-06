package com.wan.android.ui.tree;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wan.android.R;

/**
 * @author wzc
 * @date 2018/8/3
 */
public class TreeFragment extends Fragment {
    public static TreeFragment newInstance() {

        Bundle args = new Bundle();

        TreeFragment fragment = new TreeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tree_fragment, container, false);
    }
}

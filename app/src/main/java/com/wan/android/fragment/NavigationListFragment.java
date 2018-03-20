package com.wan.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wan.android.R;
import com.wan.android.activity.ContentActivity;
import com.wan.android.bean.ArticleDatas;
import com.wan.android.util.Colors;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

/**
 * 导航列表片段
 * @author wzc
 * @date 2018/3/20
 */
public class NavigationListFragment extends Fragment {
    private static final String ARG_NAVIGATION_DATAS = "arg_navigation_datas";
    public static NavigationListFragment newInstance(ArrayList<ArticleDatas> datas) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_NAVIGATION_DATAS, datas);
        NavigationListFragment fragment = new NavigationListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View mView;
    ArrayList<ArticleDatas> mData;
    private Activity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mData = (ArrayList<ArticleDatas>) getArguments().getSerializable(ARG_NAVIGATION_DATAS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_navigation_list, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TagFlowLayout tagFlowLayout = (TagFlowLayout) mView.findViewById(R.id.navigation_list_flow_layout);
        final ArrayList<Integer> colors = Colors.randomList(mData.size());
        tagFlowLayout.setAdapter(new TagAdapter<ArticleDatas>(mData) {
            @Override
            public View getView(FlowLayout parent, int position, ArticleDatas d) {
                TextView textView = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.tv, tagFlowLayout, false);
                textView.setText(d.getTitle());
                textView.setTextColor(colors.get(position));
                return textView;
            }
        });
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ArticleDatas d = mData.get(position);
                ContentActivity.start(mActivity, d.getTitle(),d.getLink(),d.getId());
                return true;
            }
        });
    }
}

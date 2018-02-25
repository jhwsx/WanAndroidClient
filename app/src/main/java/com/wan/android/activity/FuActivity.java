package com.wan.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wan.android.R;
import com.wan.android.util.Colors;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 常用页面
 *
 * @author wzc
 * @date 2018/2/24
 */
public class FuActivity extends BaseActivity {
    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView"};
    private TagFlowLayout mTagFlowLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_fu);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final ArrayList<Integer> colors = Colors.randomList(mVals.length);
        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        mTagFlowLayout.setAdapter(new TagAdapter<String>(Arrays.asList(mVals)) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tv, mTagFlowLayout, false);
                textView.setText(s);
                textView.setTextColor(colors.get(position));
                return textView;
            }
        });
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(mContext, mVals[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}

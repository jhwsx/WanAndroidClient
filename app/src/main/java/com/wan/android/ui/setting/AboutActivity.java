package com.wan.android.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.wan.android.R;
import com.wan.android.ui.base.BaseActivity;
import com.wan.android.util.AppUtils;

/**
 * 关于
 *
 * @author wzc
 * @date 2018/8/30
 */
public class AboutActivity extends BaseActivity {

    private static final String TAG = AboutActivity.class.getSimpleName();
    public static void start(Context context) {
        Intent starter = new Intent(context, AboutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.about));
        TextView tvAppVersionName = (TextView) findViewById(R.id.tv_app_version_name);
        tvAppVersionName.setText(String.format(getString(R.string.text_v), AppUtils.getAppVersionName()));
        TextView tvContent = (TextView) findViewById(R.id.tv_about_content);
        tvContent.setText(Html.fromHtml(getString(R.string.about_content)));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected boolean hasFragment() {
        return false;
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void setUp() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

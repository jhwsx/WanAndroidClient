package com.wan.android.ui.setting;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.TextUtils;

import com.wan.android.R;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.base.BaseActivity;
import com.wan.android.ui.login.LoginActivity;
import com.wan.android.util.AppUtils;

import java.io.File;

import javax.inject.Inject;

/**
 * @author wzc
 * @date 2018/8/28
 */
public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceClickListener {
    private Preference mPreferenceScore;
    private Preference mPreferenceShareApk;
    private Preference mPreferenceServiceFeedback;
    private Preference mPreferenceTucao;
    private Preference mPreferenceAbout;
    private BaseActivity mActivity;
    @Inject
    SettingsPresenter<SettingsContract.View> mPresenter;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from the xml.
        addPreferencesFromResource(R.xml.app_preferences);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
        }
        initPreferences();

        setPreferencesListeners();
    }

    private void initPreferences() {
        mPreferenceScore = findPreference(getString(R.string.key_score));
        mPreferenceShareApk = findPreference(getString(R.string.key_share_apk));
        mPreferenceServiceFeedback = findPreference(getString(R.string.key_service_feedback));
        mPreferenceTucao = findPreference(getString(R.string.key_tucao_feedback));
        mPreferenceAbout = findPreference(getString(R.string.key_about));
    }

    private void setPreferencesListeners() {
        mPreferenceScore.setOnPreferenceClickListener(this);
        mPreferenceShareApk.setOnPreferenceClickListener(this);
        mPreferenceServiceFeedback.setOnPreferenceClickListener(this);
        mPreferenceTucao.setOnPreferenceClickListener(this);
        mPreferenceAbout.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (TextUtils.equals(preference.getKey(), getString(R.string.key_score))) {
            scoreApp();
            return true;
        }

        if (TextUtils.equals(preference.getKey(), getString(R.string.key_share_apk))) {
            shareFile();
            return true;
        }

        if (TextUtils.equals(preference.getKey(), getString(R.string.key_service_feedback))) {
            serviceFeedback();
            return true;
        }

        if (TextUtils.equals(preference.getKey(), getString(R.string.key_tucao_feedback))) {
            if (mPresenter.getLoginStatus()) {
                RoastActivity.start(mActivity);
            } else {
                LoginActivity.start(mActivity);
            }
            return true;
        }
        if (TextUtils.equals(preference.getKey(), getString(R.string.key_about))) {
            AboutActivity.start(mActivity);
            return true;
        }
        return false;
    }

    private void scoreApp() {
        String appPkg = AppUtils.getAppPackageName();
        Uri uri = Uri.parse("market://details?id=" + appPkg);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getActivity().getPackageManager().resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.score)));
            } catch (ActivityNotFoundException e) {
                // ignored
            }
        }
    }

    private void shareFile() {
        File apkFile = AppUtils.getApkFile();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile));
        if (getActivity().getPackageManager().resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
            } catch (ActivityNotFoundException e) {
                // ignored
            }
        }
    }

    private void serviceFeedback() {
        if (AppUtils.isAppInstalled("com.tencent.mobileqq")) {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=765684580";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // ignored
                }
            }
        }
    }

    public ActivityComponent getActivityComponent() {
        return mActivity.getActivityComponent();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }
}

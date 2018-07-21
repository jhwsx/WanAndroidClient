package com.wan.android.setting;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.constant.SpConstants;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.event.LogoutMessageEvent;
import com.wan.android.data.bean.VersionUpdateData;
import com.wan.android.loginregister.LoginActivity;
import com.wan.android.my.AboutActivity;
import com.wan.android.util.AppUtils;
import com.wan.android.util.EdgeEffectUtils;
import com.wan.android.util.NightModeUtils;
import com.wan.android.util.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * @author wzc
 * @date 2018/5/28
 */
public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceClickListener, SettingsContract.View {

    private static final String TAG = SettingsFragment.class.getSimpleName();
    private Preference mPreferenceScore;
    private Preference mPreferenceShareApk;
    private Preference mPreferenceServiceFeedback;
    private Preference mPreferenceTucao;
    private Preference mPreferenceCheckUpdate;
    private Preference mPreferenceAbout;
    private Preference mPreferenceLogout;
    private String mWanAndroidApkFilePath;
    private SettingsContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from the xml.
        addPreferencesFromResource(R.xml.app_preferences);

        initPreferences();

        initData();

        setPreferencesListeners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView = getListView();
        mRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(),
                NightModeUtils.isNightMode() ? R.color.color_background_night : R.color.color_background));
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                EdgeEffectUtils.setRecyclerViewEdgeEffect(mRecyclerView);
            }
        });
        return view;
    }

    private void initData() {
        String username = PreferenceUtils.getString(getContext(), SpConstants.KEY_USERNAME, "");
        mPreferenceLogout.setVisible(!TextUtils.isEmpty(username));
    }

    private void initPreferences() {
        mPreferenceScore = findPreference(getString(R.string.key_score));
        mPreferenceShareApk = findPreference(getString(R.string.key_share_apk));
        mPreferenceServiceFeedback = findPreference(getString(R.string.key_service_feedback));
        mPreferenceTucao = findPreference(getString(R.string.key_tucao_feedback));
        mPreferenceCheckUpdate = findPreference(getString(R.string.key_check_update));
        mPreferenceAbout = findPreference(getString(R.string.key_about));
        mPreferenceLogout = findPreference(getString(R.string.key_logout));
    }

    private void setPreferencesListeners() {
        mPreferenceScore.setOnPreferenceClickListener(this);
        mPreferenceShareApk.setOnPreferenceClickListener(this);
        mPreferenceServiceFeedback.setOnPreferenceClickListener(this);
        mPreferenceTucao.setOnPreferenceClickListener(this);
        mPreferenceCheckUpdate.setOnPreferenceClickListener(this);
        mPreferenceAbout.setOnPreferenceClickListener(this);
        mPreferenceLogout.setOnPreferenceClickListener(this);
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
            String username = PreferenceUtils.getString(getContext(), SpConstants.KEY_USERNAME, "");
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(getActivity(), R.string.please_login_first, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                TucaoActivity.start(getActivity());
            }

            return true;
        }

        if (TextUtils.equals(preference.getKey(), getString(R.string.key_check_update))) {
            mPresenter.checkVersion();
            return true;
        }

        if (TextUtils.equals(preference.getKey(), getString(R.string.key_about))) {
            AboutActivity.start(getActivity());
            return true;
        }

        if (TextUtils.equals(preference.getKey(), getString(R.string.key_logout))) {
            logout();
            return true;
        }

        return false;
    }

    private void logout() {
        PreferenceUtils.putString(getActivity(), SpConstants.KEY_USERNAME, "");
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getActivity()));
        cookieJar.clear();
        cookieJar.clearSession();
        EventBus.getDefault().post(new LogoutMessageEvent());

        initData();

        getActivity().finish();
    }

    private void serviceFeedback() {
        if (AppUtils.isAppInstalled("com.tencent.mobileqq")) {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=765684580";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "activity not found", e);
                    }
                }
            }
        }
    }

    private void scoreApp() {
        String appPkg = AppUtils.getAppPackageName();
        Uri uri = Uri.parse("market://details?id=" + appPkg);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.score)));
            } catch (ActivityNotFoundException e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "activity not found", e);
                }
            }
        }
    }

    public void shareFile() {
        File apkFile = AppUtils.getApkFile();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile));
        if (getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
            } catch (ActivityNotFoundException e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "activity not found", e);
                }
            }
        }
    }

    @Override
    public void showBadNetwork() {
        Toast.makeText(getActivity(), R.string.check_network, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("正在检查更新");
        mProgressDialog.show();
    }

    @Override
    public void showNewVersionDialog(final VersionUpdateData data) {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
            mProgressDialog = null;
        }
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.new_version)
                .setMessage(getActivity().getString(R.string.new_version_info, data.getVersionName(), data.getInfo()))
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.startDownloadNewVersion(data);
                    }
                }).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    @Override
    public void showHadLatestVersion() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
            mProgressDialog = null;
        }
        Toast.makeText(getActivity(), R.string.up_to_date_version, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCheckVersionFail(CommonException e) {
        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

}

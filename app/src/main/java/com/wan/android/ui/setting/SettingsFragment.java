package com.wan.android.ui.setting;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.wan.android.R;

/**
 * @author wzc
 * @date 2018/8/28
 */
public class SettingsFragment extends PreferenceFragmentCompat {
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

    }
}

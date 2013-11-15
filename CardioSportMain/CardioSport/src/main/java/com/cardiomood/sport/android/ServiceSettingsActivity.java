package com.cardiomood.sport.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.view.MenuItem;
import com.cardiomood.sport.android.tools.config.ConfigurationConstants;
import com.cardiomood.sport.android.tools.config.PreferenceHelper;
import com.cardiomood.sport.android.tools.PreferenceActivityBase;

/**
 * Project: CardioSport
 * User: danon
 * Date: 15.06.13
 * Time: 17:20
 */
public class ServiceSettingsActivity extends PreferenceActivityBase<ServiceSettingsActivity.ServiceSettingsFragment> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public ServiceSettingsFragment createMainFragment() {
        return new ServiceSettingsFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ServiceSettingsFragment extends PreferenceActivityBase.AbstractMainFragment implements ConfigurationConstants {

        private EditTextPreference mProtocolPref;
        private EditTextPreference mHostPref;
        private EditTextPreference mPortPref;
        private EditTextPreference mPathPref;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.service_settings);

            PreferenceHelper helper = new PreferenceHelper(getActivity().getApplicationContext());
            helper.setPersistent(true);
            helper.putString(SERVICE_HOST, helper.getString(SERVICE_HOST, DEFAULT_SERVICE_HOST));
            helper.putString(SERVICE_PROTOCOL, helper.getString(SERVICE_PROTOCOL, DEFAULT_SERVICE_PROTOCOL));
            helper.putString(SERVICE_PORT, helper.getString(SERVICE_PORT, DEFAULT_SERVICE_PORT));
            helper.putString(SERVICE_PATH, helper.getString(SERVICE_PATH, DEFAULT_SERVICE_PATH));

            mProtocolPref = (EditTextPreference) findPreference(SERVICE_PROTOCOL);
            mHostPref = (EditTextPreference) findPreference(SERVICE_HOST);
            mPortPref = (EditTextPreference) findPreference(SERVICE_PORT);
            mPathPref = (EditTextPreference) findPreference(SERVICE_PATH);

            refreshSummaries();
        }

        private void refreshSummaries() {
            updatePrefSummary(mProtocolPref, mHostPref, mPortPref, mPathPref);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            refreshSummaries();
        }
    }
}
package com.cardiomood.sport.android.tools;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.*;
import android.text.TextUtils;

public abstract class PreferenceActivityBase<F extends PreferenceActivityBase.AbstractMainFragment> extends PreferenceActivity {

	private F fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragment = createMainFragment();
		
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, fragment).commit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Register the listener whenever a key changes
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(fragment);
	}
	
	@Override
    protected void onPause() {
        super.onPause();

        // Unregister the listener whenever a key changes            
        PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(fragment); 
    }
	
	public F getFragment() {
		return fragment;
	}
	
	public abstract F createMainFragment();
	
	public abstract static class AbstractMainFragment extends PreferenceFragment
													  implements OnSharedPreferenceChangeListener {
		public void updatePrefSummary(String defaultSummary, Preference... prefs) {
			for (Preference pref : prefs) {
				if (pref instanceof EditTextPreference) {
					EditTextPreference etp = (EditTextPreference) pref;
				    if (!TextUtils.isEmpty(etp.getText()))
				    	etp.setSummary(etp.getText());
				    else etp.setText(defaultSummary);
				} else if (pref instanceof ListPreference) {
					ListPreference lp = (ListPreference) pref;
				    if (!TextUtils.isEmpty(lp.getValue()))
				    	lp.setSummary(lp.getEntry());
				    else lp.setSummary(defaultSummary);
				}
			}
		}
		
		public void updatePrefSummary(Preference... prefs) {
			for (Preference pref : prefs) {
				if (pref instanceof EditTextPreference) {
					EditTextPreference etp = (EditTextPreference) pref;
				    	etp.setSummary(etp.getText());
				} else if (pref instanceof ListPreference) {
					ListPreference lp = (ListPreference) pref;
				    	lp.setSummary(lp.getEntry());
				}
			}
		}
		
	}
}

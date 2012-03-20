package com.svenkapudija.best.hr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PreferencesActivity extends PreferenceActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
		
		final CheckBoxPreference showPastEvents = (CheckBoxPreference) findPreference("show_past_events");
		showPastEvents.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				SharedPreferences customSharedPreference = PreferenceManager.getDefaultSharedPreferences(PreferencesActivity.this);
				SharedPreferences.Editor editor = customSharedPreference.edit();
				
				if (showPastEvents.isChecked()) {
					editor.putBoolean("show_past_events", true);
				} else {
					editor.putBoolean("show_past_events", false);
				}
				
				editor.commit();
				
				return true;
			}
		});
    }
}
package com.svenkapudija.best.hr;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class PreferencesActivity extends PreferenceActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
		
        /*
		final CheckBoxPreference drawRoutes = (CheckBoxPreference) findPreference("draw_routes");
		drawRoutes.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				SharedPreferences customSharedPreference = getSharedPreferences("bestPreferences", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = customSharedPreference.edit();
				
				if (drawRoutes.isChecked()) {
					editor.putBoolean("draw_routes", true);
				} else {
					editor.putBoolean("draw_routes", false);
				}
				
				editor.commit();
				
				return true;
			}
		});
		*/
    }
}
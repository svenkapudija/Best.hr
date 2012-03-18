package com.svenkapudija.best.hr;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.localytics.android.LocalyticsSession;
import com.markupartist.android.widget.ActionBar;
import com.svenkapudija.best.hr.database.DatabaseHelper;
import com.svenkapudija.best.hr.utils.LocalyticsPreferences;
import com.svenkapudija.best.hr.utils.Preferences;

public class RootActivity extends Activity {

	private DatabaseHelper helper;
	protected SQLiteDatabase dbWriteable;
	protected ActionBar actionBar;
	protected LocalyticsSession localyticsSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		helper = new DatabaseHelper(this);
		dbWriteable = helper.getWritableDatabase();
		
		this.localyticsSession = new LocalyticsSession(this.getApplicationContext(), LocalyticsPreferences.LOCALYTICS_APP_KEY);
		this.localyticsSession.open();
		this.localyticsSession.upload();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		localyticsSession.open();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		this.localyticsSession.close();
        this.localyticsSession.upload();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	public void showToast(String value) {
		Toast.makeText(RootActivity.this, value, Toast.LENGTH_LONG).show();
	}
	
	public void showToast(String value, boolean lengthShort) {
		if (lengthShort) {
			Toast.makeText(RootActivity.this, value, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(RootActivity.this, value, Toast.LENGTH_LONG).show();
		}
	}
}

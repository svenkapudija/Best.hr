package com.svenkapudija.best.hr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.utils.LocalyticsPreferences;

public class MainActivity extends RootActivity {
	
	private ImageButton news;
	private ImageButton events;
	private ImageButton reports;
	private ImageButton members;
	private ImageButton contact;
	
	private void getUIElements() {
		news = (ImageButton) findViewById(R.id.news);
		events = (ImageButton) findViewById(R.id.events);
		reports = (ImageButton) findViewById(R.id.reports);
		members = (ImageButton) findViewById(R.id.members);
		contact = (ImageButton) findViewById(R.id.contact);
	}

	public void setupActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.addAction(new Action() {
			public void performAction(View view) {
				localyticsSession.tagEvent(LocalyticsPreferences.HOME_ACTIVITY_ACTIONBAR_INFO);
				Intent i = new Intent(MainActivity.this, InfoActivity.class);
				startActivityForResult(i, 200);
			}

			public int getDrawable() {
				return R.drawable.actionbar_info;
			}

			public CharSequence getText() {
				return "";
			}
		});
		actionBar.addAction(new Action() {
			public void performAction(View view) {
				localyticsSession.tagEvent(LocalyticsPreferences.HOME_ACTIVITY_ACTIONBAR_OPTIONS);
				Intent i = new Intent(MainActivity.this, PreferencesActivity.class);
				startActivityForResult(i, 200);
			}

			public int getDrawable() {
				return R.drawable.actionbar_options;
			}

			public CharSequence getText() {
				return "";
			}
		});
		
		actionBar.setHome(R.drawable.actionbar_logotype);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getUIElements();
        setupActionBar();
        
        news.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				localyticsSession.tagEvent(LocalyticsPreferences.HOME_ACTIVITY_NEWS);
				Intent i = new Intent(MainActivity.this, NewsActivity.class);
				startActivityForResult(i, 200);
			}
		});
        
        events.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				localyticsSession.tagEvent(LocalyticsPreferences.HOME_ACTIVITY_EVENTS);
				Intent i = new Intent(MainActivity.this, EventsActivity.class);
				startActivityForResult(i, 200);
			}
		});
        
        reports.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				localyticsSession.tagEvent(LocalyticsPreferences.HOME_ACTIVITY_REPORTS);
				Intent i = new Intent(MainActivity.this, AnnualReportsActivity.class);
				startActivityForResult(i, 200);
			}
		});
        
        members.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				localyticsSession.tagEvent(LocalyticsPreferences.HOME_ACTIVITY_MEMBERS);
				Intent i = new Intent(MainActivity.this, MembersActivity.class);
				startActivityForResult(i, 200);
			}
		});
        
        contact.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				localyticsSession.tagEvent(LocalyticsPreferences.HOME_ACTIVITY_CONTACT);
				Intent i = new Intent(MainActivity.this, ContactActivity.class);
				startActivityForResult(i, 200);
			}
		});
    }
}
package com.svenkapudija.best.hr;

import com.markupartist.android.widget.ActionBar;

import android.content.Intent;
import android.drm.DrmStore.Action;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.svenkapudija.best.hr.internet.SimpleHttpClient;
import com.svenkapudija.best.hr.models.AnnualReport;
import com.svenkapudija.best.hr.models.Event;
import com.svenkapudija.best.hr.models.Member;
import com.svenkapudija.best.hr.models.News;
import com.svenkapudija.best.hr.utils.LocalyticsPreferences;
import com.svenkapudija.best.hr.utils.Utils;

/**
 * Application made for BEST Code Challenge 2.0 by BEST.hr 
 * 
 * @author Sven Kapuðija
 *
 */
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
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
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
				// If user didn't download any news and doesn't have internet connection, popup a dialog
		        if(News.getCount(dbWriteable) == 0 && !SimpleHttpClient.haveConnection(MainActivity.this)) {
		        	Utils.noInternetConnectionDialog(MainActivity.this, getString(R.string.no_internet_connection_message_data));
				} else {
					Intent i = new Intent(MainActivity.this, NewsActivity.class);
					startActivityForResult(i, 200);
				}
			}
		});
        
        events.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				localyticsSession.tagEvent(LocalyticsPreferences.HOME_ACTIVITY_EVENTS);
				
				if(Event.getCount(dbWriteable) == 0 && !SimpleHttpClient.haveConnection(MainActivity.this)) {
		        	Utils.noInternetConnectionDialog(MainActivity.this, getString(R.string.no_internet_connection_message_data));
				} else {
					Intent i = new Intent(MainActivity.this, EventsActivity.class);
					startActivityForResult(i, 200);
				}
			}
		});
        
        reports.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				localyticsSession.tagEvent(LocalyticsPreferences.HOME_ACTIVITY_REPORTS);
				
				if(AnnualReport.getCount(dbWriteable) == 0 && !SimpleHttpClient.haveConnection(MainActivity.this)) {
		        	Utils.noInternetConnectionDialog(MainActivity.this, getString(R.string.no_internet_connection_message_data));
				} else {
					Intent i = new Intent(MainActivity.this, AnnualReportsActivity.class);
					startActivityForResult(i, 200);
				}
			}
		});
        
        members.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				localyticsSession.tagEvent(LocalyticsPreferences.HOME_ACTIVITY_MEMBERS);
				
				if(Member.getCount(dbWriteable) == 0 && !SimpleHttpClient.haveConnection(MainActivity.this)) {
		        	Utils.noInternetConnectionDialog(MainActivity.this, getString(R.string.no_internet_connection_message_data));
				} else {
					Intent i = new Intent(MainActivity.this, MembersActivity.class);
					startActivityForResult(i, 200);
				}
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
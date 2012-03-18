package com.svenkapudija.best.hr;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.internet.BestHrApi;
import com.svenkapudija.best.hr.models.Event;
import com.svenkapudija.best.hr.models.News;
import com.svenkapudija.best.hr.utils.Preferences;

public class MainActivity extends RootActivity {
	
	private void getUIElements() {
		
	}

	public void setupActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.addAction(new Action() {
			public void performAction(View view) {
				Intent i = new Intent(MainActivity.this, NewsActivity.class);
				startActivityForResult(i, 200);
			}

			public int getDrawable() {
				return R.drawable.action_bar_news;
			}

			public CharSequence getText() {
				return "";
			}
		});
		
		actionBar.setHome(R.drawable.action_bar_logotype);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getUIElements();
        setupActionBar();
        
        BestHrApi api = new BestHrApi(this);
        
        /*
        ArrayList<Event> seminars = Event.readAll(this.dbWriteable);
        for (Event event : seminars) {
			Log.d(Preferences.DEBUG_TAG, "Events: " + event.toString());
		}
        */
        
        /*
        ArrayList<Event> seminars = api.getSeminars();
        if(!seminars.isEmpty()) {
			for (Event event : seminars) {
				Log.d(Preferences.DEBUG_TAG, "Events: " + event.toString());
				event.setDatabase(this.dbWriteable);
				if(!event.exists()) {
					Log.d(Preferences.DEBUG_TAG, "Event does not exist!");
					event.insertOrUpdate();
				}
			}
		}
        */
        
        /*
        ArrayList<AnnualReport> reports = api.getAnnualReports();
        if(!reports.isEmpty()) {
			for (AnnualReport report : reports) {
				Log.d(Preferences.DEBUG_TAG, "Report: " + report.toString());
				report.setDatabase(dbWriteable);
				report.insertOrUpdate();
			}
		}
        */
        
        /*
        ArrayList<AnnualReport> reports = AnnualReport.readAll(this.dbWriteable);
        for (AnnualReport report1 : reports) {
			Log.d(Preferences.DEBUG_TAG, "Report: " + report1.toString());
		}
        */
        
        /*
        News news = api.getLastNews();
        Log.d(Preferences.DEBUG_TAG, "Last news is: " + news.toString());
        */
        
        /*
        ArrayList<Person> members = api.getBoardMembers();
		if(!members.isEmpty()) {
			for (Person member : members) {
				Log.d(Preferences.DEBUG_TAG, "Member: " + member.toString());
				member.setDatabase(dbWriteable);
				member.insertOrUpdate();
			}
		}
		*/
        
        /*
        ArrayList<Person> members = Person.readAll(this.dbWriteable);
        for (Person person : members) {
			Log.d(Preferences.DEBUG_TAG, "Person: " + person.toString());
		}
        */
        
        /*
        ArrayList<News> allNews = api.getNews();
		if(!allNews.isEmpty()) {
			for (News news : allNews) {
				Log.d(Preferences.DEBUG_TAG, "News: " + news.toString());
				news = api.getNews(news.getId());
				
				news.setDatabase(dbWriteable);
				//if(!news.exists()) news.insertOrUpdate();
			}
		}
		*/
    }
}
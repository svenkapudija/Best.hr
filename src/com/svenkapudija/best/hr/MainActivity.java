package com.svenkapudija.best.hr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.internet.BestHrApi;
import com.svenkapudija.best.hr.models.AnnualReport;
import com.svenkapudija.best.hr.models.Event;
import com.svenkapudija.best.hr.models.News;
import com.svenkapudija.best.hr.models.Person;
import com.svenkapudija.best.hr.utils.Preferences;

public class MainActivity extends RootActivity {
	
	private void getUIElements() {
		
	}

	public void setupActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.addAction(new Action() {
			public void performAction(View view) {
				
			}

			public int getDrawable() {
				return 0;
			}

			public CharSequence getText() {
				return "Kontakt";
			}
		});
		
		actionBar.setHomeAction(new Action() {
			public void performAction(View view) {
				
				setResult(RESULT_OK);
				finish();
			}

			public int getDrawable() {
				return R.drawable.icon;
			}

			public CharSequence getText() {
				return null;
			}
		});
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
				if(!event.exists())
					event.insertOrUpdate();
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
        
        ArrayList<Person> members = Person.readAll(this.dbWriteable);
        for (Person person : members) {
			Log.d(Preferences.DEBUG_TAG, "Person: " + person.toString());
		}
        
        /*
        ArrayList<News> allNews = api.getNews();
		if(!allNews.isEmpty()) {
			for (News news : allNews) {
				Log.d(Preferences.DEBUG_TAG, "News: " + news.toString());
				news = api.getNews(news.getId());
				news.setDatabase(dbWriteable);
				news.insertOrUpdate();
			}
		}
		*/
    }
}
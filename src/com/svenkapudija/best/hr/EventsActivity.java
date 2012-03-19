package com.svenkapudija.best.hr;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.adapters.EventAdapter;
import com.svenkapudija.best.hr.adapters.EventRow;
import com.svenkapudija.best.hr.api.BestHrApi;
import com.svenkapudija.best.hr.models.Event;
import com.svenkapudija.best.hr.utils.DateUtils;
import com.svenkapudija.best.hr.utils.Preferences;

public class EventsActivity extends RootActivity {
	
	private ListView listview;
	private EventAdapter listviewAdapter;
	private ArrayList<EventRow> listData = new ArrayList<EventRow>();
	
	private ArrayList<Event> events = new ArrayList<Event>();
	
	private void getUIElements() {
		listview = (ListView) findViewById(R.id.listview);
	}

	public void setupActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		
		// Refresh
		actionBar.addAction(new Action() {
			public void performAction(View view) {
				new DownloadEvents().execute();
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
        setContentView(R.layout.activity_events);
        
        getUIElements();
        setupActionBar();
        
        Calendar c = Calendar.getInstance(); // Current time
        listviewAdapter = new EventAdapter(this, listData, c.getTime());
		listview.setAdapter(listviewAdapter);
		
		events = Event.readAll(dbWriteable);
		if(events.isEmpty()) {
			new DownloadEvents().execute();
		} else {
			iterateThroughEvents();
		}
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				EventRow eventRow = listData.get(position);
				if(!eventRow.isHeader()) {
					Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(eventRow.getEvent().getUrl()));
				    startActivity(browse);
				}
			}
		});
    }
    
    /**
	 * Display refreshed data.
	 */
	private void iterateThroughEvents() {
		int month = 0;
		for (Event event : events) {
			Calendar c = Calendar.getInstance();
			c.setTime(event.getStartDate());
			if(c.get(Calendar.MONTH) != month) {
				month = c.get(Calendar.MONTH);
				listData.add(new EventRow(DateUtils.getMonthName(month) + " " + c.get(Calendar.YEAR) + "."));
			}
			listData.add(new EventRow(event));
		}
		listviewAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Class used for retrieving events from BEST API.
	 */
	private class DownloadEvents extends AsyncTask<String, Integer, Integer> {
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
			progressDialog = ProgressDialog.show(EventsActivity.this, null, "Preuzimam...");
		}
	
		@Override
		protected Integer doInBackground(String... params) {
			BestHrApi api = new BestHrApi(EventsActivity.this);
	        events = api.getEvents();
	        if(!events.isEmpty()) {
				for (Event event : events) {
					Log.d(Preferences.DEBUG_TAG, "Events: " + event.toString());
					event.setDatabase(EventsActivity.this.dbWriteable);
					if(!event.exists()) {
						event.insertOrUpdate();
					}
				}
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
			iterateThroughEvents();
			progressDialog.cancel();
		}
	}
}
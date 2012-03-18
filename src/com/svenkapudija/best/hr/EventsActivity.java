package com.svenkapudija.best.hr;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.adapters.EventAdapter;
import com.svenkapudija.best.hr.adapters.EventRow;
import com.svenkapudija.best.hr.internet.BestHrApi;
import com.svenkapudija.best.hr.models.Event;
import com.svenkapudija.best.hr.utils.DateUtils;

public class EventsActivity extends RootActivity {
	
	private EventAdapter listviewAdapter;
	private ArrayList<EventRow> rows = new ArrayList<EventRow>();
	private ListView listview;
	
	private void getUIElements() {
		listview = (ListView) findViewById(R.id.listview);
	}

	public void setupActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.addAction(new Action() {
			public void performAction(View view) {
				
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
        
        BestHrApi api = new BestHrApi(this);
        
		ArrayList<Event> events = Event.readAll(dbWriteable);
		int month = 0;
		if(!events.isEmpty()) {
			//rows.add(new PersonRow("Èlanovi uprave"));
			for (Event event : events) {
				Calendar c = Calendar.getInstance();
				c.setTime(event.getStartDate());
				if(c.get(Calendar.MONTH) != month) {
					month = c.get(Calendar.MONTH);
					rows.add(new EventRow(DateUtils.getMonthName(month) + " " + c.get(Calendar.YEAR) + "."));
				}
				rows.add(new EventRow(event));
			}
		}
		
		Calendar c = Calendar.getInstance();
		
		listviewAdapter = new EventAdapter(this, rows, c.getTime());
		listview.setAdapter(listviewAdapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				EventRow eventRow = rows.get(position);
				if(!eventRow.isHeader()) {
					Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(eventRow.getEvent().getUrl()));
				    startActivity(browse);
				}
			}
		});
    }
}
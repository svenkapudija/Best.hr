package com.svenkapudija.best.hr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.adapters.EventRow;
import com.svenkapudija.best.hr.adapters.PersonAdapter;
import com.svenkapudija.best.hr.adapters.PersonRow;
import com.svenkapudija.best.hr.api.BestHrApi;
import com.svenkapudija.best.hr.models.Event;
import com.svenkapudija.best.hr.models.Member;
import com.svenkapudija.best.hr.utils.DateUtils;
import com.svenkapudija.best.hr.utils.Preferences;

public class BoardMembersActivity extends RootActivity {
	
	private PersonAdapter listviewAdapter;
	private ArrayList<PersonRow> rows = new ArrayList<PersonRow>();
	private ListView listview;
	private ArrayList<Member> members = new ArrayList<Member>();
	
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
	
	private class DownloadMembers extends AsyncTask<String, Integer, Integer> {
		private ProgressDialog progressDialog;
		
		public DownloadMembers() {
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
			progressDialog = ProgressDialog.show(BoardMembersActivity.this, null, "Preuzimam...");
		}
	
		@Override
		protected Integer doInBackground(String... params) {
			BestHrApi api = new BestHrApi(BoardMembersActivity.this);
			members = api.getBoardMembers();
				if(!members.isEmpty()) {
					for (Member member : members) {
						member.setDatabase(dbWriteable);
						if(!member.exists())
							member.insertOrUpdate();
					}
				}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
			iterateThroughMembers();
			progressDialog.cancel();
		}
	}
	
	private void iterateThroughMembers() {
		rows.add(new PersonRow("BEST seminari"));
		for (Member member : members) {
			if(member.getType().equals("vivaldi"))
				rows.add(new PersonRow(member.getName(), member.getRole(), member.getEmail(),member.getPhone()));
		}
		
		rows.add(new PersonRow("Èlanovi uprave"));
		for (Member member : members) {
			if(member.getType().equals("board"))
				rows.add(new PersonRow(member.getName(), member.getRole(), member.getEmail(),member.getPhone()));
		}
		
		listviewAdapter.notifyDataSetChanged();
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_members);
        
        getUIElements();
        setupActionBar();

		listviewAdapter = new PersonAdapter(this, rows);
		listview.setAdapter(listviewAdapter);
		
        members = Member.readAll(dbWriteable);
		if(!members.isEmpty()) {
			iterateThroughMembers();
		} else {
			new DownloadMembers().execute();
		}
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
			}
		});
    }
}
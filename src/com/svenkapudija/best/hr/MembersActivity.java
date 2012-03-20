package com.svenkapudija.best.hr;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.adapters.PersonAdapter;
import com.svenkapudija.best.hr.adapters.PersonRow;
import com.svenkapudija.best.hr.api.BestHrApi;
import com.svenkapudija.best.hr.models.Member;

public class MembersActivity extends RootActivity {
	
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
				new DownloadMembers(true).execute();
			}

			public int getDrawable() {
				return R.drawable.actionbar_refresh;
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
    
    private class DownloadMembers extends AsyncTask<String, Integer, Integer> {
		private ProgressDialog progressDialog;
		private boolean clear = false;
		private boolean nothingAdded = true;
		
		public DownloadMembers() {
		}
		
		public DownloadMembers(boolean clear) {
			this.clear = clear;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
			progressDialog = ProgressDialog.show(MembersActivity.this, null, "Ažuriranje u tijeku...");
		}
	
		@Override
		protected Integer doInBackground(String... params) {
			BestHrApi api = new BestHrApi(MembersActivity.this);
			members = api.getBoardMembers();
				if(!members.isEmpty()) {
					if(clear) rows.clear();
					
					for (Member member : members) {
						member.setDatabase(dbWriteable);
						if(!member.exists()) {
							member.insertOrUpdate();
							nothingAdded = false;
						}
					}
				}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
			iterateThroughMembers();
			progressDialog.cancel();
			
			if(nothingAdded)
				showToast("Veæ imate najnovije podatke.");
		}
	}
}
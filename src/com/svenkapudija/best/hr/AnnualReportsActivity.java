package com.svenkapudija.best.hr;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.adapters.AnnualReportAdapter;
import com.svenkapudija.best.hr.api.BestHrApi;
import com.svenkapudija.best.hr.models.AnnualReport;
import com.svenkapudija.best.hr.models.Event;
import com.svenkapudija.best.hr.utils.Preferences;

public class AnnualReportsActivity extends RootActivity {
	
	private AnnualReportAdapter listviewAdapter;
	private ArrayList<AnnualReport> rows = new ArrayList<AnnualReport>();
	private GridView gridView;
	private ArrayList<AnnualReport> reports = new ArrayList<AnnualReport>();
	
	private void getUIElements() {
		gridView = (GridView) findViewById(R.id.gridView);
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
	
	private class DownloadReports extends AsyncTask<String, Integer, Integer> {
		private ProgressDialog progressDialog;
		
		public DownloadReports() {
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
			progressDialog = ProgressDialog.show(AnnualReportsActivity.this, null, "Preuzimam...");
		}
	
		@Override
		protected Integer doInBackground(String... params) {
			BestHrApi api = new BestHrApi(AnnualReportsActivity.this);
			reports = api.getAnnualReports();
				if(!reports.isEmpty()) {
					for (AnnualReport report : reports) {
						report.setDatabase(dbWriteable);
						if(!report.exists())
							report.insertOrUpdate();
					}
				}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
			iterateThroughReports();
			progressDialog.cancel();
		}
	}
	
	private void iterateThroughReports() {
		for (AnnualReport report : reports) {
			rows.add(report);
		}
		
		listviewAdapter.notifyDataSetChanged();
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_reports);
        
        getUIElements();
        setupActionBar();
        
        listviewAdapter = new AnnualReportAdapter(this, rows);
		gridView.setAdapter(listviewAdapter);
		
		reports = AnnualReport.readAll(dbWriteable);
		if(!reports.isEmpty()) {
			iterateThroughReports();
		} else {
			new DownloadReports().execute();
		}
		
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				AnnualReport report = rows.get(position);
				Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(report.getLink()));
			    startActivity(browse);
			}
		});
    }
}
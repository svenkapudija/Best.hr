package com.svenkapudija.best.hr;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.adapters.AnnualReportAdapter;
import com.svenkapudija.best.hr.internet.BestHrApi;
import com.svenkapudija.best.hr.models.AnnualReport;

public class AnnualReportsActivity extends RootActivity {
	
	private AnnualReportAdapter listviewAdapter;
	private ArrayList<AnnualReport> rows = new ArrayList<AnnualReport>();
	private GridView gridView;
	
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
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_reports);
        
        getUIElements();
        setupActionBar();
        
        BestHrApi api = new BestHrApi(this);
        
		ArrayList<AnnualReport> reports = AnnualReport.readAll(dbWriteable);
		if(!reports.isEmpty()) {
			//rows.add(new PersonRow("Èlanovi uprave"));
			for (AnnualReport report : reports) {
				rows.add(report);
			}
		}
		
		listviewAdapter = new AnnualReportAdapter(this, rows);
		gridView.setAdapter(listviewAdapter);
		
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
package com.svenkapudija.best.hr;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.svenkapudija.best.hr.adapters.PersonAdapter;
import com.svenkapudija.best.hr.adapters.PersonRow;
import com.svenkapudija.best.hr.internet.BestHrApi;

public class ContactActivity extends RootActivity {
	
	// UI elements / MapView
	private MapView mapView;
	private MapController mapController;
	private Projection projection;
	private List<Overlay> mapOverlays;
	
	private PersonAdapter listviewAdapter;
	private ArrayList<PersonRow> rows = new ArrayList<PersonRow>();
	private ListView listview;
	
	private ImageButton googleMapsThumb;
	
	private void getUIElements() {
		/*
		mapView = (MapView) findViewById(R.id.mapview);
		mapOverlays = mapView.getOverlays();
		mapController = mapView.getController();
		projection = mapView.getProjection();
		*/
		
		listview = (ListView) findViewById(R.id.listview);
		googleMapsThumb = (ImageButton) findViewById(R.id.googleMapsThumb);
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
        setContentView(R.layout.activity_contact);
        
        getUIElements();
        setupActionBar();
        
        BestHrApi api = new BestHrApi(this);
        
        /*
        ArrayList<Person> members = api.getBoardMembers();
		if(!members.isEmpty()) {
			for (Person member : members) {
				Log.d(Preferences.DEBUG_TAG, "Member: " + member.toString());
				member.setDatabase(dbWriteable);
				if(!member.exists())
					member.insertOrUpdate();
			}
		}
		*/
		
        googleMapsThumb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
				Uri.parse("http://maps.google.com/maps?q=45.801693,15.970495&num=1&t=m&z=17"));
				startActivity(intent);
			}
		});
        
        rows.add(new PersonRow("BEST Zagreb", "ured", "berst@asd.com", "1231132"));
		rows.add(new PersonRow("Unska 3, 10000 Zagreb", "adresa"));
		//rows.add(new PersonRow("www.best.hr", "web-stranica"));
		rows.add(new PersonRow("85079637865", "OIB"));
		rows.add(new PersonRow("1458841", "matièni broj"));
		rows.add(new PersonRow("2360000-1101434925", "žiro raèun"));
		
		listviewAdapter = new PersonAdapter(this, rows);
		listview.setAdapter(listviewAdapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
			}
		});
    }
}
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
import com.svenkapudija.best.hr.utils.Preferences;

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
		actionBar.setHome(R.drawable.action_bar_logotype);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        
        getUIElements();
        setupActionBar();
        
        googleMapsThumb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
				Uri.parse("http://maps.google.com/maps?q=45.801693,15.970495&num=1&t=m&z=17"));
				startActivity(intent);
			}
		});
        
        rows.add(new PersonRow("BEST Zagreb", "ured", Preferences.BEST_HR_EMAIL, Preferences.BEST_HR_PHONE));
		rows.add(new PersonRow(Preferences.BEST_HR_ADDRESS, "adresa"));
		rows.add(new PersonRow(Preferences.BEST_HR_OIB, "OIB"));
		rows.add(new PersonRow(Preferences.BEST_HR_MB, "matièni broj"));
		rows.add(new PersonRow(Preferences.BEST_HR_ZR, "žiro raèun"));
		
		listviewAdapter = new PersonAdapter(this, rows);
		listview.setAdapter(listviewAdapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				/* Better pop-up some dialog so user can realize what the "clipboard" is
				
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
				clipboard.setText(rows.get(position).getTitle());
				
				showToast(rows.get(position).getSubtitle() + " je kopiran u clipboard");
				*/
			}
		});
    }
}
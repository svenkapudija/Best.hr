package com.svenkapudija.best.hr;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;
import com.markupartist.android.widget.ActionBar;
import com.svenkapudija.best.hr.adapters.PersonAdapter;
import com.svenkapudija.best.hr.adapters.PersonRow;
import com.svenkapudija.best.hr.overlays.IconOverlay;
import com.svenkapudija.best.hr.utils.Preferences;

public class ContactActivity extends MapActivity {
	
	/**
	 * debug keystore
	 * 0WFINWstSxr9wrI1gE6KQEscospoGv5MU1NN1HQ
	 * 92:18:C9:CA:E9:01:C7:77:7B:43:1E:F5:01:6D:C2:2D
	 * 
	 * sven keystore
	 * 1C:DD:85:36:75:F4:8E:78:05:03:BC:9A:6F:51:8D:C0
	 * 0WFINWstSxr-4C-IOhMtJYJG_41taoYqCWKJ2oA
	 * 
	 */
	
	// UI elements / MapView
	private MapView mapView;
	private MapController mapController;
	private Projection projection;
	private IconOverlay bestOverlay;
	protected ActionBar actionBar;
	
	private PersonAdapter listviewAdapter;
	private ArrayList<PersonRow> rows = new ArrayList<PersonRow>();
	private ListView listview;
	
	private void getUIElements() {
		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();
		projection = mapView.getProjection();
		
		listview = (ListView) findViewById(R.id.listview);
	}

	public void setupActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHome(R.drawable.actionbar_logotype);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        
        getUIElements();
        setupActionBar();
        
        bestOverlay = new IconOverlay(getResources().getDrawable(R.drawable.best_map_icon), mapView);
        bestOverlay.addOverlayIcon("BEST Zagreb, sjedište", Preferences.BEST_HR_ADDRESS, 45.801745, 15.970495);
        bestOverlay.animateTo(0);
        bestOverlay.nowPopulate();
		mapView.getOverlays().add(bestOverlay);
		mapController.setZoom(16);
        
        rows.add(new PersonRow("BEST Zagreb", "ured", Preferences.BEST_HR_EMAIL, Preferences.BEST_HR_PHONE));
		rows.add(new PersonRow(Preferences.BEST_HR_OIB, "OIB"));
		rows.add(new PersonRow(Preferences.BEST_HR_MB, "matièni broj"));
		rows.add(new PersonRow(Preferences.BEST_HR_ZR, "žiro raèun"));
		rows.add(new PersonRow(Preferences.BEST_HR_WEBSITE, "web-stranica"));
		
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

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
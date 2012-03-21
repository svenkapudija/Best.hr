package com.svenkapudija.best.hr;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.localytics.android.LocalyticsSession;
import com.markupartist.android.widget.ActionBar;
import com.svenkapudija.best.hr.adapters.PersonAdapter;
import com.svenkapudija.best.hr.adapters.PersonRow;
import com.svenkapudija.best.hr.overlays.IconOverlay;
import com.svenkapudija.best.hr.utils.LocalyticsPreferences;
import com.svenkapudija.best.hr.utils.Preferences;

public class ContactActivity extends MapActivity {
	
	protected LocalyticsSession localyticsSession;
	
	// UI elements / MapView
	private MapView mapView;
	private MapController mapController;
	private IconOverlay bestOverlay;
	protected ActionBar actionBar;
	
	private PersonAdapter listviewAdapter;
	private ArrayList<PersonRow> rows = new ArrayList<PersonRow>();
	private ListView listview;
	
	private void getUIElements() {
		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();
		
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
        
        this.localyticsSession = new LocalyticsSession(this.getApplicationContext(), LocalyticsPreferences.LOCALYTICS_APP_KEY);
		this.localyticsSession.open();
		this.localyticsSession.upload();
		
        getUIElements();
        setupActionBar();
        
        bestOverlay = new IconOverlay(getResources().getDrawable(R.drawable.best_map_icon), mapView);
        bestOverlay.addOverlayIcon("BEST Zagreb, sjedište", Preferences.BEST_HR_ADDRESS, 45.801745, 15.970495);
        bestOverlay.animateTo(0);
        bestOverlay.nowPopulate();
		mapView.getOverlays().add(bestOverlay);
		mapController.setZoom(16);
        
        rows.add(new PersonRow("BEST Zagreb", getString(R.string.office), Preferences.BEST_HR_EMAIL, Preferences.BEST_HR_PHONE));
		rows.add(new PersonRow(Preferences.BEST_HR_OIB, getString(R.string.oib)));
		rows.add(new PersonRow(Preferences.BEST_HR_MB, getString(R.string.maticni_broj)));
		rows.add(new PersonRow(Preferences.BEST_HR_ZR, getString(R.string.ziro_racun)));
		rows.add(new PersonRow(Preferences.BEST_HR_WEBSITE, getString(R.string.website)));
		
		listviewAdapter = new PersonAdapter(this, localyticsSession, rows);
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
	
	@Override
	protected void onResume() {
		super.onResume();
		
		localyticsSession.open();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		this.localyticsSession.close();
        this.localyticsSession.upload();
	}
}
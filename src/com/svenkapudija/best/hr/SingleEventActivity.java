package com.svenkapudija.best.hr;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.localytics.android.LocalyticsSession;
import com.markupartist.android.widget.ActionBar;
import com.svenkapudija.best.hr.database.DatabaseHelper;
import com.svenkapudija.best.hr.models.Event;
import com.svenkapudija.best.hr.overlays.IconOverlay;
import com.svenkapudija.best.hr.utils.LocalyticsPreferences;

public class SingleEventActivity extends MapActivity {
	
	protected LocalyticsSession localyticsSession;
	
	private Event event = new Event();
	
	private TextView type;
	private TextView title;
	private TextView startDate;
	private TextView endDate;
	private ImageButton link;
	
	private MapView mapView;
	private MapController mapController;
	private IconOverlay bestOverlay;
	protected ActionBar actionBar;
	
	private DatabaseHelper helper;
	private SQLiteDatabase dbWriteable;
	
	private void getUIElements() {
		type = (TextView) findViewById(R.id.type);
		title = (TextView) findViewById(R.id.title);
		startDate = (TextView) findViewById(R.id.startDate);
		endDate = (TextView) findViewById(R.id.endDate);
		link = (ImageButton) findViewById(R.id.url);
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();
	}

	public void setupActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHome(R.drawable.actionbar_logotype);
	}
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);
        
        this.localyticsSession = new LocalyticsSession(this.getApplicationContext(), LocalyticsPreferences.LOCALYTICS_APP_KEY);
		this.localyticsSession.open();
		this.localyticsSession.upload();
        
        getUIElements();
        setupActionBar();
        
    	helper = new DatabaseHelper(this);
    	dbWriteable = helper.getWritableDatabase();
		
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	event.setDatabase(dbWriteable);
        	event.setId(extras.getString("event_id"));
        	event.read();
        }
        
        type.setText(event.getType());
        title.setText(event.getName());
        startDate.setText(event.getStartDateFormatted());
        endDate.setText(event.getEndDateFormatted());
        link.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				localyticsSession.tagEvent(LocalyticsPreferences.SINGLE_EVENT_ACTIVITY_LINK);
				Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(event.getUrl()));
			    startActivity(browse);
			}
		});
        
        bestOverlay = new IconOverlay(getResources().getDrawable(R.drawable.best_map_icon), mapView);
        bestOverlay.addOverlayIcon(event.getName(), event.getLocation(), event.getLat(), event.getLng());
        bestOverlay.animateTo(0);
        bestOverlay.nowPopulate();
		mapView.getOverlays().add(bestOverlay);
		mapController.setZoom(16);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		helper.close();
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
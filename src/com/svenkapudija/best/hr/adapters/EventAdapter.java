package com.svenkapudija.best.hr.adapters;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.svenkapudija.best.hr.R;
import com.svenkapudija.best.hr.models.Event;

public class EventAdapter extends ArrayAdapter<EventRow> {

	private Context context;
	private List<EventRow> items;
	private Date currentTime;
	private LayoutInflater inflater;
	
	public EventAdapter(Context context, List<EventRow> items, Date currentTime) {
		super(context, 0, items);
		
		this.context = context;
		this.items = items;
		this.currentTime = currentTime;
		
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        final EventRow item = items.get(position);
        
        if(item.isHeader()) {
        	if (row == null || row.getTag() != "header") {
            	row = inflater.inflate(R.layout.header, null);
            	row.setTag("header");
        	}
        	
        	TextView title = (TextView) row.findViewById(R.id.title);
        	title.setText(item.getHeaderTitle());
        } else {
        	if (row == null || row.getTag() != "event") {
            	row = inflater.inflate(R.layout.event_row, null);
            	row.setTag("event");
        	}
        	
        	Event event = item.getEvent();
        	
        	TextView name = (TextView) row.findViewById(R.id.name);
        	TextView date = (TextView) row.findViewById(R.id.date);
        	TextView location = (TextView) row.findViewById(R.id.location);
        	
        	// Strike-through if event is in the past
        	if(event.getEndDate().getTime() < this.currentTime.getTime()) {
        		name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        		date.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        		location.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        	} else {
        		name.setPaintFlags(name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        		date.setPaintFlags(name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        		location.setPaintFlags(name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        	}
        	
        	name.setText(event.getName());
            date.setText(event.getStartDateFormatted() + " - " + event.getEndDateFormatted());
            location.setText(event.getLocation());
        }

        return row;
    }
}

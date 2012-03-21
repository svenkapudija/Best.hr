package com.svenkapudija.best.hr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.localytics.android.LocalyticsSession;
import com.svenkapudija.best.hr.R;
import com.svenkapudija.best.hr.utils.LocalyticsPreferences;
import com.svenkapudija.best.hr.utils.Utils;

public class PersonAdapter extends ArrayAdapter<PersonRow> {

	private LocalyticsSession localyticsSession;
	private Context context;
	private List<PersonRow> items;
	
	public PersonAdapter(Context context, LocalyticsSession localyticsSession, List<PersonRow> items) {
		super(context, 0, items);
		
		this.localyticsSession = localyticsSession;
		this.context = context;
		this.items = items;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        final PersonRow item = items.get(position);
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	if(item.isHeader()) {
    		row = inflater.inflate(R.layout.header, null);
        	TextView title = (TextView) row.findViewById(R.id.title);
            title.setText(item.getTitle());
    	} else {
    		row = inflater.inflate(R.layout.person_row, null);
        	RelativeLayout body = (RelativeLayout) row.findViewById(R.id.body);
        	
        	TextView name = (TextView) body.findViewById(R.id.name);
            TextView type = (TextView) body.findViewById(R.id.type);
            
            name.setText(item.getTitle());
            type.setText(item.getSubtitle());
            
            RelativeLayout phoneIcon = (RelativeLayout) body.findViewById(R.id.phone_icon);
            if(item.getPhone() == null) {
            	phoneIcon.setVisibility(View.GONE);
            } else {
            	ImageButton phoneButton = (ImageButton) body.findViewById(R.id.phone_button);
            	phoneButton.setOnClickListener(new View.OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					localyticsSession.tagEvent(LocalyticsPreferences.CONTACT_BY_PHONE);
    					Utils.call(context, item.getPhone());
    				}
    			});
            }
            
            RelativeLayout emailIcon = (RelativeLayout) body.findViewById(R.id.email_icon);
            if(item.getEmail() == null) {
            	emailIcon.setVisibility(View.GONE);
            	
            	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            	params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            	phoneIcon.setLayoutParams(params);
            } else {
            	ImageButton emailButton = (ImageButton) body.findViewById(R.id.email_button);
            	emailButton.setOnClickListener(new View.OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					localyticsSession.tagEvent(LocalyticsPreferences.CONTACT_BY_EMAIL);
    					Utils.sendEmail(context, item.getEmail());
    				}
    			});
            }
    	}
        
        return row;
    }
}

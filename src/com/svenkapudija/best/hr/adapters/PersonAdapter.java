package com.svenkapudija.best.hr.adapters;

import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.svenkapudija.best.hr.R;
import com.svenkapudija.best.hr.utils.Preferences;

public class PersonAdapter extends ArrayAdapter<PersonRow> {

	private Context context;
	private List<PersonRow> items;
	
	public PersonAdapter(Context context, List<PersonRow> items) {
		super(context, 0, items);
		
		this.context = context;
		this.items = items;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        final PersonRow item = items.get(position);
        
        /*
        if (row == null) {
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	row = inflater.inflate(R.layout.person_row, null);
    	}
        */
        
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
    					Intent callIntent = new Intent(Intent.ACTION_CALL);
    			        callIntent.setData(Uri.parse("tel:" + item.getPhone()));
    			        try {
    			        	context.startActivity(callIntent);
    			        } catch (ActivityNotFoundException e) {
    			        	e.printStackTrace();
    			        	Toast.makeText(context, "Greška pri pokretanju aplikacije za poziv.", Toast.LENGTH_LONG).show();
    			        }
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
    					Intent i = new Intent(Intent.ACTION_SEND);
    					i.setType("text/plain");
    					i.putExtra(Intent.EXTRA_EMAIL  , new String[]{item.getEmail()});
    					i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
    					i.putExtra(Intent.EXTRA_TEXT   , "body of email");
    					try {
    						context.startActivity(Intent.createChooser(i, "Send mail..."));
    			        } catch (ActivityNotFoundException e) {
    			        	e.printStackTrace();
    			        	Toast.makeText(context, "Greška pri pokretanju aplikacije za email.", Toast.LENGTH_LONG).show();
    			        }
    				}
    			});
            }
    	}
        
        return row;
    }
}

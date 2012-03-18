package com.svenkapudija.best.hr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.svenkapudija.best.hr.R;

public class DoubleRowAdapter extends ArrayAdapter<DoubleRow> {

	private Context context;
	private List<DoubleRow> items;
	
	public DoubleRowAdapter(Context context, List<DoubleRow> items) {
		super(context, 0, items);
		
		this.context = context;
		this.items = items;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        DoubleRow item = items.get(position);
        
        if (row == null) {
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	row = inflater.inflate(R.layout.double_row, null);
    	}

        RelativeLayout layout = (RelativeLayout) row.findViewById(R.id.text);
    	TextView name = (TextView) layout.findViewById(R.id.name);
        TextView type = (TextView) layout.findViewById(R.id.type);
        
        name.setText(item.getTitle());
        type.setText(item.getSubtitle());
        
        return row;
    }
}

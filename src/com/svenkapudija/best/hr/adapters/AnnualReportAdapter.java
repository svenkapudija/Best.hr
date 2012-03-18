package com.svenkapudija.best.hr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.svenkapudija.best.hr.R;
import com.svenkapudija.best.hr.models.AnnualReport;

public class AnnualReportAdapter extends ArrayAdapter<AnnualReport> {

	private Context context;
	private List<AnnualReport> items;
	private LayoutInflater inflater;
	
	public AnnualReportAdapter(Context context, List<AnnualReport> items) {
		super(context, 0, items);
		
		this.context = context;
		this.items = items;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        final AnnualReport report = items.get(position);
        
        if (row == null) {
        	row = inflater.inflate(R.layout.grid_item, null);
    	}
    	
    	TextView title = (TextView) row.findViewById(R.id.tekst);
    	ImageView thumb = (ImageView) row.findViewById(R.id.slika);
    	title.setText(report.getYear() + ". godina");
    	thumb.setImageBitmap(report.getThumbnail());

        return row;
    }
}

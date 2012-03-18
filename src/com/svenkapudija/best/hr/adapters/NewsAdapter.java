package com.svenkapudija.best.hr.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.svenkapudija.best.hr.R;
import com.svenkapudija.best.hr.models.News;
import com.svenkapudija.best.hr.utils.Preferences;

public class NewsAdapter extends ArrayAdapter<News> {

	private Context context;
	private List<News> items;
	
	public NewsAdapter(Context context, List<News> items) {
		super(context, 0, items);
		
		this.context = context;
		this.items = items;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        final News item = items.get(position);
        
        if (row == null) {
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	row = inflater.inflate(R.layout.news_row, null);
    	}
    	
        TextView title = (TextView) row.findViewById(R.id.author);
        TextView date = (TextView) row.findViewById(R.id.date);
        TextView intro = (TextView) row.findViewById(R.id.intro);
        ImageView thumb = (ImageView) row.findViewById(R.id.thumb);
        
    	title.setText(item.getAuthor());
    	date.setText(DateFormat.format("dd.MM.yyyy.", item.getPublished()));
    	if(item.getIntro().contains("http://www.youtube.com")) {
			intro.setText("Klikni za pokretanje YouTube videa.");
		} else {
			intro.setText(Html.fromHtml(item.getIntro().replaceAll("(?i)<(?!(/?(li|p)))[^>]*>", "")));
		}
    	if(item.getImage() != null) {
    		thumb.setVisibility(View.VISIBLE);
    		thumb.setImageBitmap(item.getImage());
    	} else {
    		thumb.setVisibility(View.GONE);
    	}
    	
        return row;
    }
}

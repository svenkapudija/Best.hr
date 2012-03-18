package com.svenkapudija.best.hr;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.adapters.NewsAdapter;
import com.svenkapudija.best.hr.internet.BestHrApi;
import com.svenkapudija.best.hr.models.News;

public class NewsActivity extends RootActivity {
	
	private NewsAdapter listviewAdapter;
	private ArrayList<News> rows = new ArrayList<News>();
	private ListView listview;
	
	private void getUIElements() {
		listview = (ListView) findViewById(R.id.listview);
	}

	public void setupActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.addAction(new Action() {
			public void performAction(View view) {
				
			}

			public int getDrawable() {
				return R.drawable.action_bar_news;
			}

			public CharSequence getText() {
				return "";
			}
		});
		
		actionBar.setHome(R.drawable.action_bar_logotype);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        
        getUIElements();
        setupActionBar();
        
		ArrayList<News> newsList = News.readAll(dbWriteable);
		if(!newsList.isEmpty()) {
			for (News news : newsList) {
				rows.add(news);
			}
		}
		
		listviewAdapter = new NewsAdapter(this, rows);
		listview.setAdapter(listviewAdapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				News news = rows.get(position);
				
				if(news.getIntro().contains("http://www.youtube.com")) {
					String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
					Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
					Matcher matcher = compiledPattern.matcher(news.getIntro());
					while(matcher.find()) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(matcher.group())));
					}
				} else {
					Intent i = new Intent(NewsActivity.this, SingleNewsActivity.class);
					i.putExtra("news_id", news.getId());
					startActivity(i);
				}
			}
		});
    }
}
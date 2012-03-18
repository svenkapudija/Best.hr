package com.svenkapudija.best.hr;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.backup.RestoreObserver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.adapters.EndlessAdapter;
import com.svenkapudija.best.hr.adapters.NewsAdapter;
import com.svenkapudija.best.hr.internet.BestHrApi;
import com.svenkapudija.best.hr.models.News;
import com.svenkapudija.best.hr.utils.Preferences;

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
        
        /*
		ArrayList<News> newsList = News.readAll(dbWriteable, 0, 15);
		if(!newsList.isEmpty()) {
			for (News news : newsList) {
				rows.add(news);
			}
		}
		
		
		listviewAdapter = new NewsAdapter(this, rows);
		listview.setAdapter(listviewAdapter);
		*/
		
	    listview.setAdapter(new DemoAdapter(rows));
		
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
    
    class DemoAdapter extends EndlessAdapter {
        private RotateAnimation rotate=null;
        private int newsCount;
        private int startCount = 0;
        private int step = 6;
        private ArrayList<News> newsList = new ArrayList<News>();
        private ArrayList<News> apiResponse = null;
        private boolean download = false;
        private boolean initialCheck = false;
        private BestHrApi api = null;
        
        DemoAdapter(List<News> items) {
          super(new NewsAdapter(NewsActivity.this, items));
          
          rotate=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                                      0.5f, Animation.RELATIVE_TO_SELF,
                                      0.5f);
          rotate.setDuration(400);
          rotate.setRepeatMode(Animation.RESTART);
          rotate.setRepeatCount(Animation.INFINITE);
          
          api = new BestHrApi(NewsActivity.this);
        }
        
        @Override
        protected View getPendingView(ViewGroup parent) {
          View row=getLayoutInflater().inflate(R.layout.row, null);
          
          View child = row.findViewById(R.id.throbber);
          child.setVisibility(View.VISIBLE);
          child.startAnimation(rotate);
          
          return(row);
        }
        
        @Override
        protected boolean cacheInBackground() {
        	// Perform the initial check, are all news downloaded?
        	if(!initialCheck) {
        		newsCount = News.getCount(dbWriteable);
                if(api.getNewsCount() > newsCount) download = true;
        		initialCheck = true;
        	}
            
        	// If I have less news in DB then on API and if (starting position+step) is larger then the news count, perform update
        	if(download && newsCount < (startCount+step+1)) {
        		// Get news list
        		if(apiResponse == null)
        			apiResponse = api.getNews();
        		
        		// Receive next step+1 news which are not in DB yet
        		if(!apiResponse.isEmpty()) {
        			int counter = step+1;
        			for (int i = 0; i < counter && i < (apiResponse.size()); i++) {
        				News news = apiResponse.get(i);
        				news.setDatabase(dbWriteable);
        				if(!news.exists()) {
        					news = api.getNews(news.getId());
        					news.setDatabase(dbWriteable);
        					news.insertOrUpdate();
        				} else {
        					counter++;
        				}
        			}
        			
        			newsCount = News.getCount(dbWriteable);
        		}
        	}
        	
        	// Retrieve next 'step' news from DB
        	ArrayList<News> tmp = News.readAll(dbWriteable, startCount, step);
        	for(News news : tmp) {
        		newsList.add(news);
        	}
        	
        	startCount += step;
        	
          return(getWrappedAdapter().getCount() < newsCount);
        }
        
        @Override
        protected void appendCachedData() {
          if (getWrappedAdapter().getCount() < newsCount) {
        	// Display data
            @SuppressWarnings("unchecked")
            ArrayAdapter<News> a = (ArrayAdapter<News>) getWrappedAdapter();
            for (int i=(startCount-step);i<(startCount) && (i<newsList.size());i++) {
            	a.add(newsList.get(i));
            }
          }
        }
    }
}
package com.svenkapudija.best.hr;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.svenkapudija.best.hr.adapters.EndlessAdapter;
import com.svenkapudija.best.hr.adapters.NewsAdapter;
import com.svenkapudija.best.hr.api.BestHrApi;
import com.svenkapudija.best.hr.models.News;
import com.svenkapudija.best.hr.utils.LocalyticsPreferences;

public class NewsActivity extends RootActivity {
	
	private ArrayList<News> rows = new ArrayList<News>();
	private ListView listview;
	
	private void getUIElements() {
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
        setContentView(R.layout.activity_news);
        
        getUIElements();
        setupActionBar();
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int newsChunk = prefs.getInt("news_chunk", 5);
	    listview.setAdapter(new NewsAdapterEndless(rows, newsChunk+1));
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				final News news = rows.get(position);
				
				// Check if there is YouTube link, if it is - give option to watch video or read news
				if(news.getIntro().contains("http://www.youtube.com")) {
					String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
					Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
					final Matcher matcher = compiledPattern.matcher(news.getIntro());
					if(matcher.find()) {
						 final CharSequence[] items = {"YouTube video", "Novost"};
						 AlertDialog.Builder builder = new AlertDialog.Builder(NewsActivity.this);
						    builder.setTitle("Pogledaj");
						    builder.setItems(items, new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int item) {
						            if(item == 0) {
						            	// Run YouTube video
						            	localyticsSession.tagEvent(LocalyticsPreferences.NEWS_ACTIVITY_RUN_YOUTUBE_VIDEO);
						            	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(matcher.group())));
						            } else {
						            	// Display news
						            	localyticsSession.tagEvent(LocalyticsPreferences.NEWS_ACTIVITY_CLICK_SINGLE_NEWS);
						            	Intent i = new Intent(NewsActivity.this, SingleNewsActivity.class);
										i.putExtra("news_id", news.getId());
										startActivity(i);
						            }
						        }
						    }).show();
					}
				} else {
					// Display news
					localyticsSession.tagEvent(LocalyticsPreferences.NEWS_ACTIVITY_CLICK_SINGLE_NEWS);
					Intent i = new Intent(NewsActivity.this, SingleNewsActivity.class);
					i.putExtra("news_id", news.getId());
					startActivity(i);
				}
			}
		});
    }
    
    class NewsAdapterEndless extends EndlessAdapter {
        private RotateAnimation rotate=null;
        /** Total news count */
        private int newsCount;
        /** Starting position for news listing (<code>0</code> - newest one) */
        private int startCount = 0;
        /** How big chunks to display and download */
        private int newsChunk = 5;
        
        private ArrayList<News> newsList = new ArrayList<News>();
        private ArrayList<News> apiNews = null;
        private boolean download = false;
        private boolean initialCheck = false;
        private BestHrApi api = null;
        
        NewsAdapterEndless(List<News> items, int newsChunk) {
          super(new NewsAdapter(NewsActivity.this, items));
          
          this.newsChunk = newsChunk;
          
          rotate=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                                      0.5f, Animation.RELATIVE_TO_SELF,
                                      0.5f);
          rotate.setDuration(400);
          rotate.setRepeatMode(Animation.RESTART);
          rotate.setRepeatCount(Animation.INFINITE);
          
          api = new BestHrApi(NewsActivity.this);
        }
        
        /**
         * Method for displaying progress view (spinner).
         */
        @Override
        protected View getPendingView(ViewGroup parent) {
          View row=getLayoutInflater().inflate(R.layout.row, null);
          
          View child = row.findViewById(R.id.throbber);
          child.setVisibility(View.VISIBLE);
          child.startAnimation(rotate);
          
          return(row);
        }
        
        /**
         * Retrieve news (if necessary) from BEST API, otherwise pull from DB
         */
        @Override
        protected boolean cacheInBackground() {
        	// Perform the initial check, are all news downloaded?
        	if(!initialCheck) {
        		newsCount = News.getCount(dbWriteable);
                if(api.getNewsCount() > newsCount) download = true;
        		initialCheck = true;
        	}
            
        	// If I have less news in DB then on API and if (starting position+step) is larger then the news count, perform update
        	// (I am running out of news for display)
        	if(download && newsCount < (startCount+newsChunk+1)) {
        		// Get news list
        		if(apiNews == null)
        			apiNews = api.getNews();
        		
        		// Receive next step+1 news which are not in DB yet
        		if(!apiNews.isEmpty()) {
        			int counter = newsChunk+1;
        			for (int i = 0; i < counter && i < (apiNews.size()); i++) {
        				News news = apiNews.get(i);
        				news.setDatabase(dbWriteable);
        				if(!news.exists()) {
        					news = api.getNews(news.getId());
        					news.setDatabase(dbWriteable);
        					news.insertOrUpdate();
        				} else {
        					// Keep looking
        					counter++;
        				}
        			}
        			
        			// Refresh news count
        			newsCount = News.getCount(dbWriteable);
        		}
        	}
        	
        	// Retrieve next news chunk from DB
        	ArrayList<News> tmp = News.readAll(dbWriteable, startCount, newsChunk);
        	for(News news : tmp) {
        		newsList.add(news);
        	}
        	
        	startCount += newsChunk;
        	
          return(getWrappedAdapter().getCount() < newsCount);
        }
        
        /**
         * Display data.
         */
        @Override
        protected void appendCachedData() {
          if (getWrappedAdapter().getCount() < newsCount) {
            @SuppressWarnings("unchecked")
            ArrayAdapter<News> a = (ArrayAdapter<News>) getWrappedAdapter();
            for (int i=(startCount-newsChunk);i<(startCount) && (i<newsList.size());i++) {
            	a.add(newsList.get(i));
            }
          }
        }
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Release bitmaps from memory
		for(News news : rows) {
			if(news.getImage() != null)
				news.getImage().recycle();
		}
	}
}
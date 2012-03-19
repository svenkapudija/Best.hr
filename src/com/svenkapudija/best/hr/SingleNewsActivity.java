package com.svenkapudija.best.hr;

import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.api.BestHrApi;
import com.svenkapudija.best.hr.models.News;
import com.svenkapudija.best.hr.utils.Preferences;
import com.svenkapudija.best.hr.utils.Utils;

public class SingleNewsActivity extends RootActivity {
	
	private TextView titleTextView;
	private TextView dateTextView;
	private TextView authorTextView;
	private TextView introTextView;
	private TextView bodyTextView;
	private ImageView imageImageView;
	private News news;
	
	private void getUIElements() {
		titleTextView = (TextView) findViewById(R.id.title);
		dateTextView = (TextView) findViewById(R.id.date);
		authorTextView = (TextView) findViewById(R.id.author);
		introTextView = (TextView) findViewById(R.id.intro);
		bodyTextView = (TextView) findViewById(R.id.body);
		imageImageView = (ImageView) findViewById(R.id.image);
	}

	public void setupActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		
		// Sharing
		actionBar.addAction(new Action() {
			public void performAction(View view) {
				Intent intent = new Intent(android.content.Intent.ACTION_SEND);
				intent.setType("text/plain");
	            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, news.getTitle() + " - " + BestHrApi.BASE_URL + news.getLink());
	            intent.putExtra(android.content.Intent.EXTRA_TEXT, Utils.removeHtmlTags(news.getIntro()) + "\n" + Utils.removeHtmlTags(news.getBody()));
	            startActivity(intent);
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
        setContentView(R.layout.activity_single_news);
        
        news = new News(dbWriteable);
        
        Bundle extras = getIntent().getExtras(); 
        if(extras != null) {
        	int newsId = extras.getInt("news_id");
        	news.setId(newsId);
        }
        
        getUIElements();
        setupActionBar();
        
        news.read();
        
        setTitle(news.getTitle());
        setAuthor(news.getAuthor());
        setDate(news.getPublished());
        setIntro(news.getIntro());
        setBody(news.getBody());
        setImage(news.getImage());
    }
    
    public void setTitle(String title) {
    	this.titleTextView.setText(title);
    }
    
    public void setDate(Date date) {
    	this.dateTextView.setText(DateFormat.format("dd.MM.yyyy.", date));
    }
    
    public void setAuthor(String author) {
    	this.authorTextView.setText(author);
    }
    
    public void setIntro(String intro) {
    	if(intro != null && !intro.equals("null")) {
    		this.introTextView.setText(Html.fromHtml(intro));
    	} else {
    		this.introTextView.setVisibility(View.GONE);
    	}
    }
    
    public void setBody(String body) {
    	if(body != null && !body.equals("null")) {
    		this.bodyTextView.setText(Html.fromHtml(body));
    		Linkify.addLinks(bodyTextView, Linkify.ALL);
    	} else {
    		this.bodyTextView.setVisibility(View.GONE);
    	}
    }
    
    public void setImage(Bitmap image) {
    	if(image != null) {
    		this.imageImageView.setImageBitmap(image);
    	} else {
    		this.imageImageView.setVisibility(View.GONE);
    	}
    }
}
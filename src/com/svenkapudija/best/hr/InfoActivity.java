package com.svenkapudija.best.hr;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;

public class InfoActivity extends RootActivity {
	
	private TextView author;
	private TextView title;
	
	private void getUIElements() {
		author = (TextView) findViewById(R.id.author);
		title = (TextView) findViewById(R.id.title);
	}

	public void setupActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHome(R.drawable.actionbar_logotype);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        
        getUIElements();
        setupActionBar();
        
        author.setText(Html.fromHtml(this.getResources().getString(R.string.info_author)), TextView.BufferType.SPANNABLE);
        Linkify.addLinks(author, Linkify.ALL);
        
        String version = "";
		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        title.setText(getString(R.string.info_best_hr_mobile_app) + " " + version);
    }
}
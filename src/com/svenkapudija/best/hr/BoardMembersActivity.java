package com.svenkapudija.best.hr;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.svenkapudija.best.hr.adapters.PersonAdapter;
import com.svenkapudija.best.hr.adapters.PersonRow;
import com.svenkapudija.best.hr.internet.BestHrApi;
import com.svenkapudija.best.hr.models.Person;

public class BoardMembersActivity extends RootActivity {
	
	private PersonAdapter listviewAdapter;
	private ArrayList<PersonRow> rows = new ArrayList<PersonRow>();
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
        setContentView(R.layout.activity_board_members);
        
        getUIElements();
        setupActionBar();
        
        BestHrApi api = new BestHrApi(this);
        
        ArrayList<Person> members = Person.readAll(dbWriteable, "vivaldi");
		if(!members.isEmpty()) {
			rows.add(new PersonRow("BEST seminari"));
			for (Person member : members) {
				rows.add(new PersonRow(member.getName(), member.getRole(), member.getEmail(),member.getPhone()));
			}
		}
		
		ArrayList<Person> members2 = Person.readAll(dbWriteable, "board");
		if(!members2.isEmpty()) {
			rows.add(new PersonRow("Èlanovi uprave"));
			for (Person member : members2) {
				rows.add(new PersonRow(member.getName(), member.getRole(), member.getEmail(),member.getPhone()));
			}
		}
		
		listviewAdapter = new PersonAdapter(this, rows);
		listview.setAdapter(listviewAdapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
			}
		});
    }
}
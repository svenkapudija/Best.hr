package com.svenkapudija.best.hr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.svenkapudija.best.hr.utils.Preferences;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, Preferences.DB_NAME, null, Preferences.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL("CREATE TABLE IF NOT EXISTS android_metadata (locale TEXT DEFAULT 'en_US')");
    	db.execSQL("INSERT INTO android_metadata VALUES ('en_US')");
    	
    	// News
        db.execSQL("CREATE TABLE IF NOT EXISTS best_news (id INTEGER PRIMARY KEY, title VARCHAR, author VARCHAR," +
        		"imageLink VARCHAR, published VARCHAR, link VARCHAR, intro VARCHAR, body VARCHAR)");

        // Board members
        db.execSQL("CREATE TABLE IF NOT EXISTS best_board_members (" +
        		"id INTEGER PRIMARY KEY," +
        		"board_members_json VARCHAR)");
    	
        // Annual reports
        db.execSQL("CREATE TABLE IF NOT EXISTS best_annual_reports (year INTEGER PRIMARY KEY, thumbnailLink VARCHAR, link VARCHAR)");
        
        // Events/seminars
        db.execSQL("CREATE TABLE IF NOT EXISTS best_events (" +
        		"id VARCHAR PRIMARY KEY, url VARCHAR, name VARCHAR, type VARCHAR, location VARCHAR, startDate VARCHAR, endDate VARCHAR, lat DOUBLE, lng DOUBLE)");
        
        // Events mapping to different categories
        db.execSQL("CREATE TABLE IF NOT EXISTS best_events_categories (id INTEGER PRIMARY KEY, name VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS best_events_categories_mapping (event_id VARCHAR PRIMARY KEY, category_id INTEGER)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	
    }
}

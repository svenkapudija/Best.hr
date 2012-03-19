package com.svenkapudija.best.hr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final String DB_NAME = "best_db";
	public static final int DB_VERSION = 1;
	
	public static final String NEWS_TABLE_NAME = "best_news";
	public static final String ANNUAL_REPORTS_TABLE_NAME = "best_annual_reports";
	public static final String EVENTS_TABLE_NAME = "best_events";
	public static final String EVENTS_CATEGORIES_TABLE_NAME = "best_events";
	public static final String EVENTS_CATEGORIES_MAPPING_TABLE_NAME = "best_events";
	public static final String MEMBERS_TABLE_NAME = "best_members";
	
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL("CREATE TABLE IF NOT EXISTS android_metadata (locale TEXT DEFAULT 'en_US')");
    	db.execSQL("INSERT INTO android_metadata VALUES ('en_US')");
    	
    	// News
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NEWS_TABLE_NAME + " (id INTEGER PRIMARY KEY, title VARCHAR, author VARCHAR," +
        		"imageLink VARCHAR, published VARCHAR, date LONG, link VARCHAR, intro VARCHAR, body VARCHAR)");

        // Members/board members
        db.execSQL("CREATE TABLE IF NOT EXISTS " + MEMBERS_TABLE_NAME + " (name VARCHAR PRIMARY KEY, role VARCHAR, type VARCHAR, email VARCHAR, phone VARCHAR)");
    	
        // Annual reports
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ANNUAL_REPORTS_TABLE_NAME + " (year INTEGER PRIMARY KEY, thumbnailLink VARCHAR, link VARCHAR)");
        
        // Events/seminars
        db.execSQL("CREATE TABLE IF NOT EXISTS " + EVENTS_TABLE_NAME + " (" +
        		"id VARCHAR PRIMARY KEY, url VARCHAR, name VARCHAR, type VARCHAR, location VARCHAR, startDate VARCHAR, endDate VARCHAR, date LONG, lat DOUBLE, lng DOUBLE)");
        // Events mapping to different categories
        db.execSQL("CREATE TABLE IF NOT EXISTS " + EVENTS_CATEGORIES_TABLE_NAME + " (id INTEGER PRIMARY KEY, name VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + EVENTS_CATEGORIES_MAPPING_TABLE_NAME + " (event_id VARCHAR PRIMARY KEY, category_id INTEGER)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	
    }
}

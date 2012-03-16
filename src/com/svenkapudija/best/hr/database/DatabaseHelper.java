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
    	
        db.execSQL("CREATE TABLE IF NOT EXISTS best_news (" +
        		"id INTEGER PRIMARY KEY," +
        		"news_json VARCHAR)");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS best_board_members (" +
        		"id INTEGER PRIMARY KEY," +
        		"board_members_json VARCHAR)");
    	
        db.execSQL("CREATE TABLE IF NOT EXISTS best_annual_reports (year INTEGER PRIMARY KEY, thumbnailLink VARCHAR, link VARCHAR)");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS best_events (" +
        		"id VARCHAR PRIMARY KEY," +
        		"events_json VARCHAR)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	
    }
}

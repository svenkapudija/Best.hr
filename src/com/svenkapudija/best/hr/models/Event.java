package com.svenkapudija.best.hr.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;

import com.svenkapudija.best.hr.database.DatabaseHelper;
import com.svenkapudija.best.hr.utils.Preferences;
import com.svenkapudija.best.hr.utils.TypeCaster;

public class Event implements DatabaseInterface {
	
	// Used for serializing/deserializing
	private static final String ID = "id";
	private static final String URL = "url";
	private static final String NAME = "name";
	private static final String TYPE = "type";
	private static final String CATEGORIES = "categories";
	private static final String LOCATION = "location";
	private static final String START_DATE = "start_date";
	private static final String END_DATE = "end_date";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String LATITUDE = "lat";
	private static final String LONGITUDE = "lng";
		
	private String id;
	private String url;
	private String name;
	private String type;
	private ArrayList<String> categories = new ArrayList<String>();
	private String location;
	private Date startDate, endDate;
	private double lat, lng;
	private SQLiteDatabase database;
	
	public Event() {
		
	}
	
	public Event(SQLiteDatabase database) {
		this.database = database; 
	}
	
	public boolean exists() {
		Cursor result = this.database.rawQuery("SELECT id FROM " + DatabaseHelper.EVENTS_TABLE_NAME + " WHERE id = '" + this.getId() + "'", null);
		if (result.getCount() > 0) {
			result.close();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean read() {
		// Add categories
		Cursor categoryIdCursor = this.database.rawQuery("SELECT category_id FROM " + DatabaseHelper.EVENTS_CATEGORIES_MAPPING_TABLE_NAME + " WHERE event_id = '" + this.getId() + "'", null);
		while(categoryIdCursor.moveToNext()) {
			Cursor categoryNameCursor = this.database.rawQuery("SELECT name FROM " + DatabaseHelper.EVENTS_CATEGORIES_TABLE_NAME + " WHERE id = " + categoryIdCursor.getInt(0), null);
			while(categoryNameCursor.moveToNext()) {
				this.addCategory(categoryNameCursor.getString(0));
			}
			categoryNameCursor.close();
		}
		categoryIdCursor.close();
		
		Cursor result = this.database.rawQuery("SELECT url, name, type, location, startDate, endDate, lat, lng FROM " + DatabaseHelper.EVENTS_TABLE_NAME + " WHERE id = '" + this.getId() + "'", null);
		if (result.getCount() > 0) {
			result.moveToFirst();
			
			try {
				this.setUrl(URLDecoder.decode(result.getString(0), "utf-8"));
				this.setName(URLDecoder.decode(result.getString(1), "utf-8"));
				this.setType(URLDecoder.decode(result.getString(2), "utf-8"));
				this.setLocation(URLDecoder.decode(result.getString(3), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			this.setStartDate(TypeCaster.toDate(result.getString(4), Event.DATE_FORMAT));
			this.setEndDate(TypeCaster.toDate(result.getString(5), Event.DATE_FORMAT));
			this.setLat(result.getDouble(6));
			this.setLng(result.getDouble(7));
			
			result.close();
			return true;
		} else {
			return false;
		}
	}
	
	public static ArrayList<Event> readAll(SQLiteDatabase database) {
		ArrayList<Event> events = new ArrayList<Event>();
		
		Cursor result = database.rawQuery("SELECT id FROM " + DatabaseHelper.EVENTS_TABLE_NAME + " ORDER by DATE asc", null);
		while(result.moveToNext()) {
			Event event = new Event(database);
			event.setId(result.getString(0));
			event.read();
			events.add(event);
		}
		result.close();
		
		return events;
	}
	
	
	public boolean insertOrUpdate() {
		Log.d(Preferences.DEBUG_TAG, "Inserting category with id " + this.getId());
			
		// Insert or ignore all categories
		for(String category : this.getCategories()) {
			this.database.execSQL("INSERT OR IGNORE INTO " + DatabaseHelper.EVENTS_CATEGORIES_TABLE_NAME + " (name) VALUES ('" + category + "')");
		}
		
		// Receive category ids
		ArrayList<Integer> categoryIds = new ArrayList<Integer>();
		for(String category : this.getCategories()) {
			Cursor result = database.rawQuery("SELECT id FROM " + DatabaseHelper.EVENTS_CATEGORIES_TABLE_NAME + " WHERE name = '" + category + "'", null);
			while(result.moveToNext()) {
				categoryIds.add(result.getInt(0));
			}
			result.close();
		}
			
		// Map categories to events
		this.database.execSQL("DELETE FROM " + DatabaseHelper.EVENTS_CATEGORIES_MAPPING_TABLE_NAME + " WHERE event_id = '" + this.getId() + "'");
		for(int categoryId : categoryIds) {
			this.database.execSQL("INSERT OR IGNORE INTO " + DatabaseHelper.EVENTS_CATEGORIES_MAPPING_TABLE_NAME + " (event_id, category_id) VALUES ('" + this.getId() + "'," + categoryId + ")");
		}
		
		try {
			this.database.execSQL("INSERT OR REPLACE INTO " + DatabaseHelper.EVENTS_TABLE_NAME + " (id, url, name, type, location, startDate, endDate, date, lat, lng) VALUES" +
					"('" +
					this.getId() + "','" +
					URLEncoder.encode(this.getUrl(), "utf-8") + "','" +
					URLEncoder.encode(this.getName(), "utf-8") + "','" +
					URLEncoder.encode(this.getType(), "utf-8") + "','" +
					URLEncoder.encode(this.getLocation(), "utf-8") + "','" +
					DateFormat.format(Event.DATE_FORMAT, this.getStartDate()) + "','" +
					DateFormat.format(Event.DATE_FORMAT, this.getEndDate()) + "'," +
					this.getStartDate().getTime() + "," +
					this.getLat() + "," +
					this.getLng() +
					")"
				);
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete() {
		try {
			this.database.execSQL("DELETE FROM " + DatabaseHelper.EVENTS_TABLE_NAME + " WHERE id = '" + this.getId() + "'");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean deserialize(String jsonString) {
		try {
			JSONObject object = new JSONObject(jsonString);
			
			this.setId(TypeCaster.toString(object.getString(Event.ID)));
			this.setUrl(TypeCaster.toString(object.getString(Event.URL)));
			this.setName(TypeCaster.toString(object.optString(Event.NAME)));
			this.setType(TypeCaster.toString(object.optString(Event.TYPE)));
			JSONArray categoriesJson = new JSONArray(object.optString(Event.CATEGORIES));
			for(int i = 0; i < categoriesJson.length(); i++) {
				this.addCategory(categoriesJson.getString(i));
			}
			
			this.setLocation(TypeCaster.toString(object.optString(Event.LOCATION)));
			this.setStartDate(TypeCaster.toDate(object.optString(Event.START_DATE), Event.DATE_FORMAT));
			this.setEndDate(TypeCaster.toDate(object.optString(Event.END_DATE), Event.DATE_FORMAT));
			this.setLat(TypeCaster.toDouble(object.optString(Event.LATITUDE)));
			this.setLng(TypeCaster.toDouble(object.optString(Event.LONGITUDE)));
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String serialize() {
		try {
			JSONObject object = new JSONObject();
			object.put(Event.ID, this.getId());
			object.put(Event.URL, this.getUrl());
			object.put(Event.NAME, this.getName());
			object.put(Event.TYPE, this.getType());
			
			JSONArray categories = new JSONArray();
			for(String category : this.getCategories())
				categories.put(category);
			
			object.put(Event.CATEGORIES, categories);
			
			object.put(Event.LOCATION, this.getLocation());
			object.put(Event.START_DATE, DateFormat.format(Event.DATE_FORMAT, this.getStartDate()));
			object.put(Event.END_DATE, DateFormat.format(Event.DATE_FORMAT, this.getEndDate()));
			object.put(Event.LATITUDE, this.getLat());
			object.put(Event.LONGITUDE, this.getLng());
			
			return object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}
	
	public void addCategory(String category) {
		this.categories.add(category);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public String getStartDateFormatted() {
		return (String) DateFormat.format("dd.MM.yyyy.", this.startDate);
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	public String getEndDateFormatted() {
		return (String) DateFormat.format("dd.MM.yyyy.", this.endDate);
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", url=" + url + ", name=" + name
				+ ", type=" + type + ", categories=" + categories
				+ ", location=" + location + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", lat=" + lat + ", lng=" + lng
				+ "]";
	}
}

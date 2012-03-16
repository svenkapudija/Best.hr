package com.svenkapudija.best.hr.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.svenkapudija.best.hr.utils.TypeCaster;

public class Event implements ModelDatabaseInterface {
	
	// Used for serializing/deserializing
	private static final String ID = "id";
	private static final String URL = "url";
	private static final String NAME = "name";
	private static final String TYPE = "type";
	private static final String CATEGORIES = "categories";
	private static final String LOCATION = "location";
	private static final String START_DATE = "start_date";
	private static final String END_DATE = "end_date";
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
		Cursor result = this.database.rawQuery("SELECT events_json FROM best_events WHERE id = '" + this.getId() + "'", null);
		if (result.getCount() > 0) {
			result.close();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean read() {
		Cursor result = this.database.rawQuery("SELECT events_json FROM best_events WHERE id = '" + this.getId() + "'", null);
		if (result.getCount() > 0) {
			result.moveToFirst();
			
			if(!this.deserialize(result.getString(0))) {
				result.close();
				return false;
			}
			
			result.close();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean insertOrUpdate() {
		try {
			this.database.execSQL("INSERT OR REPLACE INTO best_events (id, events_json) VALUES ('" + this.getId() + "','" + this.serialize() + "')");
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean delete() {
		try {
			this.database.execSQL("DELETE FROM best_events WHERE id = '" + this.getId() + "'");
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
			this.setStartDate(TypeCaster.toDate(object.optString(Event.START_DATE), "yyyy-MM-dd"));
			this.setEndDate(TypeCaster.toDate(object.optString(Event.END_DATE), "yyyy-MM-dd"));
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
			object.put(Event.START_DATE, this.getStartDate().toString());
			object.put(Event.END_DATE, this.getEndDate().toString());
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

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
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

package com.svenkapudija.best.hr.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.svenkapudija.best.hr.database.DatabaseHelper;
import com.svenkapudija.best.hr.files.ImageHelper;
import com.svenkapudija.best.hr.utils.Preferences;
import com.svenkapudija.best.hr.utils.TypeCaster;

public class AnnualReport implements DatabaseInterface {
	
	// Used for serializing/deserializing
	private static final String YEAR = "year";
	private static final String THUMBNAIL_LINK = "thumbnail";
	private static final String LINK = "link";
	
	private int year;
	private Bitmap thumbnail;
	private String thumbnailLink;
	private String link;
	private SQLiteDatabase database;
	
	public AnnualReport() {
		
	}

	public AnnualReport(SQLiteDatabase database) {
		this.database = database; 
	}
	
	/**
	 * Returns number of AnnualReport objects in database.
	 * 
	 * @param database {@link SQLiteDatabase}
	 * @return number of AnnualReport objects
	 */
	public static int getCount(SQLiteDatabase database) {
		int count = 0;
		
		Cursor result = database.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.ANNUAL_REPORTS_TABLE_NAME, null);
		if (result.getCount() > 0) {
			result.moveToFirst();
			count = result.getInt(0);
		}
		result.close();
		
		return count;
	}
	
	public boolean exists() {
		Cursor result = this.database.rawQuery("SELECT year FROM " + DatabaseHelper.ANNUAL_REPORTS_TABLE_NAME + " WHERE year = " + this.getYear(), null);
		if (result.getCount() > 0) {
			result.close();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean read() {
		Cursor result = this.database.rawQuery("SELECT thumbnailLink, link FROM " + DatabaseHelper.ANNUAL_REPORTS_TABLE_NAME + " WHERE year = " + this.getYear(), null);
		if (result.getCount() > 0) {
			result.moveToFirst();
			
			try {
				this.setThumbnailLink(URLDecoder.decode(result.getString(0), "utf-8"));
				this.setLink(URLDecoder.decode(result.getString(1), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			String imageName = ImageHelper.getImageNameFromUrl(this.getThumbnailLink());
			this.setThumbnail(ImageHelper.getImageFromFile(Preferences.ANNUAL_REPORTS_DIRECTORY, imageName));
			
			result.close();
			return true;
		} else {
			return false;
		}
	}
	
	public static ArrayList<AnnualReport> readAll(SQLiteDatabase database) {
		ArrayList<AnnualReport> reports = new ArrayList<AnnualReport>();
		
		Cursor result = database.rawQuery("SELECT year, thumbnailLink, link FROM " + DatabaseHelper.ANNUAL_REPORTS_TABLE_NAME + " ORDER by YEAR desc", null);
		while(result.moveToNext()) {
			AnnualReport report = new AnnualReport(database);
			report.setYear(result.getInt(0));
			
			try {
				report.setThumbnailLink(URLDecoder.decode(result.getString(1), "utf-8"));
				report.setLink(URLDecoder.decode(result.getString(2), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			String imageName = ImageHelper.getImageNameFromUrl(report.getThumbnailLink());
			report.setThumbnail(ImageHelper.getImageFromFile(Preferences.ANNUAL_REPORTS_DIRECTORY, imageName));
			
			reports.add(report);
		}
		result.close();
		
		return reports;
	}
	
	public boolean insertOrUpdate() {
		try {
			this.database.execSQL("INSERT OR REPLACE INTO " + DatabaseHelper.ANNUAL_REPORTS_TABLE_NAME + " (year, thumbnailLink, link) VALUES" +
					"(" +
					this.getYear() + ",'" +
					URLEncoder.encode(this.getThumbnailLink(), "utf-8") + "','" +
					URLEncoder.encode(this.getLink(), "utf-8") +
					"')"
				);
			
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean delete() {
		try {
			this.database.execSQL("DELETE FROM " + DatabaseHelper.ANNUAL_REPORTS_TABLE_NAME + " WHERE year = " + this.getYear());
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean deserialize(String jsonString) {
		try {
			JSONObject object = new JSONObject(jsonString);
			
			this.setYear(TypeCaster.toInt(object.getString(AnnualReport.YEAR)));
			this.setThumbnailLink(TypeCaster.toString(object.getString(AnnualReport.THUMBNAIL_LINK)));
			this.setLink(TypeCaster.toString(object.getString(AnnualReport.LINK)));
			
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String serialize() {
		try {
			JSONObject object = new JSONObject();
			object.put(AnnualReport.YEAR, this.getYear());
			object.put(AnnualReport.THUMBNAIL_LINK, this.getThumbnailLink());
			object.put(AnnualReport.LINK, this.getLink());
			
			return object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getYear() {
		return this.year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public Bitmap getThumbnail() {
		return this.thumbnail;
	}
	public void setThumbnail(Bitmap thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public String getThumbnailLink() {
		return this.thumbnailLink;
	}
	public void setThumbnailLink(String thumbnailLink) {
		this.thumbnailLink = thumbnailLink;
	}
	
	public String getLink() {
		return this.link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public String toString() {
		return "AnnualReport [year=" + year + ", thumbnail=" + thumbnail
				+ ", thumbnailLink=" + thumbnailLink + ", link=" + link + "]";
	}
}
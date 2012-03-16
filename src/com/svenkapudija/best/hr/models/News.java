package com.svenkapudija.best.hr.models;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.svenkapudija.best.hr.utils.TypeCaster;

public class News implements ModelDatabaseInterface {
	
	// Used for serializing/deserializing
	private static final String ID = "news_id";
	private static final String TITLE = "title";
	private static final String AUTHOR = "author";
	private static final String IMAGE_LINK = "image";
	private static final String PUBLISHED = "published_at";
	private static final String LINK = "link";
	private static final String INTRO = "intro";
	private static final String BODY = "body";
	
	private int id;
	private String title;
	private String author;
	private String imageLink;
	private Bitmap image;
	private Date published;
	private String link;
	private String intro;
	private String body;
	private SQLiteDatabase database;
	
	public News() {
		
	}
	
	public News(SQLiteDatabase database) {
		this.database = database; 
	}
	
	public boolean exists() {
		Cursor result = this.database.rawQuery("SELECT news_json FROM best_news WHERE id = " + this.getId(), null);
		if (result.getCount() > 0) {
			result.close();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean read() {
		Cursor result = this.database.rawQuery("SELECT news_json FROM best_news WHERE id = " + this.getId(), null);
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
			this.database.execSQL("INSERT OR REPLACE INTO best_news (id, news_json) VALUES (" + this.getId() + ",'" + this.serialize() + "')");
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean delete() {
		try {
			this.database.execSQL("DELETE FROM best_news WHERE id = " + this.getId());
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean deserialize(String jsonString) {
		try {
			JSONObject object = new JSONObject(jsonString);
			
			this.setId(TypeCaster.toInt(object.getString(News.ID)));
			this.setTitle(TypeCaster.toString(object.getString(News.TITLE)));
			this.setAuthor(TypeCaster.toString(object.optString(News.AUTHOR)));
			this.setImageLink(TypeCaster.toString(object.optString(News.IMAGE_LINK)));
			this.setPublished(TypeCaster.toDate(object.optString(News.PUBLISHED)));
			this.setLink(TypeCaster.toString(object.optString(News.LINK)));
			this.setIntro(TypeCaster.toString(object.optString(News.INTRO)));
			this.setBody(TypeCaster.toString(object.optString(News.BODY)));
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String serialize() {
		try {
			JSONObject object = new JSONObject();
			object.put(News.ID, this.getId());
			object.put(News.TITLE, this.getTitle());
			object.put(News.AUTHOR, this.getAuthor());
			object.put(News.IMAGE_LINK, this.getImageLink());
			object.put(News.PUBLISHED, this.getPublished());
			object.put(News.LINK, this.getLink());
			object.put(News.INTRO, this.getIntro());
			object.put(News.BODY, this.getBody());
			
			return object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", author=" + author
				+ ", imageLink=" + imageLink + ", image=" + image
				+ ", published=" + published + ", link=" + link + ", intro="
				+ intro + ", body=" + body + "]";
	}
}

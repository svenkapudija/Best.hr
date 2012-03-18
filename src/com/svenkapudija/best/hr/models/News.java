package com.svenkapudija.best.hr.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;

import com.svenkapudija.best.hr.files.ImageHelper;
import com.svenkapudija.best.hr.utils.Preferences;
import com.svenkapudija.best.hr.utils.TypeCaster;

public class News implements DatabaseInterface {
	
	// Used for serializing/deserializing
	private static final String ID = "news_id";
	private static final String TITLE = "title";
	private static final String AUTHOR = "author";
	private static final String IMAGE_LINK = "image";
	private static final String PUBLISHED = "published_at";
	private static final String DATE_FORMAT = "dd.MM.yyyy.";
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
		try {
			Cursor result = this.database.rawQuery("SELECT id FROM best_news WHERE id = " + this.getId(), null);
			if (result.getCount() > 0) {
				result.close();
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean read() {
		try {
			Cursor result = this.database.rawQuery("SELECT title, author, imageLink, published, link, intro, body FROM best_news WHERE id = " + this.getId(), null);
			if (result.getCount() > 0) {
				result.moveToFirst();
				
				this.setTitle(URLDecoder.decode(result.getString(0), "utf-8"));
				this.setAuthor(URLDecoder.decode(result.getString(1), "utf-8"));
				this.setImageLink(URLDecoder.decode(result.getString(2), "utf-8"));
				this.setPublished(TypeCaster.toDate(result.getString(3), News.DATE_FORMAT));
				this.setLink(URLDecoder.decode(result.getString(4), "utf-8"));
				this.setIntro(URLDecoder.decode(result.getString(5), "utf-8"));
				this.setBody(URLDecoder.decode(result.getString(6), "utf-8"));
				
				String imageName = ImageHelper.getImageNameFromUrl(this.getImageLink());
				this.setImage(ImageHelper.getImageFromFile("besthrNews", imageName));
				
				result.close();
				return true;
			} else {
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static News getLastNews(SQLiteDatabase database) {
		try {
			Cursor result = database.rawQuery("SELECT id, title, author, imageLink, published, link, intro, body FROM best_news ORDER by DATE desc LIMIT 1", null);
			if (result.getCount() > 0) {
				result.moveToFirst();
				
				News news = new News(database);
				news.setId(result.getInt(0));
				
				news.setTitle(URLDecoder.decode(result.getString(1), "utf-8"));
				news.setAuthor(URLDecoder.decode(result.getString(2), "utf-8"));
				news.setImageLink(URLDecoder.decode(result.getString(3), "utf-8"));
				news.setPublished(TypeCaster.toDate(result.getString(4), News.DATE_FORMAT));
				news.setLink(URLDecoder.decode(result.getString(5), "utf-8"));
				news.setIntro(URLDecoder.decode(result.getString(6), "utf-8"));
				news.setBody(URLDecoder.decode(result.getString(7), "utf-8"));
				
				String imageName = ImageHelper.getImageNameFromUrl(news.getImageLink());
				news.setImage(ImageHelper.getImageFromFile("besthrNews", imageName));
				
				result.close();
				return news;
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<News> readAll(SQLiteDatabase database) {
		ArrayList<News> newsList = new ArrayList<News>();
		
		Cursor result = database.rawQuery("SELECT id, title, author, imageLink, published, link, intro, body FROM best_news ORDER by DATE desc", null);
		while(result.moveToNext()) {
			News news = new News(database);
			news.setId(result.getInt(0));
			
			try {
				news.setTitle(URLDecoder.decode(result.getString(1), "utf-8"));
				news.setAuthor(URLDecoder.decode(result.getString(2), "utf-8"));
				news.setImageLink(URLDecoder.decode(result.getString(3), "utf-8"));
				news.setPublished(TypeCaster.toDate(result.getString(4), News.DATE_FORMAT));
				news.setLink(URLDecoder.decode(result.getString(5), "utf-8"));
				news.setIntro(URLDecoder.decode(result.getString(6), "utf-8"));
				news.setBody(URLDecoder.decode(result.getString(7), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			String imageName = ImageHelper.getImageNameFromUrl(news.getImageLink());
			news.setImage(ImageHelper.getImageFromFile("besthrNews", imageName));
			
			newsList.add(news);
		}
		result.close();
		
		return newsList;
	}
	
	public static ArrayList<News> readAll(SQLiteDatabase database, int limitStart, int limitEnd) {
		ArrayList<News> newsList = new ArrayList<News>();
		
		Cursor result = database.rawQuery("SELECT id, title, author, imageLink, published, link, intro, body FROM best_news ORDER by DATE desc LIMIT " + limitStart + "," + limitEnd, null);
		while(result.moveToNext()) {
			News news = new News(database);
			news.setId(result.getInt(0));
			
			try {
				news.setTitle(URLDecoder.decode(result.getString(1), "utf-8"));
				news.setAuthor(URLDecoder.decode(result.getString(2), "utf-8"));
				news.setImageLink(URLDecoder.decode(result.getString(3), "utf-8"));
				news.setPublished(TypeCaster.toDate(result.getString(4), News.DATE_FORMAT));
				news.setLink(URLDecoder.decode(result.getString(5), "utf-8"));
				news.setIntro(URLDecoder.decode(result.getString(6), "utf-8"));
				news.setBody(URLDecoder.decode(result.getString(7), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			String imageName = ImageHelper.getImageNameFromUrl(news.getImageLink());
			news.setImage(ImageHelper.getImageFromFile("besthrNews", imageName));
			
			newsList.add(news);
		}
		result.close();
		
		return newsList;
	}
	
	public static int getCount(SQLiteDatabase database) {
		int count = 0;
		
		Cursor result = database.rawQuery("SELECT COUNT(*) FROM best_news", null);
		if (result.getCount() > 0) {
			result.moveToFirst();
			
			Log.d(Preferences.DEBUG_TAG, "result " + result.toString());
			count = result.getInt(0);
		}
		result.close();
		
		return count;
	}
	
	public boolean insertOrUpdate() {
		try {
			Log.d(Preferences.DEBUG_TAG, "database " + this.database);
			this.database.execSQL("INSERT OR REPLACE INTO best_news (id, title, author, imageLink, published, date, link, intro, body) VALUES" +
					"(" +
					this.getId() + ",'" +
					URLEncoder.encode(this.getTitle(), "utf-8") + "','" +
					URLEncoder.encode(this.getAuthor(), "utf-8") + "','" +
					URLEncoder.encode(this.getImageLink(), "utf-8") + "','" +
					DateFormat.format(News.DATE_FORMAT, this.getPublished()) + "'," +
					this.getPublished().getTime() + ",'" + 
					URLEncoder.encode(this.getLink(), "utf-8") + "','" +
					URLEncoder.encode(this.getIntro(), "utf-8") + "','" +
					URLEncoder.encode(this.getBody(), "utf-8") + 
					"')"
				);
			
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete() {
		try {
			this.database.execSQL("DELETE FROM best_news WHERE id = " + this.getId());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			e.printStackTrace();
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
			this.setPublished(TypeCaster.toDate(object.optString(News.PUBLISHED), News.DATE_FORMAT));
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
			object.put(News.PUBLISHED, DateFormat.format(News.DATE_FORMAT, this.getPublished()));
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

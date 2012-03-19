package com.svenkapudija.best.hr.api;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.svenkapudija.best.hr.files.ImageHelper;
import com.svenkapudija.best.hr.internet.SimpleHttpClient;
import com.svenkapudija.best.hr.models.AnnualReport;
import com.svenkapudija.best.hr.models.Event;
import com.svenkapudija.best.hr.models.News;
import com.svenkapudija.best.hr.models.Person;
import com.svenkapudija.best.hr.utils.Preferences;

public class BestHrApi {
	
	public static final String BASE_API_URL = "http://www.best.hr/api";
	public static final String BASE_URL = "http://www.best.hr";
		
	private static final String NEWS = "/index.php?path=/novosti";
	private static final String NEWS_AT_ID = "/index.php?path=/novosti/get&news_id=";
	private static final String CONTACT = "/index.php?path=/kontakt";
	private static final String ANNUAL_REPORT_LIST = "/index.php?path=/o-nama/gi";
	private static final String SEMINAR_LIST = "/index.php?path=/seminari/lista_seminara";
	private static final String ANY_PATH = "/index.php?path=";
		
	private Context context;
	private int type = SimpleHttpClient.HTTP_GET; // All calls are GET
	
	public BestHrApi(Context context) {
		this.context = context;
	}

	/**
	 * Retrieves news at specific <code>id</code>.
	 * 
	 * @return News object if everything went ok, <code>null</code> otherwise.
	 */
	public News getNews(int newsId) {
		SimpleHttpClient client = new SimpleHttpClient(this.getContext(), BASE_API_URL + NEWS_AT_ID + newsId, this.getType());
		client.performRequest();
		String result = client.getResultAsString();

		if (result != null) {
			try {
				JSONArray allNews = new JSONArray(result);
				if(allNews.length() > 0) {
					JSONObject newsJson = allNews.getJSONObject(0);
					News news = new News();
					news.deserialize(newsJson.toString());
					if(!news.getImageLink().equals("null"))
						news.setImage(ImageHelper.getImageFromInternet(context, Preferences.NEWS_DIRECTORY, BASE_URL + news.getImageLink()));
					return news;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return null;
	}
	
	/**
	 * Retrieves news at specific <code>id</code>.
	 * 
	 * @return news News object if everything went ok, <code>null</code> otherwise.
	 */
	public int getNewsCount() {
		SimpleHttpClient client = new SimpleHttpClient(this.getContext(), BASE_API_URL + NEWS, this.getType());
		client.performRequest();
		String result = client.getResultAsString();

		int counter = 0;
		if (result != null) {
			JSONArray allNews;
			try {
				allNews = new JSONArray(result);
				counter = allNews.length();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return counter;
	}
	
	
	/**
	 * Retrieves only last news.
	 * 
	 * @return news News object (populated with <code>id</code>, <code>title</code> and <code>imageLink</code>) if everything went ok, <code>null</code> otherwise.
	 */
	public News getLastNews() {
		SimpleHttpClient client = new SimpleHttpClient(this.getContext(), BASE_API_URL + NEWS, this.getType());
		client.performRequest();
		String result = client.getResultAsString();

		News news = null;
		if (result != null) {
			try {
				JSONArray allNews = new JSONArray(result);
				if(allNews.length() > 0) {
					JSONObject newsJson = allNews.getJSONObject(0);
					news = new News();
					news.deserialize(newsJson.toString());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return news;
	}

	/**
	 * Retrieves all the news.
	 * 
	 * @return ArrayList populated with all News object from the beginning of time.
	 */
	public ArrayList<News> getNews() {
		SimpleHttpClient client = new SimpleHttpClient(this.getContext(), BASE_API_URL + NEWS, this.getType());
		client.performRequest();
		String result = client.getResultAsString();

		ArrayList<News> newsList = new ArrayList<News>();
		if (result != null) {
			try {
				JSONArray allNews = new JSONArray(result);
				for(int i = 0; i < allNews.length(); i++) {
					JSONObject newsJson = allNews.getJSONObject(i);
					News news = new News();
					news.deserialize(newsJson.toString());
					newsList.add(news);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return newsList;
	}
	
	/**
	 * Retrieves board members.
	 * 
	 * @return ArrayList populated with Person objects (vivaldi + board members).
	 */
	public ArrayList<Person> getBoardMembers() {
		SimpleHttpClient client = new SimpleHttpClient(this.getContext(), BASE_API_URL + CONTACT, this.getType());
		client.performRequest();
		String result = client.getResultAsString();

		ArrayList<Person> boardMembers = new ArrayList<Person>();
		if (result != null) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONObject data = jsonObject.getJSONObject("data");
				
				JSONObject vivaldiJson = data.getJSONObject("vivaldi");
				Person vivaldiMember = new Person();
				vivaldiMember.deserialize(vivaldiJson.toString());
				vivaldiMember.setType("vivaldi");
				boardMembers.add(vivaldiMember);
				
				JSONArray board = data.getJSONArray("board");
				for(int i = 0; i < board.length(); i++) {
					JSONObject memberJson = board.getJSONObject(i);
					Person boardMember = new Person();
					boardMember.deserialize(memberJson.toString());
					boardMember.setType("board");
					boardMembers.add(boardMember);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return boardMembers;
	}
	
	/**
	 * Retrieves all annual reports.
	 * 
	 * @return ArrayList populated with AnnualReport objects.
	 */
	public ArrayList<AnnualReport> getAnnualReports() {
		SimpleHttpClient client = new SimpleHttpClient(this.getContext(), BASE_API_URL + ANNUAL_REPORT_LIST, this.getType());
		client.performRequest();
		String result = client.getResultAsString();

		ArrayList<AnnualReport> annualReports = new ArrayList<AnnualReport>();
		if (result != null) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray reports = jsonObject.getJSONArray("data");
				
				for(int i = 0; i < reports.length(); i++) {
					JSONObject reportJson = reports.getJSONObject(i);
					AnnualReport report = new AnnualReport();
					report.deserialize(reportJson.toString());
					if(!report.getThumbnailLink().equals("null")) {
						report.setThumbnail(ImageHelper.getImageFromInternet(context, Preferences.ANNUAL_REPORTS_DIRECTORY, report.getThumbnailLink()));
					}
					annualReports.add(report);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return annualReports;
	}
	
	/**
	 * Retrieves all seminars.
	 * 
	 * @return ArrayList populated with Event objects.
	 */
	public ArrayList<Event> getEvents() {
		SimpleHttpClient client = new SimpleHttpClient(this.getContext(), BASE_API_URL + SEMINAR_LIST, this.getType());
		client.performRequest();
		String result = client.getResultAsString();

		ArrayList<Event> events = new ArrayList<Event>();
		if (result != null) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray reports = jsonObject.getJSONArray("data");
				
				for(int i = 0; i < reports.length(); i++) {
					JSONObject eventCategory = reports.getJSONObject(i);
					JSONArray eventsArray = eventCategory.getJSONArray("events");
					
					for(int j = 0; j < eventsArray.length(); j++) {
						JSONObject eventJson = eventsArray.getJSONObject(j);
						Event event = new Event();
						event.deserialize(eventJson.toString());
						events.add(event);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return events;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}

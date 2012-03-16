package com.svenkapudija.best.hr.internet;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.svenkapudija.best.hr.models.AnnualReport;
import com.svenkapudija.best.hr.models.Event;
import com.svenkapudija.best.hr.models.News;
import com.svenkapudija.best.hr.utils.Preferences;

import android.content.Context;
import android.util.Log;

public class BestHrApi {
	
	private static final String API_KEY = "api_key"; // None for now
	private static final String BASE_API_URL = "http://www.best.hr/api";
		
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
	 * @return news News object if everything went ok, <code>null</code> otherwise.
	 */
	public News getNews(int newsId) {
		SimpleHttpClient client = new SimpleHttpClient(this.getContext(), BASE_API_URL + NEWS_AT_ID + newsId, this.getType());
		client.performRequest();
		String result = client.getResultAsString();

		News news = null;
		if (result != null) {
			try {
				JSONArray allNews = new JSONArray(result);
				if(allNews.length() > 0) {
					try {
						JSONObject newsJson = allNews.getJSONObject(0);
						news = new News();
						news.deserialize(newsJson.toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return news;
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
					try {
						JSONObject newsJson = allNews.getJSONObject(0);
						news = new News();
						news.deserialize(newsJson.toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}
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
					try {
						JSONObject newsJson = allNews.getJSONObject(i);
						News news = new News();
						news.deserialize(newsJson.toString());
						newsList.add(news);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return newsList;
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
					try {
						JSONObject reportJson = reports.getJSONObject(i);
						AnnualReport report = new AnnualReport();
						report.deserialize(reportJson.toString());
						annualReports.add(report);
					} catch (JSONException e) {
						e.printStackTrace();
					}
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
	public ArrayList<Event> getSeminars() {
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
						try {
							JSONObject eventJson = eventsArray.getJSONObject(j);
							Event event = new Event();
							event.deserialize(eventJson.toString());
							events.add(event);
						} catch (JSONException e) {
							e.printStackTrace();
						}
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

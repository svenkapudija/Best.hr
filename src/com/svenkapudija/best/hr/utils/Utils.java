package com.svenkapudija.best.hr.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

public class Utils {
	
	public static void share(Context context, String subject, String body) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, "Podijeli..."));
	}
	
	public static void sendEmail(Context context, String to) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{to});
		i.putExtra(Intent.EXTRA_SUBJECT, "Naslov");
		i.putExtra(Intent.EXTRA_TEXT   , "Poruka");
		try {
			context.startActivity(Intent.createChooser(i, "Pošalji email..."));
        } catch (ActivityNotFoundException e) {
        	e.printStackTrace();
        	Toast.makeText(context, "Greška pri pokretanju aplikacije za email.", Toast.LENGTH_LONG).show();
        }
	}
	
	public static void call(Context context, String phone) {
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        try {
        	context.startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
        	e.printStackTrace();
        	Toast.makeText(context, "Greška pri pokretanju aplikacije za poziv.", Toast.LENGTH_LONG).show();
        }
	}
	
	/**
	 * Removes all HTML tags except <code>li</code> and <code>p</code>
	 * 
	 * @param text
	 * @return String without HTML tags.
	 */
	public static String removeHtmlTagsExceptParagraphList(String text) {
		return text.replaceAll("(?i)<(?!(/?(li|p)))[^>]*>", "");
	}
	
	/**
	 * Removes all HTML tags.
	 * 
	 * @param text
	 * @return String without HTML tags.
	 */
	public static String removeHtmlTags(String text) {
		return text.replaceAll("(?i)<[^>]*>", "");
	}
	
	public static GeoPoint getGeoPoint(double lat, double lng) {
		return new GeoPoint((int) (lat*1E6), (int) (lng*1E6));
	}
}

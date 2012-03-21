package com.svenkapudija.best.hr.utils;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.svenkapudija.best.hr.R;

public class Utils {
	
	public static void noInternetConnectionDialog(final Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setMessage(message);
		//builder.setTitle();
		builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				return;
			}
		});

		builder.show();
	}
	
	public static void share(Context context, String subject, String body) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
	}
	
	public static void sendEmail(Context context, String to) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
		i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.subject));
		i.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.message));
		try {
			context.startActivity(Intent.createChooser(i, context.getString(R.string.send_email)));
        } catch (ActivityNotFoundException e) {
        	e.printStackTrace();
        	Toast.makeText(context, context.getString(R.string.send_email_error), Toast.LENGTH_LONG).show();
        }
	}
	
	public static void call(Context context, String phone) {
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        try {
        	context.startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
        	e.printStackTrace();
        	Toast.makeText(context, context.getString(R.string.call_error), Toast.LENGTH_LONG).show();
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

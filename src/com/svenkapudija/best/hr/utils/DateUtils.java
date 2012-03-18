package com.svenkapudija.best.hr.utils;

public class DateUtils {
	
	private static final String[] months = {"Sijeèanj", "Veljaèa", "Ožujak", "Travanj", "Svibanj", "Lipanj", "Srpanj", "Kolovoz", "Rujan", "Listopad", "Studeni", "Prosinac"};
	
	public static String getMonthName(int month) {
		return months[month];
	}
}

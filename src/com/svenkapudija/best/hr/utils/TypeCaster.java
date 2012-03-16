package com.svenkapudija.best.hr.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The TypeCaster is used for conversion of string values (typically received
 * from a deserializer) to primitive or complex data objects.
 * 
 * @author carr
 * 
 */
public class TypeCaster {
    private static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy.";

    public static String dateFormat = DEFAULT_DATE_FORMAT;

    public static void init() {
        dateFormat = DEFAULT_DATE_FORMAT;
    }

    public static int toInt(String s) {
        if (s.trim().equals(""))
            return 0;
        else
            return Integer.parseInt(s);
    }

    public static long toLong(String s) {
        if (s.trim().equals("")) {
            return 0;
        } else {
            return Long.parseLong(s);
        }

    }

    public static boolean toBoolean(String s) {
        s = s.trim();
        return (s.equals("1") || s.equals("true") || s.equals("TRUE"));
    }

    public static String toString(String s) {
        return s;
    }

    public static float toFloat(String s) {
        if (s.trim().equals(""))
            return 0;
        else
            return Float.parseFloat(s);
    }

    public static double toDouble(String s) {
    	if (s.trim().equals(""))
            return 0;
    	else
    		return Double.parseDouble(s);
    }
    
    public static Date toDate(String dateString) {
    	if(dateString == null || dateString.length() == 0)
    		return null;
    	
        SimpleDateFormat formater = new SimpleDateFormat(dateFormat);
        Date parsedDate = null;
        try {
            parsedDate = formater.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

    public static Date toDate(String dateString, String dateFormat) {
        SimpleDateFormat formater = new SimpleDateFormat(dateFormat);
        Date parsedDate = null;
        try {
            parsedDate = formater.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }
}
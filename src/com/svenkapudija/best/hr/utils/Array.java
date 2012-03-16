package com.svenkapudija.best.hr.utils;

import java.util.ArrayList;

public class Array {
	/**
	* Method to join array elements of type string
	* @author Hendrik Will, imwill.com
	* @param inputArray Array which contains strings
	* @param glueString String between each array element
	* @return String containing all array elements seperated by glue string
	*/
	public static String implodeString(ArrayList<String> inputArray, String glueString, boolean addQuotes) {
		String output = "";
	
		if (inputArray.size() > 0) {
			StringBuilder sb = new StringBuilder();
			if(addQuotes) sb.append("'");
			sb.append(inputArray.get(0));
			if(addQuotes) sb.append("'");
			
			for (int i=1; i<inputArray.size(); i++) {
				sb.append(glueString);
				if(addQuotes) sb.append("'");
				sb.append(inputArray.get(i));
				if(addQuotes) sb.append("'");
			}
	
			output = sb.toString();
		}
	
		return output;
	}
	
	public static String implodeInt(ArrayList<Integer> inputArray, String glueString) {
		String output = "";
	
		if (inputArray.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(inputArray.get(0));
			
			for (int i=1; i<inputArray.size(); i++) {
				sb.append(glueString);
				sb.append(inputArray.get(i));
			}
	
			output = sb.toString();
		}
	
		return output;
	}
}

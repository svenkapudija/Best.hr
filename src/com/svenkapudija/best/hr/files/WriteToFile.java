package com.svenkapudija.best.hr.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Environment;
import android.util.Log;

import com.svenkapudija.best.hr.utils.Preferences;

public class WriteToFile {
	public static void writeToFile(String string) {
		File path = Environment.getExternalStorageDirectory();
		File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Preferences.APP_NAME, Preferences.FILE_NAME + ".txt");
		String fileName = myFile.toString();
		try {
	    	if(path.canWrite()) {
		    	File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Preferences.APP_NAME);
		    	
		    	if(!myDir.exists())
		    		myDir.mkdirs();
		    	if(!myFile.exists()) {
			    	myFile.createNewFile();
		    	}
		    	
		    	FileOutputStream fis = new FileOutputStream(new File(fileName), true);
		        OutputStreamWriter isr = new OutputStreamWriter(fis, "UTF-8");
		        BufferedWriter in = new BufferedWriter(isr);

		    	in.write(string + "\r\n");
		    	in.close();
	    	} else {
	    		Log.e(Preferences.DEBUG_TAG, "cannot write to sdcard");
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
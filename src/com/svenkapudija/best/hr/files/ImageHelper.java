package com.svenkapudija.best.hr.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.svenkapudija.best.hr.internet.SimpleHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ImageHelper {
	/**
	 * Takes only the last part after "/".
	 * http://www.something.com/picture.png => picture.png
	 * @param url
	 * @return Image name with extension.
	 */
	public static String getImageNameFromUrl(String url) {
		String[] imageNameFromUrl = url.split("/");
		int length = imageNameFromUrl.length;
		return imageNameFromUrl[(length-1)];
	}
	
	public static String removeExtension(String imageName) {
		return imageName.split("\\.")[0];
	}
	
	/**
	 * 
	 * @param folderOnSdCard Folder where to look for the image.
	 * @param imageName Picture name includes extension.
	 * @return Bitmap image.
	 */
	public static Bitmap getImageFromFile(String folderOnSdCard, String imageName) {
		return BitmapFactory.decodeFile("/sdcard/" + folderOnSdCard + "/" + imageName);
	}
	
	public static Bitmap getImageFromInternet(Context context, String saveToFolder, String imageUrl) {
		Bitmap image = null;
		try {
			SimpleHttpClient imageRequest = new SimpleHttpClient(context, imageUrl, SimpleHttpClient.HTTP_GET, false);
			imageRequest.performRequest();

			image = imageRequest.getResultAsBitmap();
			if (image == null) return null;
			
			File path = Environment.getExternalStorageDirectory();
			File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + saveToFolder, ImageHelper.getImageNameFromUrl(imageUrl));
			String fileName = myFile.toString();
			if(path.canWrite()) {
		    	File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + saveToFolder);
		    	
		    	if(!myDir.exists())
		    		myDir.mkdirs();
		    	if(!myFile.exists()) {
			    	myFile.createNewFile();
		    	}

		    	FileOutputStream fis = new FileOutputStream(new File(fileName), true);
		    	image.compress(Bitmap.CompressFormat.PNG, 85, fis);
		    	fis.flush();
		    	fis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
	
	public static boolean imageExists(String folderOnSdCard, String imageName) {
		File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderOnSdCard, imageName);
		if(myFile.exists()) {
	    	return true;
    	} else {
    		return false;
    	}
	}
}

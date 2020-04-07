package com.technotium.technotiumapp.config;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageProcessing {
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    if (height > reqHeight || width > reqWidth) {
	    	final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	    return inSampleSize;
	}
	

	
	public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    
	    return BitmapFactory.decodeFile(filePath, options);
	    //return toGrayscale(BitmapFactory.decodeFile(filePath, options));
	}
	
	public static Bitmap resizeBitmap(Bitmap bitmap, int scaleSize) {
        Bitmap resizedBitmap = null;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;
        if(originalHeight > originalWidth) {
        	if(originalHeight > scaleSize){
	            newHeight = scaleSize ;
	            multFactor = (float) originalWidth/(float) originalHeight;
	            newWidth = (int) (newHeight * multFactor);
        	}
        	else{
        		newHeight = originalHeight;
        		newWidth = originalWidth;
        	}
        } else if(originalWidth > originalHeight) {
        	if(originalWidth > scaleSize){
	            newWidth = scaleSize ;
	            multFactor = (float) originalHeight/ (float)originalWidth;
	            newHeight = (int) (newWidth * multFactor);
        	}
        	else{
        		newHeight = originalHeight;
        		newWidth = originalWidth;
        	}
        } else if(originalHeight == originalWidth) {
        	if(originalHeight > scaleSize){
        		newHeight = scaleSize ;
        		newWidth = scaleSize ;
        	}
        	else{
        		newHeight = originalHeight ;
        		newWidth = originalWidth;
        	}
        }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        return resizedBitmap;
    }
	
	
	
	public static Bitmap toGrayscale(Bitmap bmpOriginal){
	    int width, height;
	    height = bmpOriginal.getHeight();
	    width = bmpOriginal.getWidth();    

	    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    Canvas c = new Canvas(bmpGrayscale);
	    Paint paint = new Paint();
	    ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
	    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	    paint.setColorFilter(f);
	    c.drawBitmap(bmpOriginal, 0, 0, paint);
	    return bmpGrayscale;
	}

}

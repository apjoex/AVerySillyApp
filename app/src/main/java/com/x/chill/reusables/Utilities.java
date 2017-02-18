package com.x.chill.reusables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apjoe on 2/17/2017.
 */

public class Utilities extends android.app.Application{

    /**
     * This method provided by Romain Guy, so it should do the job better, especially it includes case for listViews
     */
    public static Bitmap getBitmapFromView(View view, int width, int height) {

        //Pre-measure the view so that height and width don't remain null.
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        //Assign a size and position to the view and all of its descendants
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        // Create bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);

        //Create a canvas with the specified bitmap to draw into
        Canvas canvas = new Canvas(bitmap);

        // if it's scrollView we get gull size
        canvas.translate(-view.getScrollX(), -view.getScrollY());
        //Render this view (and all of its children) to the given Canvas
        view.draw(canvas);
        return bitmap;
    }


    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public static String storeImage(Context context, Bitmap image) throws InstantiationException, IllegalAccessException {
        File pictureFile = getOutputMediaFile(context);
        if (pictureFile == null) {
            Log.d("#AVSA", "Error creating media file, check storage permissions: ");// e.getMessage());
        }else{
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                image.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
                Log.d("#AVSA","File saved: ");
                MediaScannerConnection.scanFile(context, new String[]{pictureFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {
                        Log.d("#ASVA","Refreshed gallery");
                    }

                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                        Log.d("#ASVA","Refreshed gallery");
                    }
                });
            } catch (FileNotFoundException e) {
                Log.d("#AVSA","File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("#AVSA", "Error accessing file: " + e.getMessage());
            }
        }

        assert pictureFile != null;
        return pictureFile.getAbsolutePath();

    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(Context context) throws IllegalAccessException, InstantiationException {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="AVSA"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


//    Bitmap viewCapture = null;
//
//    theViewYouWantToCapture.setDrawingCacheEnabled(true);
//
//    viewCapture = Bitmap.createBitmap(theViewYouWantToCapture.getDrawingCache());
//
//    theViewYouWantToCapture.setDrawingCacheEnabled(false);
}

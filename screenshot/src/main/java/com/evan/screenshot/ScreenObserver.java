package com.evan.screenshot;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by Evan on 2018/1/19.
 */

public class ScreenObserver extends ContentObserver {
    private static final String TAG = "ScreenObserver";

    private final String[] PROJECTION = {
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
    };

    private final String MEDIA_EXTERNAL_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
    private final String FILE_NAME_PREFIX = "screenshot";
    private final String PATH_SCREENSHOT = "screenshots/";
    private Handler mainHandler;

    private ContentResolver mContentResolver;


    public ScreenObserver(Handler screenHandler,Handler mainHandler, ContentResolver contentResolver) {
        super(screenHandler);
        mContentResolver = contentResolver;
        this.mainHandler = mainHandler;
    }


    @Override
    public void onChange(boolean selfChange, Uri uri) {

        Log.d(TAG, "onChange currentThread:"+ Thread.currentThread().getName());
        if (uri.toString().matches(MEDIA_EXTERNAL_URI)) {
            Cursor cursor = null;
            try {
                cursor = mContentResolver.query(uri, PROJECTION, null, null, null);
                if (cursor != null && cursor.moveToLast()) {
                    final ScreenshotData lastData = getDataFromCursor(cursor);
                    if (lastData != null) {
                        Message msg = Message.obtain();
                        msg.what = ShotScreenHelper.MSG_CONTENT_CHANGE;
                        msg.obj = lastData;
                        mainHandler.sendMessage(msg);
                    }else {
                        cursor.moveToFirst();
                        final ScreenshotData firstData = getDataFromCursor(cursor);
                        if(firstData != null){
                            Message msg = Message.obtain();
                            msg.what = ShotScreenHelper.MSG_CONTENT_CHANGE;
                            msg.obj = firstData;
                            mainHandler.sendMessage(msg);
                        }
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    public ScreenshotData getDataFromCursor(Cursor cursor){
        final String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
        final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

        if (isPathScreenshot(path) && isFileScreenshot(fileName)) {
            return new ScreenshotData(fileName, path);
        } else {
            return null;
        }

    }

    private boolean isLastScreenshot(String fileName){
        return true;
    }

    private boolean isFileScreenshot(String fileName) {
        return fileName.toLowerCase().startsWith(FILE_NAME_PREFIX);
    }

    private boolean isPathScreenshot(String path) {
        return path.toLowerCase().contains(PATH_SCREENSHOT);
    }
}

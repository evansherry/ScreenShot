package com.evan.screenshot;

import android.content.ContentResolver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;

/**
 * Created by Evan on 2018/1/19.
 */

public class ShotScreenHelper  {

    private  Handler screenHandler;
    private  HandlerThread screenThread;
    private  ShotListener mListener;
    private  ContentResolver mContentResolver;
    private  ScreenObserver mContentObserver;
    public static final int MSG_CONTENT_CHANGE = 0x01;
    private  boolean hasRegister;

    public ShotScreenHelper(ContentResolver contentResolver,ShotListener listener) {

        screenThread = new HandlerThread("ShotScreen");
        screenThread.start();
        screenHandler = new Handler(screenThread.getLooper());

        mContentResolver = contentResolver;
        mContentObserver = new ScreenObserver(screenHandler,mHandler, contentResolver);
        mListener = listener;

    }

    public void register(){
        if(!hasRegister) {
            mContentResolver.registerContentObserver(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    true,
                    mContentObserver
            );
        }
        hasRegister = true;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_CONTENT_CHANGE && mListener != null){
                ScreenshotData data = (ScreenshotData) (msg.obj);
                mListener.onShot(data);
            }
        }
    };

    public void unregister(){
        if(hasRegister) {
            mContentResolver.unregisterContentObserver(mContentObserver);
        }
        hasRegister = false;
    }

    public void onDestroy(){
        screenThread.quit();
        screenHandler.removeMessages(MSG_CONTENT_CHANGE);
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        screenHandler = null;
        mListener = null;
    }

}

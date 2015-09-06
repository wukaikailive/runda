package com.google.runda.event;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by wukai on 2015/4/23.
 */
public class LoadImageEvent {
    private ImageView imageView;
    private Bitmap bitmap;

    public LoadImageEvent(ImageView imageView){
        this.imageView=imageView;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
}

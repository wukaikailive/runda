package com.google.runda.util;
/* 
 * 贵大计算机学院安卓培训案例代码 by cxy
 */
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class LoadImageTask extends AsyncTask<Void, Void, Bitmap>{
    ImageView imageView;
    String url;
    int imgWidth;
    int imgHeight;
    public LoadImageTask(ImageView image,String url,int imgWidth,int imgHeight){
        this.imageView=image;
        this.url=url;
        this.imgWidth=imgWidth;
        this.imgHeight=imgHeight;
    }
    
    @Override
    protected Bitmap doInBackground(Void... params) {
    	Bitmap drawable=null;
        //通过url获取到图片生成drawable
    	try {
			drawable=HttpUtil.getBitmap(url,imgWidth,imgHeight);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return drawable;
    }
    
    @Override
    protected void onPostExecute(Bitmap result) {
        if(result!=null){
            imageView.setImageBitmap(result);
            //imageView.setTag(null);
        }
    }
    
}
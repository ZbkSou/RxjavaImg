package com.ihaveu.iuzuan.rxjavaimg.imageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: bkzhou
 * Date: 2018/6/28
 * Description:
 */
public class NetworkCacheObservable extends AbstractCacheObservable {
    @Override
    public void putImage(Image image) {


    }

    @Override
    public Image getImage(String url) {
        Log.d("TAG","getImage from network");
        Bitmap bitmap = downloadImage(url);
        return new Image(url, bitmap);

    }

    /**
     *  下载图片
     * @param url
     * @return
     */
    private Bitmap downloadImage(String url) {

        Bitmap bitmap = null;
        InputStream inputStream = null;

        try{
            final URLConnection con = new URL(url).openConnection();
            inputStream = con.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(inputStream !=null){
                try{
                    inputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        }

        return bitmap;
    }
}

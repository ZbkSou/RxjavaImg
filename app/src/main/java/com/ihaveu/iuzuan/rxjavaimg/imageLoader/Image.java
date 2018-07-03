package com.ihaveu.iuzuan.rxjavaimg.imageLoader;

import android.graphics.Bitmap;

/**
 * User: bkzhou
 * Date: 2018/6/27
 * Description:
 */
public class Image {
    private String url;

    public Image(String url, Bitmap bitmap) {
        this.url = url;
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

}

package com.ihaveu.iuzuan.rxjavaimg.imageLoader;

import android.graphics.Bitmap;

import android.util.Log;
import android.util.LruCache;


/**
 * User: bkzhou
 * Date: 2018/6/28
 * Description:
 */
public class MemoryCacheObservable extends AbstractCacheObservable {
    //获取到应用的最大内存
    private int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);//kb
    //设置LruCache的缓存大小
    private int cacheSize = maxMemory/8;

    private LruCache<String,Bitmap > bitmapLruCache = new LruCache<String,Bitmap>(cacheSize){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes()*value.getHeight()/1024;
        }
    };

    @Override
    public void putImage(Image image) {
        Log.d("TAG","putImage from Memory"+image.getUrl()+image.getBitmap());
        bitmapLruCache.put(image.getUrl(),image.getBitmap());

        Log.d("TAG","bitmapLruCache"+   bitmapLruCache.size());
    }

    @Override
    public Image getImage(String url) {

        Bitmap bitmap = bitmapLruCache.get(url);
        Image image  = new Image(url,bitmap);
        Log.d("TAG","getImage from Memory"+(image.getBitmap()==null));
        Log.d("TAG","bitmapLruCache"+   bitmapLruCache.size());
        return image;
    }
}

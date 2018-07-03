package com.ihaveu.iuzuan.rxjavaimg.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * User: bkzhou
 * Date: 2018/6/28
 * Description:
 */
public class DiskCacheObservable extends AbstractCacheObservable {
    private DiskLruCache mDiskLruCache;
    private Context mContext;
    //缓存20m
    private long maxSize = 20*1024*1024;

    public DiskCacheObservable(Context context){
        this.mContext = context;
        initDiskLruCache();
    }
    @Override
    public void putImage(final Image image) {
        Observable.create(new ObservableOnSubscribe<Image>() {
            @Override
            public void subscribe(ObservableEmitter<Image> e) throws Exception {
                putDataToDiskCache(image);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    /**
     * image加入缓存
     * @param image
     */
    private void putDataToDiskCache(Image image){

        try {
            String key = DiskCacheUtil.getMd5String(image.getUrl());
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if(editor!=null){
                OutputStream outputStream = editor.newOutputStream(0);
                if (saveBitmap(image.getBitmap(), outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }

            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存 bitmap
     * @param bitmap
     * @param outputStream
     * @return
     */
    private boolean saveBitmap(Bitmap bitmap, OutputStream outputStream){
        boolean b = bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public Image getImage(String url) {
        Bitmap bitmap = getDataFromDiskCache(url);
        Image image = new Image(url,bitmap);

        Log.d("TAG","getImage from Disk"+(image.getBitmap()==null));
        return image;
    }


    //从缓存中取出 bitmap
    private Bitmap getDataFromDiskCache(String url){
        FileDescriptor fileDescriptor = null;
        FileInputStream fileInputStream = null;
        DiskLruCache.Snapshot snapshot =null;
        try{
            String key = DiskCacheUtil.getMd5String(url);
            Log.d("TAG","Disk Cache key"+key);

            snapshot = mDiskLruCache.get(key);
            if(snapshot!=null){
                fileInputStream = (FileInputStream) snapshot.getInputStream(0);
                fileDescriptor = fileInputStream.getFD();
            }
            Bitmap bitmap = null;
            if(fileDescriptor!=null){
                bitmap  = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }
            return bitmap;

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fileDescriptor== null && fileInputStream!=null){
                try{
                    fileInputStream.close();
                }catch (IOException e){

                }
            }
        }
        return null;
    }

    //实例化 cache
    private void  initDiskLruCache(){
        try{
            File cacheDir = DiskCacheUtil.getDiskCacheDir(this.mContext,"image_cache");
            if(!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            int versionCode = DiskCacheUtil.getAppVersionCode(mContext);
            mDiskLruCache = DiskLruCache.open(cacheDir,versionCode,1,maxSize);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

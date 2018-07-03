package com.ihaveu.iuzuan.rxjavaimg.imageLoader;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * User: bkzhou
 * Date: 2018/6/28
 * Description:
 */
public class RequestCreator {
    private MemoryCacheObservable memoryCacheObservable;
    private DiskCacheObservable diskCacheObservable;
    private NetworkCacheObservable networkCacheObservable;

    public RequestCreator(Context context) {
        memoryCacheObservable = new MemoryCacheObservable();
        diskCacheObservable = new DiskCacheObservable(context);
        networkCacheObservable = new  NetworkCacheObservable();
    }
    public Observable<Image> getImageFromMemory(String url){
        return memoryCacheObservable.requestImage(url);
    }
    public Observable<Image> getImageFromDisk(String url){
        return diskCacheObservable.requestImage(url)
            .filter(new Predicate<Image>() {
                @Override
                public boolean test(Image image) throws Exception {
                    return image.getBitmap()!=null;
                }
            })
            .doOnNext(new Consumer<Image>() {
                @Override
                public void accept(Image image) throws Exception {
                    memoryCacheObservable.putImage(image);
                }
            });
    }
    public Observable<Image> getImageFromNetwork(String url){
        return networkCacheObservable.requestImage(url)
            .filter(new Predicate<Image>() {
                @Override
                public boolean test(Image image) throws Exception {
                    return image.getBitmap()!=null;
                }
            })
            .doOnNext(new Consumer<Image>() {
            @Override
            public void accept(Image image) throws Exception {
                diskCacheObservable.putImage(image);
                memoryCacheObservable.putImage(image);
            }
        });
    }


}

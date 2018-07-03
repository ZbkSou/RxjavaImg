package com.ihaveu.iuzuan.rxjavaimg.imageLoader;

import android.graphics.Bitmap;
import android.util.AndroidRuntimeException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * User: bkzhou
 * Date: 2018/6/28
 * Description:缓存的抽象类
 */
public abstract class AbstractCacheObservable {

    public Observable<Image> requestImage(final String url){
        return Observable.create(new ObservableOnSubscribe<Image>() {
            @Override
            public void subscribe(ObservableEmitter<Image> e) throws Exception {

                e.onNext(getImage(url));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 保存图片
     * @param image
     */
    public abstract void putImage(Image image);


    /**
     * 具体获取 image方法
     * @param url
     * @return
     */
    public abstract Image getImage(String url);
}

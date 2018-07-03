package com.ihaveu.iuzuan.rxjavaimg.imageLoader;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import io.reactivex.Observable;
import io.reactivex.Observer;




import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.operators.observable.ObservableJust;

/**
 * User: bkzhou
 * Date: 2018/6/27
 * Description:
 */
public class RxImageLoader {

    public Context context;
    public String mUrl;
    public RequestCreator requestCreator;
    static RxImageLoader singletoon;
    private RxImageLoader(Builder builder ){
        this.context = builder.context;
        requestCreator= new RequestCreator(context);
    }

    public static RxImageLoader with(Context context){
        if(singletoon==null){
            synchronized (RxImageLoader.class){
                if(singletoon==null){
                    singletoon = new Builder(context).build();
                }
            }
        }
        return singletoon;
    }

    public RxImageLoader load(String url){
        mUrl = url;

        return singletoon;
    }
    public RxImageLoader into(final ImageView imageView){

        Observable.concat(requestCreator.getImageFromMemory(mUrl),requestCreator.getImageFromDisk(mUrl),requestCreator.getImageFromNetwork(mUrl))
            .filter(new Predicate<Image>() {
                @Override
                public boolean test(Image image) throws Exception {
//
                    Log.d("TAG","filter");

                    if(image.getBitmap()==null){
                        return false;
                    }
                    return true;
                }
            })
        .firstElement().toObservable()
            .subscribe(new Observer<Image>() {
                @Override
                public void onSubscribe(Disposable d) {
                }
                @Override
                public void onNext(Image image) {
                    Log.d("TAG",image.getUrl());
                    imageView.setImageBitmap(image.getBitmap());
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }
                @Override
                public void onComplete() {
                    Log.d("TAG","onComplete");
                }
            });


        return singletoon;
    }

    public static class  Builder{
        public Context context;
        public Builder(Context context){
            this.context = context;
        }
        public RxImageLoader build(){
            return new RxImageLoader(this);
        }

    }

}

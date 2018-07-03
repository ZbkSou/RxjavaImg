package com.ihaveu.iuzuan.rxjavaimg;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ihaveu.iuzuan.rxjavaimg.imageLoader.RxImageLoader;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button2);
        context = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxImageLoader.with(context).load("http://mmbiz.qpic.cn/mmbiz_png/via3iaqIEsXjVPJs0yFic6tBobapYt55RMYYfP153xMQOKibTuRY7Tg2IdluCeyVoyEVA3k2d84DsolPjNwYyaum2A/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1").into(imageView);
            }
        });

        //创建被观察者
//
//        Observable<String> myObservable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext("1");
//                e.onNext("2");
//                e.onComplete();
//            }
//        });
//
////创建观察者
//
//        Observer<String> mySubscriber = new Observer<String>() {
//
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.d("TAG",s);
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d("TAG","onComplete");
//            }
//        };
//
////完成订阅
//
//        myObservable.subscribe(mySubscriber);
    }
}

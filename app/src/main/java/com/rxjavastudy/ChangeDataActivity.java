package com.rxjavastudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class ChangeDataActivity extends AppCompatActivity {

    private static final String TAG = "ChangeDataActivity";

    private class TestChangeDataSubscriber extends Subscriber<Object> {

        @Override
        public void onStart() {
            Log.e(TAG, "onStart," + Thread.currentThread().getName());
        }

        @Override
        public void onCompleted() {
            Log.e(TAG, "onCompleted," + Thread.currentThread().getName());
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError", e);
        }

        @Override
        public void onNext(Object o) {
            Log.e(TAG, "onNext:" + o + "," + Thread.currentThread().getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);
//        testMap();
//        testFlatMap();
//        testFlatMap2();
        testConcatMap();
    }


    private void testMap() {
        Observable.just("one", "two", "three", "four", "five")
                .map(new Func1<String, String>() {

                    @Override
                    public String call(String s) {
                        return s.toUpperCase();
                    }
                }).subscribe(new TestChangeDataSubscriber());
    }

    private void testFlatMap() {
        Observable.just("o,n,e", "t,w,o", "t,h,r,e,e", "f,o,u,r", "f,i,v,e")
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return Observable.from(s.split(","));
                    }
                }).subscribe(new TestChangeDataSubscriber());
    }

    private void testFlatMap2() {
        Observable.just(100, 150)
                .flatMap(new Func1<Integer, Observable<?>>() {
                    @Override
                    public Observable<?> call(final Integer integer) {
                        return Observable.interval(integer, TimeUnit.MILLISECONDS)
                                .map(new Func1<Long, Object>() {
                                    @Override
                                    public Object call(Long aLong) {
                                        return integer;
                                    }
                                }).take(5);
                    }
                }).subscribe(new TestChangeDataSubscriber());
    }

    private void testConcatMap() {
        Observable.just(100, 150)
                .concatMap(new Func1<Integer, Observable<?>>() {
                    @Override
                    public Observable<?> call(final Integer integer) {
                        return Observable.interval(integer, TimeUnit.MILLISECONDS)
                                .map(new Func1<Long, Object>() {
                                    @Override
                                    public Object call(Long aLong) {
                                        return integer;
                                    }
                                }).take(5);
                    }
                }).subscribe(new TestChangeDataSubscriber());
    }

}

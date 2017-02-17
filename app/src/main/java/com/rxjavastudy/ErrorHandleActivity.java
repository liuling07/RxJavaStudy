package com.rxjavastudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class ErrorHandleActivity extends AppCompatActivity {

    private static final String TAG = "ErrorHandleActivity";

    private class TestErrorHandleSubscriber extends Subscriber<Object> {

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
        setContentView(R.layout.activity_error_handle);
//        testErrorReturn();
//        testOnErrorResumeNext();
        testRetry();
    }

    private void testErrorReturn() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("One");
                subscriber.onNext("Two");
                subscriber.onError(new Exception("Error"));
            }
        }).onErrorReturn(new Func1<Throwable, String>() {
            @Override
            public String call(Throwable throwable) {
                return "Three";
            }
        }).subscribe(new TestErrorHandleSubscriber());
    }

    private void testOnErrorResumeNext() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("One");
                subscriber.onNext("Two");
                subscriber.onError(new Exception("Error"));
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {
            @Override
            public Observable<? extends String> call(Throwable throwable) {
                return Observable.just("Three", "Four");
            }
        }).subscribe(new TestErrorHandleSubscriber());
    }
    
    /*private void testOnErrorResumeNext1() {
        final Observable getInfoObservable = ApiService.getInfoObservable();
        getInfoObservable.onErrorResumeNext(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                // 如果token过期，需要重新请求token
                if(throwable是token过期错误) {
                    getRefreshTokenObservable().flatMap(new Func1<String, Observable<?>>() {
                        @Override
                        public Observable<?> call(String s) {
                            // 这里刷新完token后使用flatMap转换为原来的getInfoObservable，以便继续获取信息的请求
                            return getInfoObservable;
                        }
                    });
                }
                return Observable.error(throwable);
            }
        }).subscribe(observer);
    }

    public Observable<String> getRefreshTokenObservable() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        observer.onNext(ApiService.refreshToken());
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }*/

    private void testRetry() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                int i = new Random().nextInt();
                if (i % 3 == 0) {
                    subscriber.onNext(i);
                } else {
                    subscriber.onError(new Exception("Error"));
                }
            }
        }).retry(1).subscribe(new TestErrorHandleSubscriber());
    }
}

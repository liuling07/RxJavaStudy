package com.rxjavastudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class FilterDataActivity extends AppCompatActivity {

    private static final String TAG = "FilterDataActivity";

    private class TestFilterDataSubscriber extends Subscriber<Object> {

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
        setContentView(R.layout.activity_filter_data);
//        testFilter();
//        testFirst();
//        testIgnoreElement();
//        testLast();
//        testTake();
//        testTakeLast();
//        testTakeLastBuffer();
//        testSkip();
//        testTakeWhile();
//        testSkipWhile();
//        testTakeUntil();
//        testSkipUntil();
//        testDistinct();
        testDistinctUntilChanged();
    }

    private void testFilter() {
        Observable.range(1, 10)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer % 2 == 0;
                    }
                }).subscribe(new TestFilterDataSubscriber());
    }

    private void testFirst() {
        // 返回1
//        Observable.range(1, 10)
//                .first()
//                .subscribe(new TestFilterDataSubscriber());
        // 如果Observable不发射任何数据，使用first会抛出NoSuchElementException异常
//        Observable.empty()
//                .first()
//                .subscribe(new TestFilterDataSubscriber());
        // 使用firstOrDefault传入一个默认值，如果Observable不发射任何数据则使用默认值
//        Observable.empty()
//                .firstOrDefault(1)
//                .subscribe(new TestFilterDataSubscriber());

        Observable.range(1, 10)
                .first(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer % 2 == 0;
                    }
                })
                .subscribe(new TestFilterDataSubscriber());
    }

    private void testIgnoreElement() {
        Observable.range(1, 10)
                .ignoreElements()
                .subscribe(new TestFilterDataSubscriber());
        //上面和下面等价
        Observable.range(1, 10)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return false;
                    }
                })
                .subscribe(new TestFilterDataSubscriber());
    }

    private void testLast() {
        Observable.range(1, 10)
                .last()
                .subscribe(new TestFilterDataSubscriber());
    }

    private void testTake() {
        /*Observable.range(1, 10)
                .take(5)
                .subscribe(new TestFilterDataSubscriber());*/
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .take(5000, TimeUnit.MILLISECONDS)
                .subscribe(new TestFilterDataSubscriber());

    }

    private void testSkip() {
        Observable.range(1, 10)
                .skip(5)
                .subscribe(new TestFilterDataSubscriber());
    }

    private void testTakeFirst() {
        Observable.range(1, 10)
                .takeFirst(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer % 2 == 0;
                    }
                })
                .subscribe(new TestFilterDataSubscriber());
    }

    private void testTakeLast() {
        Observable.range(1, 10)
                .takeLast(5)
                .subscribe(new TestFilterDataSubscriber());
    }

    private void testTakeLastBuffer() {
        Observable.range(1, 10)
                .takeLastBuffer(5)
                .subscribe(new TestFilterDataSubscriber());
    }

    private void testTakeWhile() {
        Observable.range(1, 10)
                .takeWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 5;
                    }
                }).subscribe(new TestFilterDataSubscriber());
    }

    private void testSkipWhile() {
        Observable.range(1, 10)
                .skipWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 5;
                    }
                }).subscribe(new TestFilterDataSubscriber());
    }

    private void testTakeUntil() {
        Observable.just(3, 6, 7, 8, 9)
                .takeUntil(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 5;
                    }
                }).subscribe(new TestFilterDataSubscriber());
    }

    private void testSkipUntil() {
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .skipUntil(Observable.timer(5000, TimeUnit.MILLISECONDS))
                .subscribe(new TestFilterDataSubscriber());
    }

    private void testDistinct() {
//        Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                subscriber.onNext(1);
//                subscriber.onNext(1);
//                subscriber.onNext(2);
//                subscriber.onNext(3);
//                subscriber.onNext(2);
//                subscriber.onCompleted();
//            }
//        }).distinct().subscribe(new TestFilterDataSubscriber());

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("First");
                subscriber.onNext("Second");
                subscriber.onNext("Third");
                subscriber.onNext("Fourth");
                subscriber.onNext("Fifth");
                subscriber.onCompleted();
            }
        }).distinct(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                return s.charAt(0);
            }
        }).subscribe(new TestFilterDataSubscriber());
    }

    private void testDistinctUntilChanged() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onNext(2);
                subscriber.onCompleted();
            }
        }).distinctUntilChanged().subscribe(new TestFilterDataSubscriber());
    }

}

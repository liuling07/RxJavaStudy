package com.rxjavastudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class CheckDataActivity extends AppCompatActivity {

    private static final String TAG = "CheckDataActivity";

    private class TestCheckDataSubscriber extends Subscriber<Object> {

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
        setContentView(R.layout.activity_check_data);
//        testAll();
//        testExists();
        testIsEmpty();
    }

    private void testAll() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(2);
                subscriber.onNext(4);
                subscriber.onNext(6);
                subscriber.onNext(8);
                subscriber.onCompleted();
            }
        }).all(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer % 2 == 0;
            }
        }).subscribe(new TestCheckDataSubscriber());
    }

    private void testExists() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(1);
                subscriber.onNext(3);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        }).exists(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer % 2 == 0;
            }
        }).subscribe(new TestCheckDataSubscriber());
    }


    private void testContains() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(1);
                subscriber.onNext(3);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        }).contains(0).subscribe(new TestCheckDataSubscriber());
    }

    private void testIsEmpty() {
        // true
        Observable.empty().isEmpty()
                .subscribe(new TestCheckDataSubscriber());
        // false
        Observable.just(1)
                .subscribe(new TestCheckDataSubscriber());
    }

    private void testDefaultIfEmpty() {
        Observable.empty().defaultIfEmpty(2)
                .subscribe(new TestCheckDataSubscriber());
    }

}

package com.rxjavastudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class CombineObservableActivity extends AppCompatActivity {

    private static final String TAG = "CombineObservable";

    private class TestCombineDataSubscriber extends Subscriber<Object> {

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
        setContentView(R.layout.activity_combine_observable);
//        testConcat();
//        testRepeat();
        testRepeatWhen();
    }

    private void testConcat() {
        Observable observable1 = Observable.range(1, 5);
        Observable observable2 = Observable.range(11, 5);
        Observable.concat(observable1, observable2)
                .subscribe(new TestCombineDataSubscriber());
    }

    private void testRepeat() {
        Observable.range(1, 2)
                .repeat(2)
                .subscribe(new TestCombineDataSubscriber());
    }

    private void testRepeatWhen() {
        Observable.range(1, 2)
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return Observable.range(1, 2);
                    }
                })
                .subscribe(new TestCombineDataSubscriber());
    }
}

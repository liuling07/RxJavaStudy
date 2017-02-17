package com.rxjavastudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

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
//        testRepeatWhen();
//        testStartWith();
//        testAmb();
//        testMerge();
//        testMergeDelayError();
//        testZip();
        testCombineLatest();
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
                        return Observable.timer(200, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribe(new TestCombineDataSubscriber());
    }

    private void testStartWith() {
        Observable.range(1, 2)
                .startWith(0)
                .subscribe(new TestCombineDataSubscriber());
    }

    private void testAmb() {
        Observable observable = Observable.timer(300, TimeUnit.MILLISECONDS)
                .map(new Func1() {
                    @Override
                    public String call(Object o) {
                        return "Java";
                    }
                });
        Observable observable1 = Observable.timer(100, TimeUnit.MILLISECONDS)
                .map(new Func1() {
                    @Override
                    public String call(Object o) {
                        return "C++";
                    }
                });
        Observable observable2 = Observable.timer(200, TimeUnit.MILLISECONDS)
                .map(new Func1() {
                    @Override
                    public String call(Object o) {
                        return "Python";
                    }
                });
        Observable.amb(observable, observable1, observable2)
                .subscribe(new TestCombineDataSubscriber());
//        observable.ambWith(observable1).ambWith(observable2)
//                .subscribe(new TestCombineDataSubscriber());
    }

    private void testMerge() {
        Observable observable = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(new Func1() {
                    @Override
                    public String call(Object o) {
                        return "Java";
                    }
                });
        Observable observable1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .map(new Func1() {
                    @Override
                    public String call(Object o) {
                        return "C++";
                    }
                });
        Observable.merge(observable, observable1).take(10)
                .subscribe(new TestCombineDataSubscriber());
    }

    private void testMergeDelayError() {
        Observable observable = Observable.concat(
                Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2), Observable.error(new Exception("")));
        Observable observable1 = Observable.interval(200, TimeUnit.MILLISECONDS)
                .take(5);
        Observable.mergeDelayError(observable, observable1)
                .subscribe(new TestCombineDataSubscriber());
    }

    private void testZip() {
        Observable observable = Observable.interval(200, TimeUnit.MILLISECONDS).take(5);
        Observable observable1 = Observable.interval(100, TimeUnit.MILLISECONDS).take(3);
        Observable.zip(observable, observable1, new Func2() {
            @Override
            public Object call(Object o, Object o2) {
                return o + "-" + o2;
            }
        }).subscribe(new TestCombineDataSubscriber());
    }

    private void testCombineLatest() {
        Observable observable = Observable.interval(100, TimeUnit.MILLISECONDS).take(5);
        Observable observable1 = Observable.interval(200, TimeUnit.MILLISECONDS).take(3);
        Observable.combineLatest(observable, observable1, new Func2() {
            @Override
            public Object call(Object o, Object o2) {
                return o + "-" + o2;
            }
        }).subscribe(new TestCombineDataSubscriber());
    }
}

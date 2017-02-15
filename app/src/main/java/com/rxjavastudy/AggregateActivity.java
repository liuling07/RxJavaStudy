package com.rxjavastudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;

public class AggregateActivity extends AppCompatActivity {

    private static final String TAG = "AggregateActivity";

    private class TestAggregateDataSubscriber extends Subscriber<Object> {

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
        setContentView(R.layout.activity_aggregate);
//        testReduce();
//        testScan();
//        testToList();
//        testToSortedList();
//        testToMap();
//        testMultiMap();
        testGroupBy();
    }

    private void testReduce() {
        Observable.range(1, 10)
                .reduce(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer + integer2;
                    }
                }).subscribe(new TestAggregateDataSubscriber());
    }

    private void testScan() {
        Observable.range(1, 10)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer + integer2;
                    }
                }).subscribe(new TestAggregateDataSubscriber());
    }

    private void testToList() {
        Observable.range(1, 5)
                .toList().subscribe(new TestAggregateDataSubscriber());
    }

    private void testToSortedList() {
        Observable.range(1, 5)
                .toSortedList(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer2 - integer;
                    }
                })
                .subscribe(new TestAggregateDataSubscriber());
    }

    private void testToMap() {
        Observable.just("one", "two", "three")
                .toMap(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s.substring(0, 1);
                    }
                }).subscribe(new TestAggregateDataSubscriber());
    }

    private void testMultiMap() {
        Observable.just("one", "two", "three", "four", "five")
                .toMultimap(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s.substring(0, 1);
                    }
                }).subscribe(new TestAggregateDataSubscriber());
    }

    private void testGroupBy() {
        Observable.just("one", "two", "three", "four", "five")
                .groupBy(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s.substring(0, 1);
                    }
                }).subscribe(new Action1<GroupedObservable<String, String>>() {
            @Override
            public void call(GroupedObservable<String, String> stringStringGroupedObservable) {
                stringStringGroupedObservable.last().subscribe(new TestAggregateDataSubscriber());
            }
        });
    }

}

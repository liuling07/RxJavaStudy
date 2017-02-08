package com.rxjavastudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;

public class CreateDataActivity extends AppCompatActivity {

    private static final String TAG = "CreateDataActivity";

    private class TestCreateDataSubscriber extends Subscriber<Object> {

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
        setContentView(R.layout.activity_create_data);
//        testJust();
//        testFrom();
//        testTimer();
//        testInterval();
//        testRange();
//        testDefer();
//        testCreate();
//        testEmpty();
//        testError();
        testNever();
    }

    private void testJust() {
        Observable<String> observable = Observable.just("one", "two", "three", "four");
        observable.subscribe(new TestCreateDataSubscriber());
    }

    private void testFrom() {
        // 直接接收数组参数
        String[] datas = {"one", "two", "three", "four"};
//        Observable<String> observable = Observable.from(datas);

        // 接收List集合参数
//        Observable<String> observable = Observable.from(Arrays.asList(datas));

        //接收Future作为参数
        /**
         * 已Future作为参数创建Observable时，在观察者接收数据时依赖Future返回的结果，内部会调用Future.get()，而get()方法是会阻塞的
         * 如果Future.call()执行比较耗时（或者Future没有启动）的话，Future.get()方法会阻塞，此时如果观察者在主线程中执行的话会卡主界面。
         * 所以使用from(Future<? extends T> future)时应注意。
         */
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                return "hello rxjava";
            }
        });
        new Thread(futureTask).start();
        Observable<String> observable = Observable.from(futureTask);
        /**
         * 也可以设置超时时间，from(Future<? extends T> future, long timeout, TimeUnit unit)，
         * 如果超时了还没有获取到结果则会抛出TimeoutException异常，调用onError。
         */
//        Observable<String> observable = Observable.from(futureTask, 2000, TimeUnit.MILLISECONDS);
        /**
         * 也可以指定等待Future返回结果的线程，from(Future<? extends T> future, Scheduler scheduler)
         * 效果和observeOn(Schedulers.computation())一样
         */
//        Observable<String> observable = Observable.from(futureTask, Schedulers.computation());

        observable.subscribe(new TestCreateDataSubscriber());
    }

    private void testTimer() {
        // 1秒钟后发射数据0，然后结束。
        Observable observable = Observable.timer(1000, TimeUnit.MILLISECONDS);
        // 1秒钟后开始周期性发射数据（从0开始依次递增），间隔1秒。这种用法被被废弃了，被interval替代了。
//        Observable observable = Observable.timer(1000, 1000, TimeUnit.MILLISECONDS);
        observable.subscribe(new TestCreateDataSubscriber());
    }

    private void testInterval() {
        // 以1秒为周期定时发送数据
        // Observable observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        // 延时5秒钟后再以1秒为周期定时发送数据
        Observable observable = Observable.interval(5000, 1000, TimeUnit.MILLISECONDS);
        final Subscription subscription = observable.subscribe(new TestCreateDataSubscriber());
        // 10秒钟之后，取消订阅，则Observable停止发射数据
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                subscription.unsubscribe();
            }
        }, 10000);
    }


    private void testRange() {
        // 从5开始，发射5个数据
        Observable observable = Observable.range(5, 5);
        observable.subscribe(new TestCreateDataSubscriber());
    }

    private void testDefer() {
        Observable observable = Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable call() {
                return Observable.just(System.currentTimeMillis());
            }
        });
        observable.subscribe(new TestCreateDataSubscriber());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        observable.subscribe(new TestCreateDataSubscriber());
    }

    private void testCreate() {
        //使用create实现Observable.just("one", "two", "three", "four")一样的功能
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("one");
                subscriber.onNext("two");
                subscriber.onNext("three");
                subscriber.onNext("four");
//                subscriber.onCompleted();
            }
        });
        observable.subscribe(new TestCreateDataSubscriber());
    }

    private void testEmpty() {
        Observable observable = Observable.empty();
        observable.subscribe(new TestCreateDataSubscriber());
    }

    private void testError() {
        Observable observable = Observable.error(new NullPointerException());
        observable.subscribe(new TestCreateDataSubscriber());
    }

    private void testNever() {
        Observable observable = Observable.never();
        observable.subscribe(new TestCreateDataSubscriber());
    }

}

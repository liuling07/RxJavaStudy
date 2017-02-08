package com.rxjavastudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RxJavaBasisActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaBasisActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_basis);

        // 创建被观察者Observable
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello RxJava!");
                subscriber.onCompleted();
            }
        });

        // 创建观察者Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError", e);
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "onNext," + s);
            }
        };

        // 订阅
//        observable.subscribe(observer);


        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, "onNext," + s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                Log.e(TAG, "onError", e);
            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.e(TAG, "onCompleted");
            }
        };

        // 只订阅onNext事件
//        observable.subscribe(onNextAction);

        // 只订阅onNext和onError事件
//        observable.subscribe(onNextAction, onErrorAction);

        // 订阅onNext、onError和onCompleted事件
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);

    }

    public class User {
        String sex;
        int age;
    }

    private Observable<List<User>> getAllUser() {
        return Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                List<User> userList = null;
                // 从数据库中查询所有的用户
                subscriber.onNext(userList);
                subscriber.onCompleted();
            }
        });
    }

    // 查找所有用户
    private void testGetAllUser() {
        getAllUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onStart() {
                        // 显示Loading
                    }

                    @Override
                    public void onCompleted() {
                        // 关闭Loading
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 关闭Loading，并提示出错
                    }

                    @Override
                    public void onNext(List<User> users) {
                        // 显示用户信息
                    }
                });
    }

    // 查找所有男性用户
    private void testGetAllManUser() {
        getAllUser().flatMap(new Func1<List<User>, Observable<User>>() {
                    @Override
                    public Observable<User> call(List<User> users) {
                        return Observable.from(users);
                    }
                }).filter(new Func1<User, Boolean>() {
                    @Override
                    public Boolean call(User user) {
                        return "男".equals(user.sex);
                    }
                }).toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onStart() {
                        // 显示Loading
                    }

                    @Override
                    public void onCompleted() {
                        // 关闭Loading
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 关闭Loading，并提示出错
                    }

                    @Override
                    public void onNext(List<User> users) {
                        // 显示用户信息
                    }
                });
    }

    // 查找年纪最大的10个男性用户
    private void testGetTop10OldestManUser() {
        getAllUser().flatMap(new Func1<List<User>, Observable<User>>() {
            @Override
            public Observable<User> call(List<User> users) {
                return Observable.from(users);
            }
        }).filter(new Func1<User, Boolean>() {
            @Override
            public Boolean call(User user) {
                return "男".equals(user.sex);
            }
        }).toSortedList(new Func2<User, User, Integer>() {
            @Override
            public Integer call(User user, User user2) {
                return user2.age - user.age;
            }
        }).take(10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onStart() {
                        // 显示Loading
                    }

                    @Override
                    public void onCompleted() {
                        // 关闭Loading
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 关闭Loading，并提示出错
                    }

                    @Override
                    public void onNext(List<User> users) {
                        // 显示用户信息
                    }
                });
    }


}

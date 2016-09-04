package com.alenbeyond.sujin.rx;

import com.alenbeyond.sujin.bean.SuJinDes;
import com.alenbeyond.sujin.bean.SuJinHome;
import com.alenbeyond.sujin.crawler.SuJin;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AlenBeyond on 2016/5/24.
 */
public class ApiManager {

    public static void getObSujinHome(final int page, Observer<List<SuJinHome>> subscribe) {
        Observable.fromCallable(new Callable<List<SuJinHome>>() {
            @Override
            public List<SuJinHome> call() throws Exception {
                return SuJin.getSuJinHome(page);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscribe);
    }

    public static void getObSujinDes(final String url, Observer<SuJinDes> subscribe) {
        Observable.fromCallable(new Callable<SuJinDes>() {
            @Override
            public SuJinDes call() throws Exception {
                return SuJin.getSujinDes(url);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscribe);
    }

}

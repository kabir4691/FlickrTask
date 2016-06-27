package com.kabir.flickrtask.main.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kabir.flickrtask.main.api.FlickrApi;
import com.kabir.flickrtask.main.model.FlickrItem;
import com.kabir.flickrtask.main.view.FlickrView;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Kabir on 27-06-2016.
 */
public class FlickrPresenterImpl implements FlickrPresenter {

    private FlickrView flickrView;

    private FlickrApi flickrApi;
    private CompositeSubscription compositeSubscription;

    public FlickrPresenterImpl(@NonNull FlickrView flickrView, @NonNull FlickrApi flickrApi) {
        this.flickrView = flickrView;
        this.flickrApi = flickrApi;
    }

    @Override
    public boolean attachView(@Nullable Bundle extras) {

        compositeSubscription = new CompositeSubscription();
        return true;
    }

    @Override
    public void detachView() {

        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        flickrApi.cancelGetImagesCalls();
    }

    @Override
    public void loadData() {

        Observable<List<FlickrItem>> observable = flickrApi.getImages();
        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<FlickrItem>>() {
                    @Override
                    public void call(List<FlickrItem> flickrItems) {
                        if (!flickrView.isViewDestroyed()) {
                            flickrView.setContent(flickrItems);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        compositeSubscription.add(subscription);
    }
}

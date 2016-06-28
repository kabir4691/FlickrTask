package com.kabir.flickrtask.main.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kabir.flickrtask.main.api.FlickrApi;
import com.kabir.flickrtask.main.model.FlickrItem;
import com.kabir.flickrtask.main.view.FlickrView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        flickrView.showLoading(false);
        flickrView.showContent(false);
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
    public void loadData(@NonNull final String tags, int count) {

        if (tags.isEmpty()) {
            flickrView.showToast("Enter tags as comma seperated values (no spaces)");
            return;
        }

        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(tags);
        if (matcher.find()) {
            flickrView.showToast("No spaces allowed");
            return;
        }

        if (count <= 0) {
            flickrView.showToast("Count must be greater than 0");
            return;
        }

        flickrView.setContent(null);
        flickrView.showContent(false);
        flickrView.showLoading(true);

        Observable<List<FlickrItem>> observable = flickrApi.getImages(tags, count);
        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<FlickrItem>>() {
                    @Override
                    public void call(List<FlickrItem> flickrItems) {
                        if (!flickrView.isViewDestroyed()) {

                            flickrView.showLoading(false);
                            flickrView.setContent(flickrItems);
                            flickrView.showContent(true);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        throwable.printStackTrace();
                        if (!flickrView.isViewDestroyed()) {
                            flickrView.showLoading(false);
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
}

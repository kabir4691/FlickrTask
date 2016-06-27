package com.kabir.flickrtask.main.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Kabir on 27-06-2016.
 */
public interface FlickrPresenter {

    boolean attachView(@Nullable Bundle extras);
    void detachView();

    void loadData();
}

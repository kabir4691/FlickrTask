package com.kabir.flickrtask.main.view;

import android.support.annotation.Nullable;

import com.kabir.flickrtask.main.model.FlickrItem;

import java.util.List;

/**
 * Created by Kabir on 27-06-2016.
 */
public interface FlickrView {

    boolean isViewDestroyed();

    void setContent(@Nullable List<FlickrItem> flickrItems);
}

package com.kabir.flickrtask.main.api;

import com.kabir.flickrtask.main.model.FlickrItem;

import java.util.List;

import rx.Observable;

/**
 * Created by Kabir on 27-06-2016.
 */
public interface FlickrApi {

    String GET_IMAGES_API_TAG = "get_images_api_tag";

    Observable<List<FlickrItem>> getImages();

    void cancelGetImagesCalls();
}
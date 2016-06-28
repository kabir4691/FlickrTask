package com.kabir.flickrtask.main.api;

import android.support.annotation.NonNull;

import com.kabir.flickrtask.main.model.FlickrItem;

import java.util.List;

import rx.Observable;

/**
 * Created by Kabir on 27-06-2016.
 */
public interface FlickrApi {

    String BASE_URL = "https://api.flickr.com/services/rest/";
    String METHOD = "flickr.photos.search";
    String API_KEY = "32aaa9f2cbc1b2e897e1b1d9276506f5";
    String FORMAT = "json";
    String NO_JSON_CALLBACK = "true";

    String GET_IMAGES_API_TAG = "get_images_api_tag";

    Observable<List<FlickrItem>> getImages(@NonNull String tags, int count);

    void cancelGetImagesCalls();
}
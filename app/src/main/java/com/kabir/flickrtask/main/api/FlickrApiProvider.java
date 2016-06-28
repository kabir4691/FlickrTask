package com.kabir.flickrtask.main.api;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.kabir.flickrtask.main.model.FlickrItem;
import com.kabir.flickrtask.utils.api.ApiParameter;
import com.kabir.flickrtask.utils.api.provider.ApiProvider;
import com.kabir.flickrtask.utils.api.provider.VolleyApiProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Kabir on 27-06-2016.
 */
public class FlickrApiProvider implements FlickrApi {

    private ApiProvider apiProvider;

    public FlickrApiProvider() {
        apiProvider = new VolleyApiProvider();
    }

    @Override
    public Observable<List<FlickrItem>> getImages(@NonNull String tags, int count) {

        StringBuilder builder = new StringBuilder();
        builder.append(BASE_URL)
               .append("?")
               .append(ApiParameter.METHOD).append("=").append(FlickrApi.METHOD)
               .append("&")
               .append(ApiParameter.API_KEY).append("=").append(FlickrApi.API_KEY)
               .append("&")
               .append(ApiParameter.TAGS).append("=").append(tags)
               .append("&")
               .append(ApiParameter.FORMAT).append("=").append(FlickrApi.FORMAT)
               .append("&")
               .append(ApiParameter.NO_JSON_CALLBACK).append("=").append(FlickrApi.NO_JSON_CALLBACK)
               .append("&")
               .append(ApiParameter.PER_PAGE).append("=").append(String.valueOf(count));

        return apiProvider.callApi(Request.Method.GET, builder.toString(), null, GET_IMAGES_API_TAG)
                          .subscribeOn(Schedulers.io())
                          .observeOn(AndroidSchedulers.mainThread())
                          .map(new Func1<String, JSONArray>() {
                              @Override
                              public JSONArray call(String response) {

                                  try {

                                      JSONObject jsonObject = new JSONObject(response);
                                      JSONObject photosObject = jsonObject.getJSONObject(ApiParameter.PHOTOS);
                                      JSONArray photoArray = photosObject.getJSONArray(ApiParameter.PHOTO);
                                      return photoArray;
                                  } catch (JSONException e) {
                                      e.printStackTrace();
                                      return null;
                                  }
                              }
                          })
                          .flatMap(new Func1<JSONArray, Observable<List<FlickrItem>>>() {
                              @Override
                              public Observable<List<FlickrItem>> call(JSONArray jsonArray) {

                                  if (jsonArray == null || jsonArray.length() == 0) {
                                      return Observable.just(null);
                                  }

                                  List<FlickrItem> list = new ArrayList<>();
                                  for (int i = 0; i < jsonArray.length(); i++) {

                                      try {

                                          JSONObject jsonObject = jsonArray.getJSONObject(i);
                                          FlickrItem commitItem = FlickrItem.parseJSON(jsonObject);
                                          list.add(commitItem);
                                      } catch (JSONException e) {
                                          e.printStackTrace();
                                      }
                                  }

                                  return Observable.just(list);
                              }
                          });
    }

    @Override
    public void cancelGetImagesCalls() {
        apiProvider.cancelApiCalls(GET_IMAGES_API_TAG);
    }
}

package com.kabir.flickrtask.main.model;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.kabir.flickrtask.utils.RandomColorGenerator;
import com.kabir.flickrtask.utils.api.ApiParameter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kabir on 27-06-2016.
 */
public class FlickrItem {

    private String id;
    private String secret;
    private String server;
    private String farm;
    private String title;
    private String imageUrl;
    @ColorRes private int imagePlaceholder;
    private boolean isFlipped;

    private FlickrItem() {
    }

    public static FlickrItem parseJSON(JSONObject jsonObject) throws JSONException {

        FlickrItem flickrItem = new FlickrItem();

        flickrItem.id = jsonObject.getString(ApiParameter.ID);
        flickrItem.secret = jsonObject.getString(ApiParameter.SECRET);
        flickrItem.server = jsonObject.getString(ApiParameter.SERVER);
        flickrItem.farm = jsonObject.getString(ApiParameter.FARM);
        flickrItem.title = jsonObject.getString(ApiParameter.TITLE);
        flickrItem.imageUrl = buildImageUrl(flickrItem);
        flickrItem.imagePlaceholder = RandomColorGenerator.getRandomColor();
        flickrItem.isFlipped = false;

        return flickrItem;
    }

    @NonNull
    private static String buildImageUrl(@NonNull FlickrItem flickrItem) {

//        https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg

        StringBuilder builder = new StringBuilder();
        builder.append("https://farm")
               .append(flickrItem.getFarm())
               .append(".staticflickr.com/")
               .append(flickrItem.getServer())
               .append("/")
               .append(flickrItem.getId())
               .append("_")
               .append(flickrItem.getSecret())
               .append(".jpg");
        return builder.toString();
    }

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public String getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @ColorRes
    public int getImagePlaceholder() {
        return imagePlaceholder;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }
}

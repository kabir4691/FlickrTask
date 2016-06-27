package com.kabir.flickrtask.utils.image.provider;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.device.yearclass.YearClass;

/**
 * Created by Kabir on 27-06-2016.
 */
public class GlideImageProvider implements ImageProvider {

    private Glide glide;
    private RequestManager requestManager;
    private final DecodeFormat decodeFormat;

    public GlideImageProvider(@NonNull Context context) {
        glide = Glide.get(context);
        requestManager = Glide.with(context);
        decodeFormat = getDecodeFormat(context);
    }

    @Override
    public void displayImage(@NonNull String url, float thumbnailQuality, @ColorRes int placeHolder,
            @ColorRes int errorHolder, @NonNull ImageView target) {

//        target.setBackgroundColor(bgColor);
        BitmapRequestBuilder requestBuilder = requestManager.load(url)
                                                            .asBitmap();

        if (placeHolder != -1) {
            requestBuilder = requestBuilder.placeholder(placeHolder);
        }
        if (errorHolder != -1) {
            requestBuilder = requestBuilder.error(errorHolder);
        }

        if (thumbnailQuality > 0f) {
            requestBuilder = requestBuilder.thumbnail(thumbnailQuality)
                                           .dontAnimate();
        }

        requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL)
                      .format(decodeFormat)
                      .into(target);
    }

    @Override
    public void releaseMemory() {
        glide.clearMemory();
    }

    private DecodeFormat getDecodeFormat(@NonNull Context context) {
        int year = YearClass.get(context);
        return year <= YearClass.CLASS_2011 ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888;
    }
}

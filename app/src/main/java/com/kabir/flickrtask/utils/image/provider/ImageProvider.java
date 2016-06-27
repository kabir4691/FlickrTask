package com.kabir.flickrtask.utils.image.provider;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Created by Kabir on 27-06-2016.
 */
public interface ImageProvider {

    void displayImage(@NonNull String url, float thumbnailQuality, @ColorRes int placeHolder, @ColorRes int errorHolder,
            @NonNull ImageView target);

    void releaseMemory();
}

package com.kabir.flickrtask.utils;

import android.support.annotation.ColorRes;

import java.util.Random;

/**
 * Created by Kabir on 27-06-2016.
 */
public class RandomColorGenerator {

    public static final int colors[] = {
            android.R.color.holo_blue_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_red_dark,
            android.R.color.holo_purple
    };

    @ColorRes
    public static int getRandomColor() {
        Random random = new Random();
        return colors[random.nextInt(colors.length)];
    }
}

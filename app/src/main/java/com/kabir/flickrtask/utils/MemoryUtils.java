package com.kabir.flickrtask.utils;

/**
 * Created by Kabir on 27-06-2016.
 */
public class MemoryUtils {

    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static float getPercentAvailableMemory() {
        return 100f * (1f - (float)getTotalMemory()/getMaxMemory());
    }
}

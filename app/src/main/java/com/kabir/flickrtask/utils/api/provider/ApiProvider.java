package com.kabir.flickrtask.utils.api.provider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import rx.Observable;

/**
 * Created by Kabir on 27-06-2016.
 */
public interface ApiProvider {

    Observable<String> callApi(int method, @NonNull String url, @Nullable Map<String, String> params,
            @Nullable Object apiTag);

    void cancelApiCalls(@NonNull Object tag);
}

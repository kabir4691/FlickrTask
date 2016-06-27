package com.kabir.flickrtask.utils.api.provider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.kabir.flickrtask.utils.AppSingleton;
import com.kabir.flickrtask.utils.api.model.VolleyRequest;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by Kabir on 27-06-2016.
 */
public class VolleyApiProvider implements ApiProvider {

    private RequestQueue requestQueue;

    public VolleyApiProvider() {

    }

    @Override
    public Observable<String> callApi(final int method, @NonNull final String url,
            @Nullable final Map<String, String> params, @Nullable final Object apiTag) {

        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                RequestFuture<String> future = RequestFuture.newFuture();

                VolleyRequest request = new VolleyRequest(method, url, future, params);

                if (apiTag != null) {
                    request.setTag(apiTag);
                }

                request.setShouldCache(false);
                getRequestQueue().add(request);

                try {

                    String response = future.get();
                    return Observable.just(response);
                } catch (InterruptedException | ExecutionException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @Override
    public void cancelApiCalls(@NonNull Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    private RequestQueue getRequestQueue() {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(AppSingleton.getInstance());
        }
        return requestQueue;
    }
}

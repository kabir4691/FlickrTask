package com.kabir.flickrtask.utils.api.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.RequestFuture;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Jay Rambhia on 2/9/16.
 */
public class VolleyRequest extends Request<String> {

    private static final String TAG = "VolleyRequest";

    private int method;
    private String url;
    private RequestFuture<String> future;
    private Map<String, String> params;

    public VolleyRequest(int method, @NonNull String url, @NonNull RequestFuture<String> future,
            @Nullable Map<String, String> params) {
        super(method, url, future);

        this.method = method;
        this.url = url;
        this.future = future;
        this.params = params;

        Log.d(TAG, "url : " + getMethodString(method) + " " + url);
        if (params != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("params : ");
            for (String key : params.keySet()) {
                sb.append(key);
                sb.append(" : ");
                sb.append(params.get(key));
                sb.append(", ");
            }

            Log.d(TAG, sb.toString());
        } else {
            Log.d(TAG, "null params");
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        try {
            String responseString = new String(response.data,
                                               HttpHeaderParser.parseCharset(response.headers));

            Log.d(TAG, "response : " + getMethodString(method) + " " + url + " : " + responseString);
            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        future.onResponse(response);
    }

    @NonNull
    private String getMethodString(int method) {
        switch (method) {
            case Method.GET:
                return "GET";
            case Method.POST:
                return "POST";
            case Method.PUT:
                return "PUT";
            case Method.DELETE:
                return "DELETE";
            default:
                return "";
        }
    }
}

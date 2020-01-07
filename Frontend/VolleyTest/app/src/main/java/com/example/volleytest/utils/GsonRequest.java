package com.example.volleytest.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final TypeToken<T> type;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    public GsonRequest(TypeToken<T> type, int method, String url, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener)
    {
        super(method, url, errorListener);
        this.type = type;
        this.headers = headers;
        this.listener = listener;
    }

    public GsonRequest(Class<T> clazz, int method, String url, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(new TypeToken<T>(){}, method, url, headers, listener, errorListener);
    }

    public GsonRequest(Class<T> clazz, int method, String url, Response.Listener<T> listener) {
        this(clazz, method, url, null, listener, HttpUtils.errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);

            return (Response<T>) Response.success(gson.fromJson(json, type.getType()), cacheEntry);

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
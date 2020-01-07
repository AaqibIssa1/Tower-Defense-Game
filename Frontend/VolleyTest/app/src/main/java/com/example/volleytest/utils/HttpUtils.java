package com.example.volleytest.utils;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.volleytest.App;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class HttpUtils
{
    private static final String TAG = HttpUtils.class.getSimpleName();
    public static final String sr2Server = "http://coms-309-sr-2.misc.iastate.edu:8080";
    public static final GenericErrorListener errorListener = new GenericErrorListener();

    public static class GenericErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    }
    public static class CompletableFutureListener<T> implements Response.Listener<T> {
        private CompletableFuture<T> future;

        public CompletableFutureListener(CompletableFuture<T> future) {
            this.future = future;
        }

        @Override
        public void onResponse(T response) {
            future.complete(response);
        }
    }
    public enum ContentType
    {
        APP_JSON {
            @Override
            public String toString() {
                return "application/json";
            }
        }
    }


    public static String endpoint(String endpoint) {
        return sr2Server + "/" + endpoint;
    }

    public static <T> Future<T> GET(Class<T> clazz, String endpoint) {
        return request(clazz, RequestContainer.GET(endpoint(endpoint)));
    }

    public static <T> Future<T> GET(TypeToken<T> type, String endpoint) {
        return request(type, RequestContainer.GET(endpoint(endpoint)));
    }

    public static <T> T GETNow(Class<T> clazz, String endpoint) {
        return App.getFuture(GET(clazz, endpoint));
    }

    public static <T> T GETNow(TypeToken<T> type, String endpoint) {
        return App.getFuture(GET(type, endpoint));
    }

    public static <T> Future<T> POST(Class<T> clazz, String endpoint, JSONObject body) {
        return request(clazz, RequestContainer.POST(endpoint(endpoint), body));
    }

    public static <T> Future<T> POST(Class<T> clazz, String endpoint, JSONArray body) {
        return request(clazz, RequestContainer.POST(endpoint(endpoint), body));
    }

    public static <T> Future<T> POST(Class<T> clazz, String endpoint, String body, ContentType contentType) {
        return request(clazz, RequestContainer.POST(endpoint(endpoint), body, contentType));
    }

    public static <T,U> Future<T> POST(Class<T> clazz, String endpoint, U body) {
        return request(clazz, RequestContainer.POST(endpoint(endpoint), body));
    }

    public static <T> Future<T> POST(TypeToken<T> type, String endpoint, JSONObject body) {
        return request(type, RequestContainer.POST(endpoint(endpoint), body));
    }

    public static <T> Future<T> POST(TypeToken<T> type, String endpoint, JSONArray body) {
        return request(type, RequestContainer.POST(endpoint(endpoint), body));
    }

    public static <T> Future<T> POST(TypeToken<T> type, String endpoint, String body, ContentType contentType) {
        return request(type, RequestContainer.POST(endpoint(endpoint), body, contentType));
    }

    public static <T,U> Future<T> POST(TypeToken<T> type, String endpoint, U body) {
        return request(type, RequestContainer.POST(endpoint(endpoint), body));
    }

    public static <T> T POSTNow(Class<T> clazz, String endpoint, JSONObject body) {
        return App.getFuture(POST(clazz, endpoint, body));
    }

    public static <T> T POSTNow(Class<T> clazz, String endpoint, JSONArray body) {
        return App.getFuture(POST(clazz, endpoint, body));
    }

    public static <T> T POSTNow(Class<T> clazz, String endpoint, String body, ContentType contentType) {
        return App.getFuture(POST(clazz, endpoint, body, contentType));
    }

    public static <T,U> T POSTNow(Class<T> clazz, String endpoint, U body) {
        return App.getFuture(POST(clazz, endpoint, body));
    }

    public static <T> T POSTNow(TypeToken<T> type, String endpoint, JSONObject body) {
        return App.getFuture(POST(type, endpoint, body));
    }

    public static <T> T POSTNow(TypeToken<T> type, String endpoint, JSONArray body) {
        return App.getFuture(POST(type, endpoint, body));
    }

    public static <T> T POSTNow(TypeToken<T> type, String endpoint, String body, ContentType contentType) {
        return App.getFuture(POST(type, endpoint, body));
    }

    public static <T,U> T POSTNow(TypeToken<T> type, String endpoint, U body) {
        return App.getFuture(POST(type, endpoint, body));
    }

    public static <T> Future<T> request(Class<T> clazz, RequestContainer request) {
        return (Future<T>) (
                clazz == String.class? requestString(request) :
                        clazz == JSONObject.class? requestJsonObject(request) :
                                clazz == JSONArray.class? requestJsonArray(request) :
                                        requestGson(TypeToken.get(clazz), request)
        );
    }

    public static <T> Future<T> request(TypeToken<T> type, RequestContainer request) {
        return (Future<T>) (
                type.getType() == String.class? requestString(request) :
                        type.getType() == JSONObject.class? requestJsonObject(request) :
                                type.getType() == JSONArray.class? requestJsonArray(request) :
                                        requestGson(type, request)
        );
    }

    private static Future<String> requestString(final RequestContainer request)
    {
        final CompletableFuture<String> result = new CompletableFuture<>();

        App.getInstance().addToRequestQueue(
                new StringRequest(request.getMethod(), request.getUrl(),
                        new CompletableFutureListener<>(result), errorListener) {
                    @Override
                    public byte[] getBody() {
                        return request.getBody();
                    }

                    @Override
                    public String getBodyContentType() {
                        return request.getContentType();
                    }
                }
        );

        return result;
    }

    private static Future<JSONObject> requestJsonObject(final RequestContainer request)
    {
        final CompletableFuture<JSONObject> result = new CompletableFuture<>();

        App.getInstance().addToRequestQueue(
                new JsonObjectRequest(request.getMethod(), request.getUrl(), null,
                        new CompletableFutureListener<>(result), errorListener) {
                    @Override
                    public byte[] getBody() {
                        return request.getBody();
                    }

                    @Override
                    public String getBodyContentType() {
                        return request.getContentType();
                    }
                }
        );

        return result;
    }

    private static Future<JSONArray> requestJsonArray(final RequestContainer request)
    {
        final CompletableFuture<JSONArray> result = new CompletableFuture<>();

        App.getInstance().addToRequestQueue(
                new JsonArrayRequest(request.getMethod(), request.getUrl(), null,
                        new CompletableFutureListener<>(result), errorListener) {
                    @Override
                    public byte[] getBody() {
                        return request.getBody();
                    }

                    @Override
                    public String getBodyContentType() {
                        return request.getContentType();
                    }
                }
        );

        return result;
    }

    private static <T> Future<T> requestGson(TypeToken<T> type, final RequestContainer request)
    {
        final CompletableFuture<T> result = new CompletableFuture<>();

        App.getInstance().addToRequestQueue(
                new GsonRequest(type, request.getMethod(), request.getUrl(), request.getHeaders(),
                        new CompletableFutureListener<>(result), errorListener) {
                    @Override
                    public byte[] getBody() {
                        return request.getBody();
                    }

                    @Override
                    public String getBodyContentType() {
                        return request.getContentType();
                    }
                }
        );

        return result;
    }

}

package edu.coms.sr2.utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.reflect.TypeToken;

import edu.coms.sr2.App;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class HttpUtils
{
    private static final String TAG = HttpUtils.class.getSimpleName();
    public static final String sr2Server = "http://coms-309-sr-2.misc.iastate.edu:8080";
    public static final GenericErrorListener errorListener = new GenericErrorListener();

    public static class GenericErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            if(error.networkResponse != null)
                Log.e(TAG, "Volley Error: "
                    + "\nStatus: " + error.networkResponse.statusCode
                    + "\nBody: " + new String(error.networkResponse.data));
            else
                Log.e(TAG, "Volley Error: No network response?");
        }
    }

    public static class FutureListener<T> implements Response.Listener<T>
    {
        private CompletableFuture<T> future;
        public FutureListener() { future = new CompletableFuture<T>(); }
        public Future<T> getFuture() { return future; }
        @Override
        public void onResponse(T response) {
            future.complete(response);
        }
    }

    public static class ConsumerListener<T> implements Response.Listener<T>
    {
        private Consumer<T> consumer;
        public ConsumerListener(Consumer<T> consumer) { this.consumer = consumer; }

        @Override
        public void onResponse(T response) { consumer.accept(response); }
    }

    public enum ContentType {
        APP_JSON {
            @Override
            public String toString() { return "application/json"; }
        }
    }

    public static String getEndpoint(String endpoint) {
        return sr2Server + "/" + endpoint;
    }

    private static RequestContainer GETContainer(String endpoint) {
        return RequestContainer.GET(getEndpoint(endpoint));
    }

    public static <T> Future<T> GET(TypeToken<T> type, String endpoint) {
        return request(type, GETContainer(endpoint));
    }

    public static <T> Future<T> GET(Class<T> clazz, String endpoint) {
        return GET(TypeToken.get(clazz), endpoint);
    }

    public static Future<String> GET(String endpoint) {
        return GET(String.class, endpoint);
    }

    public static <T> T GETNow(TypeToken<T> type, String endpoint) {
        return App.getFuture(GET(type, endpoint));
    }

    public static <T> T GETNow(Class<T> clazz, String endpoint) {
        return GETNow(TypeToken.get(clazz), endpoint);
    }

    public static String GETNow(String endpoint) {
        return GETNow(String.class, endpoint);
    }

    public static <T> Consumer<T> GET(TypeToken<T> type, String endpoint, Consumer<T> consumer) {
        return request(type, GETContainer(endpoint), consumer);
    }

    public static <T> Consumer<T> GET(Class<T> clazz, String endpoint, Consumer<T> consumer) {
        return GET(TypeToken.get(clazz), endpoint, consumer);
    }

    private static RequestContainer POSTContainer(String endpoint, JSONObject body) {
        return RequestContainer.POST(getEndpoint(endpoint), body);
    }
    private static RequestContainer POSTContainer(String endpoint, JSONArray body) {
        return RequestContainer.POST(getEndpoint(endpoint), body);
    }
    private static RequestContainer POSTContainer(String endpoint, String body, ContentType contentType) {
        return RequestContainer.POST(getEndpoint(endpoint), body, contentType);
    }
    private static <U> RequestContainer POSTContainer(String endpoint, U body) {
        return RequestContainer.POST(getEndpoint(endpoint), body);
    }

    public static <T> Future<T> POST(TypeToken<T> type, String endpoint, String body, ContentType contentType) {
        return request(type, POSTContainer(endpoint, body, contentType));
    }

    public static <T> Future<T> POST(Class<T> clazz, String endpoint, String body, ContentType contentType) {
        return POST(TypeToken.get(clazz), endpoint, body, contentType);
    }

    public static <T> Future<T> POST(TypeToken<T> type, String endpoint, JSONObject body) {
        return request(type, POSTContainer(endpoint, body));
    }

    public static <T> Future<T> POST(Class<T> clazz, String endpoint, JSONObject body) {
        return POST(TypeToken.get(clazz), endpoint, body);
    }

    public static <T> Future<T> POST(TypeToken<T> type, String endpoint, JSONArray body) {
        return request(type, POSTContainer(endpoint, body));
    }

    public static <T> Future<T> POST(Class<T> clazz, String endpoint, JSONArray body) {
        return POST(TypeToken.get(clazz), endpoint, body);
    }

    public static <T,U> Future<T> POST(TypeToken<T> type, String endpoint, U body) {
        return request(type, POSTContainer(endpoint, body));
    }

    public static <T,U> Future<T> POST(Class<T> clazz, String endpoint, U body) {
        return POST(TypeToken.get(clazz), endpoint, body);
    }

    public static <T> T POSTNow(TypeToken<T> type, String endpoint, String body, ContentType contentType) {
        return App.getFuture(POST(type, endpoint, body));
    }

    public static <T> T POSTNow(Class<T> clazz, String endpoint, String body, ContentType contentType) {
        return App.getFuture(POST(clazz, endpoint, body, contentType));
    }

    public static <T> T POSTNow(TypeToken<T> type, String endpoint, JSONObject body) {
        return App.getFuture(POST(type, endpoint, body));
    }

    public static <T> T POSTNow(Class<T> clazz, String endpoint, JSONObject body) {
        return App.getFuture(POST(clazz, endpoint, body));
    }

    public static <T> T POSTNow(TypeToken<T> type, String endpoint, JSONArray body) {
        return App.getFuture(POST(type, endpoint, body));
    }

    public static <T> T POSTNow(Class<T> clazz, String endpoint, JSONArray body) {
        return App.getFuture(POST(clazz, endpoint, body));
    }

    public static <T,U> T POSTNow(TypeToken<T> type, String endpoint, U body) {
        return App.getFuture(POST(type, endpoint, body));
    }

    public static <T,U> T POSTNow(Class<T> clazz, String endpoint, U body) {
        return App.getFuture(POST(clazz, endpoint, body));
    }

    public static <T> Consumer<T> POST(TypeToken<T> type, String endpoint, String body, ContentType contentType, Consumer<T> consumer) {
        return request(type, POSTContainer(endpoint, body, contentType), consumer);
    }

    public static <T> Consumer<T> POST(Class<T> clazz, String endpoint, String body, ContentType contentType, Consumer<T> consumer) {
        return POST(TypeToken.get(clazz), endpoint, body, contentType, consumer);
    }

    public static <T> Consumer<T> POST(TypeToken<T> type, String endpoint, JSONObject body, Consumer<T> consumer) {
        return request(type, POSTContainer(endpoint, body), consumer);
    }

    public static <T> Consumer<T> POST(Class<T> clazz, String endpoint, JSONObject body, Consumer<T> consumer) {
        return POST(TypeToken.get(clazz), endpoint, body, consumer);
    }

    public static <T> Consumer<T> POST(TypeToken<T> type, String endpoint, JSONArray body, Consumer<T> consumer) {
        return request(type, POSTContainer(endpoint, body), consumer);
    }

    public static <T> Consumer<T> POST(Class<T> clazz, String endpoint, JSONArray body, Consumer<T> consumer) {
        return POST(TypeToken.get(clazz), endpoint, body, consumer);
    }

    public static <T,U> Consumer<T> POST(TypeToken<T> type, String endpoint, U body, Consumer<T> consumer) {
        return request(type, POSTContainer(endpoint, body), consumer);
    }

    public static <T,U> Consumer<T> POST(Class<T> clazz, String endpoint, U body, Consumer<T> consumer) {
        return POST(TypeToken.get(clazz), endpoint, body, consumer);
    }

    public static <T> Response.Listener<T> request(TypeToken<T> type, RequestContainer request, Response.Listener<T> listener) {
        if(type.getType() == String.class) requestString(request, listener);
        else if(type.getType() == JSONObject.class) requestJsonObject(request, listener);
        else if(type.getType() == JSONArray.class) requestJsonArray(request, listener);
        else
            requestGson(type, request, listener);

        return listener;
    }

    public static <T> Response.Listener<T> request(Class<T> clazz, RequestContainer request, Response.Listener<T> listener) {
        return request(TypeToken.get(clazz), request, listener);
    }

    public static <T> Future<T> request(TypeToken<T> type, RequestContainer request) {
        FutureListener<T> listener = new FutureListener<>();
        request(type, request, listener);
        return listener.getFuture();
    }

    public static <T> Future<T> request(Class<T> clazz, RequestContainer request) {
        return request(TypeToken.get(clazz), request);
    }

    public static <T> Consumer<T> request(TypeToken<T> type, RequestContainer request, Consumer<T> consumer) {
        ConsumerListener<T> listener = new ConsumerListener<>(consumer);
        request(type, request, listener);
        return consumer;
    }

    public static <T> Consumer<T> request(Class<T> clazz, RequestContainer request, Consumer<T> consumer) {
        return request(TypeToken.get(clazz), request, consumer);
    }

    private static void queue(Request request) {
        App.getInstance().addToRequestQueue(request);
    }

    private static void requestString(final RequestContainer request, Response.Listener listener) {
        queue(new StringRequest(request.getMethod(), request.getUrl(), listener, errorListener) {
            @Override
            public byte[] getBody() { return request.getBody(); }
            @Override
            public String getBodyContentType() { return request.getContentType(); }
        });
    }

    private static void requestJsonObject(final RequestContainer request, Response.Listener listener) {
        queue(new JsonObjectRequest(request.getMethod(), request.getUrl(), null, listener, errorListener) {
            @Override
            public byte[] getBody() {
                return request.getBody();
            }
            @Override
            public String getBodyContentType() {
                return request.getContentType();
            }
        });
    }

    private static void requestJsonArray(final RequestContainer request, Response.Listener listener) {
        queue(new JsonArrayRequest(request.getMethod(), request.getUrl(), null, listener, errorListener) {
            @Override
            public byte[] getBody() { return request.getBody(); }
            @Override
            public String getBodyContentType() { return request.getContentType(); }
        });
    }

    private static <T> void requestGson(TypeToken<T> type, final RequestContainer request, Response.Listener<T> listener) {
        queue(new GsonRequest(type, request.getMethod(), request.getUrl(), request.getHeaders(), listener, errorListener) {
            @Override
            public byte[] getBody() { return request.getBody(); }
            @Override
            public String getBodyContentType() { return request.getContentType(); }
        });
    }

}

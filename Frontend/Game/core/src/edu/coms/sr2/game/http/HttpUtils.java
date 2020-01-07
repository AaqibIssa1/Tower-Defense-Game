package edu.coms.sr2.game.http;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.google.gson.reflect.TypeToken;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import edu.coms.sr2.game.Constants;
import edu.coms.sr2.game.utils.ThreadSafeFuture;

public class HttpUtils
{
    private static final String TAG = HttpUtils.class.getSimpleName();

    public enum ContentType {
        APP_JSON { @Override public String toString() { return "application/json"; } }
    }

    public enum StatusCategory {
        GOOD {
            @Override
            public boolean check(HttpStatus status) {
                return status.getStatusCode() >= 200 && status.getStatusCode() < 300;
            }
        },
        BAD {
            @Override
            public boolean check(HttpStatus status) {
                return !GOOD.check(status);
            }
        };

        public abstract boolean check(HttpStatus status);
        public static StatusCategory get(HttpStatus status) {
            for(StatusCategory category : values())
                if(category.check(status))
                    return category;

            return BAD;
        }
    }
    public static boolean isGood(HttpStatus status) { return StatusCategory.GOOD.check(status); }
    public static StatusCategory getCategory(HttpStatus status) { return StatusCategory.get(status); }

    public static String getEndpoint(String endpoint) {
        return "http://" + Constants.sr2Server + ":" + Constants.sr2HttpPort + "/" + endpoint;
    }

    private static HttpRequest makeGETRequest(String endpoint) {
        return HttpRequest.GET(getEndpoint(endpoint));
    }

    public static <T> ThreadSafeFuture<T> GET(TypeToken<T> type, String endpoint) {
        return request(type, makeGETRequest(endpoint));
    }

    public static <T> ThreadSafeFuture<T> GET(Class<T> clazz, String endpoint) {
        return GET(TypeToken.get(clazz), endpoint);
    }

    public static ThreadSafeFuture<String> GET(String endpoint) {
        return GET(String.class, endpoint);
    }

    public static <T> T GETNow(TypeToken<T> type, String endpoint) {
        return GET(type, endpoint).get();
    }

    public static <T> T GETNow(Class<T> clazz, String endpoint) {
        return GETNow(TypeToken.get(clazz), endpoint);
    }

    public static String GETNow(String endpoint) {
        return GETNow(String.class, endpoint);
    }

    public static <T> Consumer<T> GET(TypeToken<T> type, String endpoint, Consumer<T> consumer) {
        return request(type, makeGETRequest(endpoint), consumer);
    }

    public static <T> Consumer<T> GET(Class<T> clazz, String endpoint, Consumer<T> consumer) {
        return GET(TypeToken.get(clazz), endpoint, consumer);
    }

    private static HttpRequest makePOSTRequest(String endpoint, String body, String contentType) {
        return HttpRequest.POST(getEndpoint(endpoint), body, contentType);
    }

    private static <U> HttpRequest makePOSTRequest(String endpoint, U body) {
        return HttpRequest.POST(getEndpoint(endpoint), body);
    }

    public static <T> ThreadSafeFuture<T> POST(TypeToken<T> type, String endpoint, String body, ContentType contentType) {
        return request(type, makePOSTRequest(endpoint, body, contentType.toString()));
    }

    public static <T> ThreadSafeFuture<T> POST(Class<T> clazz, String endpoint, String body, ContentType contentType) {
        return POST(TypeToken.get(clazz), endpoint, body, contentType);
    }

    public static ThreadSafeFuture<String> POST(String endpoint, String body, ContentType contentType) {
        return POST(String.class, endpoint, body, contentType);
    }

    public static <T> ThreadSafeFuture<T> POST(TypeToken<T> type, String endpoint, String body, String contentType) {
        return request(type, makePOSTRequest(endpoint, body, contentType));
    }

    public static <T> ThreadSafeFuture<T> POST(Class<T> clazz, String endpoint, String body, String contentType) {
        return POST(TypeToken.get(clazz), endpoint, body, contentType);
    }

    public static ThreadSafeFuture<String> POST(String endpoint, String body, String contentType) {
        return POST(String.class, endpoint, body, contentType);
    }

    public static <T,U> ThreadSafeFuture<T> POST(TypeToken<T> type, String endpoint, U body) {
        return request(type, makePOSTRequest(endpoint, body));
    }

    public static <T,U> ThreadSafeFuture<T> POST(Class<T> clazz, String endpoint, U body) {
        return POST(TypeToken.get(clazz), endpoint, body);
    }

    public static <U> ThreadSafeFuture<String> POST(String endpoint, U body) {
        return POST(String.class, endpoint, body);
    }

    public static <T> T POSTNow(TypeToken<T> type, String endpoint, String body, ContentType contentType) {
        return POST(type, endpoint, body, contentType).get();
    }

    public static <T> T POSTNow(Class<T> clazz, String endpoint, String body, ContentType contentType) {
        return POST(clazz, endpoint, body, contentType).get();
    }

    public static String POSTNow(String endpoint, String body, ContentType contentType) {
        return POST(endpoint, body, contentType).get();
    }

    public static <T> T POSTNow(TypeToken<T> type, String endpoint, String body, String contentType) {
        return POST(type, endpoint, body, contentType).get();
    }

    public static <T> T POSTNow(Class<T> clazz, String endpoint, String body, String contentType) {
        return POST(clazz, endpoint, body, contentType).get();
    }

    public static String POSTNow(String endpoint, String body, String contentType) {
        return POST(endpoint, body, contentType).get();
    }

    public static <T,U> T POSTNow(TypeToken<T> type, String endpoint, U body) {
        return POST(type, endpoint, body).get();
    }

    public static <T,U> T POSTNow(Class<T> clazz, String endpoint, U body) {
        return POST(clazz, endpoint, body).get();
    }

    public static <U> String POSTNow(String endpoint, U body) {
        return POST(endpoint, body).get();
    }

    public static <T> Consumer<T> POST(TypeToken<T> type, String endpoint, String body, ContentType contentType, Consumer<T> consumer) {
        return request(type, makePOSTRequest(endpoint, body, contentType.toString()), consumer);
    }

    public static <T> Consumer<T> POST(Class<T> clazz, String endpoint, String body, ContentType contentType, Consumer<T> consumer) {
        return POST(TypeToken.get(clazz), endpoint, body, contentType, consumer);
    }

    public static Consumer<String> POST(String endpoint, String body, ContentType contentType, Consumer<String> consumer) {
        return POST(String.class, endpoint, body, contentType, consumer);
    }

    public static <T,U> Consumer<T> POST(TypeToken<T> type, String endpoint, U body, Consumer<T> consumer) {
        return request(type, makePOSTRequest(endpoint, body), consumer);
    }

    public static <T,U> Consumer<T> POST(Class<T> clazz, String endpoint, U body, Consumer<T> consumer) {
        return POST(TypeToken.get(clazz), endpoint, body, consumer);
    }

    public static <U> Consumer<String> POST(String endpoint, U body, Consumer<String> consumer) {
        return POST(String.class, endpoint, body, consumer);
    }

    public static <T> Consumer<T> request(TypeToken<T> type, HttpRequest request, Consumer<T> consumer) {
        return requestGson(type, request, consumer);
    }

    public static <T> Consumer<T> request(Class<T> clazz, HttpRequest request, Consumer<T> consumer) {
        return request(TypeToken.get(clazz), request, consumer);
    }

    public static Consumer<String> request(HttpRequest request, Consumer<String> consumer) {
        return request(String.class, request, consumer);
    }

    public static <T> ThreadSafeFuture<T> request(TypeToken<T> type, HttpRequest request) {
        CompletableFuture<T> future = new CompletableFuture<>();
        request(type, request, val->future.complete(val));
        return new ThreadSafeFuture<>(future);
    }

    public static <T> ThreadSafeFuture<T> request(Class<T> clazz, HttpRequest request) {
        return request(TypeToken.get(clazz), request);
    }

    public static ThreadSafeFuture<String> request(HttpRequest request) {
        return request(String.class, request);
    }

    public static <T> T requestNow(TypeToken<T> type, HttpRequest request) {
        return request(type, request).get();
    }

    public static <T> T requestNow(Class<T> clazz, HttpRequest request) {
        return request(clazz, request).get();
    }

    public static String requestNow(HttpRequest request) {
        return request(request).get();
    }

    private static void sendGdxRequest(HttpRequest request, Net.HttpResponseListener listener) {
        Gdx.net.sendHttpRequest(request.toGdxRequest(), listener);
    }

    private static <T> Consumer<T> requestGson(TypeToken<T> type, final HttpRequest request, Consumer<T> consumer)
    {
        sendGdxRequest(request, new HttpGsonListener(type, consumer));
        return consumer;
    }

}

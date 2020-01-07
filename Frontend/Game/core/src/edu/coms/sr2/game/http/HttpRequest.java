package edu.coms.sr2.game.http;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import edu.coms.sr2.game.Application;


public class HttpRequest
{
    private HttpMethod method;
    private String url;
    private String body;
    private String contentType;
    private Map<String,String> headers;

    public HttpRequest(HttpMethod method, String url, String body, String contentType, Map<String,String> headers) {
        this.method = method;
        this.url = url;
        this.body = body;
        this.contentType = contentType;
        this.headers = headers;
    }

    public HttpRequest(HttpMethod method, String url, String body, HttpUtils.ContentType contentType) {
        this(method, url, body, contentType.toString(), null);
    }

    public <U> HttpRequest(HttpMethod method, String url, U body) {
        this(method, url, Application.gson.toJson(body), HttpUtils.ContentType.APP_JSON);
    }

    public static HttpRequest GET(String url) {
        return new HttpRequest(HttpMethod.GET, url, null, null, null);
    }

    public static HttpRequest POST(String url, String body, String contentType) {
        return new HttpRequest(HttpMethod.POST, url, body, contentType, null);
    }

    public static HttpRequest POST(String url, String body, HttpUtils.ContentType contentType) {
        return POST(url, body, contentType.toString());
    }

    public static <U> HttpRequest POST(String url, U body) {
        return new HttpRequest(HttpMethod.POST, url, body);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Net.HttpRequest toGdxRequest() {
        return new HttpRequestBuilder().newRequest().method(method.toString()).url(url).header("Content-Type", contentType).content(body).build();
    }

    public <T> Consumer<T> make(TypeToken<T> type, Consumer<T> consumer) {
        return HttpUtils.request(type, this, consumer);
    }

    public <T> Consumer<T> make(Class<T> clazz, Consumer<T> consumer) {
        return HttpUtils.request(clazz, this, consumer);
    }

    public Consumer<String> make(Consumer<String> consumer) {
        return HttpUtils.request(this, consumer);
    }

    public <T> Future<T> make(TypeToken<T> type) {
        return HttpUtils.request(type, this);
    }

    public <T> Future<T> make(Class<T> clazz) {
        return HttpUtils.request(clazz, this);
    }

    public Future<String> make() {
        return HttpUtils.request(this);
    }

    public <T> T makeNow(TypeToken<T> type) {
        return HttpUtils.requestNow(type, this);
    }

    public <T> T makeNow(Class<T> clazz) {
        return HttpUtils.requestNow(clazz, this);
    }

    public String makeNow() {
        return HttpUtils.requestNow(this);
    }
}

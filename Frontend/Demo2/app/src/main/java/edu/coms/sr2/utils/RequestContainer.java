package edu.coms.sr2.utils;

import edu.coms.sr2.utils.HttpUtils.ContentType;
import com.android.volley.Request;
import edu.coms.sr2.App;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class RequestContainer
{
    private int method;
    private String url;
    private byte[] body;
    private String contentType;
    private Map<String,String> headers;

    public RequestContainer(int method, String url, byte[] body, String contentType, Map<String,String> headers) {
        this.method = method;
        this.url = url;
        this.body = body;
        this.contentType = contentType;
        this.headers = headers;
    }

    public RequestContainer(int method, String url, byte[] body, ContentType contentType) {
        this(method, url, body, contentType.toString(), null);
    }

    public <U> RequestContainer(int method, String url, U body) {
        this(method, url, App.gson.toJson(body).getBytes(), ContentType.APP_JSON);
    }

    public static RequestContainer GET(String url) {
        return new RequestContainer(Request.Method.GET, url, null, null, null);
    }

    public static RequestContainer POST(String url, byte[] body, String contentType) {
        return new RequestContainer(Request.Method.POST, url, body, contentType, null);
    }

    public static RequestContainer POST(String url, byte[] body, ContentType contentType) {
        return POST(url, body, contentType.toString());
    }

    public static RequestContainer POST(String url, String body, String contentType) {
        return POST(url, body.getBytes(), contentType);
    }

    public static RequestContainer POST(String url, String body, ContentType contentType) {
        return POST(url, body.getBytes(), contentType);
    }

    public static RequestContainer POST(String url, JSONArray body) {
        return POST(url, body.toString().getBytes(), ContentType.APP_JSON);
    }

    public static RequestContainer POST(String url, JSONObject body) {
        return POST(url, body.toString().getBytes(), ContentType.APP_JSON);
    }

    public static <U> RequestContainer POST(String url, U body) {
        return new RequestContainer(Request.Method.POST, url, body);
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
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
}

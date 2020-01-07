package edu.coms.sr2.game.http;

import com.badlogic.gdx.Net;
import com.google.gson.reflect.TypeToken;

import java.util.function.Consumer;

import edu.coms.sr2.game.Application;

public class HttpGsonListener<T> implements Net.HttpResponseListener
{
    public static final String TAG = HttpGsonListener.class.getSimpleName();
    private TypeToken<T> type;
    private Consumer<T> consumer;

    public HttpGsonListener(TypeToken<T> type, Consumer<T> consumer)
    {
        this.type = type;
        this.consumer = consumer;
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse)
    {
        if(HttpUtils.isGood(httpResponse.getStatus()))
        {
            String data = httpResponse.getResultAsString();

            if(type.getType() == String.class)
                consumer.accept((T) data);
            else
                consumer.accept((T) Application.gson.fromJson(data, type.getType()));
        }
        else {
            Application.error(TAG, httpResponse.getResultAsString());
        }
    }

    @Override
    public void failed(Throwable t) {
        Application.error(TAG, t);
    }

    @Override
    public void cancelled() {
    }
}

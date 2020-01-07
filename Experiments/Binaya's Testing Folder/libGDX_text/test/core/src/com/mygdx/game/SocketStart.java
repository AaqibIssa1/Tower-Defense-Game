package com.mygdx.game;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketStart {

    private Socket socket;
    {
        try{
            socket = IO.socket("http://205.237.185.214:9182");
        }catch(URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    public void onCreate() {
        socket.connect();
    }

    public void onDestroy() {
        socket.disconnect();
    }

}

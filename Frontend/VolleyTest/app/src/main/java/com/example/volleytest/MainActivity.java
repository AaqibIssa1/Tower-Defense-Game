package com.example.volleytest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.TextView;

import com.example.volleytest.utils.HttpUtils;
import com.example.volleytest.utils.RequestContainer;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);

        Thread thread = new Thread(){
            @Override
            public void run() {
               LoginResponse response = HttpUtils.POSTNow(LoginResponse.class, "login", new LoginInfo("nathan@iastate.edu", "nathan_pass"));
               setText(response.toString());
                
               //setText(String.valueOf(HttpUtils.GETNow(Profile[].class, "profiles").length));
            }
        };
        thread.start();
    }

    public void setText(final String message) {
        runOnUiThread(()->text.setText(message));
    }
}

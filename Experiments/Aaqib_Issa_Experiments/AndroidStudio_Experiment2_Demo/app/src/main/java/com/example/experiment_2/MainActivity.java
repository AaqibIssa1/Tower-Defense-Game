package com.example.experiment_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText)findViewById(R.id.textName);
        Password = (EditText)findViewById(R.id.textPass);
        Login = (Button)findViewById(R.id.btnLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                success(Name.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void success(String name, String pass) {
        if ((name.equals("Aaqib Issa")) && (pass.equals("password"))){
            Intent i = new Intent(MainActivity.this, NewActivity.class);
            startActivity(i);
        }
        else {
            TextView resultTextView = (TextView) findViewById(R.id.resultTextView);
            resultTextView.setText("Wrong Name or Password! Try Again!");
        }
    }
}

package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstNumberText = (EditText) findViewById(R.id.firstNumberText);
                EditText secondNumberText = (EditText) findViewById(R.id.secondNumberText);
                TextView resultTextView = (TextView) findViewById(R.id.resultTextView);

                int num1 = Integer.parseInt(firstNumberText.getText().toString());
                int num2 = Integer.parseInt(secondNumberText.getText().toString());
                int result = num1 + num2;
                resultTextView.setText(result + "");

            }
        });

        Button subButton = (Button) findViewById(R.id.subButton);
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstNumberText = (EditText) findViewById(R.id.firstNumberText);
                EditText secondNumberText = (EditText) findViewById(R.id.secondNumberText);
                TextView resultTextView = (TextView) findViewById(R.id.resultTextView);

                int num1 = Integer.parseInt(firstNumberText.getText().toString());
                int num2 = Integer.parseInt(secondNumberText.getText().toString());
                int result = num1 - num2;
                resultTextView.setText(result + "");

            }
        });

        Button multButton = (Button) findViewById(R.id.multButton);
        multButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstNumberText = (EditText) findViewById(R.id.firstNumberText);
                EditText secondNumberText = (EditText) findViewById(R.id.secondNumberText);
                TextView resultTextView = (TextView) findViewById(R.id.resultTextView);

                int num1 = Integer.parseInt(firstNumberText.getText().toString());
                int num2 = Integer.parseInt(secondNumberText.getText().toString());
                int result = num1 * num2;
                resultTextView.setText(result + "");

            }
        });

        Button divButton = (Button) findViewById(R.id.divButton);
        divButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstNumberText = (EditText) findViewById(R.id.firstNumberText);
                EditText secondNumberText = (EditText) findViewById(R.id.secondNumberText);
                TextView resultTextView = (TextView) findViewById(R.id.resultTextView);

                int num1 = Integer.parseInt(firstNumberText.getText().toString());
                int num2 = Integer.parseInt(secondNumberText.getText().toString());
                int result = num1 / num2;
                resultTextView.setText(result + "");

            }
        });
    }
}

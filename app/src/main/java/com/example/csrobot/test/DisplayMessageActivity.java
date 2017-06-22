package com.example.csrobot.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        String message = "DEFAULT";
        String masterIP = "DEFAULT";

        Intent intent = getIntent();

        if (intent.hasExtra("EXTRA_MESSAGE")) {
            message = intent.getStringExtra("EXTRA_MESSAGE");
        }
        if (intent.hasExtra("EXTRA_SPINNER")) {
            masterIP = intent.getStringExtra("EXTRA_SPINNER");
        }

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(message);

        TextView MASTER_IP = (TextView) findViewById(R.id.MASTER_IP);
        MASTER_IP.setText(masterIP);
    }
}

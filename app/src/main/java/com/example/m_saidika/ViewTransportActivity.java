package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewTransportActivity extends AppCompatActivity {
    private TextView txt;
    private String serviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transport);

        txt=findViewById(R.id.txt);

        Intent intent=getIntent();
        serviceId=intent.getStringExtra("id");

        txt.setText(serviceId);
    }
}
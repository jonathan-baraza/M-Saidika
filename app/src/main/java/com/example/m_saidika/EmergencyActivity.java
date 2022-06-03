package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.URI;

public class EmergencyActivity extends AppCompatActivity {

    public TextView policeTxt, ambulanceTxt, fireTxt, counsellingTxt, sSecurityTxt;
    public Button policeBtn, ambulanceBtn, fireBtn, counsellingChatBtn, counsellingBtn, sSecurityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        policeTxt=findViewById(R.id.policeTxt);
        ambulanceTxt=findViewById(R.id.ambulanceTxt);
        fireTxt=findViewById(R.id.fireTxt);
        counsellingTxt=findViewById(R.id.counsellingTxt);
        sSecurityTxt=findViewById(R.id.sSecurityTxt);
        policeBtn=findViewById(R.id.policeBtn);
        ambulanceBtn=findViewById(R.id.ambulanceBtn);
        fireBtn=findViewById(R.id.fireBtn);
        counsellingChatBtn=findViewById(R.id.counsellingChatBtn);
        counsellingBtn=findViewById(R.id.counsellingBtn);
        sSecurityBtn=findViewById(R.id.sSecurityBtn);

        policeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:991"));
                startActivity(intent);
            }
        });

        ambulanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:991"));
                startActivity(intent);
            }
        });

        fireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:991"));
                startActivity(intent);
            }
        });

        counsellingChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        counsellingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:25791483633"));
                startActivity(intent);
            }
        });

        sSecurityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:991"));
                startActivity(intent);
            }
        });

    }
}
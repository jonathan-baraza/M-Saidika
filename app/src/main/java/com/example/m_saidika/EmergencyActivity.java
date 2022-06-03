package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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

    }
}
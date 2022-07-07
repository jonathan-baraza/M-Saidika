package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {
    private ImageView loadImage;
    private TextView txtWait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadImage=findViewById(R.id.loadImage);
        txtWait=findViewById(R.id.txtWait);
        Glide.with(SplashActivity.this).load(R.drawable.loading2).into(loadImage);

        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long l) {
                if(l<3000){
                    txtWait.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        }.start();
    }
}
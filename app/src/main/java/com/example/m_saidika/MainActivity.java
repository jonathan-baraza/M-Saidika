package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public ImageView jobImage,transportImage,foodImage,housingImage,sPImage,emergencyImage;
    public TextView jobText,transportText,foodText,housingText,sPText,emergencyText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        jobImage=findViewById(R.id.jobImage);
        transportImage=findViewById(R.id.transportImage);
        foodImage=findViewById(R.id.foodImage);
        housingImage=findViewById(R.id.housingImage);
        sPImage=findViewById(R.id.sPImage);
        emergencyImage=findViewById(R.id.emergencyImage);
        jobText=findViewById(R.id.jobText);
        transportText=findViewById(R.id.transportText);
        foodText=findViewById(R.id.foodText);
        housingText=findViewById(R.id.housingText);
        sPText=findViewById(R.id.sPText);
        emergencyText=findViewById(R.id.emergencyText);


        mAuth=FirebaseAuth.getInstance();


    }
}
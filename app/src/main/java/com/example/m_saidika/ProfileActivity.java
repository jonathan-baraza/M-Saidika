package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {
    //update form elements
    public ImageView btnCloseUpdateForm,imgUpdate;
    public EditText fNameUpdate,lNameUpdate,phoneUpdate,bioUpdate;
    public Button btnUpdateProfilePhoto,btnUpdateProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        btnCloseUpdateForm =findViewById(R.id.btnCloseUpdateForm);
        imgUpdate=findViewById(R.id.imgUpdate);
        fNameUpdate=findViewById(R.id.fNameUpdate);
        lNameUpdate=findViewById(R.id.lNameUpdate);
        phoneUpdate=findViewById(R.id.phoneUpdate);
        bioUpdate=findViewById(R.id.bioUpdate);
        btnUpdateProfilePhoto=findViewById(R.id.btnUpdateProfilePhoto);
        btnUpdateProfile=findViewById(R.id.btnUpdateProfile);


        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO validate update inputs
            }
        });
    }
}
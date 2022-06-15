package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewFullPhotoActivity extends AppCompatActivity {

    public ImageView viewPermitApp, backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_photo);

        viewPermitApp = findViewById(R.id.viewPermitApp);
        backArrow = findViewById(R.id.backArrow);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();
        String photo = intent.getStringExtra("photoUrl");

        if(photo.length()>0){
            Picasso.get().load(photo).placeholder(R.drawable.loader2).into(viewPermitApp);
        }else{
            viewPermitApp.setImageResource(R.drawable.img_holder);
        }
    }
}
package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m_saidika.Models.ApplicationItem;
import com.example.m_saidika.Models.FoodItem;
import com.example.m_saidika.Models.JobItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewJobActivity extends AppCompatActivity {

    public TextView viewJobCompanyName, viewJobLocation, viewJobRequirements, viewJobDescription, viewJobPhone;
    public ImageView backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        viewJobCompanyName = findViewById(R.id.viewJobCompanyName);
        viewJobLocation = findViewById(R.id.viewJobLocation);
        viewJobRequirements = findViewById(R.id.viewJobRequirements);
        viewJobDescription = findViewById(R.id.viewJobDescription);
        viewJobPhone = findViewById(R.id.viewJobPhone);

        backArrow = findViewById(R.id.backArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewJobActivity.this, JobActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String jobId = intent.getStringExtra("jobId");

        fetchData(jobId);

    }

    private void fetchData(String jobId) {
        FirebaseDatabase.getInstance().getReference().child("Jobs").child(jobId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                JobItem jobItem = snapshot.getValue(JobItem.class);
                viewJobCompanyName.setText(jobItem.getCompanyName());
                viewJobLocation.setText(jobItem.getLocation());
                viewJobDescription.setText(jobItem.getDescription());
                viewJobRequirements.setText(jobItem.getRequirements());
                viewJobPhone.setText(jobItem.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
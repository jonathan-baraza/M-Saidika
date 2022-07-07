package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_saidika.Models.ApplicationItem;
import com.example.m_saidika.Models.FoodItem;
import com.example.m_saidika.Models.JobItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewJobActivity extends AppCompatActivity {

    public TextView viewJobCompanyName, viewJobLocation, viewJobRequirements, viewJobDescription, viewJobPhone;
    public ImageView backArrow;
    public Button delete;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        viewJobCompanyName = findViewById(R.id.viewJobCompanyName);
        viewJobLocation = findViewById(R.id.viewJobLocation);
        viewJobRequirements = findViewById(R.id.viewJobRequirements);
        viewJobDescription = findViewById(R.id.viewJobDescription);
        viewJobPhone = findViewById(R.id.viewJobPhone);
        delete = findViewById(R.id.delete);
        backArrow = findViewById(R.id.backArrow);

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewJobActivity.this, JobActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String jobId = intent.getStringExtra("jobId");
        String owner = intent.getStringExtra("owner");

        fetchData(jobId);

        if (fUser.getUid().equals(owner)){
            delete.setVisibility(View.VISIBLE);
        }

       delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Jobs").child(jobId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Intent intent = new Intent(ViewJobActivity.this, JobActivity.class);
                           startActivity(intent);
                       }
                   }
               });
           }
       });

    }

    private void fetchData(String jobId) {
        FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Jobs").child(jobId).addValueEventListener(new ValueEventListener() {
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
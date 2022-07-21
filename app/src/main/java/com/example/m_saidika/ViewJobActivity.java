package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.m_saidika.Models.ApplicationItem;
import com.example.m_saidika.Models.FoodItem;
import com.example.m_saidika.Models.JobItem;
import com.example.m_saidika.Models.Profile;
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

    private TextView viewJobCompanyName, viewJobLocation, viewJobRequirements, viewJobDescription, viewJobPhone;
    private ImageView profilePic;
    private Button delete,btnCall;
    private Toolbar toolbar;

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
        toolbar=findViewById(R.id.toolbar);
        profilePic=findViewById(R.id.profilePic);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(viewJobDescription.getText().toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCall=findViewById(R.id.btnCall);

        fUser= FirebaseAuth.getInstance().getCurrentUser();


        Intent intent = getIntent();
        String jobId = intent.getStringExtra("jobId");
        String owner = intent.getStringExtra("owner");

        fetchData(jobId);
        fetchOwnerProfile(owner);

        if (fUser.getUid().equals(owner)){
            delete.setVisibility(View.VISIBLE);
        }

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+viewJobPhone.getText().toString()));
                startActivity(intent);
            }
        });


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

    private void fetchOwnerProfile(String owner) {
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(owner).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile ownerProfile=snapshot.getValue(Profile.class);
                if(ownerProfile.getPhoto().length()>0){
                    Picasso.get().load(ownerProfile.getPhoto()).placeholder(R.drawable.loading).into(profilePic);
                }else{
                    Glide.with(ViewJobActivity.this).load(R.drawable.job_interview).into(profilePic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
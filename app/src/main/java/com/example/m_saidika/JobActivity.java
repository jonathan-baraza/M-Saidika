package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.m_saidika.Adapters.JobAdapter;
import com.example.m_saidika.Models.JobItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JobActivity extends AppCompatActivity {

    public Button postJob;
    public RecyclerView jobRecyclerView;
    public JobAdapter jobAdapter;
    public LinearLayoutManager layoutManager;
    public ArrayList<JobItem> allJobItems;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        postJob = findViewById(R.id.postJob);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        initializeRecyclerView();

        postJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JobActivity.this, PostJobActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initializeRecyclerView() {
        jobRecyclerView = findViewById(R.id.jobRecyclerView);
        allJobItems = new ArrayList<>();
        layoutManager=new LinearLayoutManager(JobActivity.this);
        jobRecyclerView.setLayoutManager(layoutManager);
        jobAdapter = new JobAdapter(JobActivity.this,allJobItems);
        jobRecyclerView.setAdapter(jobAdapter);
        jobAdapter.notifyDataSetChanged();
        fetchJobsFromDB();
    }

    private void fetchJobsFromDB() {
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("Jobs");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allJobItems.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    JobItem jobItem = snapshot.getValue(JobItem.class);
                    allJobItems.add(jobItem);
                }
                jobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
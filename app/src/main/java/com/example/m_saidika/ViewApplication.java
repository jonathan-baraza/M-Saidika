package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m_saidika.Models.ApplicationItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewApplication extends AppCompatActivity {

    public TextView viewCompanyName, viewServiceType, viewLocation, viewDescription, viewVerificationStatus;
    public ImageView viewPermit;
    public Button btnAccept, btnReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_application);

        viewCompanyName = findViewById(R.id.viewCompanyName);
        viewServiceType = findViewById(R.id.viewServiceType);
        viewLocation = findViewById(R.id.viewLocation);
        viewDescription = findViewById(R.id.viewDescription);
        viewVerificationStatus = findViewById(R.id.viewVerificationStatus);
        viewPermit = findViewById(R.id.viewPermit);
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("uid");

        fetchData(userId);

    }

    private void fetchData(String userId) {
        FirebaseDatabase.getInstance().getReference().child("Applications").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ApplicationItem applicationItem = snapshot.getValue(ApplicationItem.class);
                viewCompanyName.setText(applicationItem.getCompanyName());
                viewServiceType.setText(applicationItem.getServiceType());
                viewLocation.setText(applicationItem.getLocation());
                viewDescription.setText(applicationItem.getDescription());
                viewVerificationStatus.setText(applicationItem.getVerificationStatus());
                Picasso.get().load(applicationItem.getPermit()).into(viewPermit);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
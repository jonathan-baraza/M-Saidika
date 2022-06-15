package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_saidika.Models.ApplicationItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewApplication extends AppCompatActivity {

    public TextView viewCompanyName, viewServiceType, viewLocation, viewDescription, viewVerificationStatus;
    public ImageView viewPermit, backArrow;
    public Button btnAccept, btnReject;

    public String photoUrl;

    public AlertDialog.Builder builder;

    //loading feature
    public ProgressDialog pd;

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
        backArrow = findViewById(R.id.backArrow);

        pd=new ProgressDialog(ViewApplication.this);
        pd.setCancelable(false);
        pd.setMessage("Loading...please wait...");
        pd.create();

        builder = new AlertDialog.Builder(ViewApplication.this);
        builder.setTitle("Error").setCancelable(false).create();
        builder.setIcon(R.drawable.ic_warning_yellow);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        Intent intent = getIntent();
        String userId = intent.getStringExtra("uid");

        fetchData(userId);

        viewPermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewApplication.this, ViewPermitApplication.class);
                intent.putExtra("permit", photoUrl);
                startActivity(intent);
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewApplication.this, ApplicationsActivity.class);
                startActivity(intent);
            }
        });

       btnAccept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               acceptApplication(userId);
           }
       });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectApplication(userId);
            }
        });


    }

    private void rejectApplication(String userId) {
        pd.show();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("Applications").child(userId);
        HashMap<String,Object> applicationData =new HashMap<>();
        applicationData.put("verificationStatus","rejected");

        dbRef.updateChildren(applicationData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    pd.dismiss();
                    builder.setTitle("Failed to reject Application");
                    builder.setMessage("There was an error, try again later");
                    builder.show();
                }
                else{
                    pd.dismiss();
                    Toast.makeText(ViewApplication.this, "Application Rejected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void acceptApplication( String userId) {
        pd.show();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("Applications").child(userId);
        HashMap<String,Object> applicationData =new HashMap<>();
        applicationData.put("verificationStatus","accepted");

        dbRef.updateChildren(applicationData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    pd.dismiss();
                    builder.setTitle("Failed to accept Application");
                    builder.setMessage("There was an error, try again later");
                    builder.show();
                }
                else{
                    pd.dismiss();
                    Toast.makeText(ViewApplication.this, "Application Accepted", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                photoUrl = applicationItem.getPermit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
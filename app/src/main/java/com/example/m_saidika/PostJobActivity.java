package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PostJobActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText jobCompanyName, jobLocation, jobPhoneNumber, jobDescription, jobRequirements;
    private Button btnSubmit;
    private AlertDialog.Builder builder;

    //loading feature
    private ProgressDialog pd;

    //Authentication with firebase
    private FirebaseAuth mAuth;

    private DatabaseReference databaseRef;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Job Vacancy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        jobCompanyName = findViewById(R.id.jobCompanyName);
        jobLocation = findViewById(R.id.jobLocation);
        jobPhoneNumber = findViewById(R.id.jobPhoneNumber);
        jobDescription = findViewById(R.id. jobDescription);
        jobRequirements = findViewById(R.id.jobRequirements);
        btnSubmit = findViewById(R.id.btnSubmit);

        pd=new ProgressDialog(PostJobActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading...please wait...");
        pd.create();

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        mAuth= FirebaseAuth.getInstance();
        databaseRef= FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Jobs");

        builder = new AlertDialog.Builder(PostJobActivity.this);
        builder.setTitle("Input Error").setCancelable(false).create();
        builder.setIcon(R.drawable.ic_warning_yellow);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobCompanyNameTxt = jobCompanyName.getText().toString().trim();
                String jobLocationTxt = jobLocation.getText().toString().trim();
                String jobPhoneNumberTxt = jobPhoneNumber.getText().toString().trim();
                String jobDescriptionTxt = jobDescription.getText().toString().trim();
                String jobRequirementsTxt = jobRequirements.getText().toString().trim();

                if(TextUtils.isEmpty(jobCompanyNameTxt)){
                    builder.setMessage("You must enter company name");
                    builder.show();
                }else if(TextUtils.isEmpty(jobLocationTxt)){
                    builder.setMessage("You must enter location");
                    builder.show();
                }else if(TextUtils.isEmpty(jobPhoneNumberTxt)){
                    builder.setMessage("You must enter phone number");
                    builder.show();
                }else if(TextUtils.isEmpty(jobDescriptionTxt)){
                    builder.setMessage("You must enter job description");
                    builder.show();
                }else if(TextUtils.isEmpty(jobRequirementsTxt)){
                    builder.setMessage("You must enter job requirements");
                    builder.show();
                }else {
                    postJob(jobCompanyNameTxt, jobLocationTxt, jobPhoneNumberTxt, jobDescriptionTxt, jobRequirementsTxt);
                }

            }
        });

    }

    private void postJob(String jobCompanyNameTxt, String jobLocationTxt, String jobPhoneNumberTxt, String jobDescriptionTxt, String jobRequirementsTxt) {
        pd.show();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Jobs");


        String key = dbRef.push().getKey();

        HashMap<String,Object> jobData=new HashMap<>();
        jobData.put("companyName",jobCompanyNameTxt);
        jobData.put("location",jobLocationTxt);
        jobData.put("phone",jobPhoneNumberTxt);
        jobData.put("description",jobDescriptionTxt);
        jobData.put("requirements",jobRequirementsTxt);
        jobData.put("owner",fUser.getUid());
        jobData.put("jobId",key);


        dbRef.child(key).setValue(jobData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    pd.dismiss();
                    builder.setTitle("Job post Failed");
                    builder.setMessage("There was an error while posting job vacancy, try again later");
                    builder.show();
                }
                else{
                    pd.dismiss();
                    Toast.makeText(PostJobActivity.this, "Job post was successful", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    finish();
                }
            }
        });

    }

    private void clearInputs() {
        jobCompanyName.setText("");
        jobLocation.setText("");
        jobPhoneNumber.setText("");
        jobDescription.setText("");
        jobRequirements.setText("");
    }
}
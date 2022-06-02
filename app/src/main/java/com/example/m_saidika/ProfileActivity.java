package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.TextView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.m_saidika.Models.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;


public class ProfileActivity extends AppCompatActivity {
    //update form elements
    public LinearLayout updateForm;
    public ImageView btnCloseUpdateForm,imgUpdate;
    public EditText fNameUpdate,lNameUpdate,phoneUpdate,bioUpdate;
    public Button btnShowUpdateForm,btnUpdateProfilePhoto,btnUpdateProfile;


    public Button btnBack, btnUpdate;
    public ImageView profilePic;
    public TextView name, email, phone, idNo,admNo;


    //Validator
    public InputValidation inputValidation;

    public AlertDialog.Builder builder;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);
        profilePic = findViewById(R.id.profilePic);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        idNo = findViewById(R.id.idNo);
        admNo=findViewById(R.id.admNo);

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        email.setText("Email: "+fUser.getEmail().toString());

        getProfileDetails();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });



        updateForm=findViewById(R.id.updateForm);
        btnShowUpdateForm=findViewById(R.id.btnShowUpdateForm);
        btnCloseUpdateForm =findViewById(R.id.btnCloseUpdateForm);
        imgUpdate=findViewById(R.id.imgUpdate);
        fNameUpdate=findViewById(R.id.fNameUpdate);
        lNameUpdate=findViewById(R.id.lNameUpdate);
        phoneUpdate=findViewById(R.id.phoneUpdate);
        bioUpdate=findViewById(R.id.bioUpdate);
        btnUpdateProfilePhoto=findViewById(R.id.btnUpdateProfilePhoto);
        btnUpdateProfile=findViewById(R.id.btnUpdateProfile);

        inputValidation=new InputValidation();

        builder=new AlertDialog.Builder(ProfileActivity.this);
        builder.setIcon(R.drawable.ic_warning_yellow);

        builder.setTitle("Input Update Error").setCancelable(false).setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Dismiss alert builder
            }
        }).create();


        btnShowUpdateForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateForm.setVisibility(View.VISIBLE);
            }
        });

        btnCloseUpdateForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateForm.setVisibility(View.GONE);
            }
        });


        btnUpdateProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(ProfileActivity.this);
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fNameUpdateTxt=fNameUpdate.getText().toString();
                String lNameUpdateTxt=lNameUpdate.getText().toString();
                String phoneUpdateTxt=phoneUpdate.getText().toString();
                String bioUpdateTxt=bioUpdate.getText().toString();

                if(TextUtils.isEmpty(fNameUpdateTxt)){
                    builder.setMessage("You must enter first name before updating");
                    builder.show();
                }else if(TextUtils.isEmpty(lNameUpdateTxt)){
                    builder.setMessage("You must enter last name before updating");
                    builder.show();
                }else if(TextUtils.isEmpty(phoneUpdateTxt)){
                    builder.setMessage("You must enter your phone number before updating");
                    builder.show();
                }else if(TextUtils.isEmpty(bioUpdateTxt)){
                    builder.setMessage("You must enter your bio before updating");
                    builder.show();
                }else if(!inputValidation.isValidName(fNameUpdateTxt)){
                        builder.setMessage("Only characters are allowed on your first name");
                        builder.show();
                }
                else if(!inputValidation.isValidName(lNameUpdateTxt)){
                    builder.setMessage("Only characters are allowed on your last name");
                    builder.show();
                }else if(!(phoneUpdateTxt.length()==12)){
                    builder.setMessage("Invalid phone number...check the number of digits and start with 254...");
                    builder.show();
                }else{
                    Toast.makeText(ProfileActivity.this, "Continuing with update", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getProfileDetails() {
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(fUser.getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile userProfie=snapshot.getValue(Profile.class);
                name.setText(userProfie.getFirstName()+" "+userProfie.getLastName());
                phone.setText("Phone: "+userProfie.getPhone());
                if(TextUtils.isEmpty(userProfie.getAdmNo())){
                    idNo.setText("ID Number: "+userProfie.getIdNo().toString());
                    admNo.setVisibility(View.GONE);
                }else{
                    admNo.setText("Admission Number: "+userProfie.getAdmNo().toString());
                    idNo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
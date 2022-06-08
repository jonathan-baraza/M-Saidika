package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.util.HashMap;



public class ProfileActivity extends AppCompatActivity {
    //update form elements
    public LinearLayout updateForm,profileDetails;
    public ImageView btnCloseUpdateForm,imgUpdate,backArrow;
    public EditText phoneUpdate,bioUpdate;
    public Button btnShowUpdateForm,btnUpdateProfilePhoto,btnUpdateProfile;


    public ImageView profilePic;
    public TextView name, email, phone, idNo,admNo;

    public Uri imageUri;
    public String downloadUrl;


    //Validator
    public InputValidation inputValidation;

    public AlertDialog.Builder builder;

    public ProgressDialog pd;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        pd=new ProgressDialog(ProfileActivity.this);
        pd.setCancelable(false);





        profileDetails=findViewById(R.id.profileDetails);

        profilePic = findViewById(R.id.profilePic);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        idNo = findViewById(R.id.idNo);
        admNo=findViewById(R.id.admNo);

        backArrow=findViewById(R.id.backArrow);

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        email.setText("Email: "+fUser.getEmail().toString());

        getProfileDetails();


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateForm.setVisibility(View.GONE);
                profileDetails.setVisibility(View.VISIBLE);
            }
        });



        updateForm=findViewById(R.id.updateForm);
        btnShowUpdateForm=findViewById(R.id.btnShowUpdateForm);
        btnCloseUpdateForm =findViewById(R.id.btnCloseUpdateForm);
        imgUpdate=findViewById(R.id.imgUpdate);
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
                profileDetails.setVisibility(View.GONE);
                updateForm.setVisibility(View.VISIBLE);

            }
        });

        btnCloseUpdateForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateForm.setVisibility(View.GONE);
                profileDetails.setVisibility(View.VISIBLE);
            }
        });


        btnUpdateProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ProfileActivity.this);
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Updating profile");
                pd.create();
                pd.show();
                String phoneUpdateTxt=phoneUpdate.getText().toString();
                String bioUpdateTxt=bioUpdate.getText().toString();

                if(TextUtils.isEmpty(phoneUpdateTxt)){
                    pd.dismiss();
                    builder.setMessage("You must enter your phone number before updating");
                    builder.show();
                }else if(TextUtils.isEmpty(bioUpdateTxt)){
                    pd.dismiss();
                    builder.setMessage("You must enter your bio before updating");
                    builder.show();
                }else if(!(phoneUpdateTxt.length()==12)){
                    pd.dismiss();
                    builder.setMessage("Invalid phone number...check the number of digits and start with 254...");
                    builder.show();
                }else{
                    updateProfileDetails(phoneUpdateTxt,bioUpdateTxt);
                }
            }
        });

    }

    private void updateProfileDetails(String phone, String bio) {
        if(imageUri!=null){
            String fileExtension=imageUri.getLastPathSegment().substring(imageUri.getLastPathSegment().length()-3);
            StorageReference storageRef= FirebaseStorage.getInstance().getReference().child("ProfilesPhotos").child(System.currentTimeMillis()+fileExtension);
            StorageTask uploadTask=storageRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        pd.dismiss();
                        builder.setMessage("Failed to upload your photo");
                        builder.show();
                        return null;
                    }else{

                        return storageRef.getDownloadUrl();
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri result=task.getResult();
                    downloadUrl=result.toString();
                    insertProfileData(bio,phone,downloadUrl);
                }
            });

        }else{
            insertProfileData(bio,phone,"");
        }


    }

    private void insertProfileData(String bio, String phone, String downloadUrl) {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("Profiles").child(fUser.getUid());
        HashMap<String,Object> profileData=new HashMap<>();
        profileData.put("bio",bio);
        profileData.put("phone",phone);
        if(downloadUrl.length()>0){
            profileData.put("photo",downloadUrl);
        }

        dbRef.updateChildren(profileData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    pd.dismiss();
                    builder.setTitle("Failed to update profile");
                    builder.setMessage("There was an error while updating profile, try again later");
                    builder.show();
                }
                else{
                    pd.dismiss();
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getProfileDetails() {
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(fUser.getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile userProfile=snapshot.getValue(Profile.class);
                name.setText(userProfile.getFirstName()+" "+userProfile.getLastName());
                phone.setText("Phone: "+userProfile.getPhone());
                phoneUpdate.setText(userProfile.getPhone());
                bioUpdate.setText(userProfile.getBio());

                if(userProfile.getPhoto().length()>0){
                    Picasso.get().load(userProfile.getPhoto()).into(imgUpdate);
                }else{
                    imgUpdate.setImageResource(R.drawable.img_holder);
                }

                if(TextUtils.isEmpty(userProfile.getAdmNo())){
                    idNo.setText("ID Number: "+userProfile.getIdNo().toString());
                    admNo.setVisibility(View.GONE);
                }else{
                    admNo.setText("Admission Number: "+userProfile.getAdmNo().toString());
                    idNo.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            imgUpdate.setImageURI(imageUri);

        }
    }
}


package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class PostHouseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText aptName, aptLocation, aptPhoneNumber, aptRent, aptDescription;
    private ImageView aptPic;
    private LinearLayout picLin;
    private Button btnSubmit;
    private AlertDialog.Builder builder;
    private Uri imageUri;

    //loading feature
    private ProgressDialog pd;

    //Authentication with firebase
    private FirebaseAuth mAuth;

    private DatabaseReference databaseRef;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_house);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Vacant House");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        aptName = findViewById(R.id.aptName);
        aptLocation = findViewById(R.id.aptLocation);
        aptPhoneNumber = findViewById(R.id.aptPhoneNumber);
        aptRent = findViewById(R.id.aptRent);
        aptDescription = findViewById(R.id.aptDescription);
        aptPic = findViewById(R.id.aptPic);
        picLin = findViewById(R.id.picLin);
        btnSubmit = findViewById(R.id.btnSubmit);

        pd=new ProgressDialog(PostHouseActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading...please wait...");
        pd.create();

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        mAuth= FirebaseAuth.getInstance();
        databaseRef= FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Houses");

        builder = new AlertDialog.Builder(PostHouseActivity.this);
        builder.setTitle("Input Error").setCancelable(false).create();
        builder.setIcon(R.drawable.ic_warning_yellow);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        picLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(PostHouseActivity.this);
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aptNameTxt = aptName.getText().toString().trim();
                String aptLocationTxt = aptLocation.getText().toString().trim();
                String aptPhoneNumberTxt = aptPhoneNumber.getText().toString().trim();
                String aptRentTxt = aptRent.getText().toString().trim();
                String aptDescriptionTxt = aptDescription.getText().toString().trim();


                if(TextUtils.isEmpty(aptNameTxt)){
                    builder.setMessage("You must enter apartment name");
                    builder.show();
                }else if(TextUtils.isEmpty(aptLocationTxt)){
                    builder.setMessage("You must enter location");
                    builder.show();
                }else if(TextUtils.isEmpty(aptPhoneNumberTxt)){
                    builder.setMessage("You must enter phone number");
                    builder.show();
                }else if(TextUtils.isEmpty(aptRentTxt)){
                    builder.setMessage("You must enter rent amount");
                    builder.show();
                }else if(TextUtils.isEmpty(aptDescriptionTxt)) {
                    builder.setMessage("You must enter apartment description");
                    builder.show();
                }else if (imageUri==null){
                    builder.setMessage("You must upload your apartment picture");
                    builder.show();
                }else {
                    submitAptPic(aptNameTxt, aptLocationTxt, aptPhoneNumberTxt, aptRentTxt, aptDescriptionTxt);
                }

            }
        });


    }

    private void submitAptPic(String aptNameTxt, String aptLocationTxt, String aptPhoneNumberTxt, String aptRentTxt, String aptDescriptionTxt) {
        pd.show();
        String fileExtension=imageUri.getLastPathSegment().substring(imageUri.getLastPathSegment().length()-3);
        StorageReference storageRef= FirebaseStorage.getInstance().getReference().child("Apartments").child(System.currentTimeMillis()+fileExtension);

        StorageTask uploadTask=storageRef.putFile(imageUri);

        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()){
                    builder.setTitle("Apartment Picture Upload error");
                    builder.setMessage("Failed to upload apartment picture");
                    builder.show();
                }

                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri result=task.getResult();
                String downloadImageUrl=result.toString();
                postHouse(aptNameTxt, aptLocationTxt, aptPhoneNumberTxt, aptRentTxt, aptDescriptionTxt, downloadImageUrl);
            }
        });
    }

    public void postHouse(String aptNameTxt, String aptLocationTxt, String aptPhoneNumberTxt, String aptRentTxt, String aptDescriptionTxt, String downloadImageUrl){
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Houses");

        String key = dbRef.push().getKey();

        HashMap<String,Object> houseData=new HashMap<>();
        houseData.put("apartmentName",aptNameTxt);
        houseData.put("location",aptLocationTxt);
        houseData.put("phoneNumber",aptPhoneNumberTxt);
        houseData.put("rent",aptRentTxt);
        houseData.put("description",aptDescriptionTxt);
        houseData.put("apartmentPic",downloadImageUrl);
        houseData.put("owner",fUser.getUid());
        houseData.put("houseId",key);

        dbRef.child(key).setValue(houseData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    pd.dismiss();
                    builder.setTitle("House post failed");
                    builder.setMessage("There was an error while posting house vacancy, try again later");
                    builder.show();
                }
                else{
                    pd.dismiss();
                    Toast.makeText(PostHouseActivity.this, "House post was successful", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    finish();
                }
            }
        });
    }

    private void clearInputs() {
        aptName.setText("");
        aptLocation.setText("");
        aptPhoneNumber.setText("");
        aptDescription.setText("");
        aptRent.setText("");
        aptPic.setImageResource(R.drawable.ic_house);
        imageUri = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            aptPic.setImageURI(imageUri);
        }
    }

}
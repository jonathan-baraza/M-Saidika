package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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


public class ServiceProviderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public Spinner spinner;
    public EditText companyName, location, description;
    public ImageView permit;
    public Button btnSubmit, btnApplications;
    public LinearLayout permitLin;
    public AlertDialog.Builder builder;
    public String serviceType = "";
    public InputValidation inputValidation;

    public Uri imageUri;

    //loading feature
    public ProgressDialog pd;

    //Authentication with firebase
    private FirebaseAuth mAuth;

    private DatabaseReference databaseRef;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);

        companyName=findViewById(R.id.companyName);
        location=findViewById(R.id.location);
        description=findViewById(R.id.description);
        permit =findViewById(R.id.permit);
        permitLin=findViewById(R.id.permitLin);
        btnSubmit=findViewById(R.id.btnSubmit);
        btnApplications=findViewById(R.id.btnApplications);
        spinner = findViewById(R.id.spinner);

        pd=new ProgressDialog(ServiceProviderActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading...please wait...");
        pd.create();

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        mAuth= FirebaseAuth.getInstance();
        databaseRef= FirebaseDatabase.getInstance().getReference().child("Applications");

        builder = new AlertDialog.Builder(ServiceProviderActivity.this);
        builder.setTitle("Input Error").setCancelable(false).create();
        builder.setIcon(R.drawable.ic_warning_yellow);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        btnApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceProviderActivity.this, ApplicationsActivity.class);
                startActivity(intent);
            }
        });

        permitLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ServiceProviderActivity.this);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String companyNameTxt = companyName.getText().toString().trim();
                String locationTxt = location.getText().toString().trim();
                String descriptionTxt = description.getText().toString().trim();

                if(TextUtils.isEmpty(companyNameTxt)){
                    builder.setMessage("You must enter company name");
                    builder.show();
                }else if(TextUtils.isEmpty(locationTxt)){
                    builder.setMessage("You must enter location");
                    builder.show();
                }else if(TextUtils.isEmpty(descriptionTxt)){
                    builder.setMessage("You must enter description");
                    builder.show();
                 }else if(serviceType==null || !(serviceType.length() >0)){
                    builder.setMessage("You must pick a service type");
                    builder.show();
                }else if(imageUri==null){
                    builder.setMessage("You must upload your business permit");
                    builder.show();
                }
                else {
                    submitPermitPhoto(companyNameTxt,locationTxt,descriptionTxt);
                }
            }

        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Service_Type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void submitPermitPhoto(String companyNameTxt, String locationTxt, String descriptionTxt) {
        pd.show();
        String fileExtension=imageUri.getLastPathSegment().substring(imageUri.getLastPathSegment().length()-3);
        StorageReference storageRef= FirebaseStorage.getInstance().getReference().child("Permits").child(System.currentTimeMillis()+fileExtension);

        StorageTask uploadTask=storageRef.putFile(imageUri);

        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()){
                    builder.setTitle("Permit Upload error");
                    builder.setMessage("Failed to add permit");
                    builder.show();
                }

                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri result=task.getResult();
                String downloadImageUrl=result.toString();
                submitApplication( companyNameTxt,locationTxt,descriptionTxt,downloadImageUrl);
            }
        });
    }

    private void submitApplication(String companyNameTxt, String locationTxt, String descriptionTxt,String downloadImageUrl ) {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("Applications");
        HashMap<String,Object> applicationData=new HashMap<>();
        applicationData.put("companyName",companyNameTxt);
        applicationData.put("location",locationTxt);
        applicationData.put("description",descriptionTxt);
        applicationData.put("serviceType",serviceType);
        applicationData.put("logo","");
        applicationData.put("permit",downloadImageUrl);
        applicationData.put("userId",fUser.getUid());
        applicationData.put("verificationStatus","pending");

        String key = fUser.getUid();

        dbRef.child(key).updateChildren(applicationData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    pd.dismiss();
                    builder.setTitle("Application Failed");
                    builder.setMessage("There was an error while submitting the application, try again later");
                    builder.show();
                }
                else{
                    pd.dismiss();
                    Toast.makeText(ServiceProviderActivity.this, "Application was successful", Toast.LENGTH_SHORT).show();
                    clearInputs();
                }
            }
        });

    }

    private void clearInputs() {
        companyName.setText("");
        description.setText("");
        location.setText("");
        permit.setImageResource(R.drawable.doc);
        imageUri = null;
        spinner.setSelection(0);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        serviceType = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            permit.setImageURI(imageUri);
        }
    }
}
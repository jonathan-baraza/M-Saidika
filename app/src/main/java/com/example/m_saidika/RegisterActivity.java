package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    public EditText fName, lName, email, phone, password, confirmPassword, admNo, idNo;
    public TextView goToLogin;
    public RadioGroup radioGroup;
    public RadioButton studentButton, residentButton;
    public Button btnRegister;
    public AlertDialog.Builder builder;
    public InputValidation inputValidation;
    
    //loading feature
    public ProgressDialog pd;
    
    //Authentication with firebase
    private FirebaseAuth mAuth;

    private DatabaseReference databaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        admNo = findViewById(R.id.admNo);
        idNo = findViewById(R.id.idNo);
        goToLogin = findViewById(R.id.goToLogin);
        radioGroup = findViewById(R.id.radioGroup);
        studentButton = findViewById(R.id.studentButton);
        residentButton = findViewById(R.id.residentButton);
        btnRegister = findViewById(R.id.btnRegister);
        
        pd=new ProgressDialog(RegisterActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Authenticating...please wait...");
        pd.create();
        
        mAuth=FirebaseAuth.getInstance();
        databaseRef=FirebaseDatabase.getInstance().getReference().child("Profiles");


        builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Input Error").setCancelable(false).create();
        builder.setIcon(R.drawable.ic_warning_yellow);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        inputValidation=new InputValidation();



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.studentButton:
                        showStudentDetails(admNo,idNo);
                        break;

                    case R.id.residentButton:
                        showResidentDetails(admNo,idNo);
                        break;

                    default:
                        //Do nothing

                }
            }
        });



        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fNameTxt = fName.getText().toString().trim();
                String lNameTxt = lName.getText().toString().trim();
                String emailTxt = email.getText().toString().trim();
                String phoneTxt = phone.getText().toString().trim();
                String admNoTxt = admNo.getText().toString().trim();
                String idNoTxt = idNo.getText().toString().trim();
                String passwordTxt = password.getText().toString().trim();
                String confirmPasswordTxt = confirmPassword.getText().toString().trim();

                if(TextUtils.isEmpty(fNameTxt)){
                    builder.setMessage("You must enter first name");
                    builder.show();
                }else if(TextUtils.isEmpty(lNameTxt)){
                    builder.setMessage("You must enter last name");
                    builder.show();
                }else if(TextUtils.isEmpty(emailTxt)){
                    builder.setMessage("You must enter email");
                    builder.show();
                }else if(TextUtils.isEmpty(phoneTxt)){
                    builder.setMessage("You must enter phone number");
                    builder.show();
                }else if(!studentButton.isChecked() && !residentButton.isChecked()){
                    builder.setMessage("You must select whether you are a student or resident");
                    builder.show();
                }else if(studentButton.isChecked() && TextUtils.isEmpty((admNoTxt))){
                    builder.setMessage("You must enter admission number ");
                    builder.show();
                }else if(residentButton.isChecked() && TextUtils.isEmpty(idNoTxt)){
                    builder.setMessage("You must enter ID number");
                    builder.show();
                }else if(TextUtils.isEmpty(passwordTxt)){
                    builder.setMessage("You must enter password");
                    builder.show();
                }else if(TextUtils.isEmpty(confirmPasswordTxt)){
                    builder.setMessage("You must confirm password");
                    builder.show();
                }else{
                    if(!inputValidation.isValidName(fNameTxt)){
                        builder.setMessage("Only characters allowed in your first name");
                        builder.show();
                    }
                    else if(!inputValidation.isValidName(lNameTxt)){
                        builder.setMessage("Only characters allowed in your last name");
                        builder.show();
                    }
                    else if(!inputValidation.isValidEmail(emailTxt)){
                        builder.setMessage("Invalid email format");
                        builder.show();
                    }else if(!(phoneTxt.length()==12)){
                        builder.setMessage("Invalid phone number...check the number of digits and start with 254...");
                        builder.show();
                    }else if (residentButton.isChecked() && idNo.length()<6){
                        builder.setMessage("ID number must be greater than 6 characters");
                        builder.show();
                    }
                    else if (passwordTxt.length()<6){
                        builder.setMessage("Password must be greater than 6 characters");
                        builder.show();
                    }else if (!passwordTxt.equals(confirmPasswordTxt)){
                        builder.setMessage("Confirm password does not match password");
                        builder.show();
                    }else{
                        registerUser(emailTxt,passwordTxt,fNameTxt,lNameTxt,phoneTxt,residentButton.isChecked(),studentButton.isChecked(),admNoTxt,idNoTxt);
                    }

                }

            }
        });


    }

    private void registerUser(String emailTxt, String passwordTxt,String fNameTxt,String lNameTxt,String phoneTxt,boolean resident,boolean student, String admNo,String idNo) {
        pd.show();
        mAuth.createUserWithEmailAndPassword(emailTxt,passwordTxt).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {


                HashMap<String,Object> userData=new HashMap<>();
                userData.put("firstName",fNameTxt);
                userData.put("lastName",lNameTxt);
                userData.put("phone",phoneTxt);
                if(student){
                    userData.put("admNo",admNo);
                    userData.put("idNo","");
                }else{
                    userData.put("idNo",idNo);
                    userData.put("admNo","");
                }
                userData.put("photo","");
                userData.put("bio","");


                databaseRef.child(authResult.getUser().getUid().toString()).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registration was successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        }
                        else{
                            pd.dismiss();
                            builder.setTitle("Error");
//                builder.setMessage("Registration failed, try again later.");
                            builder.setMessage(task.getException().toString());

                            builder.show();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                builder.setTitle("Error");
//                builder.setMessage("Registration failed, try again later.");
                  builder.setMessage(e.toString());
                System.out.println(e);
                builder.show();
            }
        });
    }


    private void showResidentDetails(EditText admNo, EditText idNo) {
     admNo.setVisibility(View.GONE);
        idNo.setVisibility(View.VISIBLE);
    }

    private void showStudentDetails(EditText admNo, EditText idNo) {
        idNo.setVisibility(View.GONE);
        admNo.setVisibility(View.VISIBLE);
    }
}
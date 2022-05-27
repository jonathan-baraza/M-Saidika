package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    public EditText email,password;
    public Button btnLogin;
    public TextView goToRegister;
    public AlertDialog.Builder builder;
    public InputValidation inputValidation;
    //loading feature
    public ProgressDialog pd;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btnLogin=findViewById(R.id.btnLogin);
        goToRegister=findViewById(R.id.goToRegister);

        inputValidation=new InputValidation();
        
        mAuth=FirebaseAuth.getInstance();

        pd=new ProgressDialog(LoginActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Authenticating...please wait...");
        pd.create();

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        //TODO: Set Regex to validate inputs for both login and register.
        

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailTxt=email.getText().toString();
                String passwordTxt=password.getText().toString();
                builder=new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Input Error");
                builder.setIcon(R.drawable.ic_warning_yellow);
                builder.setCancelable(false).setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Dismisses builder.
                    }
                }).create();

                if(TextUtils.isEmpty(emailTxt)){
                    builder.setMessage("Your must enter email");
                    builder.show();
                }else if(TextUtils.isEmpty(passwordTxt)){
                    builder.setMessage("You must enter password");
                    builder.show();
                }else if(!inputValidation.isValidEmail(emailTxt)){
                    builder.setMessage("Invalid Email format");
                    builder.show();
                }else if (passwordTxt.length()<6){
                    builder.setMessage("Password must be greater than 6 characters");
                    builder.show();
                }else{
                    handleLogin(emailTxt,passwordTxt);
                }

            }
        });
        
    }

    private void handleLogin(String emailTxt, String passwordTxt) {
        pd.show();
        mAuth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "Login was successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                builder.setMessage("Login Failed");
                builder.show();
            }
        });
    }
}
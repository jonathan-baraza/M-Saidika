package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    public TextView email;
    public Button btn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.email);
        btn=findViewById(R.id.btn);

        mAuth=FirebaseAuth.getInstance();

        if(mAuth!=null){
            email.setText(mAuth.getCurrentUser().getEmail());

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    Toast.makeText(MainActivity.this, "You have been signed out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });
        }


    }
}
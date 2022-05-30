package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    public TextView email,btnProfile;

    private FirebaseAuth mAuth;

    //Side menu
    public ImageView openSideMenu,closeSideMenu;
    public RelativeLayout sideMenu,btnSignOut;

    private Animation openSideMenuAnimation,closeSideMenuAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.email);
        btnSignOut=findViewById(R.id.btnSignOut);
        btnProfile=findViewById(R.id.btnProfile);

        mAuth=FirebaseAuth.getInstance();


        //sideMenu
        sideMenu=findViewById(R.id.sideMenu);
        openSideMenu=findViewById(R.id.openSideMenu);
        closeSideMenu=findViewById(R.id.closeSideMenu);


        openSideMenuAnimation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.open_side_menu);
        closeSideMenuAnimation=AnimationUtils.loadAnimation(MainActivity.this,R.anim.close_side_menu);

        closeSideMenuAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sideMenu.setVisibility(View.GONE);
                openSideMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        openSideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSideMenu.setVisibility(View.GONE);
                sideMenu.setVisibility(View.VISIBLE);
                sideMenu.startAnimation(openSideMenuAnimation);

            }
        });

        closeSideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sideMenu.startAnimation(closeSideMenuAnimation);
            }
        });

        if(mAuth!=null){
            email.setText(mAuth.getCurrentUser().getEmail());

            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    Toast.makeText(MainActivity.this, "You have been signed out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });

            btnProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                }
            });
        }


    }
}
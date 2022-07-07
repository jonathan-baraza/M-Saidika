package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_saidika.Models.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public ImageView jobImage,transportImage,foodImage,housingImage,sPImage,emergencyImage;
    public TextView jobText,transportText,foodText,housingText,sPText,emergencyText,email,btnProfile,authName,allApplications,recentActivities,help,about;

    private LinearLayout adminNav,userNav;
    private ImageView chat;

    private FirebaseAuth mAuth;

    //Side menu
    public ImageView openSideMenu,closeSideMenu;
    public RelativeLayout sideMenu,btnSignOut;

    private Animation openSideMenuAnimation,closeSideMenuAnimation;

    private CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.email);
        btnSignOut=findViewById(R.id.btnSignOut);


        jobImage=findViewById(R.id.jobImage);
        transportImage=findViewById(R.id.transportImage);
        foodImage=findViewById(R.id.foodImage);
        housingImage=findViewById(R.id.housingImage);
        sPImage=findViewById(R.id.sPImage);
        emergencyImage=findViewById(R.id.emergencyImage);
        jobText=findViewById(R.id.jobText);
        transportText=findViewById(R.id.transportText);
        foodText=findViewById(R.id.foodText);
        housingText=findViewById(R.id.housingText);
        sPText=findViewById(R.id.sPText);
        emergencyText=findViewById(R.id.emergencyText);


        //side menu
        authName=findViewById(R.id.authName);
        btnProfile=findViewById(R.id.btnProfile);
        allApplications=findViewById(R.id.allApplications);
        recentActivities=findViewById(R.id.recentActivities);
        chat=findViewById(R.id.chat);
        help=findViewById(R.id.help);
        about=findViewById(R.id.about);

        adminNav=findViewById(R.id.adminNav);
        userNav=findViewById(R.id.userNav);

        mAuth=FirebaseAuth.getInstance();

        //sideMenu
        sideMenu=findViewById(R.id.sideMenu);
        openSideMenu=findViewById(R.id.openSideMenu);
        closeSideMenu=findViewById(R.id.closeSideMenu);
        profilePic=findViewById(R.id.profilePic);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AllChatsActivity.class));
            }
        });


        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FoodActivity.class));
            }
        });

        jobImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, JobActivity.class));
            }
        });

        housingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HouseActivity.class));
            }
        });

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

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });

        allApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ApplicationsActivity.class));
            }
        });


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "You have been signed out.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

        emergencyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EmergencyActivity.class);
                startActivity(intent);
            }
        });

        sPImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ServiceProviderActivity.class);
                startActivity(intent);
            }
        });

    }




    @Override
    protected void onStart() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }else{
            getProfileDetails();
        }
        super.onStart();
    }

    private void getProfileDetails() {
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile userProfile=snapshot.getValue(Profile.class);
                if(userProfile.getPhoto().length()>0){
                    Picasso.get().load(userProfile.getPhoto()).placeholder(R.drawable.loader2).into(profilePic);
                }else{
                    profilePic.setImageResource(R.drawable.avatar1);
                }

//                setUpPageAsPerRole(userProfile.getRole(),userProfile.getFirstName()+" "+userProfile.getLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void setUpPageAsPerRole(String role,String fullName) {
//        switch (role){
//            case "admin":
//                adminNav.setVisibility(View.VISIBLE);
//                userNav.setVisibility(View.GONE);
//                authName.setText("ADMINISTRATOR");
//                break;
//            case "user":
//                userNav.setVisibility(View.VISIBLE);
//                adminNav.setVisibility(View.GONE);
//                authName.setText(fullName);
//                break;
//            default:
//                break;
//        }
//    }

}
package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    //Splash
    private LinearLayout splash;
    private ImageView loadImage;
    private TextView loadTxt;

    public ImageView jobImage,transportImage,foodImage,housingImage,sPImage,emergencyImage;
    public TextView jobText,transportText,foodText,housingText,sPText,emergencyText,email,btnProfile,authName,allApplications,orders,manageUsers,transportServices,help,about;

    private LinearLayout panelistNav,adminNav,profileNav,foodServiceNav,transportServiceNav;
    private ImageView chat;

    private FirebaseAuth mAuth;

    //Side menu
    public ImageView openSideMenu,closeSideMenu;
    public RelativeLayout sideMenu,btnSignOut;

    private Animation openSideMenuAnimation,closeSideMenuAnimation;

    private CircleImageView profilePic;

    private FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //splash
        splash=findViewById(R.id.splash);
        loadImage=findViewById(R.id.loadImage);
        loadTxt=findViewById(R.id.loadTxt);
        Glide.with(MainActivity.this).load(R.drawable.loading).into(loadImage);

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
        manageUsers=findViewById(R.id.manageUsers);

        loadTheAnimations();

        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                splash.setVisibility(View.GONE);
            }
        }.start();


        //side menu
        authName=findViewById(R.id.authName);
        btnProfile=findViewById(R.id.btnProfile);
        allApplications=findViewById(R.id.allApplications);
        orders=findViewById(R.id.orders);
        transportServices=findViewById(R.id.tranportServices);
        chat=findViewById(R.id.chat);
        help=findViewById(R.id.help);
        about=findViewById(R.id.about);

        adminNav=findViewById(R.id.adminNav);
        panelistNav=findViewById(R.id.panelistNav);
        profileNav=findViewById(R.id.profileNav);
        foodServiceNav=findViewById(R.id.foodServiceNav);
        transportServiceNav=findViewById(R.id.transportServiceNav);

        mAuth=FirebaseAuth.getInstance();
        fUser=FirebaseAuth.getInstance().getCurrentUser();

        //sideMenu
        sideMenu=findViewById(R.id.sideMenu);
        openSideMenu=findViewById(R.id.openSideMenu);
        closeSideMenu=findViewById(R.id.closeSideMenu);
        profilePic=findViewById(R.id.profilePic);

        manageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ManageUsersActivity.class));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,HelpActivity.class));
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AllChatsActivity.class));
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AllFoodOrdersActivity.class));
            }
        });

        transportServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(MainActivity.this,ViewTransportActivity.class);
               intent.putExtra("id",fUser.getUid());
               startActivity(intent);
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

        transportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,TransportActivity.class));
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
                finish();
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

    private void loadTheAnimations() {
        Glide.with(MainActivity.this).load(R.drawable.job_interview).into(jobImage);
        Glide.with(MainActivity.this).load(R.drawable.vehicle3).into(transportImage);
        Glide.with(MainActivity.this).load(R.drawable.fast_food).into(foodImage);
        Glide.with(MainActivity.this).load(R.drawable.m_home).into(housingImage);
        Glide.with(MainActivity.this).load(R.drawable.m_application).into(sPImage);
        Glide.with(MainActivity.this).load(R.drawable.heal).into(emergencyImage);
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
                checkUserStatus();
                setUpPageAsPerRole(userProfile.getRole(),userProfile.getFirstName()+" "+userProfile.getLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUserStatus() {
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile userProfile=snapshot.getValue(Profile.class);
                if(!(userProfile.getStatus().equals("active"))){
                    startActivity(new Intent(MainActivity.this,DisabledActivity.class));
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpPageAsPerRole(String role,String fullName) {
        switch (role){
            case "admin":
                adminNav.setVisibility(View.VISIBLE);
                profileNav.setVisibility(View.GONE);
                authName.setText("ADMINISTRATOR");
                foodServiceNav.setVisibility(View.GONE);
                break;
            case "panelist":
                panelistNav.setVisibility(View.VISIBLE);
                adminNav.setVisibility(View.GONE);
                profileNav.setVisibility(View.GONE);
                authName.setText("PANELIST");
                foodServiceNav.setVisibility(View.GONE);
                break;
            case "user":
                adminNav.setVisibility(View.GONE);
                authName.setText(fullName);
                foodServiceNav.setVisibility(View.GONE);
                break;
            case "Food":
                adminNav.setVisibility(View.GONE);
                authName.setText(fullName);
                foodServiceNav.setVisibility(View.VISIBLE);
                break;
            case "Housing":
                adminNav.setVisibility(View.GONE);
                authName.setText(fullName);
                foodServiceNav.setVisibility(View.VISIBLE);
                break;
            case "Transport":
                adminNav.setVisibility(View.GONE);
                authName.setText(fullName);
                foodServiceNav.setVisibility(View.GONE);
                transportServiceNav.setVisibility(View.VISIBLE);
                break;
            default:
                adminNav.setVisibility(View.GONE);
                authName.setText(fullName);
                foodServiceNav.setVisibility(View.GONE);
                transportServiceNav.setVisibility(View.GONE);
                break;
        }
    }


}
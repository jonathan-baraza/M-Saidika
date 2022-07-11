package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.m_saidika.Adapters.HouseAdapter;
import com.example.m_saidika.Adapters.JobAdapter;
import com.example.m_saidika.Models.HouseItem;
import com.example.m_saidika.Models.JobItem;
import com.example.m_saidika.Models.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HouseActivity extends AppCompatActivity {
    //Splash
    private LinearLayout splash;
    private ImageView loadImage;

    private Toolbar toolbar;
    private FloatingActionButton postHouse;
    private RecyclerView houseRecyclerView;
    private HouseAdapter houseAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<HouseItem> allHouseItems;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);


        //splash
        splash=findViewById(R.id.splash);
        loadImage=findViewById(R.id.loadImage);
        Glide.with(HouseActivity.this).load(R.drawable.loading).into(loadImage);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Houses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        postHouse = findViewById(R.id.postHouse);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        setupPageAsPerRole();
        initializeRecyclerView();

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                splash.setVisibility(View.GONE);
            }
        }.start();

        postHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HouseActivity.this, PostHouseActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setupPageAsPerRole() {
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile userProfile=snapshot.getValue(Profile.class);
                if(userProfile.getRole().equals("Housing")){
                    postHouse.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeRecyclerView() {
        houseRecyclerView = findViewById(R.id.houseRecyclerView);
        allHouseItems = new ArrayList<>();
        layoutManager=new LinearLayoutManager(HouseActivity.this);
        houseRecyclerView.setLayoutManager(layoutManager);
        houseAdapter = new HouseAdapter(HouseActivity.this,allHouseItems);
        houseRecyclerView.setAdapter(houseAdapter);
        houseAdapter.notifyDataSetChanged();
        fetchJobsFromDB();
    }

    private void fetchJobsFromDB() {
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Houses");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allHouseItems.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    HouseItem houseItem = snapshot.getValue(HouseItem.class);
                    allHouseItems.add(houseItem);
                }
                houseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.m_saidika.Adapters.AllUsersAdapter;
import com.example.m_saidika.Models.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private LinearLayoutManager layoutManager;
    private AllUsersAdapter allUsersAdapter;
    private ArrayList<Profile> allUsers;
    private RecyclerView usersRecView;

    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initUsersRecView();
    }

    private void initUsersRecView() {
        usersRecView=findViewById(R.id.usersRecView);
        layoutManager=new LinearLayoutManager(ManageUsersActivity.this);
        usersRecView.setLayoutManager(layoutManager);
        allUsers=new ArrayList<>();
        allUsersAdapter=new AllUsersAdapter(allUsers,ManageUsersActivity.this);
        usersRecView.setAdapter(allUsersAdapter);
        allUsersAdapter.notifyDataSetChanged();
        fetchAllUsers();
    }

    private void fetchAllUsers() {
        FirebaseDatabase.getInstance().getReference().child("Profiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Profile userProfile=snapshot.getValue(Profile.class);
                    if(!userProfile.getUserId().equals(fUser.getUid())){
                        allUsers.add(userProfile);
                    }
                }

                allUsersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
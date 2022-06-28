package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.m_saidika.Adapters.ApplicationAdapter;
import com.example.m_saidika.Adapters.MenuAdapter;
import com.example.m_saidika.Models.ApplicationItem;
import com.example.m_saidika.Models.FoodItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApplicationsActivity extends AppCompatActivity {

    public RecyclerView applicationRecyclerView;
    public ApplicationAdapter applicationAdapter;
    public LinearLayoutManager layoutManager;
    public ArrayList<ApplicationItem> allApplicationItems;
    public ImageView backArrow;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);

        backArrow = findViewById(R.id.backArrow);

        fUser= FirebaseAuth.getInstance().getCurrentUser();
        initializeRecyclerView();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initializeRecyclerView() {
        applicationRecyclerView = findViewById(R.id.applicationRecyclerView);
        allApplicationItems= new ArrayList<>();
        layoutManager=new LinearLayoutManager(ApplicationsActivity.this);
        applicationRecyclerView.setLayoutManager(layoutManager);
        applicationAdapter = new ApplicationAdapter(ApplicationsActivity.this,allApplicationItems);
        applicationRecyclerView.setAdapter(applicationAdapter);
        applicationAdapter.notifyDataSetChanged();
        fetchApplicationsFromDB();
    }

    private void fetchApplicationsFromDB() {
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("Applications");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allApplicationItems.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ApplicationItem applicationItem = snapshot.getValue(ApplicationItem.class);
                    allApplicationItems.add(applicationItem);
                }
                applicationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
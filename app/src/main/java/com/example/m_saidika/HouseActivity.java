package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.m_saidika.Adapters.HouseAdapter;
import com.example.m_saidika.Adapters.JobAdapter;
import com.example.m_saidika.Models.HouseItem;
import com.example.m_saidika.Models.JobItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HouseActivity extends AppCompatActivity {

    public Button postHouse;
    public RecyclerView houseRecyclerView;
    public HouseAdapter houseAdapter;
    public LinearLayoutManager layoutManager;
    public ArrayList<HouseItem> allHouseItems;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        postHouse = findViewById(R.id.postHouse);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        initializeRecyclerView();

        postHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HouseActivity.this, PostHouseActivity.class);
                startActivity(intent);
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
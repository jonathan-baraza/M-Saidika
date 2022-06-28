package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.m_saidika.Adapters.FoodServiceAdapter;
import com.example.m_saidika.Models.ApplicationItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public RecyclerView foodServicesRecView;
    public FoodServiceAdapter foodServiceAdapter;
    public LinearLayoutManager layoutManager;
    public ArrayList<ApplicationItem> allApplications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Food Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        initRecyclerView();
        
    }

    private void initRecyclerView() {
        foodServicesRecView=findViewById(R.id.foodServicesRecView);
        layoutManager=new LinearLayoutManager(FoodActivity.this);
        foodServicesRecView.setLayoutManager(layoutManager);
        allApplications=new ArrayList<>();
        foodServiceAdapter=new FoodServiceAdapter(FoodActivity.this,allApplications);
        foodServicesRecView.setAdapter(foodServiceAdapter);
        foodServiceAdapter.notifyDataSetChanged();

        fetchFoodServices();
    }

    private void fetchFoodServices() {
        FirebaseDatabase.getInstance().getReference().child("Applications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allApplications.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ApplicationItem item=snapshot.getValue(ApplicationItem.class);
                    if(item.getServiceType().equals("Food")){
                        allApplications.add(item);
                    }
                }
                foodServiceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.m_saidika.Adapters.ApplicationServiceAdapter;
import com.example.m_saidika.Models.ApplicationItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransportActivity extends AppCompatActivity {
    private Toolbar toolbar;
    
    private RecyclerView transportRecView;
    private LinearLayoutManager layoutManager;
    private ArrayList<ApplicationItem> allTransportServices;
    private ApplicationServiceAdapter transportAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transport Services");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initTransportRecView();
    }

    private void initTransportRecView() {
        transportRecView=findViewById(R.id.transportRecView);
        layoutManager=new LinearLayoutManager(TransportActivity.this);
        transportRecView.setLayoutManager(layoutManager);
        allTransportServices=new ArrayList<>();
        transportAdapter=new ApplicationServiceAdapter(TransportActivity.this,allTransportServices,"Transport");
        transportRecView.setAdapter(transportAdapter);
        transportAdapter.notifyDataSetChanged();
        
        fetchTransportServices();
    }

    private void fetchTransportServices() {
        FirebaseDatabase.getInstance().getReference().child("Applications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allTransportServices.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ApplicationItem applicationItem=snapshot.getValue(ApplicationItem.class);
                    if(applicationItem.getVerificationStatus().equals("accepted") && applicationItem.getServiceType().equals("Transport")){
                        allTransportServices.add(applicationItem);
                    }
                }
                transportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
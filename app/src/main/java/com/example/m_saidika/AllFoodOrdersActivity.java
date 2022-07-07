package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.m_saidika.Adapters.OrderAdapter;
import com.example.m_saidika.Models.OrderItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllFoodOrdersActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView allOrdersRecView;
    private int numOfOrders=0;

    private LinearLayoutManager layoutManager;
    private ArrayList<OrderItem> allOrders;
    private OrderAdapter orderAdapter;

    private FirebaseUser fUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_food_orders);

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Orders ("+numOfOrders+")");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        initOrdersRecView();
    }

    private void initOrdersRecView() {
        allOrdersRecView=findViewById(R.id.allOrdersRecView);
        layoutManager=new LinearLayoutManager(AllFoodOrdersActivity.this);
        allOrdersRecView.setLayoutManager(layoutManager);
        allOrders=new ArrayList<>();
        orderAdapter=new OrderAdapter(allOrders,AllFoodOrdersActivity.this,"restaurant",fUser.getUid());
        allOrdersRecView.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
        fetchOrdersFromDB();
    }

    private void fetchOrdersFromDB() {
        FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Food").child(fUser.getUid()).child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allOrders.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    OrderItem orderItem=snapshot.getValue(OrderItem.class);
                    allOrders.add(orderItem);
                }
                orderAdapter.notifyDataSetChanged();
                getSupportActionBar().setTitle("All Orders ("+allOrders.size()+")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.m_saidika.Adapters.OrderAdapter;
import com.example.m_saidika.Models.OrderItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerFoodOrders extends AppCompatActivity {
    private String foodServiceId;

    private Toolbar toolbar;
    private RecyclerView customerOrdersRecView;
    private LinearLayoutManager layoutManager;
    private ArrayList<OrderItem> allOrders;
    private OrderAdapter orderAdapter;

    private FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_orders);


        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        Intent intent=getIntent();
        foodServiceId=intent.getStringExtra("foodServiceId");

        initOrdersRecView();


    }

    private void initOrdersRecView() {
        customerOrdersRecView=findViewById(R.id.customerOrdersRecView);
        layoutManager=new LinearLayoutManager(CustomerFoodOrders.this);
        customerOrdersRecView.setLayoutManager(layoutManager);
        allOrders=new ArrayList<>();
        orderAdapter=new OrderAdapter(allOrders,CustomerFoodOrders.this,"customer",foodServiceId);
        customerOrdersRecView.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
        fetchOrders();
    }

    private void fetchOrders() {
        FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Food").child(foodServiceId).child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allOrders.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    OrderItem orderItem=snapshot.getValue(OrderItem.class);
                    if(orderItem.getUserId().equals(fUser.getUid())){
                        allOrders.add(orderItem);
                    }
                }

                orderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.m_saidika.Models.OrderItem;
import com.example.m_saidika.Models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewOrderActivity extends AppCompatActivity {

    private String orderId,userType,foodServiceId;
    private TextView foodName,price,customerName,time,orderStatus;
    private Button btnPending,btnInProgress,btnOnTheWay;

    private ImageView image;

    private FirebaseUser fUser;
    private ProgressDialog pd;

    private Toolbar toolbar;
    private LinearLayout buttonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fUser= FirebaseAuth.getInstance().getCurrentUser();
        orderStatus=findViewById(R.id.orderStatus);
        image=findViewById(R.id.image);
        foodName=findViewById(R.id.foodName);
        price=findViewById(R.id.price);
        customerName=findViewById(R.id.customerName);
        time=findViewById(R.id.time);
        btnPending =findViewById(R.id.btnPending);
        btnInProgress=findViewById(R.id.btnInProgress);
        btnOnTheWay=findViewById(R.id.btnOnTheWay);

        buttonsLayout=findViewById(R.id.buttonsLayout);

        pd=new ProgressDialog(ViewOrderActivity.this);
        pd.setMessage("Updating status...please Wait");
        pd.create();


        Intent intent=getIntent();
        orderId=intent.getStringExtra("orderId");
        userType=intent.getStringExtra("userType");
        foodServiceId=intent.getStringExtra("foodServiceId");

        setUpPage(userType);

        FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Food").child(foodServiceId).child("Orders").child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrderItem orderItem=snapshot.getValue(OrderItem.class);
                foodName.setText(orderItem.getName());
                price.setText("Ksh "+orderItem.getPrice()+"/=");
                time.setText("Date ordered: "+orderItem.getTime());
                setImageStatus(orderItem.getStatus());


                FirebaseDatabase.getInstance().getReference().child("Profiles").child(orderItem.getUserId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Profile customerProfile =snapshot.getValue(Profile.class);
                        customerName.setText("Ordered by: "+customerProfile.getFirstName()+" "+customerProfile.getLastName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrderStatus("pending");
            }
        });

        btnInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrderStatus("preparing");
            }
        });

        btnOnTheWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrderStatus("delivering");
            }
        });





    }

    private void setImageStatus(String status) {
        if(status.equals("pending")){
            Glide.with(this).load(R.drawable.waiting).into(image);
            orderStatus.setText("order is pending");
        }else if(status.equals("preparing")){
            Glide.with(this).load(R.drawable.preparing_food).into(image);
            orderStatus.setText("order is being prepared");
        }else {
//            Picasso.get().load(R.drawable.delivery).into(image);
            Glide.with(this).load(R.drawable.delivery).into(image);
            orderStatus.setText("order is being delivered");
        }
    }

    private void setUpPage(String userType) {
        if(userType.equals("customer")){
            buttonsLayout.setVisibility(View.GONE);

        }else{
            buttonsLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateOrderStatus(String status) {
        pd.show();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Food").child(fUser.getUid()).child("Orders").child(orderId);
        HashMap<String,Object> statusUpdate=new HashMap<>();
        statusUpdate.put("status",status);
        ref.updateChildren(statusUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(ViewOrderActivity.this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ViewOrderActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ViewOrderActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
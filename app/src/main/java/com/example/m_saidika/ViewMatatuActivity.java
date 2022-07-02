package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.m_saidika.Models.Matatu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewMatatuActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String matatuId,serviceId, amountToBePaid,numberPlateTxt;

    private TextView numberPlate,destination,departureTime,capacity,seatsRemaining,price;
    private Button btnBook;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matatu);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        image=findViewById(R.id.image);
        numberPlate=findViewById(R.id.numberPlate);
        destination=findViewById(R.id.destination);
        departureTime=findViewById(R.id.departureTime);
        capacity=findViewById(R.id.capacity);
        seatsRemaining=findViewById(R.id.seatsRemaining);
        price=findViewById(R.id.price);
        btnBook=findViewById(R.id.btnBook);


        Glide.with(ViewMatatuActivity.this).load(R.drawable.vehicle).into(image);


        Intent intent=getIntent();
        matatuId=intent.getStringExtra("matatuId");
        serviceId=intent.getStringExtra("serviceId");

        FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Transport").child(serviceId).child(matatuId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Matatu matatu=snapshot.getValue(Matatu.class);
                getSupportActionBar().setTitle(matatu.getNumberPlate());
                numberPlate.setText("Number plate: "+matatu.getNumberPlate());
                destination.setText("Destination: "+matatu.getDestination());
                departureTime.setText("Departure time: "+matatu.getDepartureTime());
                capacity.setText("Total capacity: "+matatu.getCapacity());
                int seatsRem=Integer.parseInt(matatu.getCapacity())-matatu.getTotalPassengers();
                seatsRemaining.setText("Seats remaining: "+seatsRem);
                price.setText("Fare price: "+matatu.getFarePrice());
                amountToBePaid =matatu.getFarePrice();
                numberPlateTxt=matatu.getNumberPlate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewMatatuActivity.this,PaymentActivity.class);
                intent.putExtra("name","Transport to "+destination.getText().toString());
                intent.putExtra("amountToBePaid", amountToBePaid);
                intent.putExtra("serviceId",serviceId);
                intent.putExtra("type","Transport");
                intent.putExtra("matatuId",matatuId);
                intent.putExtra("price",price.getText().toString());
                intent.putExtra("numberPlate",numberPlateTxt);
                startActivity(intent);
            }
        });


    }
}
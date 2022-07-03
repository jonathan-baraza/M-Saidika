package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.m_saidika.Adapters.AllUsersAdapter;
import com.example.m_saidika.Models.Matatu;
import com.example.m_saidika.Models.Passenger;
import com.example.m_saidika.Models.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewMatatuActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String matatuId,serviceId, amountToBePaid,numberPlateTxt;

    private TextView numberPlate,destination,departureTime,capacity,seatsRemaining,price,noPassengersText;
    private Button btnBook,btnFull,btnPassengers,closePassengersList;

    private ImageView image;

    private FirebaseUser fUser;

    private LinearLayout viewLayout,passengersLayout;
    private RecyclerView passengersRecView;
    private LinearLayoutManager layoutManager;
    private ArrayList<Profile> allPassengers;
    private AllUsersAdapter passengersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matatu);

        Intent intent=getIntent();
        matatuId=intent.getStringExtra("matatuId");
        serviceId=intent.getStringExtra("serviceId");

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewLayout=findViewById(R.id.viewLayout);
        passengersLayout=findViewById(R.id.passengersLayout);

        image=findViewById(R.id.image);
        numberPlate=findViewById(R.id.numberPlate);
        destination=findViewById(R.id.destination);
        departureTime=findViewById(R.id.departureTime);
        capacity=findViewById(R.id.capacity);
        seatsRemaining=findViewById(R.id.seatsRemaining);
        price=findViewById(R.id.price);
        btnBook=findViewById(R.id.btnBook);
        btnFull=findViewById(R.id.btnFull);
        btnPassengers=findViewById(R.id.btnPassengers);
        closePassengersList=findViewById(R.id.closePassengersList);
        noPassengersText=findViewById(R.id.noPassengersText);


        Glide.with(ViewMatatuActivity.this).load(R.drawable.vehicle).into(image);

        if(fUser.getUid().equals(serviceId)){
            btnPassengers.setVisibility(View.VISIBLE);
        }

        initPassengersRecView();




        FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Transport").child(serviceId).child(matatuId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Matatu matatu=snapshot.getValue(Matatu.class);
                getSupportActionBar().setTitle(matatu.getNumberPlate());
                numberPlate.setText("Number plate: "+matatu.getNumberPlate());
                destination.setText("Destination: "+matatu.getDestination());
                departureTime.setText("Departure time: "+matatu.getDepartureTime());
                capacity.setText("Total capacity: "+matatu.getCapacity());

                FirebaseDatabase.getInstance().getReference().child("Passengers").child(matatuId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Passenger> allPassengers =new ArrayList<>();
                        allPassengers.clear();
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Passenger passenger=snapshot.getValue(Passenger.class);
                            allPassengers.add(passenger);
                        }

                        int seatsRem=Integer.parseInt(matatu.getCapacity())-allPassengers.size();
                        seatsRemaining.setText("Seats remaining: "+seatsRem);

                        if(seatsRem>0){
                            btnBook.setVisibility(View.VISIBLE);
                            btnFull.setVisibility(View.GONE);
                        }else{
                            btnBook.setVisibility(View.GONE);
                            btnFull.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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

        btnPassengers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLayout.setVisibility(View.GONE);
                passengersLayout.setVisibility(View.VISIBLE);
            }
        });
        closePassengersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passengersLayout.setVisibility(View.GONE);
                viewLayout.setVisibility(View.VISIBLE);
            }
        });


    }

    private void initPassengersRecView() {
        passengersRecView=findViewById(R.id.passengersRecView);
        layoutManager=new LinearLayoutManager(ViewMatatuActivity.this);
        passengersRecView.setLayoutManager(layoutManager);
        allPassengers=new ArrayList<>();
        passengersAdapter=new AllUsersAdapter(allPassengers,ViewMatatuActivity.this);
        passengersRecView.setAdapter(passengersAdapter);
        passengersAdapter.notifyDataSetChanged();
//        if(allPassengers.size()==0){
//            noPassengersText.setVisibility(View.VISIBLE);
//            passengersRecView.setVisibility(View.GONE);
//        }else{
//            passengersRecView.setVisibility(View.VISIBLE);
//            noPassengersText.setVisibility(View.GONE);
//        }

        fetchMatatuPassengers();
    }

    private void fetchMatatuPassengers() {
        FirebaseDatabase.getInstance().getReference().child("Passengers").child(matatuId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allPassengers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Passenger passenger=snapshot.getValue(Passenger.class);
                    FirebaseDatabase.getInstance().getReference().child("Profiles").child(passenger.getUserId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Profile passengerProfile=snapshot.getValue(Profile.class);
                            allPassengers.add(passengerProfile);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                passengersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
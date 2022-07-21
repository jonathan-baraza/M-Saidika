package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m_saidika.Models.HouseItem;
import com.example.m_saidika.Models.JobItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewHouseActivity extends AppCompatActivity {

    public TextView viewHouseName, viewHouseLocation, viewHouseRent, viewHouseDescription, viewHousePhone;
    public ImageView backArrow, viewHousePic;
    public Button delete;

    public String housePhotoUrl;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house);

        viewHouseName = findViewById(R.id.viewHouseName);
        viewHouseLocation = findViewById(R.id.viewHouseLocation);
        viewHouseRent = findViewById(R.id.viewHouseRent);
        viewHouseDescription = findViewById(R.id.viewHouseDescription);
        viewHousePhone = findViewById(R.id.viewHousePhone);
        viewHousePic = findViewById(R.id.viewHousePic);
        delete = findViewById(R.id.delete);
        backArrow = findViewById(R.id.backArrow);

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String houseId = intent.getStringExtra("houseId");
        String owner = intent.getStringExtra("owner");

        fetchData(houseId);

        if (fUser.getUid().equals(owner)){
            delete.setVisibility(View.VISIBLE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Houses").child(houseId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(ViewHouseActivity.this, HouseActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        viewHousePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewHouseActivity.this, ViewFullPhotoActivity.class);
                intent.putExtra("photoUrl",housePhotoUrl);
                startActivity(intent);
            }
        });

    }

    private void fetchData(String houseId) {
        FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Houses").child(houseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HouseItem houseItem = snapshot.getValue(HouseItem.class);
                viewHouseName.setText(houseItem.getApartmentName());
                viewHouseLocation.setText(houseItem.getLocation());
                viewHouseRent.setText(houseItem.getRent());
                viewHouseDescription.setText(houseItem.getDescription());
                viewHousePhone.setText(houseItem.getPhoneNumber());
                Picasso.get().load(houseItem.getApartmentPic()).into(viewHousePic);
                housePhotoUrl = houseItem.getApartmentPic();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
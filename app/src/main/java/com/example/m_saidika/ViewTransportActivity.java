package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.m_saidika.Adapters.MatatuAdapter;
import com.example.m_saidika.Models.Matatu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewTransportActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private String serviceId;

    private LinearLayout addMatatuForm;
    private TextView closeAddForm;

    private RelativeLayout transportArea;

    private EditText numberPlate,destination,capacity,price;
    private TimePicker departureTime;
    private Button submitForm;

    private String departureTimeSelected;
    private String AM_PM;

    private AlertDialog.Builder builder;
    private ProgressDialog pd;

    private FirebaseUser fUser;

    private RecyclerView matatuRecView;
    private LinearLayoutManager layoutManager;
    private MatatuAdapter matatuAdapter;
    private ArrayList<Matatu> allMatatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transport);

        Intent intent=getIntent();
        serviceId=intent.getStringExtra("id");
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tranport Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        transportArea=findViewById(R.id.transportArea);
        fab=findViewById(R.id.addMatatu);
        addMatatuForm=findViewById(R.id.addMatatuForm);
        closeAddForm=findViewById(R.id.closeAddForm);

        numberPlate=findViewById(R.id.numberPlate);
        destination=findViewById(R.id.destination);
        departureTime=findViewById(R.id.departureTime);
        capacity=findViewById(R.id.capacity);
        price=findViewById(R.id.price);
        submitForm=findViewById(R.id.submitForm);

        departureTime.setIs24HourView(true);

        if(fUser.getUid().equals(serviceId)){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.GONE);
        }

        initMatatuRecView();

        builder=new AlertDialog.Builder(ViewTransportActivity.this);
        builder.setIcon(R.drawable.ic_warning_yellow);
        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss builder
            }
        });

        pd=new ProgressDialog(ViewTransportActivity.this);
        pd.setMessage("Please wait...");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transportArea.setVisibility(View.GONE);
                addMatatuForm.setVisibility(View.VISIBLE);
            }
        });

        closeAddForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMatatuForm.setVisibility(View.GONE);
                transportArea.setVisibility(View.VISIBLE);
            }
        });




        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                int hr=departureTime.getCurrentHour();
                String selectedHour=String.valueOf(hr);
                if(hr>=12){
                    hr=12-hr;
                    selectedHour=String.valueOf(Math.abs(hr));
                    AM_PM="PM";
                }else{
                    AM_PM="AM";
                }

                if(hr==0){
                    selectedHour="00";
                }

                int min=departureTime.getCurrentMinute();
                String selectedMinute=String.valueOf(min);

                if(String.valueOf(min).length()==1){
                    selectedMinute="0"+min;

                }

                //set full time
                departureTimeSelected=selectedHour+":"+selectedMinute+" "+AM_PM;


                //validating inputs
                String numberPlateTxt=numberPlate.getText().toString();
                String destinationTxt=destination.getText().toString();
                String capacityTxt=capacity.getText().toString();
                String farePriceTxt=price.getText().toString();

                //validation errors
                builder.setTitle("Input Error");
                if(TextUtils.isEmpty(numberPlateTxt)){
                    pd.dismiss();
                    builder.setMessage("Enter number plate");
                    builder.create().show();
                }else if(TextUtils.isEmpty(destinationTxt)){
                    pd.dismiss();
                    builder.setMessage("Enter Destination");
                    builder.create().show();
                }else if(TextUtils.isEmpty(capacityTxt)){
                    pd.dismiss();
                    builder.setMessage("Enter capacity");
                    builder.create().show();
                }else if(TextUtils.isEmpty(farePriceTxt)){
                    pd.dismiss();
                    builder.setMessage("Enter fare price");
                    builder.create().show();
                }else{
                    addMatatu(numberPlateTxt,destinationTxt,capacityTxt,farePriceTxt);
                }
            }
        });





    }

    private void initMatatuRecView() {
        matatuRecView=findViewById(R.id.matatuRecView);
        layoutManager=new LinearLayoutManager(ViewTransportActivity.this);
        matatuRecView.setLayoutManager(layoutManager);
        allMatatus=new ArrayList<>();
        matatuAdapter=new MatatuAdapter(ViewTransportActivity.this,allMatatus,serviceId);
        matatuRecView.setAdapter(matatuAdapter);
        matatuAdapter.notifyDataSetChanged();
        fetchAllMatatus();
    }

    private void fetchAllMatatus() {
        FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Transport").child(serviceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allMatatus.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Matatu matatu=snapshot.getValue(Matatu.class);
                    allMatatus.add(matatu);
                }

                matatuAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addMatatu(String numberPlate, String destination, String capacity, String farePrice) {
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Transport").child(serviceId);
        String key=dbRef.push().getKey();

        HashMap<String,Object> inputDetails=new HashMap<>();
        inputDetails.put("numberPlate",numberPlate);
        inputDetails.put("destination",destination);
        inputDetails.put("capacity",capacity);
        inputDetails.put("farePrice",farePrice);
        inputDetails.put("departureTime",departureTimeSelected);
        inputDetails.put("totalPassengers",0);
        inputDetails.put("matatuId",key);

        dbRef.child(key).setValue(inputDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(ViewTransportActivity.this, "Matatu added", Toast.LENGTH_SHORT).show();
                    resetPage();
                }else{
                    builder.setTitle("Failed to add Matatu");
                    builder.setMessage(task.getException().toString());

                }
            }
        });


    }

    private void resetPage() {
        numberPlate.setText("");
        destination.setText("");
        capacity.setText("");
        price.setText("");
        closeAddForm.performClick();
    }
}
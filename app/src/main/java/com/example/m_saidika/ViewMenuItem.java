package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ViewMenuItem extends AppCompatActivity {
    private TextView name,price;
    private ImageView image;
    private Button btnOrder;
    private Toolbar toolbar;
    private String amountToBePaid;
    private String foodServiceId;
    private EditText destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu_item);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Food");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        name=findViewById(R.id.name);
        image=findViewById(R.id.image);
        price=findViewById(R.id.price);
        destination=findViewById(R.id.destination);
        btnOrder=findViewById(R.id.btnOrder);

        Intent intent=getIntent();
        foodServiceId=intent.getStringExtra("foodServiceId");
        Picasso.get().load(intent.getStringExtra("photo")).placeholder(R.drawable.loader2).into(image);
        name.setText(intent.getStringExtra("name"));
        price.setText("Ksh "+intent.getStringExtra("price")+"/=");
        amountToBePaid=intent.getStringExtra("price");


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String destinationTxt=destination.getText().toString();
                if(TextUtils.isEmpty(destinationTxt)){
                    Toast.makeText(ViewMenuItem.this, "You must enter the delivery destination.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(ViewMenuItem.this,PaymentActivity.class);
                    intent.putExtra("serviceId",foodServiceId);
                    intent.putExtra("type","foodOrder");
                    intent.putExtra("name",name.getText().toString());
                    intent.putExtra("price",price.getText().toString());
                    intent.putExtra("amountToBePaid",amountToBePaid);
                    intent.putExtra("destination",destinationTxt);
                    startActivity(intent);
                    finish();
                }


            }
        });

    }
}
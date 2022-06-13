package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ViewMenuItem extends AppCompatActivity {
    public TextView name,price;
    public ImageView image;
    public Button btnOrder;
    public Toolbar toolbar;
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
        btnOrder=findViewById(R.id.btnOrder);

        Intent intent=getIntent();
        Picasso.get().load(intent.getStringExtra("photo")).placeholder(R.drawable.loader2).into(image);
        name.setText(intent.getStringExtra("name"));
        price.setText(intent.getStringExtra("price"));

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewMenuItem.this,PaymentActivity.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("price",price.getText().toString());
                startActivity(intent);
            }
        });

    }
}
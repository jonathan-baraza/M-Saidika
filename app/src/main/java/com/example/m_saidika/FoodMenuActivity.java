package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FoodMenuActivity extends AppCompatActivity {
    public ImageView closeAddForm,imgUpdate;
    public Button btnSelectPicture,btnAddFood;
    public EditText foodName,foodPrice;
    public FloatingActionButton fab;
    public LinearLayout addFoodForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        InputValidation inputValidation=new InputValidation();

        closeAddForm=findViewById(R.id.closeAddForm);
        imgUpdate=findViewById(R.id.imgUpdate);
        btnSelectPicture=findViewById(R.id.btnSelectPicture);
        btnAddFood=findViewById(R.id.btnAddFood);
        foodName=findViewById(R.id.foodName);
        foodPrice=findViewById(R.id.foodPrice);
        fab=findViewById(R.id.fab);

        addFoodForm=findViewById(R.id.addFoodForm);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.GONE);
                addFoodForm.setVisibility(View.VISIBLE);
            }
        });

        closeAddForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFoodForm.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });
    }
}
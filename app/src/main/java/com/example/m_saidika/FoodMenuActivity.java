package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.m_saidika.Adapters.MenuAdapter;
import com.example.m_saidika.Models.FoodItem;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodMenuActivity extends AppCompatActivity {
    public ImageView closeAddForm,imgUpdate,goBack;
    public Button btnSelectPicture,btnAddFood;
    public EditText foodName,foodPrice;
    public FloatingActionButton fab;
    public ScrollView addFoodForm;

    public Uri imageUri;
    public String downloadUrl;

    public AlertDialog.Builder builder;
    public ProgressDialog pd;

    public FirebaseUser fUser;

    //RecyclerView
    public RecyclerView foodMenuRecyclerView;
    public MenuAdapter menuAdapter;
    public LinearLayoutManager layoutManager;
    public ArrayList<FoodItem> allFoodItems;

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

        goBack=findViewById(R.id.goBack);

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        addFoodForm=findViewById(R.id.addFoodForm);

        builder=new AlertDialog.Builder(FoodMenuActivity.this);
        builder.setCancelable(false);
        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Dismiss builder;
            }
        }).create();
        builder.setIcon(R.drawable.ic_warning_yellow);

        pd=new ProgressDialog(FoodMenuActivity.this);
        pd.setCancelable(false);


        initializeRecyclerView();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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

        btnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(FoodMenuActivity.this);
            }
        });

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodNameTxt=foodName.getText().toString();
                String foodPriceTxt=foodPrice.getText().toString();

                pd.setMessage("Adding to menu...please wait...");
                pd.create();
                pd.show();
                builder.setTitle("Input Error");

                if(TextUtils.isEmpty(foodNameTxt)){
                    pd.dismiss();
                    builder.setMessage("You must enter the name of the food");
                    builder.show();

                }else if(imageUri==null){
                    pd.dismiss();
                    builder.setMessage("You must select an image for the food item.");
                    builder.show();

                }else if(TextUtils.isEmpty(foodPriceTxt)){
                    pd.dismiss();
                    builder.setMessage("You must enter the name of the food");
                    builder.show();
                }else{
                    insertFoodToDB(foodNameTxt,foodPriceTxt);
                }
            }
        });


    }

    private void initializeRecyclerView() {
        foodMenuRecyclerView=findViewById(R.id.foodMenuRecyclerView);
        allFoodItems=new ArrayList<>();
        layoutManager=new GridLayoutManager(FoodMenuActivity.this,3);
        foodMenuRecyclerView.setLayoutManager(layoutManager);
        menuAdapter=new MenuAdapter(FoodMenuActivity.this,allFoodItems);
        foodMenuRecyclerView.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();
        fetchFoodItemsFromDB();
    }

    private void fetchFoodItemsFromDB() {
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Food").child(fUser.getUid()).child("Menu");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allFoodItems.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    FoodItem foodItem=snapshot.getValue(FoodItem.class);
                    allFoodItems.add(foodItem);
                }
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void insertFoodToDB(String foodName, String foodPrice) {
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Food").child(fUser.getUid()).child("Menu");

        String fileExtension=imageUri.getLastPathSegment().substring(imageUri.getLastPathSegment().length()-3);
        StorageReference storageRef= FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis()+fileExtension);
        StorageTask uploadTask=storageRef.putFile(imageUri);

        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()){
                    pd.dismiss();
                    builder.setTitle("Upload Error");
                    builder.setMessage("Could not upload your image!");
                }
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri result=task.getResult();
                downloadUrl=result.toString();

                HashMap<String,Object> menuData=new HashMap<>();
                menuData.put("photo",downloadUrl);
                menuData.put("name",foodName);
                menuData.put("price",foodPrice);

                String key=dbRef.push().getKey();

                dbRef.child(key).setValue(menuData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(FoodMenuActivity.this, "Your food was added successfully", Toast.LENGTH_SHORT).show();
                            clearInputs();

                        }else{
                            builder.setTitle("Error").setMessage("There was an error while adding your food item, try again later.");
                            builder.show();
                            imageUri=null;
                        }                    }
                });


            }
        });


    }

    private void clearInputs() {
        foodName.setText("");
        foodPrice.setText("");
        downloadUrl=null;
        closeAddForm.performClick();
        imgUpdate.setImageResource(R.drawable.img_holder);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            imgUpdate.setImageURI(imageUri);
        }
    }
}
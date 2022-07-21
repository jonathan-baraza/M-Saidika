package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.m_saidika.Constants.BUSINESS_SHORT_CODE;
import static com.example.m_saidika.Constants.CALLBACKURL;
import static com.example.m_saidika.Constants.PARTYB;
import static com.example.m_saidika.Constants.PASSKEY;
import static com.example.m_saidika.Constants.TRANSACTION_TYPE;

import com.example.m_saidika.Models.AccessToken;
import com.example.m_saidika.Models.Matatu;
import com.example.m_saidika.Models.MpesaResponseItem;
import com.example.m_saidika.Models.Profile;
import com.example.m_saidika.Models.STKPush;
import com.example.m_saidika.Models.STKPushFirstResponse;
import com.example.m_saidika.Services.DarajaApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity{
    public DarajaApiClient mApiClient;
    public ProgressDialog pd;
    public boolean isReady=false;

    public TextView name,price;
    public String amountToBePaid,matatuId,numberPlate,destination;
    public FirebaseUser fUser;

    public Button btnPay;
    public Profile userProfile;

    public MpesaResponseItem mpesaRespone;

    public AlertDialog.Builder builder;
    public String serviceId;
    public String paymentType;

    public int totalNumOfPassengers;

    public boolean updatedList=false;
    public boolean insertedOrder=false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        fUser= FirebaseAuth.getInstance().getCurrentUser();
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        btnPay=findViewById(R.id.btnPay);

        Intent intent=getIntent();
        name.setText(intent.getStringExtra("name"));
        amountToBePaid=intent.getStringExtra("amountToBePaid");
        serviceId=intent.getStringExtra("serviceId");
        paymentType=intent.getStringExtra("type");
        price.setText(intent.getStringExtra("price"));

       if(paymentType.equals("Transport")){
            matatuId=intent.getStringExtra("matatuId");
            numberPlate=intent.getStringExtra("numberPlate");
        }
        if(paymentType.equals("foodOrder")){
            destination=intent.getStringExtra("destination");
        }

        ButterKnife.bind(this);
        pd = new ProgressDialog(this);
        pd.setIcon(R.drawable.ic_baseline_hourglass_top_24);
        pd.setCancelable(false);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        builder=new AlertDialog.Builder(PaymentActivity.this);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAccessToken();
            }
        });

        getUserProfileDetails();

    }

    private void getUserProfileDetails() {
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile=snapshot.getValue(Profile.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAccessToken() {
        pd.setMessage("Processing your request");
        pd.setTitle("Please Wait...");
        pd.setIndeterminate(true);
        pd.show();
        mApiClient.setGetAccessToken(true);
//        mApiClient.setAuthToken("5urAAbvs3NlyQpmdzz9Ui7tSYGTM");
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body().accessToken);
                    mApiClient.setAuthToken(response.body().accessToken);
                    mApiClient.setAuthToken("84AXr8Ur3altbvhNtuPQOk2vlN4j");
                    isReady=true;
                    performSTKPush(userProfile.getPhone(),amountToBePaid);
                }else{
                    System.out.println("Access token ERRRRROOORR INNNNN");
                    System.out.println(response);
                    Toast.makeText(PaymentActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                System.out.println("Access token Errorrr");
                System.out.println(t);
            }
        });
    }

    public void performSTKPush(String phone_number,String amount) {

        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "Paying for "+name.getText().toString(), //Account reference
                "M-saidika"  //Transaction description
        );

        mApiClient.setGetAccessToken(false);

        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPushFirstResponse>() {
            @Override
            public void onResponse(@NonNull Call<STKPushFirstResponse> call, @NonNull Response<STKPushFirstResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());
                        pd.setMessage("Waiting for your action...");
                        new CountDownTimer(20000,1000) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                fetchMpesaResponseFromDB(response.body().checkoutRequestID);
                            }
                        }.start();
                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPushFirstResponse> call, @NonNull Throwable t) {
                pd.dismiss();
                Timber.e(t);
            }
        });
    }

    private void fetchMpesaResponseFromDB(String checkoutRequestID) {
        FirebaseDatabase.getInstance().getReference().child("Payments").child(checkoutRequestID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mpesaRespone=snapshot.getValue(MpesaResponseItem.class);
                handleUserFeedback(checkoutRequestID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleUserFeedback(String checkoutRequestID) {
//        if(mpesaRespone==null || mpesaRespone.getResultCode()==null){
//            new CountDownTimer(3000,1000){
//                @Override
//                public void onTick(long l) {
//
//                }
//
//                @Override
//                public void onFinish() {
//                    fetchMpesaResponseFromDB(checkoutRequestID);
//                }
//            }.start();
//
//        }else{

            if(mpesaRespone.resultCode.equals("0")){
                if(paymentType.equals("foodOrder")){
                    pd.setMessage("Payment was successfull, now placing your order...");
                    //Recording the order after successfull payment
                    DateFormat df=new SimpleDateFormat("h:mm a EEE, MMM d, yyyy");
                    String time=df.format(Calendar.getInstance().getTime());
                    DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Food").child(serviceId).child("Orders");
                    String key=dbRef.push().getKey();
                    HashMap<String,Object> orderDetails=new HashMap<>();
                    orderDetails.put("paymentId",checkoutRequestID);
                    orderDetails.put("orderId",key);
                    orderDetails.put("userId",fUser.getUid());
                    orderDetails.put("name",name.getText().toString());
                    orderDetails.put("price",amountToBePaid);
                    orderDetails.put("time", time);
                    orderDetails.put("status", "pending");
                    orderDetails.put("destination", destination);
                    dbRef.child(key).setValue(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                            if(task.isSuccessful()){
                                builder.setIcon(R.drawable.ic_baseline_done_24_success);
                                builder.setTitle("Payment Successfull");
                                builder.setMessage("Your payment was successfully done and order is placed.");
                                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(PaymentActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PaymentActivity.this,FoodActivity.class));
                                        finish();
                                    }
                                });
                                builder.create().show();
                            }else{
                                Toast.makeText(PaymentActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

                }else if(paymentType.equals("Transport")){
                    pd.setMessage("Payment was successfull, now booking your transport...");
                    //Recording the order after successfull payment

                        DateFormat df=new SimpleDateFormat("h:mm a EEE, MMM d, yyyy");
                        String time=df.format(Calendar.getInstance().getTime());
                        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("Passengers").child(matatuId);

                        HashMap<String,Object> passengerDetails=new HashMap<>();
                        passengerDetails.put("paymentId",checkoutRequestID);
                        passengerDetails.put("userId",fUser.getUid());
                        passengerDetails.put("time",time);

                        dbRef.child(fUser.getUid()).setValue(passengerDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    builder.setIcon(R.drawable.ic_baseline_done_24_success);
                                    builder.setTitle("Payment Successfull");
                                    builder.setMessage("Your payment was successfully done and recorded.");
                                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            Toast.makeText(PaymentActivity.this, "Matatu booked successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(PaymentActivity.this,TransportActivity.class));
                                            finish();
                                        }
                                    });
                                    builder.create().show();

                                }else{
                                    Toast.makeText(PaymentActivity.this, "Failed to book matatu", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(PaymentActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                        });

            }else{
                    finish();
                }
            }
            else{
                //Means some error occured, either cancellation, invalid entry or timeout reached.
                builder.setIcon(R.drawable.ic_baseline_cancel_danger);
                builder.setTitle("Payment Failed");
                builder.setMessage(mpesaRespone.getResultDesc());
                builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.create().show();
            }
//        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

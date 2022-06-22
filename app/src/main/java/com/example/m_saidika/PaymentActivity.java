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
import com.example.m_saidika.Models.MpesaResponseItem;
import com.example.m_saidika.Models.Profile;
import com.example.m_saidika.Models.STKPush;
import com.example.m_saidika.Models.STKPushFirstResponse;
import com.example.m_saidika.Services.DarajaApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentActivity extends AppCompatActivity{
    private DarajaApiClient mApiClient;
    private ProgressDialog pd;
    public boolean isReady=false;

    public TextView name,price;
    private String amountToBePaid;
    private FirebaseUser fUser;

    public Button btnPay;
    public Profile userProfile;

    private MpesaResponseItem mpesaRespone;

    public AlertDialog.Builder builder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        fUser= FirebaseAuth.getInstance().getCurrentUser();
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        btnPay=findViewById(R.id.btnPay);

        Intent intent=getIntent();
        name.setText(intent.getStringExtra("name"));
        price.setText(intent.getStringExtra("price"));
        amountToBePaid=intent.getStringExtra("amountToBePaid");

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
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                    isReady=true;
                    performSTKPush(userProfile.getPhone(),amountToBePaid);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    public void performSTKPush(String phone_number,String amount) {
        pd.setMessage("Processing your request");
        pd.setTitle("Please Wait...");
        pd.setIndeterminate(true);
        pd.show();
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
                        new CountDownTimer(4000,1000) {
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
        if(mpesaRespone==null || mpesaRespone.getResultCode()==null){
            new CountDownTimer(3000,1000){
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    fetchMpesaResponseFromDB(checkoutRequestID);
                }
            }.start();

        }else{
            pd.dismiss();
            if(mpesaRespone.resultCode.equals("0")){
                //Means payment was successfull
                builder.setIcon(R.drawable.ic_baseline_done_24_success);
                builder.setTitle("Payment Successfull");
                builder.setMessage("Your payment was successfully done and recorded.");
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PaymentActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.create().show();
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
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

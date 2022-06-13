package com.example.m_saidika;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import butterknife.ButterKnife;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import retrofit2.Call;
//import timber.log.Timber;
//
//import static com.example.m_saidika.Constants.BUSINESS_SHORT_CODE;
//import static com.example.m_saidika.Constants.CALLBACKURL;
//import static com.example.m_saidika.Constants.PARTYB;
//import static com.example.m_saidika.Constants.PASSKEY;
//import static com.example.m_saidika.Constants.TRANSACTION_TYPE;
//
//import com.example.m_saidika.Models.AccessToken;
//import com.example.m_saidika.Models.STKPush;
//import com.example.m_saidika.Services.DarajaApiClient;
//
//import java.io.IOException;
//
//
//public class PaymentActivity extends AppCompatActivity {
//    public TextView name,price;
//    public Button btnPay;
//
//    //MPESA
//    private DarajaApiClient mApiClient;
//    private ProgressDialog pd;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment);
//
//        name = findViewById(R.id.name);
//        price = findViewById(R.id.price);
//        btnPay = findViewById(R.id.btnPay);
//
//        Intent intent = getIntent();
//        name.setText(intent.getStringExtra("name"));
//        price.setText("Pay ksh " + intent.getStringExtra("price"));
//
//        pd = new ProgressDialog(PaymentActivity.this);
//        pd.setTitle("Processing your payment");
//        pd.setMessage("Please wait...");
//        pd.create();
//
//        ButterKnife.bind(PaymentActivity.this);
//
//        mApiClient = new DarajaApiClient();
//        mApiClient.setIsDebug(true);
//
//
//        btnPay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getAccessToken();
//            }
//        });
//        getAccessToken();
//    }
//
//        public void getAccessToken() {
//            mApiClient.setGetAccessToken(true);
//            mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
//                @Override
//                public void onFailure(okhttp3.Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                    if(response.isSuccessful()){
//                        mApiClient.setAuthToken(response.body().accessToken);
//                    }
//                }
//            });
//        }
//
//
//        public void performSTKPush(String phone_number,String amount) {
//            pd.setMessage("Processing your request");
//            pd.setTitle("Please Wait...");
//            pd.setIndeterminate(true);
//            pd.show();
//            String timestamp = Utils.getTimestamp();
//            STKPush stkPush = new STKPush(
//                    BUSINESS_SHORT_CODE,
//                    Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
//                    timestamp,
//                    TRANSACTION_TYPE,
//                    String.valueOf(amount),
//                    Utils.sanitizePhoneNumber(phone_number),
//                    PARTYB,
//                    Utils.sanitizePhoneNumber(phone_number),
//                    CALLBACKURL,
//                    "MPESA Android Test", //Account reference
//                    "Testing"  //Transaction description
//            );
//
//            mApiClient.setGetAccessToken(false);
//
//            //Sending the data to the Mpesa API, remember to remove the logging when in production.
//            mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
//                @Override
//                public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
//                    pd.dismiss();
//                    try {
//                        if (response.isSuccessful()) {
//                            Timber.d("post submitted to API. %s", response.body());
//                        } else {
//                            Timber.e("Response %s", response.errorBody().string());
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
//                    pd.dismiss();
//                    Timber.e(t);
//                }
//            });
//        }
//
//        @Override
//        public void onPointerCaptureChanged(boolean hasCapture) {
//
//        }
//
//    }





import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import butterknife.BindView;
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
import com.example.m_saidika.Models.STKPush;
import com.example.m_saidika.Services.DarajaApiClient;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;
    public boolean isReady=false;



    @BindView(R.id.btnPay)
    Button mPay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(this);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        mPay.setOnClickListener(this);

        getAccessToken();
    }

    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                    isReady=true;
                    performSTKPush("254704783187","1");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view== mPay){
           getAccessToken();
        }
    }

    public void performSTKPush(String phone_number,String amount) {
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
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
                "MPESA Android Test", //Account reference
                "Testing"  //Transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());
                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

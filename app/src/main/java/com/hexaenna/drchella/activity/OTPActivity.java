package com.hexaenna.drchella.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.api.SendSMSApiClient;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.hexaenna.drchella.service.SMSBroadCastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmit;
    ApiInterface apiInterface;
    String isConnection = null;
    RelativeLayout rldMainOtp;
    TSnackbar snackbar;
    View snackbarView;
    NetworkChangeReceiver networkChangeReceiver;
    ProgressBar progressBar;
    EditText edtOTP;
    LinearLayout ldtResendOTP;
    String mobileNumber;
    String userName = "urchospitals",fromWhere;
    String password = "admin123";
    String senderId = "URCmed";
    String message = "";

    UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);


                if (isConnection == null) {
                    Bundle b = intent.getExtras();
                    if (b.getString(Constants.MESSAGE)!= null) {
                        isConnection = b.getString(Constants.MESSAGE);
                        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {


                                getNetworkState();
                                Log.e("check splash","check");

                        }
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        this.registerReceiver(networkChangeReceiver,
                intentFilter);



        final DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        if (databaseHandler.getContact("0").equals("English"))
        {
            setContentView(R.layout.otp_activity);

        }else if (databaseHandler.getContact("0").equals("Tamil"))
        {
            setContentView(R.layout.tamil_otp_activity);

        }

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        edtOTP = (EditText) findViewById(R.id.edtOTP);

        progressBar = (ProgressBar) findViewById(R.id.progressOtp);
        progressBar.setVisibility(View.GONE);
        rldMainOtp = (RelativeLayout) findViewById(R.id.rldMainOtp);

        ldtResendOTP = (LinearLayout) findViewById(R.id.ldtResendOTP);
        ldtResendOTP.setOnClickListener(this);


        mobileNumber =userRegisterDetails.getMobileNum();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle.getString("fromWhere") != null) {
            fromWhere = bundle.getString("fromWhere");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSubmit:
                if (!edtOTP.getText().toString().isEmpty())
                    sendOtp();
                break;

            case R.id.ldtResendOTP:
                resendOtp();
                break;
        }
    }

    private void resendOtp() {

        progressBar.setVisibility(View.VISIBLE);
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("mobile",mobileNumber);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<RegisterRequestAndResponse> call = apiInterface.reSendOTP(jsonObject);
            call.enqueue(new Callback<RegisterRequestAndResponse>() {
                @Override
                public void onResponse(Call<RegisterRequestAndResponse> call, Response<RegisterRequestAndResponse> response) {
                    if (response.isSuccessful()) {
                        RegisterRequestAndResponse requestAndResponse = response.body();
                        if (requestAndResponse.getStatus_message() != null)
                        {
                            progressBar.setVisibility(View.GONE);
                            if (fromWhere.equals(Constants.status_code1))
                            {
                                Toast.makeText(getApplicationContext(),requestAndResponse.getStatus_message(),Toast.LENGTH_LONG).show();

                            }else if (fromWhere.equals(Constants.status_code_1))
                            {
                                UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                                userRegisterDetails.setOtp(requestAndResponse.getVerify_code());
                                sendSms();
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<RegisterRequestAndResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);

                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(rldMainOtp, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Action Button", "onClick triggered");

                        }
                    });
            snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
            snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            textView.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }
    }


    private void sendOtp() {

        progressBar.setVisibility(View.VISIBLE);
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("verify_code","Dr - "+edtOTP.getText().toString());
                jsonObject.put("mobile",mobileNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<RegisterRequestAndResponse> call = apiInterface.verify_OTP(jsonObject);
            call.enqueue(new Callback<RegisterRequestAndResponse>() {
                @Override
                public void onResponse(Call<RegisterRequestAndResponse> call, Response<RegisterRequestAndResponse> response) {
                    if (response.isSuccessful()) {
                        RegisterRequestAndResponse requestAndResponse = response.body();
                        if (requestAndResponse.getStatus_code() != null)
                        {
                            if (requestAndResponse.getStatus_code().equals(Constants.status_code1))
                            {
                                Intent  intent = new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                                OTPActivity.this.finish();
                            }else if (requestAndResponse.getStatus_code().equals(Constants.status_code0))
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Invalid OTP!",Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<RegisterRequestAndResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);

                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(rldMainOtp, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Action Button", "onClick triggered");

                        }
                    });
            snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
            snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            textView.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }
    }


    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {
                snackbar = TSnackbar
                        .make(rldMainOtp, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Action Button", "onClick triggered");

                            }
                        });
                snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
                snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                textView.setTypeface(null, Typeface.BOLD);
                snackbar.show();


            } else if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                if (fromWhere.equals(Constants.status_code1)) {
                    sendSms();
                }else if (fromWhere.equals(Constants.status_code_1))
                {
                    resendOtp();
                }
            }
        }
    }


    public  void sendSms()
    {
        progressBar.setVisibility(View.VISIBLE);
        UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
        message = userRegisterDetails.getOtp() +" is your Dr.Chella App Verification Code.";
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = SendSMSApiClient.getClient().create(ApiInterface.class);

            Call<String> call = apiInterface.sendMessage(userName,password,senderId,message,userRegisterDetails.getMobileNum(),userRegisterDetails.getUniqueId());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);

                        Log.e("response", response.body());

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Failed",  t.getMessage());



                }


            });

        }else
        {
            snackbar = TSnackbar
                    .make(rldMainOtp, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Action Button", "onClick triggered");

                        }
                    });
            snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
            snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            textView.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }
    }

    private SMSBroadCastReceiver receiver = new SMSBroadCastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                String message = intent.getStringExtra("message");
                String[] splitedMsg = message.split(" ");
                message = splitedMsg[0];
               /* splitedMsg = message.split("-");
                message = splitedMsg[1];*/
                edtOTP.setText(splitedMsg[2]);
                sendOtp();
                Log.e("message from otp",message);

            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (networkChangeReceiver == null)
        {
            Log.e("reg","Do not unregister receiver as it was never registered");
        }
        else
        {
            Log.e("reg","Unregister receiver");
            unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
    }
}

package com.hexaenna.drchella.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.utils.NetworkChangeReceiver;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);


                Bundle b = intent.getExtras();
                isConnection = b.getString(Constants.MESSAGE);
                Log.e("newmesage", "" + isConnection);
                getNetworkState();
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


        Bundle bundle = this.getIntent().getExtras();
        if (bundle.getString("mobileNumber") != null) {
            mobileNumber = bundle.getString("mobileNumber");
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
                            Toast.makeText(getApplicationContext(),requestAndResponse.getStatus_message(),Toast.LENGTH_LONG).show();
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
                jsonObject.put("verify_code",edtOTP.getText().toString());
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
                                Intent  intent = new Intent(getApplicationContext(),BookAppointmentActivity.class);
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
            }
        }
    }
}

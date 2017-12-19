package com.hexaenna.drchella.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.MoreItemsActivity;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.api.SendSMSApiClient;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.hexaenna.drchella.service.SMSBroadCastReceiver;
import com.hexaenna.drchella.utils.Config;
import com.hexaenna.drchella.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment  implements MoreItemsActivity.OnBackPressedListener {

    Switch txtSwithOn;
    View rootView;
    TextView txtOn,txtUpdatePhoneNumber;
    DatabaseHandler databaseHandler;
    ApiInterface apiInterface;
    String isConnection = null;
    Button btnOk,btnSend;
    NetworkChangeReceiver networkChangeReceiver;
    EditText edtUpdatedNumber,edtE_mail_otp,edtEMobileotp;
    String userName = "urchospitals";
    String password = "admin123";
    String senderId = "URCmed";
    String message = "";
    LinearLayout ldtchaneNumber,ldtchaneOtp;
    ProgressBar progress;
    TextInputLayout txtInputMobileNumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);

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




                        }
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        getActivity().registerReceiver(networkChangeReceiver,
                intentFilter);
        databaseHandler = new DatabaseHandler(getActivity());
        txtSwithOn = (Switch) rootView.findViewById(R.id.txtSwithOn);
        txtOn = (TextView) rootView.findViewById(R.id.txtOn);
        txtUpdatePhoneNumber = (TextView) rootView.findViewById(R.id.txtUpdatePhoneNumber);
        txtUpdatePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        txtSwithOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((Switch) v).isChecked();
                if(on)
                {
                    //Do something when switch is on/checked
                    txtOn.setText("On");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Config.TOPIC_GLOBAL);
                    databaseHandler.updateNotify("0");
                    Log.e("sub","subscribeToTopic");
                }
                else
                {
                    //Do something when switch is off/unchecked
                    txtOn.setText("Off");
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    databaseHandler.updateNotify("1");
                    Log.e("sub","unsubscribeToTopic");
                }
            }
        });

        if (!databaseHandler.checkTableForNotify())
            getDataBaseConnect();
        String notify = databaseHandler.getNotify("0");
        //0 means off,1 means on
        if (notify.equals("1"))
        {
            txtSwithOn.setChecked(false);
            txtOn.setText("Off");
        }else if (notify.equals("0"))
        {
            txtSwithOn.setChecked(true);
            txtOn.setText("On");
        }
        Log.e("notify",notify);
        ((MoreItemsActivity) getActivity()).setOnBackPressedListener(this);
        return rootView;
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.updated_number);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(R.color.white)));
        btnOk = (Button) dialog.findViewById(R.id.btnOk);
        edtUpdatedNumber = (EditText) dialog.findViewById(R.id.edtUpdatedNumber);
        ldtchaneOtp = (LinearLayout) dialog.findViewById(R.id.ldtchaneOtp);
        ldtchaneOtp.setVisibility(View.VISIBLE);
        ldtchaneNumber = (LinearLayout) dialog.findViewById(R.id.ldtchaneNumber);
        ldtchaneNumber.setVisibility(View.GONE);
        edtE_mail_otp = (EditText) dialog.findViewById(R.id.edtE_mail_otp);
        edtEMobileotp = (EditText) dialog.findViewById(R.id.edtEMobileotp);
        progress = (ProgressBar) dialog.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        btnSend = (Button) dialog.findViewById(R.id.btnSend);
        txtInputMobileNumber = (TextInputLayout) dialog.findViewById(R.id.txtInputMobileNumber);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        isMobileValidate();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtUpdatedNumber.getText().toString().equals("")) {
                    progress.setVisibility(View.VISIBLE);
                    submitOTP(dialog);
                }

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                sendOtp();
            }
        });
        dialog.show();
    }


    public boolean isMobileValidate()
    {
        final boolean[] isMblNum = {false};
        edtUpdatedNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() < 10)
                {
                    txtInputMobileNumber.setErrorEnabled(true);
                    txtInputMobileNumber.setError("Enter your correct mobile number");
                    isMblNum[0] = false;

                }else
                {
                    isMblNum[0] = true;
                    txtInputMobileNumber.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(edtUpdatedNumber.getText().toString().isEmpty())
        {


            isMblNum[0] = false;

        }else
        {
            isMblNum[0] = true;

        }
        return isMblNum[0];
    }

    private void getDataBaseConnect() {

        databaseHandler.addNotify("1");
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();

    }



    public  void sendSms()
    {

        UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
        message = userRegisterDetails.getOtp() +" is your conformation code.don't share with others";
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = SendSMSApiClient.getClient().create(ApiInterface.class);

            Call<String> call = apiInterface.sendMessage(userName,password,senderId,message,edtUpdatedNumber.getText().toString(),"123");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {

                        Log.e("response", response.body());
                        String code= response.body();
                        String[] codeSplit = code.split(":");
                        codeSplit = codeSplit[1].split(" ");
                        if (codeSplit[1].equals("000")) {
                            ldtchaneOtp.setVisibility(View.GONE);
                            ldtchaneNumber.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Failed",  t.getMessage());



                }


            });

        }
    }

    public void submitOTP(final Dialog dialog)
    {
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();
            try {
                UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                jsonObject.put("email", userRegisterDetails.getE_mail());
                jsonObject.put("phone_code", "Dr - "+edtEMobileotp.getText().toString());
                jsonObject.put("email_code", "Dr - "+edtE_mail_otp.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<RegisterRequestAndResponse> call = apiInterface.submitOTP(jsonObject);
            call.enqueue(new Callback<RegisterRequestAndResponse>() {
                @Override
                public void onResponse(Call<RegisterRequestAndResponse> call, Response<RegisterRequestAndResponse> response) {
                    if (response.isSuccessful()) {
                        RegisterRequestAndResponse requestAndResponse = response.body();
                        if (requestAndResponse.getStatus_code().equals(Constants.status_code1)) {
                            databaseHandler.updateMobileNumber(edtUpdatedNumber.getText().toString());

                        }else
                        {
                            Toast.makeText(getActivity(),requestAndResponse.getStatus_message(),Toast.LENGTH_LONG).show();
                        }

                        progress.setVisibility(View.GONE);
                        dialog.cancel();

                    }
                }

                @Override
                public void onFailure(Call<RegisterRequestAndResponse> call, Throwable t) {


                }
            });

        }

    }
    private void sendOtp() {

        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();
            try {
                UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                jsonObject.put("email", userRegisterDetails.getE_mail());
                jsonObject.put("updt_phone", edtUpdatedNumber.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<RegisterRequestAndResponse> call = apiInterface.update_PhoneNumber(jsonObject);
            call.enqueue(new Callback<RegisterRequestAndResponse>() {
                @Override
                public void onResponse(Call<RegisterRequestAndResponse> call, Response<RegisterRequestAndResponse> response) {
                    if (response.isSuccessful()) {
                        RegisterRequestAndResponse requestAndResponse = response.body();
                        if (requestAndResponse.getStatus_code() != null) {
                            if (requestAndResponse.getStatus_code().equals(Constants.status_code0))
                            {
                                progress.setVisibility(View.GONE);
                                Toast.makeText(getActivity(),requestAndResponse.getStatus_message(),Toast.LENGTH_LONG).show();

                            }else if (requestAndResponse.getStatus_code().equals(Constants.status_code1)) {
                                UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                                userRegisterDetails.setOtp(requestAndResponse.getVerify_code());
                                sendSms();
                            }

                        }

                    }
                }

                @Override
                public void onFailure(Call<RegisterRequestAndResponse> call, Throwable t) {


                }
            });

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

//                sendOtp();
                Log.e("message from otp",message);

            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }
}

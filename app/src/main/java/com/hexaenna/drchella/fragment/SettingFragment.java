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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    Button btnOk;
    NetworkChangeReceiver networkChangeReceiver;
    EditText edtUpdatedNumber;
    String userName = "urchospitals";
    String password = "admin123";
    String senderId = "URCmed";
    String message = "";
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
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp();
            }
        });
        dialog.show();
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
        message = userRegisterDetails.getOtp() +" is your .";
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = SendSMSApiClient.getClient().create(ApiInterface.class);

            Call<String> call = apiInterface.sendMessage(userName,password,senderId,message,edtUpdatedNumber.getText().toString(),"123");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {


                        Log.e("response", response.body());

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Failed",  t.getMessage());



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
}

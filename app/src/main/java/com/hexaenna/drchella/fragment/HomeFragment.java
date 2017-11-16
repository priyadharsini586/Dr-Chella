package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.hexaenna.drchella.Model.AppointmentDetails;
import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.Model.TimeAndDateResponse;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.BookAppointmentActivity;
import com.hexaenna.drchella.activity.OTPActivity;
import com.hexaenna.drchella.activity.RegistrationActivity;
import com.hexaenna.drchella.activity.ViewAppointmentActivity;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.utils.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 11/10/2017.
 */

public class HomeFragment extends Fragment {

    View rootView;
    String isConnection = null;
    LinearLayout rldMainLayout,ldtNoAppoint,ldtAppointment;
    TSnackbar snackbar;
    View snackbarView;
    NetworkChangeReceiver networkChangeReceiver;
    ApiInterface apiInterface;
    TextView txtBookAppointment,txtRemaingDays,txtHospitalName,txtAddress,txtTime;
    Button btnView;
    ImageView imgScedule;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

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
        getActivity().registerReceiver(networkChangeReceiver,
                intentFilter);

        rldMainLayout = (LinearLayout) rootView.findViewById(R.id.ldtMainview);
        txtBookAppointment = (TextView) rootView.findViewById(R.id.txtBookAppointment);
        txtRemaingDays = (TextView) rootView.findViewById(R.id.txtRemaingDays);
        txtHospitalName = (TextView) rootView.findViewById(R.id.txtHospitalName);
        txtAddress = (TextView) rootView.findViewById(R.id.txtAddress);
        txtTime = (TextView) rootView.findViewById(R.id.txtTime);

        ldtNoAppoint = (LinearLayout) rootView.findViewById(R.id.ldtNoAppoint);
        ldtNoAppoint.setVisibility(View.GONE);
        ldtAppointment = (LinearLayout) rootView.findViewById(R.id.ldtAppointment);
        ldtAppointment.setVisibility(View.GONE);

        btnView = (Button) rootView.findViewById(R.id.btnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewAppointmentActivity.class);
                startActivity(intent);
            }
        });
        imgScedule = (ImageView) rootView.findViewById(R.id.imgScedule);
        imgScedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BookAppointmentActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
        return rootView;
    }



    private void registerDetails() {


        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();
            Calendar cal = Calendar.getInstance();
            final DateFormat[] dateForRequest = {new SimpleDateFormat("dd.MM.yyyy")};
            String formattedDate = dateForRequest[0].format(cal.getTime());
            try {
                UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                jsonObject.put("email",userRegisterDetails.getE_mail());
                jsonObject.put("cur_date",formattedDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<TimeAndDateResponse> call = apiInterface.recent_appintment(jsonObject);
            call.enqueue(new Callback<TimeAndDateResponse>() {
                @Override
                public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e("successfull","success");
                        TimeAndDateResponse timeAndDateResponse = response.body();
                        ldtNoAppoint.setVisibility(View.GONE);
                        ldtAppointment.setVisibility(View.GONE);
                        if (timeAndDateResponse.getStatus_code() != null) {
                            if (timeAndDateResponse.getStatus_code().equals(Constants.status_code1)) {
                                ldtAppointment.setVisibility(View.VISIBLE);
                                try {
                                    Date newDate= dateForRequest[0].parse(timeAndDateResponse.getDate());
                                    dateForRequest[0] = new SimpleDateFormat("dd MMM yyyy");
                                   String date = dateForRequest[0].format(newDate);
                                    txtTime.setText(date +" at "+ timeAndDateResponse.getTime());


                                    Calendar calCurr = Calendar.getInstance();
                                    Calendar day = Calendar.getInstance();
                                    day.setTime(new SimpleDateFormat("dd.MM.yyyy").parse(timeAndDateResponse.getDate()));
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                    String formattedDate = df.format(day.getTime());
                                    String formattedDate1 = df.format(calCurr.getTime());
                                    if(day.after(calCurr)){
                                        Log.e("date","Days Left: " + (day.get(Calendar.DAY_OF_MONTH) -(calCurr.get(Calendar.DAY_OF_MONTH))));
                                        txtRemaingDays.setText(day.get(Calendar.DAY_OF_MONTH) -(calCurr.get(Calendar.DAY_OF_MONTH)) + " " +getActivity().getResources().getString(R.string.remaining));

                                    }else if (formattedDate.equals(formattedDate1))
                                    {
                                        txtRemaingDays.setText("You Have an Appointment with doctor Today.");
                                    }


                                    Log.e("date","Days Left: " + formattedDate);
                                    Log.e("date","Days Left: " + formattedDate1);

                                    AppointmentDetails appointmentDetails = AppointmentDetails.getInstance();
                                    appointmentDetails.setCity(timeAndDateResponse.getCity_id());
                                    appointmentDetails.setDate(timeAndDateResponse.getDate());
                                    appointmentDetails.setTime(timeAndDateResponse.getTime());

                                    getAddress(timeAndDateResponse.getCity_id());

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }else if (timeAndDateResponse.getStatus_code().equals(Constants.status_code0))
                            {
                                ldtNoAppoint.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<TimeAndDateResponse> call, Throwable t) {
                    Log.e("output", t.getMessage());
                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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

    private void getAddress(String city) {

        if (city.equals("1"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.chennai_hospital));
            txtAddress.setText(getActivity().getString(R.string.chennai_hospital_address));
        }else if (city.equals("2"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.erode_hospital));
            txtAddress.setText(getActivity().getString(R.string.erode_hospital_address));
        }else if (city.equals("3"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.coimbatore_hospital));
            txtAddress.setText(getActivity().getString(R.string.coimbatore_hospital_address));
        }else if (city.equals("4"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.namakkal_hospital));
            txtAddress.setText(getActivity().getString(R.string.namakkal_hospital_address));
        }else if (city.equals("5"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.mayiladu_hospital));
            txtAddress.setText(getActivity().getString(R.string.may_hospital_address));
        }else if (city.equals("6"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.kollidam_hospital));
            txtAddress.setText(getActivity().getString(R.string.kollidam_hospital_address));
        }
    }

    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {
                snackbar = TSnackbar
                        .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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
                registerDetails();
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (networkChangeReceiver == null)
        {
            Log.e("reg","Do not unregister receiver as it was never registered");
        }
        else
        {
            Log.e("reg","Unregister receiver");
            getActivity().unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getNetworkState();

    }
}


/*
www.1message.com
        username:urchospitals
        password:admin123
*/


package com.hexaenna.drchella.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hexaenna.drchella.Model.AppointmentDetails;
import com.hexaenna.drchella.Model.BookingDetails;
import com.hexaenna.drchella.Model.TimeAndDateResponse;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.utils.Constants;

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

public class ViewAppointmentActivity extends AppCompatActivity {

    TextView txtName,txtDate,txtTime,txtHospital,txtHospitalName,txtConfirmationNumber,txtTransactionNumber,txtHospitalAddressName;
    ProgressBar proConfirm;
    ApiInterface apiInterface;
    ImageView btnBack,maps;
    String cityAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        txtName = (TextView) findViewById(R.id.txtName);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtHospital= (TextView) findViewById(R.id.txtHospital);
        txtHospitalName= (TextView) findViewById(R.id.txtHospitalName);
        txtConfirmationNumber= (TextView) findViewById(R.id.txtConfirmationNumber);
        txtTransactionNumber= (TextView) findViewById(R.id.txtTransactionNumber);
        txtHospitalAddressName = (TextView) findViewById(R.id.txtHospitalAddressName);

        proConfirm = (ProgressBar) findViewById(R.id.proConfirma);
        proConfirm.setVisibility(View.GONE);
        maps = (ImageView) findViewById(R.id.maps);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fromCity",cityAddress);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getPatientDetails();

    }

    private void getPatientDetails() {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        proConfirm.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();

        try {
            UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
            AppointmentDetails appointmentDetails = AppointmentDetails.getInstance();
            jsonObject.put("city",appointmentDetails.getCity());
            jsonObject.put("adate",appointmentDetails.getDate());
            jsonObject.put("atime",appointmentDetails.getTime());
            jsonObject.put("user_email_id",userRegisterDetails.getE_mail());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<AppointmentDetails> call = apiInterface.appointment_details(jsonObject);
        call.enqueue(new Callback<AppointmentDetails>() {
            @Override
            public void onResponse(Call<AppointmentDetails> call, Response<AppointmentDetails> response) {
                if (response.isSuccessful()) {
                    proConfirm.setVisibility(View.GONE);
                    AppointmentDetails appointmentDetails = response.body();

                    txtName.setText(appointmentDetails.getPatient_name());
                    txtTime.setText(appointmentDetails.getTime());
                    txtConfirmationNumber.setText("Conformation No : "+appointmentDetails.getConfm_no());
                    txtTransactionNumber.setText("Transaction Time : "+appointmentDetails.getBooking_time());
                    DateFormat dateForRequest = new SimpleDateFormat("dd.MM.yyyy");
                    Date newDate= null;
                    try {
                        newDate = dateForRequest.parse(appointmentDetails.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dateForRequest = new SimpleDateFormat("dd MMM yyyy");
                    String date = dateForRequest.format(newDate);
                    txtDate.setText(date);
                    cityAddress = appointmentDetails.getCity_id();
                    getAddress(appointmentDetails.getCity_id());
                }
            }

            @Override
            public void onFailure(Call<AppointmentDetails> call, Throwable t) {
                Log.e("output", t.getMessage());
            }
        });
    }


    private void getAddress(String city) {

        if (city.equals("1"))
        {
            txtHospital.setText(getString(R.string.chennai_hospital));
            txtHospitalAddressName.setText(R.string.chennai_hospital);
            txtHospitalName.setText(getString(R.string.chennai_hospital_address));
        }else if (city.equals("2"))
        {
            txtHospital.setText(getString(R.string.erode_hospital));
            txtHospitalAddressName.setText(R.string.erode_hospital);
            txtHospitalName.setText(getString(R.string.erode_hospital_address));
        }else if (city.equals("3"))
        {
            txtHospital.setText(getString(R.string.coimbatore_hospital));
            txtHospitalAddressName.setText(R.string.coimbatore_hospital);
            txtHospitalName.setText(getString(R.string.coimbatore_hospital_address));
        }else if (city.equals("4"))
        {
            txtHospital.setText(getString(R.string.namakkal_hospital));
            txtHospitalAddressName.setText(R.string.namakkal_hospital);
            txtHospitalName.setText(getString(R.string.namakkal_hospital_address));
        }else if (city.equals("5"))
        {
            txtHospital.setText(getString(R.string.mayiladu_hospital));
            txtHospitalAddressName.setText(R.string.mayiladu_hospital);
            txtHospitalName.setText(getString(R.string.may_hospital_address));
        }else if (city.equals("6"))
        {
            txtHospital.setText(getString(R.string.kollidam_hospital));
            txtHospitalAddressName.setText(R.string.kollidam_hospital);
            txtHospitalName.setText(getString(R.string.kollidam_hospital_address));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

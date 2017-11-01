package com.hexaenna.drchella.activity;

import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.fragment.DateAndTimeFragment;
import com.hexaenna.drchella.fragment.RegisterDetailsFragment;

public class BookAppointmentActivity extends AppCompatActivity {


    TextView ldtBookingDetails,ldtDateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_appointment_activity);

        ldtBookingDetails = (TextView) findViewById(R.id.ldtBookingDetails);
        ldtBookingDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new RegisterDetailsFragment(), "BOOKING_ FRAGMENT");
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new DateAndTimeFragment(), "DATE_AND_TIME_FRAGMENT");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();


        ldtDateTime = (TextView) findViewById(R.id.ldtDateTime);
        ldtDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new DateAndTimeFragment(), "DATE_AND_TIME_FRAGMENT");
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
            }
        });
    }
}

package com.hexaenna.drchella.activity;

import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.applandeo.materialcalendarview.CalendarView;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.fragment.DateAndTimeFragment;

public class BookAppointmentActivity extends AppCompatActivity {


    LinearLayout ldtCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_appointment_activity);

        ldtCity = (LinearLayout) findViewById(R.id.ldtCity);
        ldtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateAndTimeFragment dateAndTimeFragment = new DateAndTimeFragment();
                dateAndTimeFragment.showDailog(BookAppointmentActivity.this);

            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DateAndTimeFragment hello = new DateAndTimeFragment();
        fragmentTransaction.add(R.id.fragment_container, hello, "HELLO");
        fragmentTransaction.commit();
    }
}

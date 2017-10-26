package com.hexaenna.drchella.activity;

import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.applandeo.materialcalendarview.CalendarView;
import com.hexaenna.drchella.R;

public class BookAppointmentActivity extends AppCompatActivity {

    android.widget.CalendarView materialCalendarView;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_appointment_activity);

        materialCalendarView  = (android.widget.CalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setMinDate(System.currentTimeMillis() - 1000);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 3);
        materialCalendarView.setMaxDate(c.getTimeInMillis());

    }
}

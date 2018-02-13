package com.hexaenna.drchella.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hexaenna.drchella.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity {

    Calendar  c,cal;
    int currentDate,currentMonth,endMonth,endDate,currentYear;
    ImageView imgHisCal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgHisCal = (ImageView) findViewById(R.id.imgHisCal);
        imgHisCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFromCalendar();
            }
        });



    }
    private void openDialogFromCalendar() {

        final Dialog dialog = new Dialog(HistoryActivity.this);
        dialog.setContentView(R.layout.open_calendar_view_admin);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(R.color.white)));
        Button btnOk =(Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();


            }
        });
        MaterialCalendarView calendarView = (MaterialCalendarView) dialog.findViewById(R.id.calendarView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            cal = Calendar.getInstance(); //Get the Calendar instance
            cal.add(Calendar.YEAR, -10);//Three months from now
            cal.getTime();

            int thisYear = c.get(Calendar.YEAR);
            int thisMonth = c.get(Calendar.MONTH);

            currentYear = thisYear;

            currentDate = c.get(Calendar.DATE);
            currentMonth = c.get(Calendar.MONTH);

            int end_year = cal.get(Calendar.YEAR);
            int end_month = cal.get(Calendar.MONTH);

            endDate = cal.get(Calendar.DATE);
            endMonth = cal.get(Calendar.MONTH);


            calendarView.state().edit()
                    .setMinimumDate(CalendarDay.from(end_year, end_month, 1))
                    .setMaximumDate(CalendarDay.from(thisYear, thisMonth, 31))
                    .commit();

        } else {
            java.util.Calendar c = java.util.Calendar.getInstance();
            java.util.Calendar cal = java.util.Calendar.getInstance();
               /* c = Calendar.getInstance();
                cal = Calendar.getInstance();*/ //Get the Calendar instance
            cal.add(Calendar.YEAR, -10);//Three months from now
            cal.getTime();

            int thisYear = c.get(java.util.Calendar.YEAR);
            int thisMonth = c.get(java.util.Calendar.MONTH);
            currentYear = thisYear;

            currentDate = c.get(java.util.Calendar.DATE);
            currentMonth = c.get(java.util.Calendar.MONTH);

            int end_year = cal.get(java.util.Calendar.YEAR);
            int end_month = cal.get(java.util.Calendar.MONTH);

            endDate = cal.get(java.util.Calendar.DATE);
            endMonth = cal.get(java.util.Calendar.MONTH);


            calendarView.state().edit()
                    .setMinimumDate(CalendarDay.from(end_year, end_month, 1))
                    .setMaximumDate(CalendarDay.from(thisYear, thisMonth, 31))
                    .commit();

        }
        calendarView.addDecorator(new PriviousDayEnableDecorator(getApplicationContext(), currentDate, currentMonth, endDate, endMonth,currentYear));
//        calendarView.addDecorators(new  SelectedDayDecorator(30, R.color.red_not_avaliable));
//        calendarView.setOnDateChangedListener(this);
        dialog.show();
    }



    private static class PriviousDayEnableDecorator implements DayViewDecorator {

        int currentDate;
        int currentMonth;
        int lastDate;
        int lastMonth,currentYear;
        Context context;
        public PriviousDayEnableDecorator(Context context,int currentDate,int currentMonth,int lastDate,int lastMonth,int currentYear)
        {
            this.currentDate = currentDate;
            this.currentMonth = currentMonth;
            this.lastDate = lastDate;
            this.lastMonth = lastMonth;
            this.currentYear = currentYear;
            this.context = context;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {

            int date = day.getDay();
            int curYear = day.getYear();

                if (currentMonth == day.getMonth())
                    return date < currentDate;
                else if (lastMonth == day.getMonth()) {
                    return date > lastDate;
                } else {
                    return false;
                }


        }


        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
         /*   if (view.areDaysDisabled())
                view.setSelectionDrawable(ContextCompat.getDrawable(context,R.drawable.calendar_selected_date));*/

        }

    }
}

package com.hexaenna.drchella.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.OTPActivity;
import com.hexaenna.drchella.adapter.CityAdapter;
import com.hexaenna.drchella.adapter.GenderSpinnerAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import fr.ganfra.materialspinner.MaterialSpinner;


public class DateAndTimeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    ListView citySpinner;
    CityAdapter citySpinnerAdapter ;
    ArrayList<String> cityList;
    View mainView;
    MaterialCalendarView calendarView;
    Calendar cal,c;
    Button btnOk;
    int currentDate,currentMonth,endMonth,endDate;
    Dialog dialog;
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mainView = inflater.inflate(R.layout.date_and_time_fragment, container, false);


        calendarView = (MaterialCalendarView) mainView.findViewById(R.id.calendarView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            cal = Calendar.getInstance(); //Get the Calendar instance
            cal.add(Calendar.MONTH,3);//Three months from now
            cal.getTime();

            int thisYear = c.get(Calendar.YEAR);
            int thisMonth = c.get(Calendar.MONTH);

            currentDate = c.get(Calendar.DATE);
            currentMonth = c.get(Calendar.MONTH);

            int end_year = cal.get(Calendar.YEAR);
            int end_month = cal.get(Calendar.MONTH);

            endDate = cal.get(Calendar.DATE);
            endMonth = cal.get(Calendar.MONTH);


            calendarView.state().edit()
                    .setMinimumDate(CalendarDay.from(thisYear,thisMonth,1))
                    .setMaximumDate(CalendarDay.from(end_year,end_month,31))
                    .commit();

        }else
        {
            c = Calendar.getInstance();
            cal = Calendar.getInstance(); //Get the Calendar instance
            cal.add(Calendar.MONTH,3);//Three months from now
            cal.getTime();

            int thisYear = c.get(Calendar.YEAR);
            int thisMonth = c.get(Calendar.MONTH);

            int end_year = cal.get(Calendar.YEAR);
            int end_month = cal.get(Calendar.MONTH);
            int end_day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);


            calendarView.state().edit()
                    .setMinimumDate(CalendarDay.from(thisYear,thisMonth,1))
                    .setMaximumDate(CalendarDay.from(end_year,end_month,end_day))
                    .commit();

        }
        calendarView.addDecorator(new PriviousDayDisableDecorator(getActivity(),currentDate,currentMonth,endDate,endMonth));


        Calendar cal1 = Calendar.getInstance();
        cal1.set(2017, 11, 1);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2017, 11, 10);

        HashSet<CalendarDay> setDays = getCalendarDaysSet(cal1, cal2);
        int myColor = R.color.colorAccent;
        calendarView.addDecorator(new BookingDecorator(myColor, setDays));


        showDailog(getActivity());

        return mainView;
    }

    private HashSet<CalendarDay> getCalendarDaysSet(Calendar cal1, Calendar cal2) {
        HashSet<CalendarDay> setDays = new HashSet<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            while (cal1.getTime().before(cal2.getTime())) {
                CalendarDay calDay = CalendarDay.from(cal1);
                setDays.add(calDay);
                cal1.add(Calendar.DATE, 1);
            }
        }

        return setDays;
    }

    public void showDailog(Context context) {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.city_items);

        // Custom Android Allert Dialog Title
        cityList = new ArrayList<>();
        cityList.add("Chennai");
        cityList.add("Erode");
        cityList.add("Coimbatore");
        cityList.add("Namakkal");
        cityList.add("Mayiladuthurai");
        cityList.add("Kollidam");
        citySpinnerAdapter = new CityAdapter(context,cityList);
        citySpinner = (ListView) dialog.findViewById(R.id.ldtList);
        citySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) citySpinner.getItemAtPosition(position);
                Toast.makeText(getActivity(),item,Toast.LENGTH_LONG).show();
            }
        });
        citySpinner.setAdapter(citySpinnerAdapter);

        btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private static class PriviousDayDisableDecorator implements DayViewDecorator {

        int currentDate;
        int currentMonth;
        int lastDate;
        int lastMonth;
        Context context;
        public PriviousDayDisableDecorator(Context context,int currentDate,int currentMonth,int lastDate,int lastMonth)
        {
            this.currentDate = currentDate;
            this.currentMonth = currentMonth;
            this.lastDate = lastDate;
            this.lastMonth = lastMonth;
            this.context = context;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {

            int date = day.getDay();
            if (currentMonth == day.getMonth())
               return date <= currentDate;
            else if (lastMonth == day.getMonth())
            {
                return date >= lastDate;
            }else {
                return false;
            }

        }


        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
//            view.setSelectionDrawable(ContextCompat.getDrawable(context,R.drawable.calendar_selected_date));

        }


    }

    private class BookingDecorator implements DayViewDecorator {
        private int mColor;
        private HashSet<CalendarDay> mCalendarDayCollection;

        public BookingDecorator(int color, HashSet<CalendarDay> calendarDayCollection) {
            mColor = color;
            mCalendarDayCollection = calendarDayCollection;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return mCalendarDayCollection.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(mColor));
            //view.addSpan(new BackgroundColorSpan(Color.BLUE));
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.calendar_selected_date));

        }
    }

}

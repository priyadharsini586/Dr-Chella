package com.hexaenna.drchella.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

            int end_year = cal.get(Calendar.YEAR);
            int end_month = cal.get(Calendar.MONTH);

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
        calendarView.addDecorator(new PriviousDayDisableDecorator());
        showDailog(getActivity());

        return mainView;
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

        boolean[] disableState;
        @Override
        public boolean shouldDecorate(CalendarDay day) {

            return disableState[day.getDay()];
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }




    }
}

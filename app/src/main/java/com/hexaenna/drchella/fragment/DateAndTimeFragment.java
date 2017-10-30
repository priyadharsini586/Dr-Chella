package com.hexaenna.drchella.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.OTPActivity;
import com.hexaenna.drchella.adapter.CityAdapter;
import com.hexaenna.drchella.adapter.GenderSpinnerAdapter;
import com.hexaenna.drchella.adapter.TimeSlotAdapter;
import com.hexaenna.drchella.custom_view.ExpandableHeightGridView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
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
    Toolbar mToolbar;
    private static final DateFormat FORMATTER = java.text.SimpleDateFormat.getDateInstance();
    //time slot
    ArrayList<String> timeSlotList;
    ExpandableHeightGridView gridView;
    Context mainContext;
    LinearLayout ldtCity;
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


            mainView = inflater.inflate(R.layout.date_and_time_fragment, container, false);

         mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        ldtCity = (LinearLayout) mToolbar.findViewById(R.id.ldtCity);
        ldtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog(getActivity());
            }
        });


        calendarView = (MaterialCalendarView) mainView.findViewById(R.id.calendarView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                c = Calendar.getInstance();
                cal = Calendar.getInstance(); //Get the Calendar instance
                cal.add(Calendar.MONTH, 3);//Three months from now
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
                        .setMinimumDate(CalendarDay.from(thisYear, thisMonth, 1))
                        .setMaximumDate(CalendarDay.from(end_year, end_month, 31))
                        .commit();

            } else {
                c = Calendar.getInstance();
                cal = Calendar.getInstance(); //Get the Calendar instance
                cal.add(Calendar.MONTH, 3);//Three months from now
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
                        .setMinimumDate(CalendarDay.from(thisYear, thisMonth, 1))
                        .setMaximumDate(CalendarDay.from(end_year, end_month, 31))
                        .commit();

            }
            calendarView.addDecorator(new PriviousDayDisableDecorator(getActivity(), currentDate, currentMonth, endDate, endMonth));
//        calendarView.addDecorators(new  SelectedDayDecorator(30, R.color.colorAccent));
          calendarView.setVisibility(View.GONE);

            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                    Log.e("check", getSelectedDatesString());
                    if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                        Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                        gridView.setVisibility(View.GONE);
                    }else
                    {
                        gridView.setExpanded(true);
                        gridView.setAdapter(new TimeSlotAdapter(getActivity(), getErodeTimeSlotList()));
                        gridView.setVisibility(View.VISIBLE);
                    }
                }
            });
        gridView = (ExpandableHeightGridView) mainView.findViewById(R.id.gridTime);

            showDailog(getActivity());

        return mainView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    private String getSelectedDatesString() {
        CalendarDay date = calendarView.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }


    public void showDailog(final Context context) {

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
        final String[] item = new String[1];
        citySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 item[0] = (String) cityList.get(position);
                Toast.makeText(context, item[0],Toast.LENGTH_LONG).show();

            }
        });
        citySpinner.setAdapter(citySpinnerAdapter);

        btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                checkList(item[0]);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = (ExpandableHeightGridView) view.findViewById(R.id.gridTime);

    }

    @Override
    public void onAttach(Context context) {
        this.mainContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void checkList(String city) {

        if (city.equals("Erode"))
        {
//            gridView = (ExpandableHeightGridView) mainView.findViewById(R.id.gridTime);
            /*if (gridView != null) {
                gridView.setExpanded(true);
                gridView.setAdapter(new TimeSlotAdapter(getActivity(), getErodeTimeSlotList()));
                gridView.setVisibility(View.VISIBLE);
            }else
            {
            }*/
            calendarView.setVisibility(View.VISIBLE);
            calendarView.addDecorator(new DayViewDecorator() {
                @Override
                public boolean shouldDecorate(CalendarDay day) {

                    if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                        if (currentMonth == day.getMonth()) {
                            if (currentDate <= day.getDay()) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return true;

                        }
                    } else {
                        Log.e("day ///", String.valueOf(day.getMonth()));
                        return false;
                    }


                }

                @Override
                public void decorate(DayViewFacade view) {
                    // add red foreground span
                    view.setDaysDisabled(false);
                    view.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)));
                }
            });
        }else if (city.equals("Chennai")){

            getChennaiCalendar();
        }else
        {
            calendarView.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
        }
    }

    private void getChennaiCalendar() {
        calendarView.setVisibility(View.VISIBLE);
        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {

                if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY ) {

                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.set(java.util.Calendar.DAY_OF_WEEK,3);
                    day.copyTo(calendar);
                    if (day.getDay() <= 7 )
                    {
                        return true;
                    }else
                    {
                    return false;}
                }else
                {
                    return false;
                }


            }

            @Override
            public void decorate(DayViewFacade view) {
                // add red foreground span
                view.setDaysDisabled(false);
                view.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)));
            }
        });
    }

    @Override
        public void onResume() {
        super.onResume();
        Log.e("activity","fragment resume");
    }

    @Override
    public void onPause() {
        Log.e("activity","fragment pause");
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.e("activity","fragment attach");
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        Log.e("activity","fragment dettach");
        super.onDetach();
    }


    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        Log.e("activity","fragment inflate");
        super.onInflate(context, attrs, savedInstanceState);
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
         /*   if (view.areDaysDisabled())
                view.setSelectionDrawable(ContextCompat.getDrawable(context,R.drawable.calendar_selected_date));*/

        }

    }


    public ArrayList<String> getErodeTimeSlotList()
    {
        timeSlotList = new ArrayList<>();
        timeSlotList.add("9.00AM");
        timeSlotList.add("9.20AM");
        timeSlotList.add("9.40AM");
        timeSlotList.add("10.00AM");
        timeSlotList.add("10.20AM");
        timeSlotList.add("10.40AM");
        timeSlotList.add("11.00AM");
        timeSlotList.add("11.20AM");
        timeSlotList.add("11.40AM");
        timeSlotList.add("12.00PM");
        timeSlotList.add("12.20PM");
        timeSlotList.add("12.40PM");
        timeSlotList.add("1.00PM");
        timeSlotList.add("Lunch");
        timeSlotList.add("1.40PM");
        timeSlotList.add("2.00PM");
        timeSlotList.add("2.20PM");
        timeSlotList.add("2.40PM");
        timeSlotList.add("3.00PM");
        timeSlotList.add("3.20PM");
        timeSlotList.add("3.40PM");
        timeSlotList.add("4.00PM");
        timeSlotList.add("4.20PM");
        timeSlotList.add("4.40PM");
        timeSlotList.add("5.00PM");
        return timeSlotList;
    }

}

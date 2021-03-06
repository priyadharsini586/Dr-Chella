package com.hexaenna.drchella.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.BookingDetails;
import com.hexaenna.drchella.Model.TimeAndDateResponse;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.BookAppointmentActivity;
import com.hexaenna.drchella.activity.HomeActivity;
import com.hexaenna.drchella.adapter.CityAdapter;
import com.hexaenna.drchella.adapter.TimeSlotAdapter;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.custom_view.ExpandableHeightGridView;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DateAndTimeFragment extends Fragment implements View.OnClickListener, BookAppointmentActivity.OnBackPressedListener {
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
    //time slot
    ArrayList<String> timeSlotList;
    ExpandableHeightGridView gridView;
    Context mainContext;
    LinearLayout ldtCity;
    final String[] item = new String[1];
    int currentPosition = 0;
    CoimbatoreDisableDcorator coimbatoreDisableDcorator;
    MayiladuthuraiDisableDcorator mayiladuthuraiDisableDcorator;
    ErodeDisableDcorator erodeDisableDcorator;
    ChennaiDisableDcorator chennaiDisableDcorator ;
    TextView txtCity,txttimer;
    ApiInterface apiInterface;
    String isConnection = null,selectCity = null,selectDate = null,selectTime = null;
    ScrollView sclRegisterMain;
    TSnackbar snackbar;
    View snackbarView;
    NetworkChangeReceiver networkChangeReceiver;
    ProgressBar progressCalendar;
    LinearLayout ldtNext;
    ScrollView scltimeMain;
    BookingDetails bookingDetails = BookingDetails.getInstance();
    private int previousSelectedPosition = -1;
    ArrayList<String> bookedListArray,blockedListArray;
    boolean doubleBackToExitPressedOnce = false;
    public static List<CalendarDay> unAvaliableList = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

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

        final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        if (databaseHandler.getContact("0").equals("English"))
        {
            mainView = inflater.inflate(R.layout.date_and_time_fragment, container, false);
            BookAppointmentActivity.txtToolbarText.setText(getActivity().getResources().getText(R.string.booking_details));

        }else if (databaseHandler.getContact("0").equals("Tamil"))
        {
            mainView = inflater.inflate(R.layout.tamil_date_and_time_fragment, container, false);
            BookAppointmentActivity.txtToolbarText.setText(getActivity().getResources().getText(R.string.tamil_booking_details));

        }



         mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);


        scltimeMain = (ScrollView) mainView.findViewById(R.id.scltimeMain);
        ldtCity = (LinearLayout) mToolbar.findViewById(R.id.ldtCity);
        txttimer = (TextView) mToolbar.findViewById(R.id.txttimer);
        txttimer.setVisibility(View.GONE);
        txtCity = (TextView) mToolbar.findViewById(R.id.txtCity);
        ldtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog(getActivity());
            }
        });
        ldtCity.setVisibility(View.VISIBLE);


        progressCalendar = (ProgressBar) mainView.findViewById(R.id.progressCalendar);
        progressCalendar.setVisibility(View.GONE);

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
                java.util.Calendar c = java.util.Calendar.getInstance();
                java.util.Calendar cal = java.util.Calendar.getInstance();
               /* c = Calendar.getInstance();
                cal = Calendar.getInstance();*/ //Get the Calendar instance
                cal.add(java.util.Calendar.MONTH, 3);//Three months from now
                cal.getTime();

                int thisYear = c.get(java.util.Calendar.YEAR);
                int thisMonth = c.get(java.util.Calendar.MONTH);

                currentDate = c.get(java.util.Calendar.DATE);
                currentMonth = c.get(java.util.Calendar.MONTH);

                int end_year = cal.get(java.util.Calendar.YEAR);
                int end_month = cal.get(java.util.Calendar.MONTH);

                endDate = cal.get(java.util.Calendar.DATE);
                endMonth = cal.get(java.util.Calendar.MONTH);


                calendarView.state().edit()
                        .setMinimumDate(CalendarDay.from(thisYear, thisMonth, 1))
                        .setMaximumDate(CalendarDay.from(end_year, end_month, 31))
                        .commit();

            }
            calendarView.addDecorator(new PriviousDayDisableDecorator(getActivity(), currentDate, currentMonth, endDate, endMonth));
//        calendarView.addDecorators(new  SelectedDayDecorator(30, R.color.red_not_avaliable));
          calendarView.setVisibility(View.GONE);

            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                    int week = 0;

                    Date date5 = date.getDate();
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    DateFormat dateForRequest = new SimpleDateFormat("dd.MM.yyyy");
                    String reportDate = df.format(date5);
                    String dateRequest = dateForRequest.format(date5);
                    Log.e("date", String.valueOf(dateRequest));
                    Date date1 = null;

                    try {
                        date1 = df.parse(reportDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date1);
                        week = cal.get(Calendar.WEEK_OF_MONTH);
                        Log.e("week date",String.valueOf(week));


                    }else
                    {
                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        cal.setTime(date1);
                        week = cal.get(java.util.Calendar.WEEK_OF_MONTH);
                        Log.e("week date",String.valueOf(week));
                    }
                    selectDate = dateRequest;
                    if (unAvaliableList.contains(date))
                    {
                        Log.e("contains","true");
                        gridView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Doctor not avaiable in this Date", Toast.LENGTH_SHORT).show();

                    }else {
                        Log.e("contains", "false");

                        if (item[0].equals("Erode") || item[0].equals("ஈரோடு")) {
                            if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY || date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY) {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                                gridView.setVisibility(View.GONE);
                            } else {

                                getTimingList(selectCity, dateRequest);

                            }
                        } else if (item[0].equals("Chennai") || item[0].equals("சென்னை")) {
                            CalendarDay selectedDate = widget.getSelectedDate();
                      /*  Date date5 = selectedDate.getDate();
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        String reportDate = df.format(date5);
                        Log.e("date", String.valueOf(reportDate));
                        Date date1 = null;
                        try {
                            date1 = df.parse(reportDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date1);
                        int week = cal.get(Calendar.WEEK_OF_MONTH);*/

                            if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) != java.util.Calendar.SUNDAY) {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            } else {
                                if (selectedDate.getDay() <= 7 || selectedDate.getDay() < 22 && selectedDate.getDay() > 14) {
                                    getTimingList(selectCity, dateRequest);
                                } else {
                                    Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else if (item[0].equals("Coimbatore") || item[0].equals("கோயம்புத்தூர்")) {
                            if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                                if (week == 2 || week == 4) {
                                    getTimingList(selectCity, dateRequest);
                                } else {
                                    Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            }
                        } else if (item[0].equals("Namakkal") || item[0].equals("நாமக்கல்")) {
                            if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                                if (week == 2 || week == 4) {
                                    getTimingList(selectCity, dateRequest);
                                } else {
                                    Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            }
                        } else if (item[0].equals("Mayiladuthurai") || item[0].equals("மயிலாடுதுறை")) {
                            if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                                if (week == 1 || week == 3) {
                                    getTimingList(selectCity, dateRequest);
                                } else {
                                    Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            }
                        } else if (item[0].equals("Kollidam") || item[0].equals("கொள்ளிடம்")) {
                            if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                                if (week == 1 || week == 3) {
                                    getTimingList(selectCity, dateRequest);
                                } else {
                                    Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        gridView = (ExpandableHeightGridView) mainView.findViewById(R.id.gridTime);

        gridView.setOnItemClickListener(myOnItemClickListener);

        ldtNext = (LinearLayout) mainView.findViewById(R.id.ldtNext);
        ldtNext.setOnClickListener(this);
        ldtNext.setEnabled(false);

        if (bookingDetails.getCity() == null) {
            showDailog(getActivity());
        }else
        {
            item[0] = bookingDetails.getCity();
            checkList(bookingDetails.getCity());
            selectCity = bookingDetails.getSelectedCity();
            selectDate = bookingDetails.getSelectedDate();
            bookedListArray = bookingDetails.getBookedList();
            blockedListArray = bookingDetails.getBlockedList();
            currentPosition = bookingDetails.getSelectedPosition();

            Date date = null;
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            try {
                date = df.parse(selectDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendarView.setSelectedDate(date);

            gridView.setExpanded(true);
            if (selectCity.equals("1")) {
                gridView.setAdapter(new TimeSlotAdapter(getActivity(), getChennaiTimeSlotList(), blockedListArray, bookedListArray));
            }else if (selectCity.equals("2"))
            {
                gridView.setAdapter(new TimeSlotAdapter(getActivity(), getErodeTimeSlotList(), blockedListArray, bookedListArray));
            }else if (selectCity.equals("3"))
            {
                gridView.setAdapter(new TimeSlotAdapter(getActivity(), getCoimbatoreTimeSlotList(), blockedListArray, bookedListArray));
            }else if (selectCity.equals("4"))
            {
                gridView.setAdapter(new TimeSlotAdapter(getActivity(), getNamakkalTimeSlotList(), blockedListArray, bookedListArray));
            }else if (selectCity.equals("5"))
            {
                gridView.setAdapter(new TimeSlotAdapter(getActivity(), getMayiladuthuraiTimeSlotList(), blockedListArray, bookedListArray));
            }else if (selectCity.equals("6"))
            {
                gridView.setAdapter(new TimeSlotAdapter(getActivity(), getKollidamTimeSlotList(), blockedListArray, bookedListArray));
            }
            gridView.setVisibility(View.VISIBLE);


        }
        ((BookAppointmentActivity) getActivity()).setOnBackPressedListener(this);
        return mainView;
    }

    AdapterView.OnItemClickListener myOnItemClickListener
            = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            String time = null;
            if (selectCity.equals("1")) {
                time = getChennaiTimeSlotList().get(position);
            }else if (selectCity.equals("2"))
            {
                time = getErodeTimeSlotList().get(position);
            }else if (selectCity.equals("3"))
            {
               time = getCoimbatoreTimeSlotList().get(position);
            }else if (selectCity.equals("4"))
            {
                time = getNamakkalTimeSlotList().get(position);
            }else if (selectCity.equals("5"))
            {
                time = getMayiladuthuraiTimeSlotList().get(position);
            }else if (selectCity.equals("6"))
            {
                time = getKollidamTimeSlotList().get(position);
            }

                currentPosition = position;
                if (!time.equals("Lunch")) {
                    selectTime = time;

                    if (blockedListArray != null) {
                        if (blockedListArray.contains(time)) {
                            Log.e("found", "Account found");

                        } else {
                            Log.e("found", "Account not found");
                            checkAvaliableOrNot(selectCity, selectDate, time, view, position);
                        }
//
                    }else
                    {
                        checkAvaliableOrNot(selectCity, selectDate, time, view, position);
                    }
                }


        }};


    private void checkAvaliableOrNot(String city, String date, String time, final View view, final int pos) {


        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("city",city);
                jsonObject.put("adate",date);
                jsonObject.put("atime",time);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressCalendar.setVisibility(View.VISIBLE);
            Call<TimeAndDateResponse> call = apiInterface.isBlocked(jsonObject);
            call.enqueue(new Callback<TimeAndDateResponse>() {
                @Override
                public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAndDateResponse timeAndDateResponse = response.body();

                        if (timeAndDateResponse.getStatus_code() != null) {
                            if (timeAndDateResponse.getStatus_code().equals(Constants.status_code1)) {
                                Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                                changeGridLayout(view,pos);
                                scltimeMain.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        scltimeMain.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                }, 100L);

                            } else if (timeAndDateResponse.getStatus_code().equals(Constants.status_code_1))
                            {
                                if (timeAndDateResponse.getStatus_message() != null)
                                    Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                            } else if (timeAndDateResponse.getStatus_code().equals(Constants.status_code0))
                            {
                                if (timeAndDateResponse.getStatus_message() != null)
                                    Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                            }
                            ldtNext.setEnabled(true);
                            progressCalendar.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<TimeAndDateResponse> call, Throwable t) {

                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(scltimeMain, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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

    private void changeGridLayout(View view,int position) {

        LinearLayout previousSelectedView = (LinearLayout) gridView.getChildAt(previousSelectedPosition);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ldtBackground);
        TextView textView = (TextView) view.findViewById(R.id.txtTimeSlot);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            linearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.selected_time_slot_bg));
            textView.setTextColor(getActivity().getResources().getColor(R.color.black));
        }else
        {
            linearLayout.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.selected_time_slot_bg));
            textView.setTextColor(getActivity().getResources().getColor(R.color.black));
        }

        if (previousSelectedPosition != position) {
            if (previousSelectedPosition != -1) {

                previousSelectedView.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.time_slot_bg));
                LinearLayout b = (LinearLayout) previousSelectedView.getChildAt(0);
                Log.e("count", String.valueOf(b.getChildCount()));
                TextView changePrevious = (TextView) b.getChildAt(0);
                changePrevious.setTextColor(Color.parseColor("#F28F20"));

            }
        }

        if (bookingDetails.getSelectedPosition() != -1)
        {
            LinearLayout selectedView = (LinearLayout) gridView.getChildAt(bookingDetails.getSelectedPosition());
            selectedView.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.time_slot_bg));
            LinearLayout b = (LinearLayout) selectedView.getChildAt(0);
            Log.e("count", String.valueOf(b.getChildCount()));
            TextView changePrevious = (TextView) b.getChildAt(0);
            changePrevious.setTextColor(Color.parseColor("#F28F20"));

            bookingDetails.setSelectedPosition(-1);
        }
        previousSelectedPosition = position;

    }

    private void getTimingList(final String city, final String date) {
        gridView.setVisibility(View.GONE);

        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("city",city);
                jsonObject.put("adate",date);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressCalendar.setVisibility(View.VISIBLE);
            Call<TimeAndDateResponse> call = apiInterface.check_time(jsonObject);
            call.enqueue(new Callback<TimeAndDateResponse>() {
                @Override
                public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAndDateResponse timeAndDateResponse = response.body();
                        if (timeAndDateResponse.getStatus_code() != null) {
                            if (timeAndDateResponse.getStatus_code().equals(Constants.status_code1)) {
                                ArrayList<String> bookList = timeAndDateResponse.getBooked_Array();
                                ArrayList<String> blockedList;
                                if (timeAndDateResponse.getBlocked_Array() != null) {
                                    blockedList = timeAndDateResponse.getBlocked_Array();
                                }else
                                {
                                    blockedList = new ArrayList<String>();
                                }
                                ArrayList<String> gridList = new ArrayList<String>();

                                if (city.equals("1"))
                                {

                                    gridList = getChennaiTimeSlotList();

                                }else if (city.equals("2"))
                                {
                                    gridList = getErodeTimeSlotList();

                                }else if (city.equals("3"))
                                {

                                    gridList = getCoimbatoreTimeSlotList();

                                }else if (city.equals("4"))
                                {

                                    gridList = getNamakkalTimeSlotList();

                                }else if (city.equals("5"))
                                {

                                    gridList = getMayiladuthuraiTimeSlotList();

                                }else if (city.equals("6"))
                                {
                                    gridList = getKollidamTimeSlotList();
                                }


                                ArrayList<String> modifyErodeList = new ArrayList<String>();

                                java.util.Calendar calendar = java.util.Calendar.getInstance();
                                DateFormat dateForRequest = new SimpleDateFormat("dd.MM.yyyy");
                                String formattedDate1 = dateForRequest.format(calendar.getTime());

                                if (date.equals(formattedDate1)) {
                                    for (int i = 0; i < gridList.size(); i++) {
                                        if (!gridList.get(i).equals("Lunch")) {
                                            String compare = compareTime(gridList.get(i));
                                            if (compare.equals("1")) {
                                                blockedList.add(gridList.get(i));
                                                modifyErodeList.add(gridList.get(i));
                                            }
                                        }
                                    }
                                    for (int i=0 ; i < gridList.size(); i++)
                                    {

                                    }
                                }
                                blockedListArray = new ArrayList<String>();
                                blockedListArray = blockedList;
                                bookedListArray = new ArrayList<String>();
                                bookedListArray = timeAndDateResponse.getBooked_Array();

                                gridView.setExpanded(true);
                                gridView.setAdapter(new TimeSlotAdapter(getActivity(), gridList, blockedList, bookList));

                                /*if (city.equals("1")) {
                                    gridView.setAdapter(new TimeSlotAdapter(getActivity(), getChennaiTimeSlotList(), blockedList, bookList));
                                }else if (city.equals("2"))
                                {
                                    gridView.setAdapter(new TimeSlotAdapter(getActivity(), erodeList, blockedList, bookList));
                                }else if (city.equals("3"))
                                {
                                    gridView.setAdapter(new TimeSlotAdapter(getActivity(), getCoimbatoreTimeSlotList(), blockedList, bookList));
                                }else if (city.equals("4"))
                                {
                                    gridView.setAdapter(new TimeSlotAdapter(getActivity(), getNamakkalTimeSlotList(), blockedList, bookList));
                                }else if (city.equals("5"))
                                {
                                    gridView.setAdapter(new TimeSlotAdapter(getActivity(), getMayiladuthuraiTimeSlotList(), blockedList, bookList));
                                }else if (city.equals("6"))
                                {
                                    gridView.setAdapter(new TimeSlotAdapter(getActivity(), getKollidamTimeSlotList(), blockedList, bookList));
                                }*/
                                gridView.setVisibility(View.VISIBLE);

                            } else {
                                if (timeAndDateResponse.getStatus_message() != null)
                                    Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressCalendar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<TimeAndDateResponse> call, Throwable t) {

                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(scltimeMain, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

   /* private String getSelectedDatesString() {
        CalendarDay date = calendarView.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }*/


    public void showDailog(final Context context) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.city_items);
        TextView txtSelectLanguage = (TextView) dialog.findViewById(R.id.txtSelectLanguage);
        // Custom Android Allert Dialog Title

        btnOk = (Button) dialog.findViewById(R.id.btnOk);
        final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        if (databaseHandler.getContact("0").equals("English"))
        {
            cityList = new ArrayList<>();
            cityList.add("Chennai");
            cityList.add("Erode");
            cityList.add("Coimbatore");
            cityList.add("Namakkal");
            cityList.add("Mayiladuthurai");
            cityList.add("Kollidam");
            btnOk.setText("OK");
            txtSelectLanguage.setText(getActivity().getResources().getText(R.string.select_city));


        }else if (databaseHandler.getContact("0").equals("Tamil"))
        {

            cityList = new ArrayList<>();
            cityList.add("சென்னை");
            cityList.add("ஈரோடு");
            cityList.add("கோயம்புத்தூர்");
            cityList.add("நாமக்கல்");
            cityList.add("மயிலாடுதுறை");
            cityList.add("கொள்ளிடம்");
            btnOk.setText("சரி");
            txtSelectLanguage.setText(getActivity().getResources().getText(R.string.tamil_select_city));

        }
        citySpinnerAdapter = new CityAdapter(context,cityList);
        citySpinner = (ListView) dialog.findViewById(R.id.ldtList);

        citySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 item[0] = (String) cityList.get(position);
//                Toast.makeText(context, item[0],Toast.LENGTH_LONG).show();

                txtCity.setText(item[0]);
                selectCity = String.valueOf(position + 1);

            }
        });
        citySpinner.setAdapter(citySpinnerAdapter);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item[0] != null) {
                    gridView.setVisibility(View.GONE);
                    dialog.dismiss();
                    checkList(item[0]);
                    checkAvailable();
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    dialog.dismiss();
                    getActivity().finish();
                }
                return true;
            }
        });
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


        if (city.equals("Erode") || city.equals("ஈரோடு"))
        {
            calendarView.setVisibility(View.VISIBLE);
            if (coimbatoreDisableDcorator != null)
                calendarView.removeDecorator(coimbatoreDisableDcorator);
            if (chennaiDisableDcorator != null)
                calendarView.removeDecorator(chennaiDisableDcorator);
            if (mayiladuthuraiDisableDcorator != null)
                calendarView.removeDecorator(mayiladuthuraiDisableDcorator);
            erodeDisableDcorator = new ErodeDisableDcorator(getActivity(), currentDate, currentMonth, endDate, endMonth);
            calendarView.addDecorator(erodeDisableDcorator);
        }else if (city.equals("Chennai") || city.equals("சென்னை")){

            calendarView.setVisibility(View.VISIBLE);
            if (coimbatoreDisableDcorator != null)
                calendarView.removeDecorator(coimbatoreDisableDcorator);
            if (erodeDisableDcorator != null)
                calendarView.removeDecorator(erodeDisableDcorator);
            if (mayiladuthuraiDisableDcorator != null)
                calendarView.removeDecorator(mayiladuthuraiDisableDcorator);
            chennaiDisableDcorator = new ChennaiDisableDcorator(getActivity(), currentDate, currentMonth, endDate, endMonth);
            calendarView.addDecorator(chennaiDisableDcorator);
        }else if (city.equals("Coimbatore") || city.equals("கோயம்புத்தூர்"))
        {
            calendarView.setVisibility(View.VISIBLE);
            if (erodeDisableDcorator != null)
                calendarView.removeDecorator(erodeDisableDcorator);
            if (chennaiDisableDcorator != null)
                calendarView.removeDecorator(chennaiDisableDcorator);
            if (mayiladuthuraiDisableDcorator != null)
                calendarView.removeDecorator(mayiladuthuraiDisableDcorator);
            coimbatoreDisableDcorator = new CoimbatoreDisableDcorator(getActivity(), currentDate, currentMonth, endDate, endMonth);
            calendarView.addDecorator(coimbatoreDisableDcorator);
        }else if (city.equals("Namakkal") || city.equals("நாமக்கல்"))
        {
            calendarView.setVisibility(View.VISIBLE);
            if (erodeDisableDcorator != null)
                calendarView.removeDecorator(erodeDisableDcorator);
            if (chennaiDisableDcorator != null)
                calendarView.removeDecorator(chennaiDisableDcorator);
            if (mayiladuthuraiDisableDcorator != null)
                calendarView.removeDecorator(mayiladuthuraiDisableDcorator);
            coimbatoreDisableDcorator = new CoimbatoreDisableDcorator(getActivity(), currentDate, currentMonth, endDate, endMonth);
            calendarView.addDecorator(coimbatoreDisableDcorator);
        }else if (city.equals("Mayiladuthurai")|| item[0].equals("மயிலாடுதுறை"))
        {
            calendarView.setVisibility(View.VISIBLE);
            if (erodeDisableDcorator != null)
                calendarView.removeDecorator(erodeDisableDcorator);
            if (chennaiDisableDcorator != null)
                calendarView.removeDecorator(chennaiDisableDcorator);
            if (coimbatoreDisableDcorator != null) {
                calendarView.removeDecorator(coimbatoreDisableDcorator);
            }
            mayiladuthuraiDisableDcorator = new MayiladuthuraiDisableDcorator(getActivity(), currentDate, currentMonth, endDate, endMonth);
            calendarView.addDecorator(mayiladuthuraiDisableDcorator);
        }else if (city.equals("Kollidam")|| item[0].equals("கொள்ளிடம்"))
        {
            calendarView.setVisibility(View.VISIBLE);
            if (erodeDisableDcorator != null)
                calendarView.removeDecorator(erodeDisableDcorator);
            if (chennaiDisableDcorator != null)
                calendarView.removeDecorator(chennaiDisableDcorator);
            if (coimbatoreDisableDcorator != null)
                calendarView.removeDecorator(coimbatoreDisableDcorator);
            mayiladuthuraiDisableDcorator = new MayiladuthuraiDisableDcorator(getActivity(), currentDate, currentMonth, endDate, endMonth);
            calendarView.addDecorator(mayiladuthuraiDisableDcorator);
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

                if (currentMonth == day.getMonth())
                {
                    if (day.getDay() >= currentDate)
                    {
                        if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY ) {
                            if (day.getDay() <= 7)
                            {
                                return false;
                            }else  if (day.getDay() < 22 && day.getDay() > 14 )
                            {
                                return false;
                            }else
                            {
                                return true;
                            }

                        }else
                        {
                            return true;
                        }
                    }else
                        {return false;}
                }else if (endMonth == day.getMonth())
                {
                    if (day.getDay() >= endDate)
                    {
                        if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY ) {
                            if (day.getDay() <= 7)
                            {
                                return false;
                            }else  if (day.getDay() < 22 && day.getDay() > 14 )
                            {
                                return false;
                            }else
                            {
                                return true;
                            }

                        }else
                        {
                            return true;
                        }
                    }else {
                        return false;
                    }
                }else if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY ) {
                        if (day.getDay() <= 7)
                        {
                           return false;
                        }else  if (day.getDay() < 22 && day.getDay() > 14 )
                        {
                            return false;
                        }else
                        {
                            return true;
                        }

                }else
                {
                    return true;
                }

            }

            @Override
            public void decorate(DayViewFacade view) {
                // add red foreground span
                view.setDaysDisabled(false);
                view.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_not_avaliable)));
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ldtNext:
//                getNextFragment();
                sendRequest();
                break;
        }
    }

    private void sendRequest() {

        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("city",selectCity);
                jsonObject.put("adate",selectDate);
                jsonObject.put("atime",selectTime);
                jsonObject.put("user_email_id",getE_mail());
                if (bookingDetails.getAppSeno() != null)
                {
                    jsonObject.put("app_sno",bookingDetails.getAppSeno());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressCalendar.setVisibility(View.VISIBLE);
            Call<TimeAndDateResponse> call = apiInterface.block_appintment(jsonObject);
            call.enqueue(new Callback<TimeAndDateResponse>() {
                @Override
                public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAndDateResponse timeAndDateResponse = response.body();

                        if (timeAndDateResponse.getStatus_code() != null) {
                            if (timeAndDateResponse.getStatus_code().equals(Constants.status_code1)) {
                                Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                                bookingDetails.setAppSeno(timeAndDateResponse.getApp_sno());
                                getNextFragment();
                            } else if (timeAndDateResponse.getStatus_code().equals(Constants.status_code_1))
                            {
                                if (timeAndDateResponse.getStatus_message() != null)
                                    Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                            } else if (timeAndDateResponse.getStatus_code().equals(Constants.status_code0))
                            {
                                if (timeAndDateResponse.getStatus_message() != null)
                                    Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                            }
                            ldtNext.setEnabled(true);
                            progressCalendar.setVisibility(View.GONE);
                        }
                    }else
                    {

                        Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                        progressCalendar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<TimeAndDateResponse> call, Throwable t) {
                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(scltimeMain, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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


    public void checkAvailable()
    {
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            progressCalendar.setVisibility(View.VISIBLE);
            Call<TimeAndDateResponse> call = apiInterface.unAvaliableList();
            call.enqueue(new Callback<TimeAndDateResponse>() {
                @Override
                public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAndDateResponse timeAndDateResponse = response.body();

                        if (timeAndDateResponse.getStatus_code() != null) {

                            List<TimeAndDateResponse.UnAvailableList> unAvailableLists = timeAndDateResponse.getList();
                            List<CalendarDay> fromCalendarDayList = new ArrayList<>();
                            List<CalendarDay> toCalendarDayList = new ArrayList<>();
                            for (int i=0 ; i < unAvailableLists.size() ; i++)
                            {
                                TimeAndDateResponse.UnAvailableList unAvailableList = unAvailableLists.get(i);
                                String fromStrDate = unAvailableList.getFrom();
                                String toStrDate = unAvailableList.getTo();

                                Date fromDate = null,toDate = null;

                                try {
                                    fromDate=new SimpleDateFormat("dd.MM.yyyy").parse(fromStrDate);
                                    toDate=new SimpleDateFormat("dd.MM.yyyy").parse(toStrDate);
                                    CalendarDay  fromDay = CalendarDay.from(fromDate);
                                    CalendarDay  toDay = CalendarDay.from(toDate);

                                    calendarView.addDecorators(new DrUnavailableDaysDisable(getActivity(),fromDate,toDate));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }


                            progressCalendar.setVisibility(View.GONE);


                        }
                    }else
                    {

                        Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                        progressCalendar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<TimeAndDateResponse> call, Throwable t) {
                    Log.e("Failure", String.valueOf(t));

                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(scltimeMain, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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

    private void getNextFragment() {

        if (item[0] != null) {
            bookingDetails.setCity(item[0]);
            bookingDetails.setSelectedCity(selectCity);
            bookingDetails.setSelectedDate(selectDate);
            bookingDetails.setSelectedTime(selectTime);
            bookingDetails.setBlockedList(blockedListArray);
            bookingDetails.setBookedList(bookedListArray);
            bookingDetails.setSelectedPosition(currentPosition);
        }
        else {
            bookingDetails.setCity(bookingDetails.getCity());
            bookingDetails.setSelectedCity(selectCity);
            bookingDetails.setSelectedDate(selectDate);
            bookingDetails.setSelectedTime(selectTime);
            bookingDetails.setBlockedList(blockedListArray);
            bookingDetails.setBookedList(bookedListArray);
            bookingDetails.setSelectedPosition(currentPosition);

        }


        DateFormat df = new SimpleDateFormat("dd MMM yyyy");

        String str = "";
        DateFormat inputFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = inputFormat.parse(selectDate);
            str = df.format(date);
           Log.e("output",str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        String reportDate = df.format(selectDate);


        showAlert(getActivity(),"Alert","Do you want to confirm and fix this appointment on "+ str + " / "+selectTime + " at "+ item[0] +" ?");


       /* BookAppointmentActivity.ldtBookingDetails.setBackgroundColor(getActivity().getResources().getColor(R.color.book_title_orange));
        BookAppointmentActivity.txtBooking.setTextColor(getActivity().getResources().getColor(R.color.white));

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        fragmentTransaction.replace(R.id.fragment_container, new RegisterDetailsFragment(), "BOOKING_ FRAGMENT");
        fragmentTransaction.addToBackStack("BOOKING_ FRAGMENT");
        fragmentTransaction.commit();*/
    }

    @Override
    public void onBackPressed() {
        if (dialog.isShowing()) {
            BookingDetails bookingDetails = BookingDetails.getInstance();
            bookingDetails.setCity(null);
            bookingDetails.setSelectedCity(null);
            bookingDetails.setSelectedDate(null);
            bookingDetails.setBookedList(null);
            bookingDetails.setBlockedList(null);
            bookingDetails.setSelectedPosition(-1);
            bookingDetails.setAppSeno(null);
            if (RegisterDetailsFragment.countDownTimer != null)
                RegisterDetailsFragment.countDownTimer.cancel();
            getActivity().finish();
        }else
        {
            BookingDetails bookingDetails = BookingDetails.getInstance();
            bookingDetails.setCity(null);
            bookingDetails.setSelectedCity(null);
            bookingDetails.setSelectedDate(null);
            bookingDetails.setBookedList(null);
            bookingDetails.setBlockedList(null);
            bookingDetails.setSelectedPosition(-1);
            bookingDetails.setAppSeno(null);
            gridView.setVisibility(View.GONE);
            if (RegisterDetailsFragment.countDownTimer != null)
                RegisterDetailsFragment.countDownTimer.cancel();

            showDailog(getActivity());
        }

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
               return date < currentDate;
            else if (lastMonth == day.getMonth())
            {
                return date > lastDate;
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

    private static class DrUnavailableDaysDisable implements DayViewDecorator
    {
        private int color;
        private Date fromDates , toDates;
        Context mContext;

        public DrUnavailableDaysDisable(Context context,Date fromDate ,Date toDate)
        {
            this.color = color;
            this.fromDates = fromDate;
            this.toDates = toDate;
            this.mContext = context;
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {

            Date calDay = day.getDate();

            if (calDay.compareTo(fromDates) >= 0)
            {
                Log.e("true","true");
                if (calDay.compareTo(toDates) <= 0 ) {
                    unAvaliableList.add(day);
                    return true;
                }else
                {
                    return false;
                }
            }else
            {

                return false;
            }


        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(false);
            view.addSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red_not_avaliable)));
        }
    }


    public ArrayList<String> getErodeTimeSlotList()
    {
        timeSlotList = new ArrayList<>();
        timeSlotList.add("9:00 AM");
        timeSlotList.add("9:20 AM");
        timeSlotList.add("9:40 AM");
        timeSlotList.add("10:00 AM");
        timeSlotList.add("10:20 AM");
        timeSlotList.add("10:40 AM");
        timeSlotList.add("11:00 AM");
        timeSlotList.add("11:20 AM");
        timeSlotList.add("11:40 AM");
        timeSlotList.add("12:00 PM");
        timeSlotList.add("12:20 PM");
        timeSlotList.add("12:40 PM");
        timeSlotList.add("1:00 PM");
        timeSlotList.add("Lunch");
        timeSlotList.add("1:40 PM");
        timeSlotList.add("2:00 PM");
        timeSlotList.add("2:20 PM");
        timeSlotList.add("2:40 PM");
        timeSlotList.add("3:00 PM");
        timeSlotList.add("3:20 PM");
        timeSlotList.add("3:40 PM");
        timeSlotList.add("4:00 PM");
        timeSlotList.add("4:20 PM");
        timeSlotList.add("4:40 PM");
        timeSlotList.add("5:00 PM");
        return timeSlotList;
    }


    public ArrayList<String> getChennaiTimeSlotList()
    {
        timeSlotList = new ArrayList<>();
        timeSlotList.add("9:00 AM");
        timeSlotList.add("9:20 AM");
        timeSlotList.add("9:40 AM");
        timeSlotList.add("10:00 AM");
        timeSlotList.add("10:20 AM");
        timeSlotList.add("10:40 AM");
        timeSlotList.add("11:00 AM");
        timeSlotList.add("11:20 AM");
        timeSlotList.add("11:40 AM");
        timeSlotList.add("12:00 PM");
        timeSlotList.add("12:20 PM");
        timeSlotList.add("12:40 PM");
        timeSlotList.add("1:00 PM");
        timeSlotList.add("Lunch");
        timeSlotList.add("1:40 PM");
        timeSlotList.add("2:00 PM");
        timeSlotList.add("2:20 PM");
        timeSlotList.add("2:40 PM");
        timeSlotList.add("3:00 PM");
        timeSlotList.add("3:20 PM");
        timeSlotList.add("3:40 PM");
        timeSlotList.add("4:00 PM");
        timeSlotList.add("4:20 PM");
        timeSlotList.add("4:40 PM");
        timeSlotList.add("5:00 PM");
        return timeSlotList;
    }

    public ArrayList<String> getCoimbatoreTimeSlotList()
    {
        timeSlotList = new ArrayList<>();
        timeSlotList.add("9:00 AM");
        timeSlotList.add("9:20 AM");
        timeSlotList.add("9:40 AM");
        timeSlotList.add("10:00 AM");
        timeSlotList.add("10:20 AM");
        timeSlotList.add("10:40 AM");
        timeSlotList.add("11:00 AM");
        timeSlotList.add("11:20 AM");
        timeSlotList.add("11:40 AM");
        timeSlotList.add("12:00 PM");
        timeSlotList.add("12:20 PM");
        timeSlotList.add("12:40 PM");
        timeSlotList.add("1:00 PM");
        timeSlotList.add("Lunch");
        timeSlotList.add("1:40 PM");
        timeSlotList.add("2:00 PM");
        timeSlotList.add("2:20 PM");
        timeSlotList.add("2:40 PM");
        timeSlotList.add("3:00 PM");
        timeSlotList.add("3:20 PM");
        timeSlotList.add("3:40 PM");
        timeSlotList.add("4:00 PM");
        return timeSlotList;
    }

    public ArrayList<String> getMayiladuthuraiTimeSlotList()
    {
        timeSlotList = new ArrayList<>();
        timeSlotList.add("9:00 AM");
        timeSlotList.add("9:20 AM");
        timeSlotList.add("9:40 AM");
        timeSlotList.add("10:00 AM");
        timeSlotList.add("10:20 AM");
        timeSlotList.add("10:40 AM");
        timeSlotList.add("11:00 AM");
        timeSlotList.add("11:20 AM");
        timeSlotList.add("11:40 AM");
        timeSlotList.add("12:00 PM");
        timeSlotList.add("12:20 PM");
        timeSlotList.add("12:40 PM");
        timeSlotList.add("1:00 PM");
        timeSlotList.add("Lunch");
        timeSlotList.add("1:40 PM");
        timeSlotList.add("2:00 PM");
        timeSlotList.add("2:20 PM");
        timeSlotList.add("2:40 PM");
        timeSlotList.add("3:00 PM");
        timeSlotList.add("3:20 PM");
        timeSlotList.add("3:40 PM");
        timeSlotList.add("4:00 PM");
        return timeSlotList;
    }

    public ArrayList<String> getNamakkalTimeSlotList()
    {
        timeSlotList = new ArrayList<>();
        timeSlotList.add("6:00 PM");
        timeSlotList.add("6:20 PM");
        timeSlotList.add("6:40 PM");
        timeSlotList.add("7:00 PM");
        timeSlotList.add("7:20 PM");
        timeSlotList.add("7:40 PM");
        timeSlotList.add("8:00 PM");
        timeSlotList.add("8:20 PM");
        timeSlotList.add("8:40 PM");
        timeSlotList.add("9:00 PM");
        timeSlotList.add("9:20 PM");
        timeSlotList.add("9:40 PM");
        timeSlotList.add("10:00 PM");
        timeSlotList.add("10:20 PM");
        timeSlotList.add("10:40 PM");
        timeSlotList.add("11:00 PM");
        return timeSlotList;
    }
    public ArrayList<String> getKollidamTimeSlotList()
    {
        timeSlotList = new ArrayList<>();
        timeSlotList.add("6:00 PM");
        timeSlotList.add("6:20 PM");
        timeSlotList.add("6:40 PM");
        timeSlotList.add("7:00 PM");
        timeSlotList.add("7:20 PM");
        timeSlotList.add("7:40 PM");
        timeSlotList.add("8:00 PM");
        timeSlotList.add("8:20 PM");
        timeSlotList.add("8:40 PM");
        timeSlotList.add("9:00 PM");
        timeSlotList.add("9:20 PM");
        timeSlotList.add("9:40 PM");
        timeSlotList.add("10:00 PM");
        return timeSlotList;
    }


    public  String getE_mail()
    {
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        String e_mail = null;
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                e_mail = account.name;
                break;

            }
        }

        return e_mail;
    }


    private static class CoimbatoreDisableDcorator implements DayViewDecorator {

        int currentDate;
        int currentMonth;
        int lastDate;
        int lastMonth;
        Context context;
        public CoimbatoreDisableDcorator(Context context,int currentDate,int currentMonth,int lastDate,int lastMonth)
        {
            this.currentDate = currentDate;
            this.currentMonth = currentMonth;
            this.lastDate = lastDate;
            this.lastMonth = lastMonth;
            this.context = context;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            int week = 0;

                        Date date5 = day.getDate();
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        String reportDate = df.format(date5);
                        Log.e("date", String.valueOf(reportDate));
                        Date date1 = null;
                        try {
                            date1 = df.parse(reportDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                week = cal.get(Calendar.WEEK_OF_MONTH);
                Log.e("week date",String.valueOf(week));


            }else
            {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(date1);
                week = cal.get(java.util.Calendar.WEEK_OF_MONTH);
                Log.e("week date",String.valueOf(week));
            }

                if (currentMonth == day.getMonth())
                {
                    if (day.getDay() >= currentDate)
                    {
                        if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                            if (week == 2 || week == 4) {
                                return false;
                            } else {
                                return true;
                            }
                        }else {
                            return true;
                        }
                    } else
                        {
                        return false;
                        }
                }else if (lastMonth == day.getMonth())
                {
                    if (day.getDay() <= lastDate)
                    { if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                        if (week == 2 || week == 4)
                        {
                            return false;
                        } else {
                            return true;
                        }
                    }else {
                        return true;
                    }
                    } else
                    {
                        return false;
                    }
                }else
                    {
                    if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY)
                    {
                        if (week == 2 || week == 4) {
                            return false;
                        } else {
                            return true;
                        }

                    }else {
                        return true;
                    }
                }


        }


        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(false);
            view.addSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red_not_avaliable)));

        }

    }





    private static class MayiladuthuraiDisableDcorator implements DayViewDecorator {

        int currentDate;
        int currentMonth;
        int lastDate;
        int lastMonth;
        Context context;
        public MayiladuthuraiDisableDcorator(Context context,int currentDate,int currentMonth,int lastDate,int lastMonth)
        {
            this.currentDate = currentDate;
            this.currentMonth = currentMonth;
            this.lastDate = lastDate;
            this.lastMonth = lastMonth;
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            int week = 0;

            Date date5 = day.getDate();
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String reportDate = df.format(date5);
            Log.e("date", String.valueOf(reportDate));
            Date date1 = null;
            try {
                date1 = df.parse(reportDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                week = cal.get(Calendar.WEEK_OF_MONTH);
                Log.e("week date",String.valueOf(week));


            }else
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                week = cal.get(Calendar.WEEK_OF_MONTH);
                Log.e("week date",String.valueOf(week));

            }

            if (currentMonth == day.getMonth())
            {
                if (day.getDay() >= currentDate)
                {
                    if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                        if (week == 1 || week == 3) {
                            return false;
                        } else {
                            return true;
                        }
                    }else {
                        return true;
                    }
                } else
                {
                    return false;
                }
            }else if (lastMonth == day.getMonth())
            {
                if (day.getDay() <= lastDate)
                { if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                    if (week == 1 || week == 3)
                    {
                        return false;
                    } else {
                        return true;
                    }
                }else {
                    return true;
                }
                } else
                {
                    return false;
                }
            }else
            {
                if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY)
                {
                    if (week == 1 || week == 3) {
                        return false;
                    } else {
                        return true;
                    }

                }else {
                    return true;
                }
            }


        }


        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(false);
            view.addSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red_not_avaliable)));

        }

    }




    private static class ChennaiDisableDcorator implements DayViewDecorator {

        int currentDate;
        int currentMonth;
        int lastDate;
        int lastMonth;
        Context context;
        public ChennaiDisableDcorator(Context context,int currentDate,int currentMonth,int lastDate,int lastMonth)
        {
            this.currentDate = currentDate;
            this.currentMonth = currentMonth;
            this.lastDate = lastDate;
            this.lastMonth = lastMonth;
            this.context = context;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if (currentMonth == day.getMonth())
            {
                if (day.getDay() >= currentDate)
                {
                    if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY ) {
                        if (day.getDay() <= 7)
                        {
                            return false;
                        }else  if (day.getDay() < 22 && day.getDay() > 14 )
                        {
                            return false;
                        }else
                        {
                            return true;
                        }

                    }else
                    {
                        return true;
                    }
                }else
                {return false;}
            }else if (lastMonth == day.getMonth())
            {
                if (day.getDay() <= lastDate)
                {
                    if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY ) {
                        if (day.getDay() <= 7)
                        {
                            return false;
                        }else  if (day.getDay() < 22 && day.getDay() > 14 )
                        {
                            return false;
                        }else
                        {
                            return false;
                        }

                    }else
                    {
                        return true;
                    }
                }else {
                    return false;
                }
            }else if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY ) {
                if (day.getDay() <= 7)
                {
                    return false;
                }else  if (day.getDay() < 22 && day.getDay() > 14 )
                {
                    return false;
                }else
                {
                    return true;
                }

            }else
            {
                return true;
            }

        }


        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(false);
            view.addSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red_not_avaliable)));

        }

    }



    private static class ErodeDisableDcorator implements DayViewDecorator {

        int currentDate;
        int currentMonth;
        int lastDate;
        int lastMonth;
        Context context;
        public ErodeDisableDcorator(Context context,int currentDate,int currentMonth,int lastDate,int lastMonth)
        {
            this.currentDate = currentDate;
            this.currentMonth = currentMonth;
            this.lastDate = lastDate;
            this.lastMonth = lastMonth;
            this.context = context;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {

                if (currentMonth == day.getMonth()) {
                    if (currentDate <= day.getDay())
                    {
                        if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY || day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY ) {
                            return true;
                        }else
                        {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else if (lastMonth == day.getMonth()){
                    if (lastDate >= day.getDay()) {
                        if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY || day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY) {
                            return true;
                        }else
                        {
                            return false;
                        }
                    }else
                    {
                        return false;
                    }

                }else
                {
                    if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY || day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY) {
                        return true;
                    }else
                    {
                        return false;
                    }
                }



        }


        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(false);
            view.addSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red_not_avaliable)));

        }

    }
    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {
                snackbar = TSnackbar
                        .make(scltimeMain, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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



    public String compareTime(String strTimeToCompare)

    {
      /*  int hour,currentHour,isampm,iscurrentAmPm;
        String strDate,ampm,currentAmPm,min,currentMin,isBeforeOrNot = null;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
        strDate = mdformat.format(calendar.getTime());

        String[] minHour = strTimeToCompare.split(":");
        hour = Integer.parseInt(minHour[0]);
        String[] min1 = minHour[1].split(" ");
        min = min1[0];
        String[] amPm = strTimeToCompare.split(" ");
        ampm = amPm[1];

        if (ampm.equals("AM"))
        {
            isampm = 1;
        }else
        {
            isampm = 2;
        }
        String[] currentminHour = strDate.split(":");
        currentHour = Integer.parseInt(currentminHour[0]);
        String[] currentmin1 = currentminHour[1].split(" ");
        currentMin = currentmin1[0];
        String[] currentamPm = strDate.split(" ");
        currentAmPm =currentamPm[1];

        if (currentAmPm.equals("AM"))
        {
            iscurrentAmPm = 1;
        }else
        {
            iscurrentAmPm = 2;
        }

        if (hour < currentHour)
        {
            if (isampm < iscurrentAmPm)
            {
                isBeforeOrNot = "YES";

            }else {
                isBeforeOrNot = "NO";

            }
        }else
        {
            if (isampm < iscurrentAmPm)
            {
                isBeforeOrNot = "YES";

            }else {
                isBeforeOrNot = "NO";

            }
        }
        return isBeforeOrNot;

    }*/
        java.util.Calendar cal = java.util.Calendar.getInstance(java.util.TimeZone.getDefault());

        int dtHour;

        int dtMin;

        int iAMPM;

        String strAMorPM=null;

        Date dtCurrentDate;



        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());



        try {



            Date TimeToCompare = sdf.parse(strTimeToCompare);

            dtMin=cal.get(java.util.Calendar.MINUTE);

            dtHour=cal.get(java.util.Calendar.HOUR);

            iAMPM=cal.get(java.util.Calendar.AM_PM);

            if (iAMPM == 1)

            {

                strAMorPM="PM";

            }



            if (iAMPM == 0)

            {

                strAMorPM="AM";

            }



            dtCurrentDate = sdf.parse(dtHour + ":" + dtMin + " " + strAMorPM);


            if(dtCurrentDate.after(TimeToCompare))

            {

                return "1";

            }

            if (dtCurrentDate.before(TimeToCompare))

            {

                return "2";

            }

            if (dtCurrentDate.equals(TimeToCompare))

            {

                return "3";

            }

        } catch (ParseException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return "4";

    }


    public void showAlert(Context context, String title, String msg)
    {
        final Dialog dialog = new Dialog(context);
//        dialog.setTitle(title);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.show_emergency_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.white)));
        LinearLayout ldtCancel = (LinearLayout) dialog.findViewById(R.id.ldtCancel);
        ldtCancel.setVisibility(View.VISIBLE);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BookAppointmentActivity.ldtBookingDetails.setBackgroundColor(getActivity().getResources().getColor(R.color.book_title_orange));
                BookAppointmentActivity.txtBooking.setTextColor(getActivity().getResources().getColor(R.color.white));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragmentTransaction.replace(R.id.fragment_container, new RegisterDetailsFragment(), "BOOKING_ FRAGMENT");
                fragmentTransaction.addToBackStack("BOOKING_ FRAGMENT");
                fragmentTransaction.commit();
                dialog.dismiss();
            }
        });
        TextView txtTitle = (TextView)  dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        TextView txtMessage = (TextView)  dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(msg);
        dialog.show();


    }
}


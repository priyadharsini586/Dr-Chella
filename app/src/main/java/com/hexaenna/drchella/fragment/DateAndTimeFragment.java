package com.hexaenna.drchella.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.net.ConnectivityManager;
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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.applandeo.materialcalendarview.CalendarView;

import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.BookingDetails;
import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.Model.TimeAndDateResponse;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.BookAppointmentActivity;
import com.hexaenna.drchella.activity.OTPActivity;
import com.hexaenna.drchella.activity.RegistrationActivity;
import com.hexaenna.drchella.adapter.CityAdapter;
import com.hexaenna.drchella.adapter.GenderSpinnerAdapter;
import com.hexaenna.drchella.adapter.TimeSlotAdapter;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.custom_view.ExpandableHeightGridView;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.utils.NetworkChangeReceiver;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
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
import java.util.Date;
import java.util.HashSet;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DateAndTimeFragment extends Fragment implements View.OnClickListener {
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

    CoimbatoreDisableDcorator coimbatoreDisableDcorator;
    MayiladuthuraiDisableDcorator mayiladuthuraiDisableDcorator;
    ErodeDisableDcorator erodeDisableDcorator;
    ChennaiDisableDcorator chennaiDisableDcorator ;
    TextView txtCity;
    ApiInterface apiInterface;
    String isConnection = null;
    ScrollView sclRegisterMain;
    TSnackbar snackbar;
    View snackbarView;
    NetworkChangeReceiver networkChangeReceiver;
    ProgressBar progressBar;

    LinearLayout ldtNext;
    ScrollView scltimeMain;
    BookingDetails bookingDetails = BookingDetails.getInstance();

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


        }else if (databaseHandler.getContact("0").equals("Tamil"))
        {
            mainView = inflater.inflate(R.layout.tamil_date_and_time_fragment, container, false);


        }



         mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        scltimeMain = (ScrollView) mainView.findViewById(R.id.scltimeMain);
        ldtCity = (LinearLayout) mToolbar.findViewById(R.id.ldtCity);
        txtCity = (TextView) mToolbar.findViewById(R.id.txtCity);
        ldtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog(getActivity());
            }
        });
        ldtCity.setVisibility(View.VISIBLE);


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
//        calendarView.addDecorators(new  SelectedDayDecorator(30, R.color.red_not_avaliable));
          calendarView.setVisibility(View.GONE);

            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                    int week = 0;

                    Date date5 = date.getDate();
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

                    if (item[0].equals("Erode") || item[0].equals("ஈரோடு"))
                    {
                        if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                            Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            gridView.setVisibility(View.GONE);
                        }else
                        {
                            getTimingList();
                            gridView.setExpanded(true);
                            gridView.setAdapter(new TimeSlotAdapter(getActivity(), getErodeTimeSlotList()));
                            gridView.setVisibility(View.VISIBLE);
                        }
                    }else if (item[0].equals("Chennai") || item[0].equals("சென்னை"))
                    {
                        CalendarDay  selectedDate = widget.getSelectedDate();
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

                        if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) != java.util.Calendar.SUNDAY)
                        {
                            Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            if (selectedDate.getDay() <= 7 || selectedDate.getDay() < 22 && selectedDate.getDay() > 14)
                            {
                                Toast.makeText(getActivity(), "Doctor avaiable", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else if (item[0].equals("Coimbatore") || item[0].equals("கோயம்புத்தூர்"))
                    {
                        if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                            if (week == 2 || week == 4) {
                                Toast.makeText(getActivity(), "Doctor avaiable", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                        }
                    }else if (item[0].equals("Namakkal") || item[0].equals("நாமக்கல்"))
                    {
                        if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                            if (week == 2 || week == 4) {
                                Toast.makeText(getActivity(), "Doctor avaiable", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                        }
                    }else if (item[0].equals("Mayiladuthurai") || item[0].equals("மயிலாடுதுறை"))
                    {
                        if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                            if (week == 1 || week == 3) {
                                Toast.makeText(getActivity(), "Doctor avaiable", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                        }
                    }else if (item[0].equals("Kollidam") || item[0].equals("கொள்ளிடம்"))
                    {
                        if (date.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
                            if (week == 1 || week == 3) {
                                Toast.makeText(getActivity(), "Doctor avaiable", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Doctor not avaiable", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        gridView = (ExpandableHeightGridView) mainView.findViewById(R.id.gridTime);

        ldtNext = (LinearLayout) mainView.findViewById(R.id.ldtNext);
        ldtNext.setOnClickListener(this);

        if (bookingDetails.getCity() == null) {
            showDailog(getActivity());
        }else
        {
            item[0] = bookingDetails.getCity();
            checkList(bookingDetails.getCity());
        }

        return mainView;
    }

    private void getTimingList() {
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("city","1");
                jsonObject.put("adate","05-11-2017");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<TimeAndDateResponse> call = apiInterface.check_time(jsonObject);
            call.enqueue(new Callback<TimeAndDateResponse>() {
                @Override
                public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAndDateResponse timeAndDateResponse = response.body();
                        if (timeAndDateResponse.getStatus_code() != null)
                        {
                            if (timeAndDateResponse.getStatus_code().equals(Constants.status_code1))
                            {
                                ArrayList<String> bookList = timeAndDateResponse.getBooked_Array();
                                ArrayList<String> blockedList = timeAndDateResponse.getBlocked_Array();

                            }else
                            {
                                if (timeAndDateResponse.getStatus_message() != null)
                                    Toast.makeText(getActivity(),timeAndDateResponse.getStatus_message(),Toast.LENGTH_SHORT).show();
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

            }
        });
        citySpinner.setAdapter(citySpinnerAdapter);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item[0] != null) {
                    dialog.dismiss();
                    checkList(item[0]);
                }
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

        if (city.equals("Erode") || city.equals("ஈரோடு"))
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
            if (coimbatoreDisableDcorator != null)
                calendarView.removeDecorator(coimbatoreDisableDcorator);
            if (chennaiDisableDcorator != null)
                calendarView.removeDecorator(chennaiDisableDcorator);
            if (mayiladuthuraiDisableDcorator != null)
                calendarView.removeDecorator(mayiladuthuraiDisableDcorator);
            erodeDisableDcorator = new ErodeDisableDcorator(getActivity(), currentDate, currentMonth, endDate, endMonth);
            calendarView.addDecorator(erodeDisableDcorator);
           /* calendarView.addDecorator(new DayViewDecorator() {
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
                        return false;
                    }


                }

                @Override
                public void decorate(DayViewFacade view) {
                    // add red foreground span
                    view.setDaysDisabled(false);
                    view.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_not_avaliable)));
                }
            });*/
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

            calendarView.setVisibility(View.VISIBLE);
            if (erodeDisableDcorator != null)
                calendarView.removeDecorator(erodeDisableDcorator);
            if (chennaiDisableDcorator != null)
                calendarView.removeDecorator(chennaiDisableDcorator);
            if (coimbatoreDisableDcorator != null)
                calendarView.removeDecorator(coimbatoreDisableDcorator);
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
                getNextFragment();
                break;
        }
    }

    private void getNextFragment() {

        if (item[0] != null)
            bookingDetails.setCity(item[0]);
        else
            bookingDetails.setCity(bookingDetails.getCity());

        BookAppointmentActivity.ldtBookingDetails.setBackgroundColor(getActivity().getResources().getColor(R.color.book_title_orange));
        BookAppointmentActivity.txtBooking.setTextColor(getActivity().getResources().getColor(R.color.white));

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        fragmentTransaction.replace(R.id.fragment_container, new RegisterDetailsFragment(), "BOOKING_ FRAGMENT");
        fragmentTransaction.addToBackStack("BOOKING_ FRAGMENT");
        fragmentTransaction.commit();
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
                        if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
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
                        if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
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
                    if (day.getCalendar().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
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
}


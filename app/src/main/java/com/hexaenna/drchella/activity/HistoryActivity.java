package com.hexaenna.drchella.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.hexaenna.drchella.Model.GetExcelModel;
import com.hexaenna.drchella.Model.HistoryDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.adapter.GetExcelAdapter;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.custom_view.SimpleDividerItemDecoration;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.hexaenna.drchella.utils.Constants;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity implements OnDateSelectedListener {

    Calendar  c,cal;
    int currentDate,currentMonth,endMonth,endDate,currentYear;
    ImageView imgHisCal;
    MaterialCalendarView calendarView;
    NetworkChangeReceiver networkChangeReceiver;
    ApiInterface apiInterface;
    String isConnection = null;
    LinearLayout ldtListExcel;
    String selectedDate = null;
    RelativeLayout rldNoApp;
    ProgressBar proHistory;
    TextView txtNoApp,txtPick;

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

        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
//                if (isConnection == null) {
                Bundle b = intent.getExtras();
                isConnection = b.getString(Constants.MESSAGE);


//                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        getApplicationContext().registerReceiver(networkChangeReceiver,
                intentFilter);


        ldtListExcel = (LinearLayout) findViewById(R.id.ldtListExcel);

        txtNoApp = (TextView) findViewById(R.id.txtNoApp);
        txtNoApp.setVisibility(View.GONE);
        proHistory = (ProgressBar) findViewById(R.id.progressHistory);
        proHistory.setVisibility(View.GONE);
        txtPick = (TextView) findViewById(R.id.txtPick);


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
                if (selectedDate != null)
                    getExcel(selectedDate);

                dialog.cancel();


            }
        });
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) +1;
        getAppointmentDate(String.valueOf(year),String.valueOf(month));
         calendarView = (MaterialCalendarView) dialog.findViewById(R.id.calendarView);
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
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                int month = date.getMonth()+1;
                int year = date.getYear();
                Log.e("month changedlistner","called" + month + " year " + year);
                getAppointmentDate(String.valueOf(year),String.valueOf(month));
            }
        });
        dialog.show();
        txtPick.setVisibility(View.GONE);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        int week = 0;
        Date date5 = date.getDate();
        DateFormat dateForRequest = new SimpleDateFormat("dd.MM.yyyy");
        selectedDate = dateForRequest.format(date5);
//        Log.e("date", String.valueOf(dateRequest));
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

            if (curYear == currentYear) {
                if (currentMonth == day.getMonth())
                    return date > currentDate;
                /*else if (lastMonth == day.getMonth()) {
                    return date > lastDate;
                }*/
                else {
                    return false;
                }
            }else
            {
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



    public class EventDecorator implements DayViewDecorator {

        private int color;
        private HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }

    public void getAppointmentDate(final String year, final String month)
    {
        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("year",year);
                    jsonObject.put("month",month);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<HistoryDetails> call = apiInterface.getAppointmentHisoryDetails(jsonObject);
                call.enqueue(new Callback<HistoryDetails>() {
                    @Override
                    public void onResponse(Call<HistoryDetails> call, Response<HistoryDetails> response) {
                        if (response.isSuccessful())
                        {
                            HistoryDetails historyDetails = response.body();
                            if (historyDetails.getStatus_code() != null)
                            {
                                if (historyDetails.getStatus_code().equals(Constants.status_code1))
                                {
                                    ArrayList<HistoryDetails.Appointment_List> details = historyDetails.getAppoinments();
                                    List<CalendarDay> calendarDayList = new ArrayList<>();
                                    for (int i = 0 ; i < details.size() ; i ++)
                                    {
                                        HistoryDetails.Appointment_List appointment_list = details.get(i);
                                        String date = appointment_list.getDate()+"/"+month+"/"+year;
                                        Date date1 = null,currentDate = null;
                                        try {
                                            date1=new SimpleDateFormat("dd/MM/yyyy").parse(date);
                                            Date date2 = Calendar.getInstance().getTime();
                                            // Display a date in day, month, year format
                                            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                            String today = formatter.format(date2);;
                                            currentDate = formatter.parse(today);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        CalendarDay  day = CalendarDay.from(date1);
                                        if (currentDate.compareTo(date1) > 0 )
                                        {
                                            calendarDayList.add(day);
                                            Log.e("compare True","true");
                                        }else
                                        {
                                            Log.e("compare False","False");
                                        }

                                    }
                                    calendarView.addDecorator(new EventDecorator(Color.RED, calendarDayList));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HistoryDetails> call, Throwable t) {

                    }
                });

            }
        }
    }


    public void getExcel(String selectedDate)
    {
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                apiInterface = ApiClient.getClient().create(ApiInterface.class);

                JSONObject jsonObject = new JSONObject();
                proHistory.setVisibility(View.VISIBLE);
                 ldtListExcel.setVisibility(View.VISIBLE);
                 txtNoApp.setVisibility(View.GONE);
                Calendar calCurr = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                final String formattedDate = df.format(calCurr.getTime());

                try {

                        jsonObject.put("adate", selectedDate);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<GetExcelModel> call = apiInterface.getExcelDetails(jsonObject);
                call.enqueue(new Callback<GetExcelModel>() {
                    @Override
                    public  void onResponse(Call<GetExcelModel> call, Response<GetExcelModel> response) {
                        if (response.isSuccessful())
                        {
                            GetExcelModel getExcelModel = response.body();

                            if (getExcelModel.getStatus_code().equals(Constants.status_code1))
                            {
                                HashMap<String,ArrayList> getCityDetails = getExcelModel.getCity();
                                ldtListExcel.removeAllViews();
                                    ArrayList lstKey = new ArrayList();
                                    ArrayList<ArrayList> lstValue = new ArrayList();
                                    for (Map.Entry<String, ArrayList> entry : getCityDetails.entrySet()) {
                                        String key = entry.getKey();
                                        lstKey.add(key);
                                        lstValue.add(entry.getValue());
                                    }

                                    LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                if (lstKey.size() != 0) {

                                    for (int i = 0; i < lstKey.size(); i++) {
                                        ArrayList getExcelModels = lstValue.get(i);
                                        ArrayList<GetExcelModel> getExcelModelsList1 = new ArrayList<GetExcelModel>();

                                        for (int j = 0; j < getExcelModels.size(); j++) {
                                            LinkedTreeMap objects = (LinkedTreeMap) getExcelModels.get(j);

                                            GetExcelModel getExcelModelList = new GetExcelModel();
                                            String name = (String) objects.get("name");
                                            String time = (String) objects.get("time");
                                            String phone = (String) objects.get("phone");
                                            String gender = (String) objects.get("gender");
                                            String age = (String) objects.get("age");
                                            String place = (String) objects.get("place");
                                            getExcelModelList.setName(name);
                                            getExcelModelList.setAge(age);
                                            getExcelModelList.setTime(time);
                                            getExcelModelList.setPhone(phone);
                                            getExcelModelList.setGender(gender);
                                            getExcelModelList.setPlace(place);
                                            getExcelModelsList1.add(getExcelModelList);


                                        }

                                        View addView = layoutInflater.inflate(R.layout.multiple_excel, null);
                                        RecyclerView recyclerView = (RecyclerView) addView.findViewById(R.id.lstLst);
                                        GetExcelAdapter getExcelAdapter = new GetExcelAdapter(getExcelModelsList1, getApplicationContext());
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        recyclerView.setNestedScrollingEnabled(false);
                                        TextView txtCoimbaore = (TextView) addView.findViewById(R.id.txtCoimbaore);
                                        String city = String.valueOf(lstKey.get(i));
                                        txtCoimbaore.setText(getAddressString(city));
                                        recyclerView.setAdapter(getExcelAdapter);

                                        ldtListExcel.addView(addView);
                                        proHistory.setVisibility(View.GONE);

                                    }
                                }


                            }else
                            {
                                txtNoApp.setVisibility(View.VISIBLE);
                                ldtListExcel.setVisibility(View.GONE);
                                proHistory.setVisibility(View.GONE);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<GetExcelModel> call, Throwable t) {
                        proHistory.setVisibility(View.GONE);
                    }
                });
            }
        }



    public String getAddressString(String city)
    {
        String returnAddress;
        if (city.equals("1"))
        {
            returnAddress = getApplicationContext().getString(R.string.chennai_hospital);

        }else if (city.equals("2"))
        {
            returnAddress = getApplicationContext().getString(R.string.erode_hospital);

        }else if (city.equals("3"))
        {
            returnAddress = getApplicationContext().getString(R.string.coimbatore_hospital);


        }else if (city.equals("4"))
        {
            returnAddress = getApplicationContext().getString(R.string.namakkal_hospital);

        }else if (city.equals("5"))
        {
            returnAddress = getApplicationContext().getString(R.string.mayiladu_hospital);
          /*  txtHospitalName.setText(getApplicationContext().getString(R.string.mayiladu_hospital));
            txtAddress.setText(getApplicationContext().getString(R.string.may_hospital_address));*/
        }else if (city.equals("6"))
        {
            returnAddress = getApplicationContext().getString(R.string.kollidam_hospital);

        }else
        {
            returnAddress = " ";
        }

        return returnAddress;
    }
}

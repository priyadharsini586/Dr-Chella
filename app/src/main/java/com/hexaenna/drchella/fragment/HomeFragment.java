package com.hexaenna.drchella.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.AppointmentDetails;
import com.hexaenna.drchella.Model.GetExcelModel;
import com.hexaenna.drchella.Model.TimeAndDateResponse;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.BookAppointmentActivity;
import com.hexaenna.drchella.activity.ViewAppointmentActivity;
import com.hexaenna.drchella.adapter.GetExcelAdapter;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.custom_view.SimpleDividerItemDecoration;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.service.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 11/10/2017.
 */

public class HomeFragment extends Fragment {

    View rootView;
    String isConnection = null;
    LinearLayout ldtNoAppoint,ldtAppointment,ldtAddAppointment;
    TSnackbar snackbar;
    RelativeLayout rldMainLayout;
    View snackbarView;
    NetworkChangeReceiver networkChangeReceiver;
    ApiInterface apiInterface;
    TextView txtBookAppointment,txtRemaingDays,txtHospitalName,txtAddress,txtTime,txtName;
    Button btnView;
    ImageView imgScedule;
    ProgressBar progressHome;
    String sendUrl = "b";
    RelativeLayout rldUsertype2,rldUsertype1;
    String[] userDetails;
    LinearLayout ldtListExcel;
    TextView txtDownload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        userDetails =  databaseHandler.getUserName("0");
        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
//                if (isConnection == null) {
                    Bundle b = intent.getExtras();
                    isConnection = b.getString(Constants.MESSAGE);
                    getNetworkState();
                    Log.e("  from home", "" + isConnection);
//                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        getActivity().registerReceiver(networkChangeReceiver,
                intentFilter);

        rldMainLayout = (RelativeLayout) rootView.findViewById(R.id.ldtMainview);
//        txtBookAppointment = (TextView) rootView.findViewById(R.id.txtBookAppointment);
//        txtRemaingDays = (TextView) rootView.findViewById(R.id.txtRemaingDays);
        txtHospitalName = (TextView) rootView.findViewById(R.id.txtHospitalName);
        txtAddress = (TextView) rootView.findViewById(R.id.txtAddress);
//        txtTime = (TextView) rootView.findViewById(R.id.txtTime);

        ldtNoAppoint = (LinearLayout) rootView.findViewById(R.id.ldtNoAppoint);
        ldtNoAppoint.setVisibility(View.GONE);
        ldtAppointment = (LinearLayout) rootView.findViewById(R.id.ldtAppointment);
        ldtAppointment.setVisibility(View.GONE);


        txtDownload = (TextView) rootView.findViewById(R.id.txtDownload);
        txtDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPdf();
               /* */

            }
        });
        ldtListExcel = (LinearLayout) rootView.findViewById(R.id.ldtListExcel);

        rldUsertype2 = (RelativeLayout) rootView.findViewById(R.id.rldUsertype2);
        rldUsertype1 = (RelativeLayout) rootView.findViewById(R.id.rldUsertype1);
        rldUsertype2.setVisibility(View.VISIBLE);
        rldUsertype1.setVisibility(View.VISIBLE);
        if (userDetails[3].equals("user")) {

            rldUsertype2.setVisibility(View.VISIBLE);
            rldUsertype1.setVisibility(View.GONE);
        }else if (userDetails[3].equals("admin")){
            rldUsertype2.setVisibility(View.VISIBLE);
            rldUsertype1.setVisibility(View.GONE);
        }

        /*btnView = (Button) rootView.findViewById(R.id.btnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewAppointmentActivity.class);
                startActivity(intent);
            }
        });*/
        imgScedule = (ImageView) rootView.findViewById(R.id.imgScedule);
        imgScedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BookAppointmentActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        progressHome =(ProgressBar) rootView.findViewById(R.id.progressHome);
        progressHome.setVisibility(View.GONE);
        ldtAddAppointment = (LinearLayout) rootView.findViewById(R.id.ldtAddAppointment);
        return rootView;
    }



    private void registerDetails() {

        progressHome.setVisibility(View.VISIBLE);
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            if (sendUrl.equals("b")) {
                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                sendUrl = "send";
                JSONObject jsonObject = new JSONObject();

                try {
                    UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                    jsonObject.put("user_email", userRegisterDetails.getE_mail());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<TimeAndDateResponse> call = apiInterface.recent_appintment(jsonObject);
                call.enqueue(new Callback<TimeAndDateResponse>() {
                    @Override
                    public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                        if (response.isSuccessful()) {
                            Log.e("successfull", String.valueOf(response.body()));
                            TimeAndDateResponse timeAndDateResponse = response.body();
                            ldtNoAppoint.setVisibility(View.GONE);
                            ldtAppointment.setVisibility(View.GONE);
                            progressHome.setVisibility(View.GONE);

                            if (timeAndDateResponse.getStatus_code() != null) {
                                if (timeAndDateResponse.getStatus_code().equals(Constants.status_code1)) {
                                    ldtAppointment.setVisibility(View.VISIBLE);
                                    List<TimeAndDateResponse.appoinments> appointmentLit = new ArrayList<TimeAndDateResponse.appoinments>(timeAndDateResponse.getAppoinments());
                                    appointmentLit = timeAndDateResponse.getAppoinments();
                                    ldtAddAppointment.removeAllViews();
                                    if (appointmentLit.size() != 0) {
                                        for (int i = 0; i < appointmentLit.size(); i++) {
                                            TimeAndDateResponse.appoinments appoinments = appointmentLit.get(i);
                                            Log.e("appointment", appoinments.getDate());
                                            LayoutInflater layoutInflater =(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            View addView = layoutInflater.inflate(R.layout.add_appointment_layout, null);
                                            txtTime = (TextView) addView.findViewById(R.id.txtTime);
                                            txtRemaingDays = (TextView) addView.findViewById(R.id.txtRemaingDays);
                                            txtName = (TextView) addView.findViewById(R.id.txtName);
                                            txtName.setText(appoinments.getPtnt_name());
                                            if (appoinments.getDate() != null) {
                                                try {
                                                    DateFormat dateForRequest = new SimpleDateFormat("dd.MM.yyyy");
                                                    String requestdate = appoinments.getDate().trim();
                                                    Date newDate = dateForRequest.parse(requestdate);
                                                    dateForRequest = new SimpleDateFormat("dd MMM yyyy");
                                                    String date = dateForRequest.format(newDate);
                                                    txtTime.setText(date + " at " + appoinments.getTime());

                                                    Calendar calCurr = Calendar.getInstance();
                                                    Calendar day = Calendar.getInstance();
                                                    day.setTime(new SimpleDateFormat("dd.MM.yyyy").parse(appoinments.getDate()));
                                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                                    String formattedDate = df.format(day.getTime());
                                                    String formattedDate1 = df.format(calCurr.getTime());


                                                    Date dateBefore = df.parse(formattedDate1);
                                                    Date dateAfter = df.parse(formattedDate);
                                                    long difference = dateAfter.getTime() - dateBefore.getTime();
                                                    int daysBetween = (int) (difference / (1000*60*60*24));

                                                    Log.e("date", "Days Left: " + daysBetween);

                                                    if (day.after(calCurr)) {
                                                        txtRemaingDays.setText(daysBetween + " " + getActivity().getResources().getString(R.string.remaining));

                                                    } else if (formattedDate.equals(formattedDate1)) {
                                                        txtRemaingDays.setText("You Have an Appointment with doctor Today.");
                                                    }


                                                    Log.e("date", "Days Left: " + formattedDate);
                                                    Log.e("date", "Days Left: " + formattedDate1);

                                                    AppointmentDetails appointmentDetails = AppointmentDetails.getInstance();
                                                    appointmentDetails.setCity(appoinments.getCity_id());
                                                    appointmentDetails.setDate(appoinments.getDate());
                                                    appointmentDetails.setTime(appoinments.getTime());

                                                    getAddress(appoinments.getCity_id());

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            addView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getActivity(), ViewAppointmentActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            ldtAddAppointment.addView(addView);
                                        }
                                    }
                               /* if (timeAndDateResponse.getDate() != null) {
                                    try {
                                        Date newDate = dateForRequest[0].parse(timeAndDateResponse.getDate());
                                        dateForRequest[0] = new SimpleDateFormat("dd MMM yyyy");
                                        String date = dateForRequest[0].format(newDate);
                                        txtTime.setText(date + " at " + timeAndDateResponse.getTime());

                                        Calendar calCurr = Calendar.getInstance();
                                        Calendar day = Calendar.getInstance();
                                        day.setTime(new SimpleDateFormat("dd.MM.yyyy").parse(timeAndDateResponse.getDate()));
                                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                        String formattedDate = df.format(day.getTime());
                                        String formattedDate1 = df.format(calCurr.getTime());
                                        if (day.after(calCurr)) {
                                            Log.e("date", "Days Left: " + (day.get(Calendar.DAY_OF_MONTH) - (calCurr.get(Calendar.DAY_OF_MONTH))));
                                            txtRemaingDays.setText(day.get(Calendar.DAY_OF_MONTH) - (calCurr.get(Calendar.DAY_OF_MONTH)) + " " + getActivity().getResources().getString(R.string.remaining));

                                        } else if (formattedDate.equals(formattedDate1)) {
                                            txtRemaingDays.setText("You Have an Appointment with doctor Today.");
                                        }


                                        Log.e("date", "Days Left: " + formattedDate);
                                        Log.e("date", "Days Left: " + formattedDate1);

                                        AppointmentDetails appointmentDetails = AppointmentDetails.getInstance();
                                        appointmentDetails.setCity(timeAndDateResponse.getCity_id());
                                        appointmentDetails.setDate(timeAndDateResponse.getDate());
                                        appointmentDetails.setTime(timeAndDateResponse.getTime());

                                        getAddress(timeAndDateResponse.getCity_id());

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }*/

                                } else if (timeAndDateResponse.getStatus_code().equals(Constants.status_code0)) {
                                    ldtNoAppoint.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TimeAndDateResponse> call, Throwable t) {
                        Log.e("failure", String.valueOf(t));
                    }
                });
            }
        }else
        {
            snackbar = TSnackbar
                    .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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

    private void getAddress(String city) {

        if (city.equals("1"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.chennai_hospital));
            txtAddress.setText(getActivity().getString(R.string.chennai_hospital_address));
        }else if (city.equals("2"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.erode_hospital));
            txtAddress.setText(getActivity().getString(R.string.erode_hospital_address));
        }else if (city.equals("3"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.coimbatore_hospital));
            txtAddress.setText(getActivity().getString(R.string.coimbatore_hospital_address));
        }else if (city.equals("4"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.namakkal_hospital));
            txtAddress.setText(getActivity().getString(R.string.namakkal_hospital_address));
        }else if (city.equals("5"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.mayiladu_hospital));
            txtAddress.setText(getActivity().getString(R.string.may_hospital_address));
        }else if (city.equals("6"))
        {
            txtHospitalName.setText(getActivity().getString(R.string.kollidam_hospital));
            txtAddress.setText(getActivity().getString(R.string.kollidam_hospital_address));
        }
    }

    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {
                snackbar = TSnackbar
                        .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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
                if (userDetails[3].equals("user")) {
                    registerDetails();
                }
                else if (userDetails[3].equals("admin")) {
                    getExcel();
                }

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sendUrl = "b";
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

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume","onResume recreated");
        if (isConnection != null) {
            sendUrl = "b";
            if (userDetails[3].equals("user")) {
                registerDetails();
            }
            else if (userDetails[3].equals("admin")) {
                getExcel();
            }
        }
    }


    public void getExcel()
    {
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            if (sendUrl.equals("b")) {
                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                sendUrl = "send";
                JSONObject jsonObject = new JSONObject();

                Calendar calCurr = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                final String formattedDate = df.format(calCurr.getTime());
                try {
                    jsonObject.put("adate","07.12.2017");

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
                                ArrayList<ArrayList> lstValue= new ArrayList();
                                for (Map.Entry<String,ArrayList> entry : getCityDetails.entrySet()) {
                                    String key = entry.getKey();
                                    lstKey.add(key);
                                    lstValue.add(entry.getValue());
                                }

                                LayoutInflater layoutInflater =(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                for (int i=0;i < lstKey.size() ; i ++)
                                {
                                    ArrayList getExcelModels = lstValue.get(i);
                                    ArrayList<GetExcelModel> getExcelModelsList1 = new ArrayList<GetExcelModel>();

                                    for (int j =0 ; j< getExcelModels.size() ;j ++)
                                    {
                                        LinkedTreeMap objects =(LinkedTreeMap) getExcelModels.get(j);

                                            GetExcelModel getExcelModelList = new GetExcelModel();
                                            String name = (String) objects.get("name");
                                            String time = (String) objects.get("time");
                                            String phone = (String) objects.get("phone");
                                            String gender = (String) objects.get("gender");
                                            String age = (String) objects.get("age");
                                            getExcelModelList.setName(name);
                                            getExcelModelList.setAge(age);
                                            getExcelModelList.setTime(time);
                                            getExcelModelList.setPhone(phone);
                                            getExcelModelList.setGender(gender);
                                            getExcelModelsList1.add(getExcelModelList);


                                    }

                                    View addView = layoutInflater.inflate(R.layout.multiple_excel, null);
                                    RecyclerView recyclerView = (RecyclerView) addView.findViewById(R.id.lstLst);
                                    GetExcelAdapter getExcelAdapter = new GetExcelAdapter(getExcelModelsList1,getActivity());
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    recyclerView.setNestedScrollingEnabled(false);
                                    TextView txtCoimbaore = (TextView) addView.findViewById(R.id.txtCoimbaore);
                                    String city = String.valueOf(lstKey.get(i));
                                    txtCoimbaore.setText(getAddressString(city));
                                    recyclerView.setAdapter(getExcelAdapter);
                                    ldtListExcel.addView(addView);
                                }


                            }else
                            {
                                rldUsertype2.setVisibility(View.VISIBLE);
                                rldUsertype1.setVisibility(View.GONE);
                                TextView txtNoApp = (TextView) rootView.findViewById(R.id.txtNoApp);
                                txtNoApp.setVisibility(View.VISIBLE);
                                txtDownload.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetExcelModel> call, Throwable t) {
                        Log.e("failure", String.valueOf(t));
                    }
                });
            }
        }
    }

    public String getAddressString(String city)
    {
        String returnAddress;
        if (city.equals("1"))
        {
            returnAddress = getActivity().getString(R.string.chennai_hospital);

        }else if (city.equals("2"))
        {
            returnAddress = getActivity().getString(R.string.erode_hospital);

        }else if (city.equals("3"))
        {
            returnAddress = getActivity().getString(R.string.coimbatore_hospital);


        }else if (city.equals("4"))
        {
            returnAddress = getActivity().getString(R.string.namakkal_hospital);

        }else if (city.equals("5"))
        {
            returnAddress = getActivity().getString(R.string.mayiladu_hospital);
            txtHospitalName.setText(getActivity().getString(R.string.mayiladu_hospital));
            txtAddress.setText(getActivity().getString(R.string.may_hospital_address));
        }else if (city.equals("6"))
        {
            returnAddress = getActivity().getString(R.string.kollidam_hospital);

        }else
        {
            returnAddress = " ";
        }

        return returnAddress;
    }


    public void download(String url,String pdfName)
    {

        boolean fileExists =  new File(Environment.getExternalStorageDirectory() + "/chella/" +  pdfName).isFile();
        if (fileExists)
            view(pdfName);
        else {
//            ldtProgressBar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            new DownloadFile().execute(url,pdfName);
        }
    }


    public void view(String pdfName)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/chella/" + pdfName);
        Uri path=null;
        if (Build.VERSION.SDK_INT >= 24) {
            path = FileProvider.getUriForFile(getActivity(), "com.hexaenna.drchella", pdfFile);
        } else {
            path = Uri.fromFile(pdfFile);
        }

        // -> filename = maven.pdf
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(getActivity(), "No Application available to view PDF ", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Integer, Void> {

        private static final int  MEGABYTE = 1024;
        public  float per = 0;
        String fileName = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File pdfFile = null;

            File folder = new File(extStorageDirectory, "chella");
            folder.mkdir();

            pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                int downloadedSize = 0;


                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    per = ((float) downloadedSize / totalSize) * 100;
                    Integer percentage = (int) per;

                    publishProgress(percentage);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            values[0] = Math.round(values[0]);
            Log.e("percentage", String.valueOf(values[0]));
//            progressBar.setProgress(values[0]);
//            txtPercentage.setText(String.valueOf(values[0]) + "%");

            if (values[0] == 100)
            {
//                ldtProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"Download Completed",Toast.LENGTH_LONG).show();
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            isCompleted = true;

        }

        /* @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.e("percentage", String.valueOf(percentage));
            progressBar.setProgress(percentage);
        }*/
    }



    public void checkPdf()
    {
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Calendar calCurr = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            final String formattedDate = df.format(calCurr.getTime());
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("adate","14.12.2017");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<TimeAndDateResponse> call = apiInterface.getReport(jsonObject);
                call.enqueue(new Callback<TimeAndDateResponse>() {
                    @Override
                    public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                        if (response.isSuccessful()) {
                            Log.e("successfull", String.valueOf(response.body()));
                            TimeAndDateResponse timeAndDateResponse = response.body();
                            if (timeAndDateResponse.getStatus_code().equals(Constants.status_code1))
                            {
                                Calendar calCurr = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                                final String formattedDate = df.format(calCurr.getTime());
                                String excelNAme = Constants.GET_EXCEL + "{\"adate\":"+ "\""+formattedDate + "\"}";
                                Log.e("excel",excelNAme);
                                download(excelNAme,"Appointments_report_"+"15.02.2017"+".pdf");

                            }else if (timeAndDateResponse.equals(Constants.status_code0)) {
                                Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_LONG).show();
                            }
                            }

                    }

                    @Override
                    public void onFailure(Call<TimeAndDateResponse> call, Throwable t) {
                        Log.e("failure", String.valueOf(t));
                    }
                });

        }
    }
}


/*
www.1message.com
        username:urchospitals
        password:admin123

        http://drchella.in/admin/excel_report.php?x={"adate":"07.12.2017}
*/


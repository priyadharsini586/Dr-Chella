package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.BookingDetails;
import com.hexaenna.drchella.Model.RegisterBookDetails;
import com.hexaenna.drchella.Model.TimeAndDateResponse;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.BookAppointmentActivity;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.api.SendSMSApiClient;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.service.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConformationFragment extends Fragment implements View.OnClickListener, BookAppointmentActivity.OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters

    TextView name,age,gender,appMblNum,patientMblNum,place,e_mail_id,address,date,time,hospital_name;
    View mainView;
    ProgressBar progressBar ;
    ApiInterface apiInterface;
    String isConnection = null,erodeHos,coimHos,namHos,mayHos,kollihos,cheHos;
    RelativeLayout relBar;
    TSnackbar snackbar;
    View snackbarView;
    NetworkChangeReceiver networkChangeReceiver;
    BookingDetails bookingDetails = BookingDetails.getInstance();
    RegisterBookDetails registerBookDetails = RegisterBookDetails.getInstance();
    Button btnConform,cancelbutton;
    String title,info,ok,cancel;
    String userName = "urchospitals";
    String password = "admin123";
    String senderId = "URCmed";
    String message = "";

    public ConformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConformationFragment newInstance(String param1, String param2) {
        ConformationFragment fragment = new ConformationFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

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
            mainView = inflater.inflate(R.layout.fragment_conformation, container, false);
            BookAppointmentActivity.txtToolbarText.setText(getActivity().getResources().getText(R.string.conformation_of_your));
            erodeHos = getActivity().getResources().getString(R.string.erode_hospital);
            coimHos = getActivity().getResources().getString(R.string.coimbatore_hospital);
            namHos = getActivity().getResources().getString(R.string.namakkal_hospital);
            mayHos = getActivity().getResources().getString(R.string.mayiladu_hospital);
            kollihos = getActivity().getResources().getString(R.string.kollidam_hospital);
            cheHos = getActivity().getResources().getString(R.string.chennai_hospital);

            title = getString(R.string.conformation);
            info = getString(R.string.info_description);
            ok= getString(R.string.ok_button);
            cancel = getString(R.string.cancel_button);



        }else if (databaseHandler.getContact("0").equals("Tamil"))
        {
            mainView = inflater.inflate(R.layout.tamil_fragment_conformation, container, false);
            BookAppointmentActivity.txtToolbarText.setText(getActivity().getResources().getText(R.string.tamil_conformation_of_your));

            erodeHos = getActivity().getResources().getString(R.string.tamil_erode_hospital);
            coimHos = getActivity().getResources().getString(R.string.tamil_coimbatore_hospital);
            namHos = getActivity().getResources().getString(R.string.tamil_namakkal_hospital);
            mayHos = getActivity().getResources().getString(R.string.tamil_mayiladu_hospital);
            kollihos = getActivity().getResources().getString(R.string.tamil_kollidam_hospital);
            cheHos = getActivity().getResources().getString(R.string.tamil_chennai_hospital);

            title = getString(R.string.tamil_conformation);
            info = getString(R.string.tamil_info_description);
            ok= getString(R.string.tamil_ok_button);
            cancel = getString(R.string.tamil_cancel);

        }




        name = (TextView) mainView.findViewById(R.id.name);
        name.setText(registerBookDetails.getName());
        age = (TextView) mainView.findViewById(R.id.age);
        age.setText(registerBookDetails.getAge());

        appMblNum = (TextView) mainView.findViewById(R.id.apllicantNum);
        appMblNum.setText("+91 " +registerBookDetails.getApplicantNumber());

        patientMblNum = (TextView) mainView.findViewById(R.id.patientNum);
        patientMblNum.setText("+91 " +registerBookDetails.getPatientNumber());
        place = (TextView) mainView.findViewById(R.id.place);
        place.setText(registerBookDetails.getPlace());
        e_mail_id = (TextView) mainView.findViewById(R.id.e_mail);
        e_mail_id.setText(registerBookDetails.getE_mailid());
        address = (TextView) mainView.findViewById(R.id.address);
        address.setText(registerBookDetails.getAddress());
        gender = (TextView) mainView.findViewById(R.id.gender);
        gender.setText(registerBookDetails.getGender());

        relBar = (RelativeLayout) mainView.findViewById(R.id.relBar);
        progressBar = (ProgressBar) mainView.findViewById(R.id.progressConform);
        progressBar.setVisibility(View.GONE);

        hospital_name = (TextView) mainView.findViewById(R.id.hospital_name);

        if (bookingDetails.getSelectedCity().equals("1"))
        {
            hospital_name.setText(cheHos);
        }else if (bookingDetails.getSelectedCity().equals("2"))
        {
            hospital_name.setText(erodeHos);
        }else if (bookingDetails.getSelectedCity().equals("3"))
        {
            hospital_name.setText(coimHos);
        }else if (bookingDetails.getSelectedCity().equals("4"))
        {
            hospital_name.setText(namHos);
        }else if (bookingDetails.getSelectedCity().equals("5"))
        {
            hospital_name.setText(mayHos);
        }else if (bookingDetails.getSelectedCity().equals("6"))
        {
            hospital_name.setText(kollihos);
        }
        btnConform = (Button) mainView.findViewById(R.id.btnConform);
        btnConform.setOnClickListener(this);
        cancelbutton = (Button) mainView.findViewById(R.id.cancelbutton);
        cancelbutton.setOnClickListener(this);

        date = (TextView) mainView.findViewById(R.id.date);
        date.setText(bookingDetails.getSelectedDate());
        time = (TextView) mainView.findViewById(R.id.timeCon);
        time.setText(bookingDetails.getSelectedTime());
        ((BookAppointmentActivity) getActivity()).setOnBackPressedListener(this);
        return mainView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnConform:
                conform();
                break;

            case R.id.cancelbutton:
                showDialg();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ConformationFragment conformationFragment = (ConformationFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CONGIRMATION_ FRAGMENT");
        if (conformationFragment != null && conformationFragment.isVisible()) {
            //DO STUFF
            showDialg();
        }
        else {
            //Whatever
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void cancelAppointment()
    {

        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("app_sno",bookingDetails.getAppSeno());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
            Call<TimeAndDateResponse> call = apiInterface.cancel_appintment(jsonObject);
            call.enqueue(new Callback<TimeAndDateResponse>() {
                @Override
                public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAndDateResponse timeAndDateResponse = response.body();

                        if (timeAndDateResponse.getStatus_code() != null) {
                            if (timeAndDateResponse.getStatus_code().equals(Constants.status_code1)) {
                                Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            } else if (timeAndDateResponse.getStatus_code().equals(Constants.status_code_1)) {
                                if (timeAndDateResponse.getStatus_message() != null)
                                    Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                            } else if (timeAndDateResponse.getStatus_code().equals(Constants.status_code0)) {
                                if (timeAndDateResponse.getStatus_message() != null)
                                    Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
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
                    .make(relBar, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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

    private void conform() {


        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                jsonObject.put("city",bookingDetails.getSelectedCity());
                jsonObject.put("adate",bookingDetails.getSelectedDate());
                jsonObject.put("atime",bookingDetails.getSelectedTime());
                jsonObject.put("user_email_id",userRegisterDetails.getE_mail());
                jsonObject.put("app_sno",bookingDetails.getAppSeno());
                jsonObject.put("name",registerBookDetails.getName());
                jsonObject.put("age",registerBookDetails.getAge());
                jsonObject.put("gender",registerBookDetails.getGender());
                jsonObject.put("applcnt_mobile",registerBookDetails.getApplicantNumber());
                jsonObject.put("patient_moble",registerBookDetails.getPatientNumber());
                jsonObject.put("place",registerBookDetails.getPlace());
                jsonObject.put("email",registerBookDetails.getE_mailid());
                jsonObject.put("address",registerBookDetails.getAddress());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
            Call<TimeAndDateResponse> call = apiInterface.book_appintment(jsonObject);
            call.enqueue(new Callback<TimeAndDateResponse>() {
                @Override
                public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAndDateResponse timeAndDateResponse = response.body();

                        if (timeAndDateResponse.getStatus_code() != null) {
                            if (timeAndDateResponse.getStatus_code().equals(Constants.status_code1)) {
                                Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                                sendSms(timeAndDateResponse.getUniqid());
                                getActivity().finish();
                            } else if (timeAndDateResponse.getStatus_code().equals(Constants.status_code_1)) {
                                if (timeAndDateResponse.getStatus_message() != null)
                                    Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                            } else if (timeAndDateResponse.getStatus_code().equals(Constants.status_code0)) {
                                if (timeAndDateResponse.getStatus_message() != null)
                                    Toast.makeText(getActivity(), timeAndDateResponse.getStatus_message(), Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
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
                    .make(relBar, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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

    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {
                snackbar = TSnackbar
                        .make(relBar, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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


    private void showDialg() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle(title);
        builder.setMessage(info);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                RegisterDetailsFragment.countDownTimer.cancel();
                cancelAppointment();

            }
        });
        builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.show();
    }



    public  void sendSms(String uniqueId)
    {

        BookingDetails bookingDetails  = BookingDetails.getInstance();
        message =  "Dear "+ registerBookDetails.getName()+ ", You have an appointment with Dr.Chella on " +bookingDetails.getSelectedDate() + " at " + bookingDetails.getSelectedTime()+".GET WELL SOON";
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = SendSMSApiClient.getClient().create(ApiInterface.class);

            Call<String> call = apiInterface.sendMessage(userName,password,senderId,message,registerBookDetails.getPatientNumber(),uniqueId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {

                        Log.e("response", response.body());

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Failed",  t.getMessage());



                }


            });

        }else
        {
            snackbar = TSnackbar
                    .make(relBar, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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
}

package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.BookingDetails;
import com.hexaenna.drchella.Model.RegisterBookDetails;
import com.hexaenna.drchella.Model.TimeAndDateResponse;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.BookAppointmentActivity;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.service.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDetailsFragment extends Fragment implements View.OnClickListener, BookAppointmentActivity.OnBackPressedListener {

    View mainView;
    MaterialSpinner spnSirName;
    Toolbar mToolbar;
    LinearLayout ldtPreviosFragment,ldtNextFragment;
    EditText edtName,edtCity,edtE_mail,edtAge,edtPatientMobileNumber,edtApplicantNumber,edtPlace,edtAddress;
    TextInputLayout txtInputName,txtInputAge,txtInputApplicantMobileNumber,txtInputMobileNumber,txtInputPlace,txtInputemail,txtInputaddress;
    RadioButton radioMale,radioFemale,radioTrans;
    RadioGroup radioSexGroup;
    String checkGender,male,female,trans ;
    RegisterBookDetails registerBookDetails =RegisterBookDetails.getInstance();
    TextView txtCity,txttimer;
    public static  CountDownTimer countDownTimer;
    final boolean[] isGender = {false};
    String isConnection = null;
    TSnackbar snackbar;
    NetworkChangeReceiver networkChangeReceiver;
    BookingDetails bookingDetails = BookingDetails.getInstance();
    ApiInterface apiInterface;
    View snackbarView;
    ScrollView relBar;
    ProgressBar progressRegister;
    String sirNAme = "Mr";
    public RegisterDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            mainView = inflater.inflate(R.layout.fragment_register_details, container, false);
            BookAppointmentActivity.txtToolbarText.setText(getActivity().getResources().getText(R.string.patient_details));

            male = getActivity().getResources().getString(R.string.english_male);
            female = getActivity().getResources().getString(R.string.english_female);
            trans = getActivity().getResources().getString(R.string.english_transgender);


        }else if (databaseHandler.getContact("0").equals("Tamil"))
        {
            mainView = inflater.inflate(R.layout.tamil_fragment_register_details, container, false);
            BookAppointmentActivity.txtToolbarText.setText(getActivity().getResources().getText(R.string.tamil_patient_details));

            male = getActivity().getResources().getString(R.string.tamil_male);
            female = getActivity().getResources().getString(R.string.tamil_female);
            trans = getActivity().getResources().getString(R.string.tamil_transgender);

        }

        spnSirName = (MaterialSpinner) mainView.findViewById(R.id.surname);
        final String[] SPINNERLIST = {"Mr", "Mrs", "Ms", "Dr","Er"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.city_list_items, SPINNERLIST);
        spnSirName.setAdapter(arrayAdapter);
        spnSirName.setSelection(0);
        spnSirName.setPaddingSafe(5,5,15,5);

        spnSirName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sirNAme = SPINNERLIST[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtCity = (TextView) mToolbar.findViewById(R.id.txtCity);
        txtCity.setVisibility(View.GONE);
        txttimer = (TextView) mToolbar.findViewById(R.id.txttimer);
        txttimer.setVisibility(View.VISIBLE);

        ldtPreviosFragment = (LinearLayout) mainView.findViewById(R.id.ldtPreviosFragment);
        ldtPreviosFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getToPreviousFragment();

            }
        });
        ldtNextFragment = (LinearLayout) mainView.findViewById(R.id.ldtNextFragment);
        ldtNextFragment.setOnClickListener(this);

        edtName =(EditText)mainView.findViewById(R.id.input_name);
        edtAge = (EditText) mainView.findViewById(R.id.edtAge);
        edtCity = (EditText)mainView.findViewById(R.id.edtCity);
        edtE_mail = (EditText) mainView.findViewById(R.id.edtmail);
        edtApplicantNumber = (EditText) mainView.findViewById(R.id.edtApplicantMobileNumber);
        edtPatientMobileNumber = (EditText) mainView.findViewById(R.id.edtMobileNumber);
        edtPlace = (EditText) mainView.findViewById(R.id.edtPlace);
        edtE_mail = (EditText) mainView.findViewById(R.id.edtE_mail);
        edtAddress = (EditText) mainView.findViewById(R.id.edtaddress);

        txtInputName = (TextInputLayout) mainView.findViewById(R.id.txtInputName);
        txtInputAge = (TextInputLayout) mainView.findViewById(R.id.txtInputAge);
        txtInputApplicantMobileNumber = (TextInputLayout) mainView.findViewById(R.id.txtInputApplicantMobileNumber);
        txtInputMobileNumber = (TextInputLayout) mainView.findViewById(R.id.txtInputMobileNumber);
        txtInputPlace = (TextInputLayout) mainView.findViewById(R.id.txtInputPlace);
        txtInputemail = (TextInputLayout) mainView.findViewById(R.id.txtInputemail);
        txtInputaddress = (TextInputLayout) mainView.findViewById(R.id.txtInputaddress);

        radioFemale = (RadioButton) mainView.findViewById(R.id.radioFemale);
        radioMale = (RadioButton) mainView.findViewById(R.id.radioMale);
        radioTrans = (RadioButton)mainView.findViewById(R.id.radioTrans);

        radioSexGroup = (RadioGroup) mainView.findViewById(R.id.radioSexGroup);

        relBar = (ScrollView) mainView.findViewById(R.id.relBar);

         progressRegister = (ProgressBar) mainView.findViewById(R.id.progressRegister);
        progressRegister.setVisibility(View.GONE);
        startTimer();

        isApplicantMobileValidate();
        isPatientMobileValidate();
        isE_mailValidate();
        isGenderValidate();
        ((BookAppointmentActivity) getActivity()).setOnBackPressedListener(this);
        return mainView;
    }

    private void getToPreviousFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.fragment_container, new DateAndTimeFragment(), "DATE_AND_TIME_FRAGMENT");
        fragmentTransaction.addToBackStack("DATE_AND_TIME_FRAGMENT");
        fragmentTransaction.commit();
        BookAppointmentActivity.ldtBookingDetails.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        BookAppointmentActivity.txtBooking.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    // TODO: Rename method, update argument and hook method into UI event

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
            case R.id.ldtNextFragment:
               /* if (isNameEmpty() && isAgeValidate() && isGender[0] && isApplicantMobileValidate() && isPatientMobileValidate() && isPlaceEmpty() && isE_mailValidate() && isAddressEmpty()) {
                    getNextFragment();
                }*/
                getNextFragment();
                break;
        }
    }

    private void getNextFragment() {


        registerBookDetails.setName(sirNAme +". "+edtName.getText().toString());
        registerBookDetails.setAge(edtAge.getText().toString());
        registerBookDetails.setApplicantNumber(edtApplicantNumber.getText().toString());
        registerBookDetails.setPatientNumber(edtPatientMobileNumber.getText().toString());
        registerBookDetails.setPlace(edtPlace.getText().toString());
        registerBookDetails.setE_mailid(edtE_mail.getText().toString());
        registerBookDetails.setAddress(edtAddress.getText().toString());

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        fragmentTransaction.replace(R.id.fragment_container, new ConformationFragment(), "CONGIRMATION_ FRAGMENT");
        fragmentTransaction.addToBackStack("CONGIRMATION_ FRAGMENT");
        fragmentTransaction.commit();
        BookAppointmentActivity.ldtConformation.setBackgroundColor(getActivity().getResources().getColor(R.color.book_title_orange));
        BookAppointmentActivity.txtConformation.setTextColor(getActivity().getResources().getColor(R.color.white));
    }
    public boolean isGenderValidate()
    {
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                isGender[0] = true;
                
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                RadioButton radioSexButton = (RadioButton) getActivity().findViewById(selectedId);

                if (radioSexButton.equals(radioMale))
                {
                    checkGender = "1";
                    registerBookDetails.setGender(male);

                }else if(radioSexButton.equals(radioFemale))
                {
                    checkGender = "2";
                    registerBookDetails.setGender(female);
                }else if (radioSexButton.equals(radioTrans))
                {
                    checkGender = "3";
                    registerBookDetails.setGender(trans);
                }

            }
        });


        return isGender[0];
    }



    public boolean isApplicantMobileValidate()
    {
        final boolean[] isMblNum = {false};
        edtApplicantNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() < 10)
                {
                    txtInputApplicantMobileNumber.setErrorEnabled(true);
                    txtInputApplicantMobileNumber.setError("Enter your correct mobile number");
                    isMblNum[0] = false;

                }else
                {
                    isMblNum[0] = true;
                    txtInputApplicantMobileNumber.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(edtApplicantNumber.getText().toString().isEmpty())
        {


            isMblNum[0] = false;

        }else
        {
            isMblNum[0] = true;

        }
        return isMblNum[0];
    }

    public boolean isPatientMobileValidate()
    {
        final boolean[] isMblNum = {false};
        edtPatientMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() < 10)
                {
                    txtInputMobileNumber.setErrorEnabled(true);
                    txtInputMobileNumber.setError("Enter your correct mobile number");
                    isMblNum[0] = false;

                }else
                {
                    isMblNum[0] = true;
                    txtInputMobileNumber.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(edtPatientMobileNumber.getText().toString().isEmpty())
        {


            isMblNum[0] = false;

        }else
        {
            isMblNum[0] = true;

        }
        return isMblNum[0];
    }

    public boolean isAgeValidate()
    {
        final boolean[] isAgeNum = {false};
        edtAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() == 0)
                {
                    txtInputAge.setErrorEnabled(true);
                    txtInputAge.setError("Enter your age");
                    isAgeNum[0] = false;

                }else
                {
                    isAgeNum[0] = true;
                    txtInputAge.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(edtAge.getText().toString().isEmpty())
        {
            txtInputAge.setErrorEnabled(true);
            txtInputAge.setError("Enter your age");
            isAgeNum[0] = false;

        }else
        {
            isAgeNum[0] = true;
            txtInputAge.setErrorEnabled(false);

        }
        return isAgeNum[0];
    }

    public boolean isE_mailValidate()
    {
        final boolean[] isE_mail = {false};
        edtE_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() == 0)
                {
                    txtInputemail.setErrorEnabled(true);
                    txtInputemail.setError("Enter your correct e_mail");
                    isE_mail[0] = false;

                }else
                {
                    if (isValidEmail(s.toString()))
                    {
                        isE_mail[0] = true;
                        txtInputemail.setErrorEnabled(false);

                    }else {
                        txtInputemail.setErrorEnabled(true);
                        txtInputemail.setError("Enter your correct email");
                        isE_mail[0] = false;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(edtE_mail.getText().toString().isEmpty())
        {

            isE_mail[0] = false;

        }else
        {
            isE_mail[0] = true;

        }

        return isE_mail[0];
    }


    public boolean isPlaceEmpty()
    {
        final boolean[] isPlace = {false};

        if (edtPlace.getText().toString().trim().isEmpty())
        {
            txtInputPlace.setErrorEnabled(true);
            txtInputPlace.setError("Enter your place");
            isPlace[0] = false;
        }else
        {
            isPlace[0] = true;
            txtInputPlace.setErrorEnabled(false);
        }
        return isPlace[0];
    }

    public boolean isNameEmpty()
    {
        final boolean[] isPlace = {false};

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() == 0)
                {
                    txtInputName.setErrorEnabled(true);
                    txtInputName.setError("Enter your Name");
                    isPlace[0] = false;

                }else
                {
                    isPlace[0] = true;
                    txtInputName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if (edtName.getText().toString().trim().isEmpty())
        {
            txtInputName.setErrorEnabled(true);
            txtInputName.setError("Enter your Name");
            isPlace[0] = false;
        }else
        {
            isPlace[0] = true;
            txtInputName.setErrorEnabled(false);
        }
        return isPlace[0];
    }
    public boolean isAddressEmpty()
    {
        final boolean[] isPlace = {false};

        if (edtAddress.getText().toString().trim().isEmpty())
        {
            txtInputaddress.setErrorEnabled(true);
            txtInputaddress.setError("Enter your address");
            isPlace[0] = false;
        }else
        {
            isPlace[0] = true;
            txtInputaddress.setErrorEnabled(false);
        }
        return isPlace[0];
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void startTimer()
    {
        countDownTimer = new CountDownTimer(60*3*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                String text = String.format(Locale.getDefault(), "%02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                txttimer.setText(text);
                Log.e("text",String.valueOf(text));
                if (text.equals("00 : 10 "))
                {
                    Animation startAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.blinking_animation);
                    txttimer.startAnimation(startAnimation);
                    txttimer.setTextColor(getActivity().getResources().getColor(R.color.red_not_avaliable));
                }
            }

            @Override
            public void onFinish() {
                cancelAppointment();
                Log.e("time finish","timer finished");

            }
        }.start();
    }



    public void cancelAppointment()
    {
        progressRegister.setVisibility(View.VISIBLE);
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("app_sno",bookingDetails.getAppSeno());

            } catch (JSONException e) {
                e.printStackTrace();
            }

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

                        }
                        progressRegister.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        Log.e("backpressed","register fragment");
        countDownTimer.cancel();
        countDownTimer = null;

        cancelAppointment();
    }
}


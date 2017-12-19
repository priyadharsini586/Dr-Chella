package com.hexaenna.drchella.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.service.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;

    ApiInterface apiInterface;
    String isConnection = null;
    ScrollView sclRegisterMain;
    TSnackbar snackbar;
    View snackbarView;
    NetworkChangeReceiver networkChangeReceiver;
    ProgressBar progressBar;
    EditText edtName,edtCity,edtMbl,edtE_mail,edtAge;
    TextInputLayout txtInputName,txtInputGender,txtInputAge,txtInputCity,txtInputMobile,txtInputEMail;
    RadioButton radioMale,radioFemale,radioTrans;
    RadioGroup radioSexGroup;
    String checkGender ;
    final boolean[] isGender = {false};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        showLanguage();
        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);

                if (isConnection == null) {
                    Bundle b = intent.getExtras();
                    isConnection = b.getString(Constants.MESSAGE);
                    Log.e("newmesage", "" + isConnection);
                    getNetworkState();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        this.registerReceiver(networkChangeReceiver,
                intentFilter);

        setContentView(R.layout.registration_activity);
        setView();
        final DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        databaseHandler.addLanguage("English","0");
    }

    private void showLanguage() {

        final DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        /*final String[] selected = new String[1];
        final AlertDialog.Builder[] builder = {new AlertDialog.Builder(RegistrationActivity.this,R.style.MyAlertDialogMaterialStyle)};
        final  String array[] = getApplicationContext().getResources().getStringArray(R.array.language);
        builder[0].setTitle("Select Your Language");
        selected[0] = array[0].toString();
        builder[0].setSingleChoiceItems(R.array.language, 0,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int id) {

                       selected[0] = array[id].toString();
                        Log.e("selected", selected[0]);


                    }
                })

                // Set the action buttons
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        databaseHandler.addLanguage(selected[0],"0");
                        if (databaseHandler.getContact("0").equals("English"))
                        {
                            setContentView(R.layout.registration_activity);
                            setView();


                        }else if (databaseHandler.getContact("0").equals("Tamil"))
                        {
                            setContentView(R.layout.tamil_registration_activity);
                            setView();
                        }
                    }
                });


        builder[0].setCancelable(false);*/
        final Dialog alertDialog = new Dialog(RegistrationActivity.this);
        alertDialog.setTitle("Select Your Language");
        alertDialog.setContentView(R.layout.language_dialog_box);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(R.color.white)));
       final RadioGroup radGroupLangu = (RadioGroup) alertDialog.findViewById(R.id.radGroupLangu);
        radGroupLangu.check(R.id.radEnglish);
        Button btnOk = (Button) alertDialog.findViewById(R.id.btnOk);
        ProgressBar progressChangeLang = (ProgressBar) alertDialog.findViewById(R.id.progressChangeLang);
        progressChangeLang.setVisibility(View.GONE);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=radGroupLangu.getCheckedRadioButtonId();
                String selected = "English";
                if (selectedId == R.id.radEnglish)
                {
                    selected = "English";
                }else if (selectedId == R.id.radTamil)
                {
                    selected = "Tamil";
                }
                Log.e("selected",selected);
                databaseHandler.addLanguage(selected,"0");
                alertDialog.dismiss();
                if (databaseHandler.getContact("0").equals("English"))
                {
                    setContentView(R.layout.registration_activity);
                    setView();


                }else if (databaseHandler.getContact("0").equals("Tamil"))
                {
                    setContentView(R.layout.tamil_registration_activity);
                    setView();
                }
            }
        });

        alertDialog.show();





    }

    private void setView()
    {
        btnRegister = (Button) findViewById(R.id.btnReg);
        btnRegister.setOnClickListener(this);

        sclRegisterMain = (ScrollView) findViewById(R.id.sclRegisterMain);

        edtName =(EditText)findViewById(R.id.edtName);
        edtAge = (EditText) findViewById(R.id.inputAge);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtMbl = (EditText) findViewById(R.id.edtMobile);
        edtE_mail = (EditText) findViewById(R.id.edtmail);

        txtInputName = (TextInputLayout) findViewById(R.id.txtInputName);
        txtInputGender = (TextInputLayout) findViewById(R.id.txtInputGender);
        txtInputCity = (TextInputLayout) findViewById(R.id.txtInputCity);
        txtInputAge = (TextInputLayout) findViewById(R.id.txtInputAge);
        txtInputMobile = (TextInputLayout) findViewById(R.id.txtInputMobile);
        txtInputEMail = (TextInputLayout) findViewById(R.id.txtInpute_mail);

        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioTrans = (RadioButton)findViewById(R.id.radioTrans);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSexGroup);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle.getString("email") != null) {
            String b = bundle.getString("email");
            edtE_mail.setText(b);
            edtE_mail.setEnabled(false);
        }


        isGenderValidate();
        isMobileValidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnReg:
                /*Intent intent = new Intent(getApplicationContext(),OTPActivity.class);
                startActivity(intent);
                RegistrationActivity.this.finish();*/

                checkValidation();
                break;
        }
    }

    private void checkValidation() {


        if (isNameValidate() && isAgeValidate() && isCityValidate() && isMobileValidate() && isE_mailValidate() && isGenderValidate())
        {
            progressBar.setVisibility(View.VISIBLE);
            registerDetails();

        }
    }

    private void registerDetails() {

        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

           /* String name = edtName.getText().toString().trim();
            String companyName = edtCompanyName.getText().toString().trim();
            String cityname  =  edtCity.getText().toString().trim();
            String pincode =edtPinCode.getText().toString().trim();
            String mbl = edtMbl.getText().toString().trim();
            final String e_mail = edtE_mail.getText().toString().trim();*/

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name",edtName.getText().toString());
                jsonObject.put("gender",checkGender);
                jsonObject.put("age",edtAge.getText().toString());
                jsonObject.put("city",edtCity.getText().toString());
                jsonObject.put("mobile",edtMbl.getText().toString());
                jsonObject.put("email",edtE_mail.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<RegisterRequestAndResponse> call = apiInterface.registerDetails(jsonObject);
            call.enqueue(new Callback<RegisterRequestAndResponse>() {
                @Override
                public void onResponse(Call<RegisterRequestAndResponse> call, Response<RegisterRequestAndResponse> response) {
                    if (response.isSuccessful()) {
                        RegisterRequestAndResponse login = response.body();
                        if (login.getStatus_code() != null)
                        {
                            if (login.getStatus_code().equals(Constants.status_code1))
                            {
                                Intent intent = new Intent(getApplicationContext(),OTPActivity.class);
                                UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                                userRegisterDetails.setMobileNum(edtMbl.getText().toString());
                                userRegisterDetails.setUniqueId(login.getUnique_code());
                                userRegisterDetails.setOtp(login.getVerify_code());
                                Bundle bundle = new Bundle();
                                bundle.putString("fromWhere", Constants.status_code1);
                                intent.putExtras(bundle);
                                userRegisterDetails.setE_mail(edtE_mail.getText().toString());
                                DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                                databaseHandler.addUser(edtName.getText().toString(),edtMbl.getText().toString(),"0","","user");
                                startActivity(intent);
                                RegistrationActivity.this.finish();
                            }else
                            {
                                if (login.getStatus_message() != null)
                                    Toast.makeText(getApplicationContext(),login.getStatus_message(),Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);

                        }

                    }
                }

                @Override
                public void onFailure(Call<RegisterRequestAndResponse> call, Throwable t) {
                    Log.e("output", t.getMessage());
                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(sclRegisterMain, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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
                        .make(sclRegisterMain, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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




    public boolean isNameValidate()
    {
        final boolean[] isName = {false};
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
                    isName[0] = false;

                }else
                {
                    isName[0] = true;
                    txtInputName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if(edtName.getText().toString().isEmpty())
        {
            txtInputName.setErrorEnabled(true);
            txtInputName.setError("Enter your Name");
            isName[0] = false;

        }else
        {
            isName[0] = true;
            txtInputName.setErrorEnabled(false);

        }

        return isName[0];
    }

    public boolean isGenderValidate()
    {
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                isGender[0] = true;

                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                RadioButton radioSexButton = (RadioButton) findViewById(selectedId);


                if (radioSexButton.equals(radioMale))
                {
                    checkGender = "1";


                }else if(radioSexButton.equals(radioFemale))
                {
                    checkGender = "2";

                }else if (radioSexButton.equals(radioTrans))
                {
                    checkGender = "3";

                }


                Toast.makeText(RegistrationActivity.this,radioSexButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        return isGender[0];
    }


    public boolean isCityValidate()
    {
        final boolean[] isCityName = {false};
        edtCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() == 0)
                {
                    txtInputCity.setErrorEnabled(true);
                    txtInputCity.setError("Enter your City Name");
                    isCityName[0] = false;

                }else
                {
                    isCityName[0] = true;
                    txtInputCity.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        if(edtCity.getText().toString().isEmpty())
        {

            txtInputCity.setErrorEnabled(true);
            txtInputCity.setError("Enter your City Name");
            isCityName[0] = false;

        }else
        {
            isCityName[0] = true;
            txtInputCity.setErrorEnabled(false);

        }

        return isCityName[0];
    }
    public boolean isMobileValidate()
    {
        final boolean[] isMblNum = {false};
        edtMbl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() < 10)
                {
                    txtInputMobile.setErrorEnabled(true);
                    txtInputMobile.setError("Enter your correct mobile number");
                    isMblNum[0] = false;

                }else
                {
                    isMblNum[0] = true;
                    txtInputMobile.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(edtMbl.getText().toString().isEmpty())
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
                    txtInputAge.setError("Enter the City Name");
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
            txtInputAge.setError("Enter the City Name");
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
                    txtInputEMail.setErrorEnabled(true);
                    txtInputEMail.setError("Enter the correct e_mail");
                    isE_mail[0] = false;

                }else
                {
                    if (isValidEmail(s.toString()))
                    {
                        isE_mail[0] = true;
                        txtInputEMail.setErrorEnabled(false);

                    }else {
                        txtInputEMail.setErrorEnabled(true);
                        txtInputEMail.setError("Enter the correct email");
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


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (networkChangeReceiver == null)
        {
            Log.e("reg","Do not unregister receiver as it was never registered");
        }
        else
        {
            Log.e("reg","Unregister receiver");
            unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
    }
}

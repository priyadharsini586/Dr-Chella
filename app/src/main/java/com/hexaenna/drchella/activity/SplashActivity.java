package com.hexaenna.drchella.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.androidadvance.topsnackbar.TSnackbar;
import com.hexaenna.drchella.Model.BookingDetails;
import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.utils.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    LinearLayout ldtSplash;
    private Animation animBounce;
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    String isConnection = null;
    RelativeLayout rldMainLayout;
    TSnackbar snackbar;
    View snackbarView;
    NetworkChangeReceiver networkChangeReceiver;
    ApiInterface apiInterface;
    String alreadySend = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);


                if (isConnection == null) {
                    Bundle b = intent.getExtras();
                    isConnection = b.getString(Constants.MESSAGE);
                    getNetworkState();
                }else
                {
                    Bundle b = intent.getExtras();
                    isConnection = b.getString(Constants.MESSAGE);
                    getNetworkState();
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        this.registerReceiver(networkChangeReceiver,
                intentFilter);
        setContentView(R.layout.splash_activity);

        ldtSplash = (LinearLayout) findViewById(R.id.ldtSplash);

        rldMainLayout = (RelativeLayout) findViewById(R.id.rldMainLayout);

        animBounce = AnimationUtils.loadAnimation(this, R.anim.splash_bounce);
        animBounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                checkEmail();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        permissions.add(Manifest.permission.GET_ACCOUNTS);
        permissions.add(Manifest.permission.READ_SMS);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.SEND_SMS);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= 23) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
                if (isConnection != null) {
                    if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
                        if (snackbar != null) {
                            snackbar.dismiss();


                        }

                    }
                }
            } else {
                ldtSplash.startAnimation(animBounce);
            }


        }
    }


      @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d("request", "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            String msg = "These permissions are mandatory for the application. Please allow access.";
                            showMessageOKCancel(msg,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);

                                                ldtSplash.startAnimation(animBounce);
//
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    getE_mail();
                    ldtSplash.startAnimation(animBounce);
                    checkEmail();
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setPositiveButton("OK",okListener )
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                })
                .create()
                .show();
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public  String getE_mail()
    {
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        String e_mail = null;
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                e_mail = account.name;
                BookingDetails bookingDetails = BookingDetails.getInstance();
                bookingDetails.setE_mail(e_mail);
                break;

            }
        }

        return e_mail;
    }

    public void onPause() {
        super.onPause();
        Log.e("e_mail","onResume");
        checkEmail();
        if (snackbar != null)
            snackbar.dismiss();

       /* if (networkChangeReceiver != null)
            unregisterReceiver(networkChangeReceiver);*/
    }



    public void onResume() {
        super.onResume();
        Log.e("e_mail","onResume");

     /*   IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        this.registerReceiver(networkChangeReceiver,
                intentFilter);*/
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


    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {
                snackbar = TSnackbar
                        .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Action Button", "onClick triggered");
                                checkEmail();

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

                    if (getE_mail() != null)
                        checkEmail();

                }
            }
        }
    }
    private void checkEmail() {

        if (alreadySend.equals("")) {
            if (isConnection != null) {
                if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
                    final String e_mail = getE_mail();
                    alreadySend = "send";
//            Log.e("djjkdfdhd",e_mail);
                    apiInterface = ApiClient.getClient().create(ApiInterface.class);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", e_mail);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<RegisterRequestAndResponse> call = apiInterface.checkEmail(jsonObject);
                    call.enqueue(new Callback<RegisterRequestAndResponse>() {
                        @Override
                        public void onResponse(Call<RegisterRequestAndResponse> call, Response<RegisterRequestAndResponse> response) {
                            if (response.isSuccessful()) {
                                RegisterRequestAndResponse login = response.body();

                                if (login.getStatus_code() != null) {

                                    Log.e("output from splash", login.getStatus_message());
                                    if (login.getStatus_code().equals(Constants.status_code0)) {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent mainIntent = new Intent(SplashActivity.this, RegistrationActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("email", e_mail);
                                                mainIntent.putExtras(bundle);
                                                SplashActivity.this.startActivity(mainIntent);
                                                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                                                SplashActivity.this.finish();
                                            }
                                        }, 2500);
                                    } else if (login.getStatus_code().equals(Constants.status_code_1)) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent mainIntent = new Intent(SplashActivity.this, OTPActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("email", e_mail);
                                                mainIntent.putExtras(bundle);
                                                SplashActivity.this.startActivity(mainIntent);
                                                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                                                SplashActivity.this.finish();
                                            }
                                        }, 2500);

                                    } else if (login.getStatus_code().equals(Constants.status_code1)) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                                                SplashActivity.this.startActivity(mainIntent);
                                                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                                                SplashActivity.this.finish();
                                            }
                                        }, 2500);
                                    }
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterRequestAndResponse> call, Throwable t) {
                            Log.e("output", t.getMessage());
                        }
                    });

                } else {
                    snackbar = TSnackbar
                            .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e("Action Button", "onClick triggered");
                                    checkEmail();
                                }
                            });
                    snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
                    snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    textView.setTypeface(null, Typeface.BOLD);
                    Log.e("show", "show in checkmail");
                    snackbar.show();
                }
            }
        }
    }
}

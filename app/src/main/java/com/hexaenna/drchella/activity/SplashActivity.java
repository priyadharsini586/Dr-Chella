package com.hexaenna.drchella.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.BookingDetails;
import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.Model.TimeAndDateResponse;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.utils.Config;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.hexaenna.drchella.utils.LoadImageTask;
import com.hexaenna.drchella.utils.NotificationUtils;
import com.hexaenna.drchella.utils.UtilsClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements  LoadImageTask.Listener {

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
    boolean isPermission ;

    private static final String TAG = SplashActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       databaseHandler = new DatabaseHandler(getApplicationContext());

        if (!databaseHandler.checkForTables()) {
            networkChangeReceiver = new NetworkChangeReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    super.onReceive(context, intent);

//                    if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {


                        if (isConnection == null) {
                            Bundle b = intent.getExtras();
                            if (b.getString(Constants.MESSAGE) != null) {
                                isConnection = b.getString(Constants.MESSAGE);
                                if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                                    if (alreadySend.equals("")) {
                                        Log.e("check splash", "check");
                                        getNetworkState();

                                    }
                                }
                            }
                        }

//                    }

                }

            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction(Constants.BROADCAST);
            this.registerReceiver(networkChangeReceiver,
                    intentFilter);
        }
//        }
        setContentView(R.layout.splash_activity);

        ldtSplash = (LinearLayout) findViewById(R.id.ldtSplash);
        UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
        userRegisterDetails.setE_mail(getE_mail());
        rldMainLayout = (RelativeLayout) findViewById(R.id.rldMainLayout);
        animBounce = AnimationUtils.loadAnimation(this, R.anim.splash_bounce);
        animBounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (!databaseHandler.checkForTables()) {
                    checkEmail();

                }else
                {
                    Log.e("check db","check db");
                    UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                    userRegisterDetails.setE_mail(getE_mail());
                    Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email", getE_mail());
                    mainIntent.putExtras(bundle);
                    SplashActivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    SplashActivity.this.finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (!databaseHandler.checkForTables())
        {
            ldtSplash.startAnimation(animBounce);
        }

        permissions.add(Manifest.permission.GET_ACCOUNTS);
        permissions.add(Manifest.permission.READ_SMS);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
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
                isPermission =false;

                Toast.makeText(getApplicationContext(),"Permissions not granted.", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(),"Permissions already granted.", Toast.LENGTH_LONG).show();
                isPermission = true;
                ldtSplash.startAnimation(animBounce);
            }


        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                        displayFirebaseRegId();

                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");

                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                    }
                }
        };
        displayFirebaseRegId();





    }

    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);


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


                                                isPermission = true;
                                                ldtSplash.startAnimation(animBounce);
//
                                            }
                                        }
                                    });
                            Log.e("check","inpermission");
                            return;
                        }
                    }
                } else {
                    isPermission = true;
                    Log.e("check","inpermission");
                   /* getE_mail();
                    ldtSplash.startAnimation(animBounce);
                    checkEmail();*/
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
        //        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        if (snackbar != null)
            snackbar.dismiss();

       /* if (networkChangeReceiver != null)
            unregisterReceiver(networkChangeReceiver);*/
    }



    public void onResume() {
        super.onResume();
        Log.e("e_mail","onResume");
        if (alreadySend.equals("")) {
            ldtSplash.startAnimation(animBounce);
            checkEmail();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
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
                if (alreadySend.equals(""))
                {
                    checkEmail();
                }
            }
        }
    }
    private void checkEmail() {

        Log.e("permission check",""+isPermission);
        if (alreadySend.equals("") && getE_mail() != null && isPermission) {
            if (isConnection != null) {
                if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
                    final String e_mail = getE_mail();


//            Log.e("djjkdfdhd",e_mail);
                    apiInterface = ApiClient.getClient().create(ApiInterface.class);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", getE_mail());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<RegisterRequestAndResponse> call = apiInterface.checkEmail(jsonObject);
                    call.enqueue(new Callback<RegisterRequestAndResponse>() {
                        @Override
                        public void onResponse(Call<RegisterRequestAndResponse> call, final Response<RegisterRequestAndResponse> response) {
                            if (response.isSuccessful()) {
                                final RegisterRequestAndResponse login = response.body();

                                if (login.getStatus_code() != null) {

                                    Log.e("output from splash", login.getStatus_message());
                                    alreadySend = "send";
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
                                        }, 2000);
                                    } else if (login.getStatus_code().equals(Constants.status_code_1)) {


                                                registerDetails(Constants.status_code_1,login.getType());
                                              /*  Intent mainIntent = new Intent(SplashActivity.this, OTPActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("email", e_mail);
                                                mainIntent.putExtras(bundle);
                                                SplashActivity.this.startActivity(mainIntent);
                                                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                                                SplashActivity.this.finish();*/



                                    } else if (login.getStatus_code().equals(Constants.status_code1)) {

                                                registerDetails(Constants.status_code1,login.getType());


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



    private void registerDetails(final String from, final String type) {

        String mailId = getE_mail();
        if (isConnection.equals(Constants.NETWORK_CONNECTED) && getE_mail() != null) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();

            try {

                jsonObject.put("user_email",getE_mail());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<TimeAndDateResponse> call = apiInterface.getProfilePic(jsonObject);
            call.enqueue(new Callback<TimeAndDateResponse>() {
                @Override
                public void onResponse(Call<TimeAndDateResponse> call, Response<TimeAndDateResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAndDateResponse timeAndDateResponse = response.body();
                        if (timeAndDateResponse.getStatus_code() != null) {
                            if (timeAndDateResponse.getStatus_code().equals(Constants.status_code1)) {
                                if (timeAndDateResponse.getProfile_pic() != null) {
                                    TimeAndDateResponse dateResponse = new TimeAndDateResponse();
                                    dateResponse.setPhoto(timeAndDateResponse.getProfile_pic());
                                    UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                                    userRegisterDetails.setMobileNum(timeAndDateResponse.getMobile());
                                    if (timeAndDateResponse.getProfile_pic() != null)
                                        databaseHandler.addUser(timeAndDateResponse.getName(),timeAndDateResponse.getMobile(),"0",timeAndDateResponse.getProfile_pic(),type);
                                    else
                                        databaseHandler.addUser(timeAndDateResponse.getName(),timeAndDateResponse.getMobile(),"0","",type);

                                   /* if (timeAndDateResponse.getLang().equals("English"))
                                    {
                                        databaseHandler.addLanguage("English","0");
                                    }else if (timeAndDateResponse.getLang().equals("Tamil"))
                                    {
                                        databaseHandler.addLanguage("Tamil","0");
                                    }*/
                                    databaseHandler.addLanguage("English","0");

                                    if (timeAndDateResponse.getProfile_pic() != null)
                                        new LoadImageTask(SplashActivity.this).execute(timeAndDateResponse.getProfile_pic());
                                    if (from.equals(Constants.status_code1)) {
                                        Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("fromWhere", Constants.status_code1);
                                        mainIntent.putExtras(bundle);
                                        SplashActivity.this.startActivity(mainIntent);
                                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                        SplashActivity.this.finish();
                                    }else if (from.equals(Constants.status_code_1))
                                    {
                                        Intent mainIntent = new Intent(SplashActivity.this, OTPActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("fromWhere", Constants.status_code_1);
                                        mainIntent.putExtras(bundle);
                                        SplashActivity.this.startActivity(mainIntent);
                                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                        SplashActivity.this.finish();
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<TimeAndDateResponse> call, Throwable t) {

                }
            });

        }

    }



    @Override
    public void onImageLoaded(Bitmap bitmap) {

        UtilsClass utilsClass  = new UtilsClass();
        String strBitmap = utilsClass.BitMapToString(bitmap);
        databaseHandler.updateProfilePic("0",strBitmap);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Error Loading Image !", Toast.LENGTH_SHORT).show();
    }
}

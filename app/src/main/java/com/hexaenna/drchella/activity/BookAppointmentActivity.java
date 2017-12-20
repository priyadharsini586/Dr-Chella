package com.hexaenna.drchella.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.BookingDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.fragment.ConformationFragment;
import com.hexaenna.drchella.fragment.DateAndTimeFragment;
import com.hexaenna.drchella.fragment.RegisterDetailsFragment;
import com.hexaenna.drchella.utils.Config;
import com.hexaenna.drchella.utils.NotificationUtils;

public class BookAppointmentActivity extends AppCompatActivity {


   public static LinearLayout ldtBookingDetails,ldtDateTime,ldtConformation;
    public static TextView txtDateTime,txtBooking,txtConformation,txtToolbarText;
    protected  OnBackPressedListener onBackPressedListener ;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        if (databaseHandler.getContact("0").equals("English"))
        {
            setContentView(R.layout.book_appointment_activity);

        }else if (databaseHandler.getContact("0").equals("Tamil"))
        {
            setContentView(R.layout.tamil_book_appointment_activity);

        }


        ldtBookingDetails = (LinearLayout) findViewById(R.id.ldtBookingDetails);
        ldtDateTime = (LinearLayout) findViewById(R.id.ldtDateTime);
        ldtConformation = (LinearLayout) findViewById(R.id.ldtConformation);

        txtDateTime  = (TextView) findViewById(R.id.txtDateTime);
        txtBooking = (TextView) findViewById(R.id.txtBooking);
        txtConformation = (TextView) findViewById(R.id.txtConformation);
        txtToolbarText = (TextView) findViewById(R.id.txtToolbarText);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new DateAndTimeFragment(), "DATE_AND_TIME_FRAGMENT");
        fragmentTransaction.addToBackStack("DATE_AND_TIME_FRAGMENT");
        fragmentTransaction.commit();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);



                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    String title = intent.getStringExtra("title");

                    if (!intent.getStringExtra("from").equals("tips")) {
                        HomeActivity homeActivity = new HomeActivity();
                        homeActivity.showAlert(BookAppointmentActivity.this, title, message);
                    }



                }
            }
        };

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.onBackPressed();
        else
            super.onBackPressed();

    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }


    public interface OnBackPressedListener  {
        void onBackPressed();
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        if (RegisterDetailsFragment.countDownTimer != null) {
            RegisterDetailsFragment.countDownTimer.cancel();
            RegisterDetailsFragment.countDownTimer = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        mRegistrationBroadcastReceiver=null;

        BookingDetails bookingDetails = BookingDetails.getInstance();
        bookingDetails.setCity(null);
        bookingDetails.setSelectedCity(null);
        bookingDetails.setSelectedDate(null);
        bookingDetails.setBookedList(null);
        bookingDetails.setBlockedList(null);
        bookingDetails.setSelectedPosition(-1);
        bookingDetails.setAppSeno(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }
}

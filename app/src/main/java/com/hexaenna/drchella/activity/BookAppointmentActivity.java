package com.hexaenna.drchella.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.fragment.ConformationFragment;
import com.hexaenna.drchella.fragment.DateAndTimeFragment;
import com.hexaenna.drchella.fragment.RegisterDetailsFragment;

public class BookAppointmentActivity extends AppCompatActivity {


   public static LinearLayout ldtBookingDetails,ldtDateTime,ldtConformation;
    public static TextView txtDateTime,txtBooking,txtConformation,txtToolbarText;
    protected  OnBackPressedListener onBackPressedListener ;
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
        /**
         * Si vous retouné true le back press ne sera pas pris en compte, sinon l'activité agira naturellement
         * @return true si votre traitement est prioritaire sinon false
         */
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

}

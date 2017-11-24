package com.hexaenna.drchella.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.fragment.DateAndTimeFragment;
import com.hexaenna.drchella.fragment.YourAppointmentFragment;


public class MoreItemsActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_items);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_more_container, new YourAppointmentFragment(), "YOUR_APPOINTMENT_FRAGMENT");
        fragmentTransaction.addToBackStack("YOUR_APPOINTMENT_FRAGMENT");
        fragmentTransaction.commit();
    }



}

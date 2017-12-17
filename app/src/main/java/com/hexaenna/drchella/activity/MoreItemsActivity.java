package com.hexaenna.drchella.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.adapter.SectionsPagerAdapter;
import com.hexaenna.drchella.fragment.AllAppointmentFragment;
import com.hexaenna.drchella.fragment.ConsultationLocationFragment;
import com.hexaenna.drchella.fragment.DailyHealthTipsFragment;
import com.hexaenna.drchella.fragment.PastAppointmentFragment;
import com.hexaenna.drchella.fragment.ProfileFragment;
import com.hexaenna.drchella.fragment.SettingFragment;
import com.hexaenna.drchella.fragment.TestimonyFragment;
import com.hexaenna.drchella.fragment.UpcomingAppointmentFragment;
import com.hexaenna.drchella.utils.Config;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.utils.NotificationUtils;


public class MoreItemsActivity extends AppCompatActivity  {

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    String fromWhereView = null;
    TextView txtTitle;
    protected  OnBackPressedListener onBackPressedListener ;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_items);
        if (getIntent().hasExtra(Constants.fromMore))
        {
            fromWhereView = getIntent().getStringExtra(Constants.fromMore);

        }
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        mViewPager = (ViewPager) findViewById(R.id.appoint_container);
        mViewPager.setVisibility(View.VISIBLE);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),MoreItemsActivity.this,"more");
        setupViewPager();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.appointment_tabs);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
        }
        tabLayout.addOnTabSelectedListener(OnTabSelectedListener);


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
                        homeActivity.showAlert(MoreItemsActivity.this, title, message);
                    }



                }
            }
        };
        showView();
    }

    private void showView() {

      if (fromWhereView.equals(Constants.your_appointment))
      {
          mViewPager.setVisibility(View.VISIBLE);
          tabLayout.setVisibility(View.VISIBLE);
          txtTitle.setText("Your Appointment");
      }else if (fromWhereView.equals(Constants.daily_health_tips))
      {
          mViewPager.setVisibility(View.GONE);
          tabLayout.setVisibility(View.GONE);
          txtTitle.setText("Daily Health Tips");
          FragmentManager fragmentManager = getSupportFragmentManager();
          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
          fragmentTransaction.add(R.id.fragment_container, new DailyHealthTipsFragment(), "DAILY_HEALTH_TIPS_FRAGMENT");
          fragmentTransaction.addToBackStack("DAILY_HEALTH_TIPS_FRAGMENT");
          fragmentTransaction.commit();
      }else if (fromWhereView.equals(Constants.testimony))
      {
          mViewPager.setVisibility(View.GONE);
          tabLayout.setVisibility(View.GONE);
          txtTitle.setText("Testimony");
          FragmentManager fragmentManager = getSupportFragmentManager();
          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
          fragmentTransaction.add(R.id.fragment_container, new TestimonyFragment(), "TESTIMONY_FRAGMENT");
          fragmentTransaction.addToBackStack("TESTIMONY_FRAGMENT");
          fragmentTransaction.commit();
      }else if (fromWhereView.equals(Constants.consulation_location))
      {

          mViewPager.setVisibility(View.GONE);
          tabLayout.setVisibility(View.GONE);
          txtTitle.setText("Consulation Location");
          FragmentManager fragmentManager = getSupportFragmentManager();
          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
          fragmentTransaction.add(R.id.fragment_container, new ConsultationLocationFragment(), "CONSULATION_LOCATION_FRAGMENT");
          fragmentTransaction.addToBackStack("CONSULATION_LOCATION_FRAGMENT");
          fragmentTransaction.commit();
      }else if (fromWhereView.equals(Constants.privacy_policy))
      {

          mViewPager.setVisibility(View.GONE);
          tabLayout.setVisibility(View.GONE);
          txtTitle.setText("Privacy Policy");
          FragmentManager fragmentManager = getSupportFragmentManager();
          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
          fragmentTransaction.add(R.id.fragment_container,ProfileFragment.newInstance("from",Constants.privacy_policy), "PRIVACY_POLICY_FRAGMENT");
          fragmentTransaction.addToBackStack("PRIVACY_POLICY_FRAGMENT");
          fragmentTransaction.commit();

      }else if (fromWhereView.equals(Constants.terms_and_condition))
      {

          mViewPager.setVisibility(View.GONE);
          tabLayout.setVisibility(View.GONE);
          txtTitle.setText("Terms And Conditions");
          FragmentManager fragmentManager = getSupportFragmentManager();
          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
          fragmentTransaction.add(R.id.fragment_container,ProfileFragment.newInstance("from",Constants.terms_and_condition), "PRIVACY_POLICY_FRAGMENT");
          fragmentTransaction.addToBackStack("PRIVACY_POLICY_FRAGMENT");
          fragmentTransaction.commit();

      }else if (fromWhereView.equals(Constants.contact))
      {
          mViewPager.setVisibility(View.GONE);
          tabLayout.setVisibility(View.GONE);
          txtTitle.setText("Contact");
          FragmentManager fragmentManager = getSupportFragmentManager();
          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
          fragmentTransaction.add(R.id.fragment_container,ProfileFragment.newInstance("from",Constants.contact), "PRIVACY_POLICY_FRAGMENT");
          fragmentTransaction.addToBackStack("PRIVACY_POLICY_FRAGMENT");
          fragmentTransaction.commit();
      }else if (fromWhereView.equals(Constants.SETTING))
      {
          mViewPager.setVisibility(View.GONE);
          tabLayout.setVisibility(View.GONE);
          txtTitle.setText("Setting");
          FragmentManager fragmentManager = getSupportFragmentManager();
          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
          fragmentTransaction.add(R.id.fragment_container,new SettingFragment(), "SETTING_FRAGMENT");
          fragmentTransaction.addToBackStack("SETTING_FRAGMENT");
          fragmentTransaction.commit();
      }




    }

    private void setupViewPager() {
        mSectionsPagerAdapter.addFrag(new AllAppointmentFragment(), "All");
        mSectionsPagerAdapter.addFrag(new UpcomingAppointmentFragment(), "Upcoming");
        mSectionsPagerAdapter.addFrag(new PastAppointmentFragment(), "Past");
    }
    private TabLayout.OnTabSelectedListener OnTabSelectedListener = new TabLayout.OnTabSelectedListener(){
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int c = tab.getPosition();
//            progress_app.setVisibility(View.GONE);
            mSectionsPagerAdapter.SetOnSelectView(tabLayout,c);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            int c = tab.getPosition();
            mSectionsPagerAdapter.SetUnSelectView(tabLayout,c);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TestimonyFragment testimonyFragment = new TestimonyFragment();
        testimonyFragment.onActivityResult(requestCode,resultCode,data);
    }



    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        mRegistrationBroadcastReceiver=null;
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

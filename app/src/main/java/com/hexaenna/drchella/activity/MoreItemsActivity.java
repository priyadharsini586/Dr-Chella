package com.hexaenna.drchella.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.adapter.SectionsPagerAdapter;
import com.hexaenna.drchella.fragment.AllAppointmentFragment;
import com.hexaenna.drchella.fragment.DailyHealthTipsFragment;
import com.hexaenna.drchella.fragment.PastAppointmentFragment;
import com.hexaenna.drchella.fragment.UpcomingAppointmentFragment;
import com.hexaenna.drchella.utils.Constants;


public class MoreItemsActivity extends AppCompatActivity  {

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    String fromWhereView = null;
    TextView txtTitle;

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
}

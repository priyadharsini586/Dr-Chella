package com.hexaenna.drchella.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.adapter.SectionsPagerAdapter;
import com.hexaenna.drchella.fragment.AllAppointmentFragment;
import com.hexaenna.drchella.fragment.PastAppointmentFragment;
import com.hexaenna.drchella.fragment.UpcomingAppointmentFragment;


public class MoreItemsActivity extends AppCompatActivity  {

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_items);

        mViewPager = (ViewPager) findViewById(R.id.appoint_container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),MoreItemsActivity.this,"more");
        setupViewPager();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.appointment_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
        }
        tabLayout.addOnTabSelectedListener(OnTabSelectedListener);

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_more_container, new AllAppointmentFragment(), "YOUR_APPOINTMENT_FRAGMENT");
        fragmentTransaction.addToBackStack("YOUR_APPOINTMENT_FRAGMENT");
        fragmentTransaction.commit();*/
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

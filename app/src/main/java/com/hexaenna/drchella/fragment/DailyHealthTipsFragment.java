package com.hexaenna.drchella.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.HealthTipsDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.MoreItemsActivity;
import com.hexaenna.drchella.adapter.CustomHealthViewPagerAdapter;
import com.hexaenna.drchella.adapter.HealthTipsAdapter;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.custom_view.RecyclerTouchListener;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.hexaenna.drchella.utils.Constants;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DailyHealthTipsFragment extends Fragment implements MoreItemsActivity.OnBackPressedListener {


  View view;
    private ArrayList<HealthTipsDetails.Tips> tipsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HealthTipsAdapter mAdapter;
    NetworkChangeReceiver networkChangeReceiver;
    ApiInterface apiInterface;
    String isConnection = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_daily_health_tips, container, false);


        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
//                if (isConnection == null) {
                Bundle b = intent.getExtras();
                isConnection = b.getString(Constants.MESSAGE);
                getNotificationTips();

//                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        getActivity().registerReceiver(networkChangeReceiver,
                intentFilter);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HealthTipsDetails.Tips tips = tipsArrayList.get(position);
                openDialognotification(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        ((MoreItemsActivity) getActivity()).setOnBackPressedListener(this);
        return view;
    }

    private void getNotificationTips() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<HealthTipsDetails> call = apiInterface.getHealthTips();
                call.enqueue(new Callback<HealthTipsDetails>() {
                    @Override
                    public void onResponse(Call<HealthTipsDetails> call, Response<HealthTipsDetails> response) {
                        if (response.isSuccessful()) {
                            HealthTipsDetails tipsDetails = response.body();
                            if (tipsDetails.getStatus_code().equals(Constants.status_code1))
                            {
                                ArrayList<HealthTipsDetails.Tips>tipses = tipsDetails.getTips();
                                if (tipses.size() != 0)
                                {
                                    tipsArrayList = new ArrayList<HealthTipsDetails.Tips>();
                                    for (int i= 0 ; i < tipses.size() ; i ++)
                                    {
                                        HealthTipsDetails.Tips tips = tipses.get(i);
                                        tips.setTips(tips.getTips());

                                        SimpleDateFormat geivenDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm a");
                                        String formattedDate= " ";
                                        try {
                                            Date date =geivenDateFormat.parse(tips.getDt_time());
                                            geivenDateFormat.applyPattern("dd-MMM-yyyy hh:mm a");
                                            formattedDate = geivenDateFormat.format(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String lang = tips.getLang();
                                        String appLang = " ";
                                        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                                        appLang = "2";
                                       /* if (databaseHandler.getContact("0").equals("English"))
                                        {

                                            appLang = "2";

                                        }else if (databaseHandler.getContact("0").equals("Tamil"))
                                        {
                                            appLang = "1";
                                        }*/

                                        tips.setDt_time(formattedDate);
                                        tips.setTitle(tips.getTitle());
                                        tips.setTips_pic(tips.getTips_pic());

                                        if (appLang.equals(lang)) {
                                            tipsArrayList.add(tips);
                                        }
                                    }
                                }
                                Log.e("size",String.valueOf(tipsArrayList.size()));
                                mAdapter = new HealthTipsAdapter(tipsArrayList,getActivity());
                                recyclerView.setAdapter(mAdapter);
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<HealthTipsDetails> call, Throwable t) {

                    }
                });
            }
        }
    }


    public void openDialognotification(int position)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.show_notification_page);

        ViewPager  viewPager = (ViewPager)dialog.findViewById(R.id.notifi_viewpager);
        CustomHealthViewPagerAdapter mAdapter = new CustomHealthViewPagerAdapter(getActivity(), tipsArrayList);
        viewPager.setAdapter(mAdapter);
       final int   dotsCount = mAdapter.getCount();
       final View[]  dots = new View[dotsCount];
        LinearLayout viewPagerCountDots = (LinearLayout) dialog.findViewById(R.id.viewPagerCountDots);
        for (int i=0;i<dots.length;i++) {
            dots[i] = new View(getActivity());


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dots[i].setBackground(getActivity().getDrawable(R.drawable.default_dot));
            }else
            {
                dots[i].setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.default_dot));
            }
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(30, ViewGroup.LayoutParams.WRAP_CONTENT));
            dots[i].setLayoutParams(lp);
            viewPagerCountDots.addView(dots[i]);
        }

        viewPager.setCurrentItem(position);
        dots[position].setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.selected_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.length; i++){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dots[i].setBackground(getActivity().getDrawable(R.drawable.default_dot));
                    }else
                    {
                        dots[i].setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.default_dot));
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dots[position].setBackground(getActivity().getDrawable(R.drawable.selected_dot));
                }else
                {
                    dots[position].setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.selected_dot));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(700,ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageClose = (ImageView) dialog.findViewById(R.id.imgClose);
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        dialog.show();
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
        getActivity().finish();
    }
}

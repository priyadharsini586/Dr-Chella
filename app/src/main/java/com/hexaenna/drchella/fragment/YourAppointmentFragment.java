package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hexaenna.drchella.Model.AllAppointmentDetails;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.adapter.MyItemRecyclerViewAdapter;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.hexaenna.drchella.utils.Constants;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YourAppointmentFragment extends Fragment {

    View view;
    private List<AllAppointmentDetails.Appoinmentslist> appointmentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyItemRecyclerViewAdapter mAdapter;
    NetworkChangeReceiver networkChangeReceiver;
    ApiInterface apiInterface;
    String isConnection = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        mAdapter = new MyItemRecyclerViewAdapter(appointmentList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
//                if (isConnection == null) {
                Bundle b = intent.getExtras();
                isConnection = b.getString(Constants.MESSAGE);
                getNetworkState();
                getAppointmentData();
                Log.e("  from home", "" + isConnection);
//                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        getActivity().registerReceiver(networkChangeReceiver,
                intentFilter);
        return view;
    }

    private void getAppointmentData() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                JSONObject jsonObject = new JSONObject();

                try {
                    UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                    jsonObject.put("user_email", userRegisterDetails.getE_mail());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<AllAppointmentDetails> call = apiInterface.allAppointment(jsonObject);
                call.enqueue(new Callback<AllAppointmentDetails>() {
                    @Override
                    public void onResponse(Call<AllAppointmentDetails> call, Response<AllAppointmentDetails> response) {
                        if (response.isSuccessful()) {

                            AllAppointmentDetails allAppointmentDetails = response.body();

                            if (allAppointmentDetails.getStatus_code().equals(Constants.status_code1)) {
                                List<AllAppointmentDetails.Appoinmentslist> appoinmentslists = allAppointmentDetails.getAppoinments();
                                for (int i = 0; i < appoinmentslists.size(); i++) {
                                    AllAppointmentDetails.Appoinmentslist appoinmentslist = appoinmentslists.get(i);
                                    appoinmentslist.setCity_id(appoinmentslist.getDate());
                                    appoinmentslist.setCity_id(appoinmentslist.getTime());
                                    appointmentList.add(appoinmentslist);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<AllAppointmentDetails> call, Throwable t) {
                        Log.e("failure", String.valueOf(t));
                    }
                });
            }
        }
    }


    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {


            } else if (isConnection.equals(Constants.NETWORK_CONNECTED)) {


            }
        }
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
    public void onResume() {
        super.onResume();

    }
}

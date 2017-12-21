package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hexaenna.drchella.Model.AllAppointmentDetails;
import com.hexaenna.drchella.Model.AppointmentDetails;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.ViewAppointmentActivity;
import com.hexaenna.drchella.adapter.MyItemRecyclerViewAdapter;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.custom_view.RecyclerTouchListener;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.hexaenna.drchella.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpcomingAppointmentFragment extends Fragment {
    View view;
    private List<AllAppointmentDetails.Appoinmentslist> appointmentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyItemRecyclerViewAdapter mAdapter;
    NetworkChangeReceiver networkChangeReceiver;
    ApiInterface apiInterface;
    String isConnection = null;
    LinearLayout txtNodata;
    ProgressBar progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progress = (ProgressBar) view.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AllAppointmentDetails.Appoinmentslist appoinmentslist = appointmentList.get(position);
                AppointmentDetails appointmentDetails = AppointmentDetails.getInstance();
                appointmentDetails.setCity(appoinmentslist.getCity_id());
                appointmentDetails.setDate(appoinmentslist.getDate());
                appointmentDetails.setTime(appoinmentslist.getTime());
                Intent intent = new Intent(getActivity(), ViewAppointmentActivity.class);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
                Log.e("position",""+position);
                deleteAppointment(position);
            }
        }));
        txtNodata = (LinearLayout) view.findViewById(R.id.txtNodata);
        txtNodata.setVisibility(View.GONE);
        return view;
    }

    private void getAppointmentData() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                JSONObject jsonObject = new JSONObject();
                progress.setVisibility(View.VISIBLE);
                try {
                    UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                    jsonObject.put("user_email", userRegisterDetails.getE_mail());
                    jsonObject.put("act","upcoming");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<AllAppointmentDetails> call = apiInterface.allAppointment(jsonObject);
                call.enqueue(new Callback<AllAppointmentDetails>() {
                    @Override
                    public void onResponse(Call<AllAppointmentDetails> call, Response<AllAppointmentDetails> response) {
                        if (response.isSuccessful()) {

                            AllAppointmentDetails allAppointmentDetails = response.body();
                            appointmentList = new ArrayList<AllAppointmentDetails.Appoinmentslist>();

                            if (allAppointmentDetails.getStatus_code().equals(Constants.status_code1)) {
                                List<AllAppointmentDetails.Appoinmentslist> appoinmentslists = allAppointmentDetails.getAppoinments();
                                for (int i = 0; i < appoinmentslists.size(); i++) {
                                    AllAppointmentDetails.Appoinmentslist appoinmentslist = appoinmentslists.get(i);
                                    appoinmentslist.setPtnt_name(appoinmentslist.getPtnt_name());
                                    appoinmentslist.setCity_id(appoinmentslist.getCity_id());
                                    appoinmentslist.setTime(appoinmentslist.getTime());
                                    appoinmentslist.setSno(appoinmentslist.getSno());
                                    appointmentList.add(appoinmentslist);
                                }
                                mAdapter = new MyItemRecyclerViewAdapter(appointmentList,getActivity());
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                                Log.e("size","" +appointmentList.size());
                                recyclerView.setVisibility(View.VISIBLE);
                                txtNodata.setVisibility(View.GONE);
                                progress.setVisibility(View.GONE);
                            }else if (allAppointmentDetails.getStatus_code().equals(Constants.status_code0))
                            {
                                recyclerView.setVisibility(View.GONE);
                                txtNodata.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.GONE);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<AllAppointmentDetails> call, Throwable t) {

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


    private void deleteAppointment(final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Cancel Appointment");
        builder.setMessage(R.string.info_description);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s_no = appointmentList.get(position).getSno();
                deleteAppointmentFromServer(s_no,dialog,position);
                MyItemRecyclerViewAdapter.ViewHolder viewHolder = (MyItemRecyclerViewAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewHolder.ldtListItem.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyItemRecyclerViewAdapter.ViewHolder viewHolder = (MyItemRecyclerViewAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewHolder.ldtListItem.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                }
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.show();

        /*Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.delete_appointment);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(R.color.white)));
        dialog.setCancelable(false);
        dialog.show();*/
    }

    private void deleteAppointmentFromServer(final String app_sno, final DialogInterface dialogInterface, final int pos) {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                JSONObject jsonObject = new JSONObject();

                try {
                    UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                    jsonObject.put("app_sno", app_sno);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<AllAppointmentDetails> call = apiInterface.deleteAppointment(jsonObject);
                call.enqueue(new Callback<AllAppointmentDetails>() {
                    @Override
                    public void onResponse(Call<AllAppointmentDetails> call, Response<AllAppointmentDetails> response) {
                        if (response.isSuccessful()) {
                            AllAppointmentDetails allAppointmentDetails = response.body();
                            if (allAppointmentDetails.getStatus_code().equals(Constants.status_code1)) {
                                dialogInterface.cancel();
                                appointmentList.remove(pos);
                                mAdapter.notifyItemRemoved(pos);
                                mAdapter.notifyItemRangeChanged(pos,appointmentList .size());
                                AllAppointmentDetails details = AllAppointmentDetails.getInstance();
                                details.setS_no(app_sno);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<AllAppointmentDetails> call, Throwable t) {

                    }
                });
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            AllAppointmentDetails allAppointmentDetails = AllAppointmentDetails.getInstance();
            if (allAppointmentDetails.getS_no() != null) {
                for (int i = 0; i < appointmentList.size(); i++) {
                    AllAppointmentDetails.Appoinmentslist appoinmentslist = appointmentList.get(i);
                    if (allAppointmentDetails.getS_no().equals(appoinmentslist.getSno())) {
                        appointmentList.remove(i);
                        mAdapter.notifyItemRemoved(i);
                        mAdapter.notifyItemRangeChanged(i, appointmentList.size());
                    }
                }
            }
        }
    }
}


package com.hexaenna.drchella.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.HealthTipsDetails;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.MoreItemsActivity;
import com.hexaenna.drchella.adapter.MoreAdapter;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.hexaenna.drchella.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreFragment extends Fragment {

    View rootView;
    ListView lstMore;
    MoreAdapter moreAdapter;
    ArrayList<String> moreList = new ArrayList<>();
    ArrayList<Integer> imgList =new ArrayList<>();
    NetworkChangeReceiver networkChangeReceiver;
    String isConnection = null;
    ProgressBar progressChangeLang;
    String[] userDetails;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_more_fragment, container, false);


        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
//                if (isConnection == null) {
                Bundle b = intent.getExtras();
                isConnection = b.getString(Constants.MESSAGE);

//                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        getActivity().registerReceiver(networkChangeReceiver,
                intentFilter);


        lstMore = (ListView) rootView.findViewById(R.id.lstMore);
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        userDetails =  databaseHandler.getUserName("0");

        moreAdapter = new MoreAdapter(getActivity(),getMoreList(),getImageList());

        lstMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String textPosition = String.valueOf(getMoreList().get(position));
                Log.e("position", String.valueOf(textPosition));
                Intent intent = new Intent(getActivity(), MoreItemsActivity.class);
                if (textPosition.equals("Your Appointment"))
                {
                    intent.putExtra(Constants.fromMore,Constants.your_appointment);
                    startActivity(intent);
                }else if (textPosition.equals("Refer Friends"))
                {
                    shareData();
                }else if (textPosition.equals("Change Language"))
                {
                    Toast.makeText(getActivity(),"Coming Soon",Toast.LENGTH_LONG).show();
//                    changeLanguage();
                }else if (textPosition.equals("Daily Health Tips"))
                {
                    intent.putExtra(Constants.fromMore,Constants.daily_health_tips);
                    startActivity(intent);
                }else if (textPosition.equals("Testimony"))
                {
                    intent.putExtra(Constants.fromMore,Constants.testimony);
                    startActivity(intent);
                }else if (textPosition.equals("Consultation Location"))
                {
                    intent.putExtra(Constants.fromMore,Constants.consulation_location);
                    startActivity(intent);
                }else if (textPosition.equals("Privcy Policy"))
                {
                    intent.putExtra(Constants.fromMore,Constants.privacy_policy);
                    startActivity(intent);
                }else if (textPosition.equals("Terms And Conditions"))
                {
                    intent.putExtra(Constants.fromMore,Constants.terms_and_condition);
                    startActivity(intent);
                }else if (textPosition.equals("Contact"))
                {
                    intent.putExtra(Constants.fromMore,Constants.contact);
                    startActivity(intent);
                }
                else if (textPosition.equals("Settings"))
                {
                    intent.putExtra(Constants.fromMore,Constants.SETTING);
                    startActivity(intent);
                }
            }
        });
        lstMore.setAdapter(moreAdapter);
        return rootView;
    }

    private void shareData() {

        String shareBody = "Get Dr. Chella App book Your Appointment which is your convenient time..?";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Text to.."));
    }

    public ArrayList getMoreList()
    {
        moreList = new ArrayList<>();
        if (!userDetails[3].equals("admin")) {
            moreList.add("Your Appointment");
        }

        moreList.add("Consultation Location");
        moreList.add("Daily Health Tips");
        moreList.add("Testimony");
        moreList.add("Change Language");
        moreList.add("Contact");
        moreList.add("Refer Friends");
        moreList.add("Terms And Conditions");
        moreList.add("Privcy Policy");
        moreList.add("Settings");
        return moreList;
    }

    public ArrayList getImageList()
    {
        imgList = new ArrayList<>();
        if (!userDetails[3].equals("admin")) {
            imgList.add(R.drawable.your_appointment);
        }


        imgList.add(R.drawable.con_location);
        imgList.add(R.drawable.daily_health_tips);
        imgList.add(R.drawable.testimony);
        imgList.add(R.drawable.lang);
        imgList.add(R.drawable.contact);
        imgList.add(R.drawable.refer_friend);
        imgList.add(R.drawable.tearm_conditions);
        imgList.add(R.drawable.lock);
        imgList.add(R.drawable.settings);
        return imgList;
    }



    private void changeLanguage() {

        final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());

        final Dialog alertDialog = new Dialog(getActivity());

        alertDialog.setTitle("Select Your Language");
        alertDialog.setContentView(R.layout.language_dialog_box);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(R.color.white)));
        final RadioGroup radGroupLangu = (RadioGroup) alertDialog.findViewById(R.id.radGroupLangu);
        alertDialog.dismiss();
        progressChangeLang= (ProgressBar) alertDialog.findViewById(R.id.progressChangeLang);
        progressChangeLang.setVisibility(View.GONE);
        if (databaseHandler.getContact("0").equals("English"))
        {

            radGroupLangu.check(R.id.radEnglish);

        }else if (databaseHandler.getContact("0").equals("Tamil"))
        {
            radGroupLangu.check(R.id.radTamil);
        }

        Button btnOk = (Button) alertDialog.findViewById(R.id.btnOk);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=radGroupLangu.getCheckedRadioButtonId();
                String selected = "English";
                if (selectedId == R.id.radEnglish)
                {
                    selected = "English";
                }else if (selectedId == R.id.radTamil)
                {
                    selected = "Tamil";
                }
                Log.e("selected",selected);
                progressChangeLang.setVisibility(View.VISIBLE);
                changeLanguageFromServer(selected,alertDialog);


            }
        });

        alertDialog.show();

    }



    private void changeLanguageFromServer(final String lang, final Dialog alertDialog) {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                JSONObject jsonObject = new JSONObject();
                UserRegisterDetails  userRegisterDetails = UserRegisterDetails.getInstance();
                try {
                    jsonObject.put("user_email",userRegisterDetails.getE_mail());
                    jsonObject.put("lang",lang);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<HealthTipsDetails> call = apiInterface.changeLanguage(jsonObject);
                call.enqueue(new Callback<HealthTipsDetails>() {
                    @Override
                    public void onResponse(Call<HealthTipsDetails> call, Response<HealthTipsDetails> response) {
                        if (response.isSuccessful()) {

                            HealthTipsDetails  tipsDetails = response.body();
                            if (tipsDetails.getStatus_code().equals(Constants.status_code1)) {
                                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                                databaseHandler.updateLanguage("0", lang);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        alertDialog.dismiss();
                                    }
                                }, 1000);
                                Toast.makeText(getActivity(),"Language Changed",Toast.LENGTH_LONG).show();
                                progressChangeLang.setVisibility(View.GONE);
                            }else
                            {
                                alertDialog.dismiss();
                                progressChangeLang.setVisibility(View.GONE);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<HealthTipsDetails> call, Throwable t) {
                        Log.e("failure", String.valueOf(t));
                    }
                });
            }
        }
    }
}

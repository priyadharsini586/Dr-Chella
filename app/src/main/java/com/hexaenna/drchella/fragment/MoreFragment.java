package com.hexaenna.drchella.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.adapter.MoreAdapter;

import java.util.ArrayList;

public class MoreFragment extends Fragment {

    View rootView;
    ListView lstMore;
    MoreAdapter moreAdapter;
    ArrayList<String> moreList = new ArrayList<>();
    int[] imgList = {R.drawable.your_appointment
            ,R.drawable.lang
            ,R.drawable.testimony
            ,R.drawable.daily_health_tips
            ,R.drawable.con_location
            ,R.drawable.contact
            ,R.drawable.refer_friend
            ,R.drawable.tearm_conditions
            ,R.drawable.lock
            ,R.drawable.settings};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_more_fragment, container, false);

        lstMore = (ListView) rootView.findViewById(R.id.lstMore);

        moreAdapter = new MoreAdapter(getActivity(),getMoreList(),imgList);

        lstMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        lstMore.setAdapter(moreAdapter);
        return rootView;
    }

    public ArrayList getMoreList()
    {
        moreList.add("Your Appointment");
        moreList.add("Change Language");
        moreList.add("Testimony");
        moreList.add("Daily Health Tips");
        moreList.add("Consultation Location");
        moreList.add("Contact");
        moreList.add("Refer Friends");
        moreList.add("Terms And Conditions");
        moreList.add("Privcy Policy");
        moreList.add("Settings");
        return moreList;
    }


}

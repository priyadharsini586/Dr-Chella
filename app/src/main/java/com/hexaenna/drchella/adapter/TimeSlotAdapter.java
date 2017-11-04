package com.hexaenna.drchella.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hexaenna.drchella.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 10/28/2017.
 */

public class TimeSlotAdapter extends BaseAdapter {
    private Context context;
    ArrayList<String>timeSlotList;
    ArrayList<String> blockedArray;
    ArrayList<String> bookedArray;
    public TimeSlotAdapter(Context context, ArrayList<String>timeSlotList, ArrayList<String> blockedArray , ArrayList<String> bookedArray) {
        this.context = context;
        this.timeSlotList = timeSlotList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate(R.layout.time_slot_layout, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.txtTimeSlot);


            textView.setText(timeSlotList.get(position));

            // set image based on selected text


        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return timeSlotList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

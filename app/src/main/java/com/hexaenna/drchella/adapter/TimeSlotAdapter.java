package com.hexaenna.drchella.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hexaenna.drchella.Model.BookingDetails;
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
        this.blockedArray = blockedArray;
        this.bookedArray = bookedArray;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {


            gridView = inflater.inflate(R.layout.time_slot_layout, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.txtTimeSlot);
            textView.setText(timeSlotList.get(position));

            String viewText = timeSlotList.get(position);

            if (bookedArray != null) {
                if (bookedArray.size() != 0) {
                    for (int i = 0; i < bookedArray.size(); i++) {
                        if (viewText.equals(bookedArray.get(i))) {

                            gridView = inflater.inflate(R.layout.booked_time_slot_layout, null);
                            TextView textbookedView = (TextView) gridView
                                    .findViewById(R.id.txtBookedTimeSlot);
                            textbookedView.setText(timeSlotList.get(position));
                            LinearLayout ldtBooked_slot = (LinearLayout) gridView.findViewById(R.id.ldtBooked_slot);
                            ldtBooked_slot.setEnabled(false);
                        }
                    }
                }
            }
            if (blockedArray != null) {
                if (blockedArray.size() != 0) {
                    for (int i = 0; i < blockedArray.size(); i++) {
                        if (viewText.equals(blockedArray.get(i))) {
                            gridView = inflater.inflate(R.layout.blocked_time_slot_layout, null);
                            TextView textblockedView = (TextView) gridView
                                    .findViewById(R.id.txtBlockedTimeSlot);
                            textblockedView.setText(timeSlotList.get(position));
                            LinearLayout ldtBooked_slot = (LinearLayout) gridView.findViewById(R.id.ldtBlocked_slot);
                            ldtBooked_slot.setEnabled(false);
                        }
                    }
                }
            }
            if (viewText.equals("Lunch"))
            {
                gridView = inflater.inflate(R.layout.blocked_time_slot_layout, null);
                TextView textblockedView = (TextView) gridView
                        .findViewById(R.id.txtBlockedTimeSlot);
                textblockedView.setText(timeSlotList.get(position));
                gridView.setEnabled(false);

            }
            BookingDetails bookingDetails  = BookingDetails.getInstance();
            if (bookingDetails.getSelectedPosition() != -1)
            {
                if (position == bookingDetails.getSelectedPosition()) {
                    LinearLayout linearLayout = (LinearLayout) gridView.findViewById(R.id.ldtBackground);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        linearLayout.setBackground(context.getResources().getDrawable(R.drawable.selected_time_slot_bg));
                        textView.setTextColor(context.getResources().getColor(R.color.black));
                    } else {
                        linearLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selected_time_slot_bg));
                        textView.setTextColor(context.getResources().getColor(R.color.black));
                    }
                }
            }

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

package com.hexaenna.drchella.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hexaenna.drchella.Model.AllAppointmentDetails;
import com.hexaenna.drchella.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<AllAppointmentDetails.Appoinmentslist> mValues;
    Context mcontext;

    public MyItemRecyclerViewAdapter(List<AllAppointmentDetails.Appoinmentslist> items,Context mcontext) {
        mValues = items;
        this.mcontext = mcontext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        DateFormat dateForRequest = new SimpleDateFormat("dd.MM.yyyy");
        String requestdate = mValues.get(position).getDate();
        Date newDate = null;
        try {
            newDate = dateForRequest.parse(requestdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateForRequest = new SimpleDateFormat("dd MMM yyyy");
        String date = dateForRequest.format(newDate);

        holder.mIdView.setText(mValues.get(position).getPtnt_name());
        Log.e("text",mValues.get(position).getPtnt_name());
        holder.mContentView.setText(mValues.get(position).getTime() + " , " +date);
        holder.mCityView.setText (getAddress(mValues.get(position).getCity_id()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.ldtListItem.setBackgroundColor(mcontext.getResources().getColor(R.color.view_color));
                }
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mCityView;
        public AllAppointmentDetails.Appoinmentslist mItem;
        public LinearLayout ldtListItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mCityView = (TextView) view.findViewById(R.id.city);
            mContentView = (TextView) view.findViewById(R.id.content);
            ldtListItem = (LinearLayout) view.findViewById(R.id.ldtListItem);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }



    private String getAddress(String city) {

        String hospitalName = null;
        if (city.equals("1"))
        {
            hospitalName = mcontext.getString(R.string.chennai_hospital);

        }else if (city.equals("2"))
        {
            hospitalName = mcontext.getString(R.string.erode_hospital);
        }else if (city.equals("3"))
        {
            hospitalName = mcontext.getString(R.string.coimbatore_hospital);

        }else if (city.equals("4"))
        {
            hospitalName = mcontext.getString(R.string.namakkal_hospital);

        }else if (city.equals("5"))
        {
            hospitalName = mcontext.getString(R.string.mayiladu_hospital);

        }else if (city.equals("6"))
        {
            hospitalName = mcontext.getString(R.string.kollidam_hospital);

        }

        return hospitalName;
    }
}

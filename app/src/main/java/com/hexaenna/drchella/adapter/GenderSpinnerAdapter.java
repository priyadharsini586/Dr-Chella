package com.hexaenna.drchella.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hexaenna.drchella.R;

import java.util.ArrayList;

/**
 * Created by admin on 10/26/2017.
 */

public class GenderSpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> genderArraylist;
    private LayoutInflater mInflater;
    public GenderSpinnerAdapter(Context context, ArrayList<String> genderList) {
       this.context= context;
        this.genderArraylist = genderList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return genderArraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.drop_down_list_items, null);
            holder = new ViewHolder();
            holder.tvGender = (TextView) convertView.findViewById(R.id.s_spinner_rating);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvGender.setText(genderArraylist.get(position));
        return convertView;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(genderArraylist.get(position));
        txt.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        txt.setBackgroundColor(context.getResources().getColor(R.color.white));
        return  txt;
    }

    class ViewHolder {
        TextView tvGender;
    }
}

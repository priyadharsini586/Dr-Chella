package com.hexaenna.drchella.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hexaenna.drchella.R;

import java.util.ArrayList;

/**
 * Created by admin on 10/27/2017.
 */

public class CityAdapter  extends BaseAdapter{
    Context context;
    ArrayList<String> genderArraylist;
    private LayoutInflater mInflater;
    public CityAdapter(Context context, ArrayList<String> genderList) {
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
            convertView = mInflater.inflate(R.layout.city_list_items, null);
            holder = new ViewHolder();
            holder.tvGender = (TextView) convertView.findViewById(R.id.txtCity);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvGender.setText(genderArraylist.get(position));
        return convertView;

    }


    class ViewHolder {
        TextView tvGender;
    }
}

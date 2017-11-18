package com.hexaenna.drchella.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexaenna.drchella.R;

import java.util.ArrayList;

/**
 * Created by admin on 11/18/2017.
 */

public class MoreAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> moreArraylist;
    private LayoutInflater mInflater;
    int[] imgList = {R.drawable.your_appointment,R.drawable.change_lan,R.drawable.testimony,R.drawable.daily_health_tips,R.drawable.con_location,R.drawable.refer_friend,R.drawable.settings};
    public MoreAdapter(Context context, ArrayList <String> moreList) {
        this.context= context;
        this.moreArraylist = moreList;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return moreArraylist.size();
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
        Log.e("list",moreArraylist.get(position));
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.more_list, null);
            holder = new ViewHolder();
            holder.tvGender = (TextView) convertView.findViewById(R.id.moreItems);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgChangeImg);
            holder.imgIcon.setImageResource(imgList[position]);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvGender.setText(moreArraylist.get(position));
        return convertView;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    class ViewHolder {
        TextView tvGender;
        ImageView imgIcon;
    }
}

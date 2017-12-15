package com.hexaenna.drchella.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.R;

import java.util.ArrayList;

/**
 * Created by admin on 11/18/2017.
 */

public class MoreAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> moreArraylist;
    private LayoutInflater mInflater;
    ArrayList imgList ;

    public MoreAdapter(Context context, ArrayList <String> moreList,ArrayList img) {
        this.context= context;
        this.moreArraylist = moreList;
        this.imgList = img;
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

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.more_list, null);
            holder = new ViewHolder();
            holder.tvGender = (TextView) convertView.findViewById(R.id.moreItems);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgChangeImg);
            holder.ldtList = (LinearLayout) convertView.findViewById(R.id.ldtList);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvGender.setText(moreArraylist.get(position));
        holder.imgIcon.setImageResource((Integer) imgList.get(position));
        return convertView;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    class ViewHolder {
        TextView tvGender;
        ImageView imgIcon;
        LinearLayout ldtList;
    }
}

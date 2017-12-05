package com.hexaenna.drchella.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexaenna.drchella.Model.HealthTipsDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.image_cache.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/29/2017.
 */

public class HealthTipsAdapter extends RecyclerView.Adapter<HealthTipsAdapter.MyViewHolder> {

   private   ArrayList<HealthTipsDetails.Tips> healthTipsDetailses ;
    ImageLoader imageLoader;
    Context context ;
    public HealthTipsAdapter(ArrayList<HealthTipsDetails.Tips> moviesList,Context context) {
        this.healthTipsDetailses = moviesList;
        this.context = context;
        imageLoader=new ImageLoader(context.getApplicationContext());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.health_tips_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HealthTipsDetails.Tips tipsDetails = healthTipsDetailses.get(position);
        holder.title.setText(tipsDetails.getTitle());
        holder.time.setText(tipsDetails.getDt_time());
        holder.noti.setText(tipsDetails.getTips());
        imageLoader.DisplayImage(tipsDetails.getTips_pic(),holder.img,R.mipmap.ic_default_health_tips);

    }

    @Override
    public int getItemCount() {
        return healthTipsDetailses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, time, noti;
        ImageView img;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtTitle);
            time = (TextView) view.findViewById(R.id.txtTime);
            noti = (TextView) view.findViewById(R.id.txtContent);
            img = (ImageView) view.findViewById(R.id.imgNoti);
        }
    }
}

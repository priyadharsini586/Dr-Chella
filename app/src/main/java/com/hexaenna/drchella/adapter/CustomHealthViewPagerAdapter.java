package com.hexaenna.drchella.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexaenna.drchella.Model.HealthTipsDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.image_cache.ImageLoader;

import java.util.List;

/**
 * Created by admin on 11/29/2017.
 */

public class CustomHealthViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<HealthTipsDetails.Tips> tipsList;
    private LayoutInflater  layoutInflater;
    ImageLoader imageLoader;
    public CustomHealthViewPagerAdapter(Context context, List<HealthTipsDetails.Tips> hotDealList) {
        this.context = context;
        this.tipsList = hotDealList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(context.getApplicationContext());
    }
    @Override
    public int getCount() {
        return tipsList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.notification_list_item, container, false);
        HealthTipsDetails.Tips tips = tipsList.get(position);
        ImageView imgNoti = (ImageView)view.findViewById(R.id.imgShowImage);
       TextView txtTitleNotifi = (TextView) view.findViewById(R.id.txtTitleNotifi);
        TextView txtContentNotifi = (TextView) view.findViewById(R.id.txtContentNotifi);
        //bind value to the View Widgets

        txtTitleNotifi.setText(tips.getTitle());
        txtContentNotifi.setText(tips.getTips());
        imageLoader.DisplayImage(tips.getTips_pic(),imgNoti);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}

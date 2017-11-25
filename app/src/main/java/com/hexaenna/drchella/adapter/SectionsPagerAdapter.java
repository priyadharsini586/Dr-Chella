package com.hexaenna.drchella.adapter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.MoreItemsActivity;
import com.hexaenna.drchella.fragment.HomeFragment;
import com.hexaenna.drchella.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/10/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private int[] selectPosition = { R.mipmap.ic_home_tab_select, R.mipmap.ic_drtalk_tab_select,R.mipmap.ic_area_expertise_tab_select,R.mipmap.ic_profile_tab_select,R.mipmap.ic_more_tab_select };
    private int[] unSelectPosition = { R.mipmap.ic_home_tab_unselect, R.mipmap.ic_drtalk_tab_unselect,R.mipmap.ic_area_expertise_tab_unselect,  R.mipmap.ic_profile_tab_unselect, R.mipmap.ic_more_tab_unselect};
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Context context;
    String from;
    public SectionsPagerAdapter(FragmentManager fm, Context context,String from) {
        super(fm);
        this.context = context;
        this.from = from;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }


    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.tabTitle);
        ImageView img = (ImageView) v.findViewById(R.id.tabImage);
        if (from.equals("more"))
        {
            img.setVisibility(View.GONE);
        }else
        {
            img.setVisibility(View.VISIBLE);
        }

        if (position == 0)
        {
            tv.setText(mFragmentTitleList.get(position));
            tv.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            img.setImageResource(selectPosition[position]);
        }else
        {
            tv.setText(mFragmentTitleList.get(position));
            img.setImageResource(unSelectPosition[position]);
            tv.setTextColor(context.getResources().getColor(R.color.unselected_tab));
        }

        return v;
    }

    public void SetOnSelectView(TabLayout tabLayout, int position)
    {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = (TextView) selected.findViewById(R.id.tabTitle);
        iv_text.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        ImageView img = (ImageView) selected.findViewById(R.id.tabImage);
        img.setImageResource(selectPosition[position]);
    }

    public void SetUnSelectView(TabLayout tabLayout,int position)
    {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = (TextView) selected.findViewById(R.id.tabTitle);
        iv_text.setTextColor(context.getResources().getColor(R.color.unselected_tab));
        ImageView img = (ImageView) selected.findViewById(R.id.tabImage);
        img.setImageResource(unSelectPosition[position]);
    }
}

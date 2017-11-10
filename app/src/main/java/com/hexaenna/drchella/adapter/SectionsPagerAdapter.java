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
import com.hexaenna.drchella.fragment.HomeFragment;
import com.hexaenna.drchella.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/10/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private int[] imageResId = { R.mipmap.ic_home_tab, R.drawable.ic_arrow_right };
    private int[] unSelectPosition = { R.mipmap.ic_home_tab_unselect, R.drawable.ic_arrow_left };
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Context context;
    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
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
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.tabTitle);
        tv.setText(mFragmentTitleList.get(position));
        ImageView img = (ImageView) v.findViewById(R.id.tabImage);
        img.setImageResource(imageResId[position]);
        return v;
    }

    public void SetOnSelectView(TabLayout tabLayout, int position)
    {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = (TextView) selected.findViewById(R.id.tabTitle);
        iv_text.setTextColor(context.getResources().getColor(R.color.white));
        ImageView img = (ImageView) selected.findViewById(R.id.tabImage);
        img.setImageResource(imageResId[position]);
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

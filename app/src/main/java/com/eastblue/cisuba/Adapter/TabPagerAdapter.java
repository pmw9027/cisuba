package com.eastblue.cisuba.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import com.eastblue.cisuba.Fragment.HomeFragment;
import com.eastblue.cisuba.R;

/**
 * Created by PJC on 2017-02-07.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    public final int PAGE_NUM = 5;

    private Context mContext;
    private String[] mTabTitle = {"HOME", "NEAR", "FIND", "PROFILE", "ETC"};
    private int[] mTabIcon = {
            R.drawable.tab1_selector,
            R.drawable.tab2_selector,
            R.drawable.tab3_selector,
            R.drawable.tab4_selector,
            R.drawable.tab5_selector
    };

    public TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_tab, null);
        v.findViewById(R.id.icon).setBackgroundResource(mTabIcon[position]);
        return v;
    }

    @Override
    public Fragment getItem(int position) {
        return new HomeFragment();
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }
}

package com.eastblue.cisuba.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import com.eastblue.cisuba.Fragment.EtcFragment;
import com.eastblue.cisuba.Fragment.FindLocationFragment;
import com.eastblue.cisuba.Fragment.HomeFragment;
import com.eastblue.cisuba.Fragment.LocationFragment;
import com.eastblue.cisuba.Fragment.ProfileFragment;
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

    static final int HOME_FRAGMENT = 0;
    static final int NEAR_FRAGMENT = 1;
    static final int FIND_FRAGMENT = 2;
    static final int PROFILE_FRAGMENT = 3;
    static final int ETC_FRAGMENT = 4;

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
        switch (position)
        {
            case HOME_FRAGMENT:
                return new HomeFragment();
            case NEAR_FRAGMENT:
                return new LocationFragment();
            case FIND_FRAGMENT:
                return new FindLocationFragment();
            case PROFILE_FRAGMENT:
                return new ProfileFragment();
            case ETC_FRAGMENT:
                return new EtcFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }
}

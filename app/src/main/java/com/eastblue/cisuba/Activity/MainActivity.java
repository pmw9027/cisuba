package com.eastblue.cisuba.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.eastblue.cisuba.Adapter.TabPagerAdapter;
import com.eastblue.cisuba.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.rcvr_tl_tabs) TabLayout mTabLayout;
    @BindView(R.id.vp_pager) ViewPager mViewPager;

    TabPagerAdapter mTabAdapter;

    //@BindView(R.id.slider) SliderLayout mBannerSlider;

    ActionBarDrawerToggle dtToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    void init() {

        setSupportActionBar(mToolbar);
        dtToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, R.string.app_name);
        mDrawerLayout.setDrawerListener(dtToggle);

        mTabLayout.setSelectedTabIndicatorHeight(0);
        mTabAdapter = new TabPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        for(int i=0; i<mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(mTabAdapter.getTabView(i));
        }

        mTabLayout.getTabAt(0).select();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }

    @OnClick(R.id.lin_notice)
    public void goNotice() {
        startActivity(new Intent(this, NoticeActivity.class).putExtra("TYPE", "NOTICE"));
    }

    @OnClick(R.id.lin_faq)
    public void goFaq() {
        startActivity(new Intent(this, NoticeActivity.class).putExtra("TYPE", "FAQ"));
    }

    @OnClick(R.id.btn_request_partner)
    public void goRequestPartner() {
        startActivity(new Intent(this, RequestPartnerActivity.class));
    }
}

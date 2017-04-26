package com.eastblue.cisuba.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.app.SearchManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastblue.cisuba.Adapter.TabPagerAdapter;
import com.eastblue.cisuba.Dialog.MainPopUpDialog;
import com.eastblue.cisuba.Fragment.ProfileFragment;
import com.eastblue.cisuba.Manager.NetworkManager;
import com.eastblue.cisuba.Model.BannerModel;
import com.eastblue.cisuba.Network.Product;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;
import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.rcvr_tl_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.vp_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_center)
    ImageView tabCenter;

    @BindView(R.id.drawer)
    LinearLayout menu;
    TabPagerAdapter mTabAdapter;

    //@BindView(R.id.slider) SliderLayout mBannerSlider;

    ActionBarDrawerToggle dtToggle;

    private long backKeyPressedTime = 0;

    public static TextView nickname;
    public static CircleImageView profileimage;
    static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        nickname = (TextView) findViewById(R.id.drawer_nick);
        profileimage = (CircleImageView) findViewById(R.id.drawer_profile);

    }

    @Override
    protected void onResume() {
        if (LoginActivity.mOAuthLoginModule != null) {
            if (LoginActivity.mOAuthLoginModule.getState(this).toString().equals("OK")) {
                ProfileFragment.nickname.setText(LoginActivity.nickname);
                MainActivity.nickname.setText(LoginActivity.nickname);
                System.out.println("resume -- " + LoginActivity.nickname);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bitmap = getBitmap(LoginActivity.profile_image);
                        } catch (Exception e) {

                        } finally {
                            if (bitmap != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProfileFragment.profileimage.setImageBitmap(bitmap);
                                        MainActivity.profileimage.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        }
        //requestMe();
        super.onResume();
    }


    void init() {
        setSupportActionBar(mToolbar);
        dtToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, R.string.app_name);
        mDrawerLayout.setDrawerListener(dtToggle);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        mTabAdapter = new TabPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(mTabAdapter.getTabView(i));
        }

        mTabLayout.getTabAt(0).select();

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==2){
                    tabCenter.setImageResource(R.drawable.tab2_round_select);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==2){
                    tabCenter.setImageResource(R.drawable.tab2_round);
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        showBanner();

    }

    void showBanner() {
        HttpUtil.api(Product.class).getMainBanner(new Callback<List<BannerModel>>() {
            @Override
            public void success(List<BannerModel> bannerModels, Response response) {
                if (bannerModels.size() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("image", NetworkManager.SERVER_URL + bannerModels.get(0).mainThumbnail);

                    MainPopUpDialog mainPopUpDialog = new MainPopUpDialog();
                    mainPopUpDialog.setArguments(bundle);
                    mainPopUpDialog.show(getSupportFragmentManager(), "MAIN POPUP");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 버튼을 한번만 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    Intent intent = new Intent(MainActivity.this, ProductSearchActivity.class);
                    intent.putExtra("name", query);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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

    @OnClick(R.id.btn_invite_kakao)
    public void inviteKakao() {
        //http://cisuba.net:8000/uploads/kakao/kakao_banner.jpg
        final KakaoLink kakaoLink;
        try {
            kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            kakaoTalkLinkMessageBuilder.addText(getResources().getString(R.string.share_message));
            kakaoTalkLinkMessageBuilder.addImage("http://cisuba.net/uploads/kakao/kakao_banner.jpg", 1000, 1000);
            kakaoTalkLinkMessageBuilder.addAppButton("앱으로 이동", new AppActionBuilder().setUrl("https://play.google.com/store/apps/details?id=com.eastblue.cisuba&hl=ko").build());
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.drawer_profile)
    public void openLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        Bitmap retBitmap = null;

        try {
            imgUrl = new URL(url);
            connection = (HttpURLConnection)imgUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            retBitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }
}

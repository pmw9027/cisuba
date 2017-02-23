package com.eastblue.cisuba.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.eastblue.cisuba.Manager.NetworkManager;
import com.eastblue.cisuba.Model.ProductModel;
import com.eastblue.cisuba.Network.Product;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.slider) SliderLayout sliderLayout;

    @BindView(R.id.lin_tag) LinearLayout linTag;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_about) TextView tvAbout;
    @BindView(R.id.tv_address) TextView tvAddress;
    @BindView(R.id.tv_time) TextView tvTime;
    @BindView(R.id.tv_phone) TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void init() {

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.rsz_tab_back_ic_white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("BANNER", R.drawable.img_sample);
        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);

            getItem(getIntent().getStringExtra("id"));
        }
    }

    void getItem(String pk) {

        HttpUtil.api(Product.class).getPartnerItem(pk, new Callback<ProductModel>() {
            @Override
            public void success(ProductModel productModel, Response response) {
                tvName.setText("["+productModel.highlightAddress + "] " +productModel.partnerName);
                tvAbout.setText(productModel.detailAbout);
                tvAddress.setText(productModel.detailAddress);
                tvTime.setText(productModel.startStime + ":" + productModel.startEtime);
                tvPhone.setText(productModel.phone);

                for(int i=0; i<productModel.tagList.size(); i++) {

                    final int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics());
                    final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

                    ImageView tagView = new ImageView(ProductDetailActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                    params.rightMargin = margin;
                    tagView.setLayoutParams(params);
                    Glide.with(ProductDetailActivity.this).load(NetworkManager.SERVER_URL + productModel.tagList.get(i).image).into(tagView);
                    linTag.addView(tagView);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}

package com.eastblue.cisuba.Activity;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.eastblue.cisuba.Manager.NetworkManager;
import com.eastblue.cisuba.Map.NMapCalloutBasicOverlay;
import com.eastblue.cisuba.Map.NMapPOIflagType;
import com.eastblue.cisuba.Map.NMapViewerResourceProvider;
import com.eastblue.cisuba.Model.ProductImageModel;
import com.eastblue.cisuba.Model.ProductModel;
import com.eastblue.cisuba.Network.Product;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.mapView) NMapView mMapView;
    @BindView(R.id.slider) SliderLayout sliderLayout;

    @BindView(R.id.frame_map) FrameLayout frameMap;
    @BindView(R.id.lin_tag) LinearLayout linTag;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_about) TextView tvAbout;
    @BindView(R.id.tv_address) TextView tvAddress;
    @BindView(R.id.tv_time) TextView tvTime;
    @BindView(R.id.tv_phone) TextView tvPhone;
    @BindView(R.id.tv_use_about) TextView tvUseAbout;
    @BindView(R.id.tv_price_morning) TextView tvPriceMorning;
    @BindView(R.id.tv_price_lunch) TextView tvPriceLunch;
    @BindView(R.id.tv_price_dinner) TextView tvPriceDinner;
    @BindView(R.id.tv_discount) TextView tvPriceDiscount;

    NMapContext mMapContext;
    NMapOverlayManager mOverlayManager;
    NMapViewerResourceProvider mMapViewerResourceProvider;

    Boolean isRecv = false;

    // 수신된 파트너 정보
    private String partnerName;
    private double lat;
    private double lng;

    final String CLIENT_ID = "N_PMI_hG0G1FAFhg8alc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);

        init();
    }

    void init() {

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.rsz_tab_back_ic_white));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMapView.setClientId(CLIENT_ID);
        mMapView.setClickable(true);
        mMapContext = new NMapContext(this);
        mMapContext.onCreate();
        mMapContext.setupMapView(mMapView);
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        mMapView.setOnMapViewTouchEventListener(new NMapView.OnMapViewTouchEventListener() {
            @Override
            public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {

            }

            @Override
            public void onLongPressCanceled(NMapView nMapView) {

            }

            @Override
            public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {

            }

            @Override
            public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {

            }

            @Override
            public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {

            }

            @Override
            public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {
                if(isRecv) {
                    Intent mapActivity = new Intent(ProductDetailActivity.this, MapDetailActivity.class);
                    mapActivity.putExtra("lat", lat);
                    mapActivity.putExtra("lng", lng);

                    if(mapActivity.getBooleanExtra("gps", false)) {
                        mapActivity.putExtra("myLat", getIntent().getDoubleExtra("lat", 0));
                        mapActivity.putExtra("myLng", getIntent().getDoubleExtra("lng", 0));
                    }

                    mapActivity.putExtra("partnerName", partnerName);
                    startActivity(mapActivity);
                }
            }
        });

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tvPhone.getText()));
                startActivity(intent);
            }
        });

        getItem(getIntent().getStringExtra("id"));
    }

    void initScroll(List<ProductImageModel> imgArray) {
        HashMap<String,String> file_maps = new HashMap<String, String>();

        for(int i=0; i<imgArray.size(); i++) {
            file_maps.put("BANNER" + i, NetworkManager.SERVER_URL + imgArray.get(i).image);
            Log.d("IMAGE", imgArray.get(i).image);
        }

        for(String name : file_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
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
        }
    }

    void getItem(String pk) {

        HttpUtil.api(Product.class).getPartnerItem(pk, new Callback<ProductModel>() {
            @Override
            public void success(ProductModel productModel, Response response) {

                isRecv = true;

                // 넘길 정보 저장
                lat = Double.parseDouble(productModel.lat);
                lng = Double.parseDouble(productModel.lng);
                partnerName = productModel.partnerName;

                // 데이터 바인딩
                tvName.setText("["+productModel.highlightAddress + "] " +productModel.partnerName);
                tvAbout.setText(productModel.detailAbout);
                tvAddress.setText(productModel.detailAddress);
                tvPriceMorning.setText("조조 " + productModel.morningPrice + "원");
                tvPriceLunch.setText("평일 " + productModel.lunchPrice + "원");
                tvPriceDinner.setText("야간 " + productModel.dinnerPrice + "원");
                tvPriceDiscount.setText(productModel.discount + " 원 할인");

                tvPhone.setText(productModel.phone);
                tvUseAbout.setText(productModel.useAbout);
                if(productModel.startStime != null && productModel.startEtime != null) {
                    String startTime = productModel.startStime.split(":")[0] + ":" + productModel.startStime.split(":")[1];
                    String endTime = productModel.startEtime.split(":")[0]+ ":" +  productModel.startEtime.split(":")[1];
                    tvTime.setText(startTime + "~" + endTime);
                }

                setMarker(Double.parseDouble(productModel.lat), Double.parseDouble(productModel.lng), productModel.partnerName);
                initScroll(productModel.imageList);

                for(int i=0; i<productModel.tagList.size(); i++) {

                    final int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics());
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

    void setMarker(double lat, double lng, String name) {
        int markerId = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);
        poiData.addPOIitem(lng, lat, name, markerId, 0);
        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapContext.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapContext.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapContext.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

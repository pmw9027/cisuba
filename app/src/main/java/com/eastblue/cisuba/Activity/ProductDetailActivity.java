package com.eastblue.cisuba.Activity;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.eastblue.cisuba.Manager.NetworkManager;
import com.eastblue.cisuba.Map.NMapCalloutBasicOverlay;
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

    NMapContext mMapContext;
    NMapOverlayManager mOverlayManager;
    NMapViewerResourceProvider mMapViewerResourceProvider;

    final String CLIENT_ID = "N_PMI_hG0G1FAFhg8alc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);

        init();
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

    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {
        // set your callout overlay
        return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);
    }

    void init() {

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.rsz_tab_back_ic_white));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMapContext = new NMapContext(this);
        mMapContext.onCreate();
        mMapView.setClientId(CLIENT_ID);
        mMapContext.setupMapView(mMapView);
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        getItem(getIntent().getStringExtra("id"));
    }

    void initScroll(List<ProductImageModel> imgArray) {
        HashMap<String,String> file_maps = new HashMap<String, String>();

        for(int i=0; i<imgArray.size(); i++) {
            file_maps.put("BANNER" + i, NetworkManager.SERVER_URL + imgArray.get(i).image);
            Log.d("IMAGE", imgArray.get(i).image);
        }

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
        }
    }

    void getItem(String pk) {

        HttpUtil.api(Product.class).getPartnerItem(pk, new Callback<ProductModel>() {
            @Override
            public void success(ProductModel productModel, Response response) {
                tvName.setText("["+productModel.highlightAddress + "] " +productModel.partnerName);
                tvAbout.setText(productModel.detailAbout);
                tvAddress.setText(productModel.detailAddress);

                if(productModel.startStime != null && productModel.startEtime != null) {
                    String startTime = productModel.startStime.split(":")[0] + ":" + productModel.startStime.split(":")[1];
                    String endTime = productModel.startEtime.split(":")[0]+ ":" +  productModel.startEtime.split(":")[1];
                    tvTime.setText(startTime + "~" + endTime);
                }

                tvPhone.setText(productModel.phone);

                // set map x,y
                NGeoPoint nGeoPoint = new NGeoPoint(Float.parseFloat(productModel.lng), Float.parseFloat(productModel.lat));
                mMapView.getMapController().setMapCenter(nGeoPoint, 14);

                NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
                poiData.beginPOIdata(1);
                poiData.addPOIitem(nGeoPoint, productModel.partnerName, 0, 0);
                poiData.endPOIdata();

                NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, getResources().getDrawable(R.drawable.ic_pin_02));
                poiDataOverlay.showAllPOIdata(0);

                initScroll(productModel.imageList);

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

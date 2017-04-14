package com.eastblue.cisuba.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eastblue.cisuba.Map.NMapPOIflagType;
import com.eastblue.cisuba.Map.NMapViewerResourceProvider;
import com.eastblue.cisuba.R;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.mapView)
    NMapView mMapView;
    NMapContext mMapContext;
    NMapViewerResourceProvider mMapViewerResourceProvider;
    NMapOverlayManager mOverlayManager;

    private final String CLIENT_ID = "N_PMI_hG0G1FAFhg8alc";
    private final int PIN_NUMBER = 1;

    // Activity Extra
    private String productName = "박주찬 찜질방";
    private double lat = 37.521223;
    private double lng = 127.0151286;
    private double myLat = 37.538484;
    private double myLng = 127.082294;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);
        ButterKnife.bind(this);

        // 찜질방 위치 지정
        productName = getIntent().getStringExtra("productName");
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        myLat = getIntent().getDoubleExtra("myLat", 0);
        myLng = getIntent().getDoubleExtra("myLng", 0);

        mToolbar.setTitle("지도 상세보기");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMapContext = new NMapContext(this);
        mMapContext.onCreate();
        mMapContext.setupMapView(mMapView);

        mMapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정

        mMapViewerResourceProvider = new NMapViewerResourceProvider(this); // 리소스 프로바이더
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider); // 오버레이 매니저

        // 마커박는 작업
        int markerId = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(lng, lat, productName, markerId, 0);

//        if (myLat != 0) {
//            Drawable myPin = getResources().getDrawable(R.drawable.my_pin);
//            myPin.setBounds(0, 0, 50, 70);
//            poiData.addPOIitem(myLng, myLat, productName, myPin, 0);
//        }

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);

        mMapView.getMapController().setMapCenter(lng,lat,11);
        mMapView.setClickable(true); // 맵 클릭 가능하게
        mMapView.setScalingFactor(2.5f);
    }
    @Override
    public void onStart() {
        super.onStart();
        mMapContext.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }

    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}

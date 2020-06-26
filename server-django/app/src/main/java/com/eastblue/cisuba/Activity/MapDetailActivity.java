package com.eastblue.cisuba.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.nhn.android.maps.NMapOverlay;
import com.eastblue.cisuba.Map.NMapPOIflagType;
import com.eastblue.cisuba.Map.NMapViewerResourceProvider;
import com.eastblue.cisuba.R;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGPoint;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.eastblue.cisuba.Util.HttpUtil.context;

public class MapDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.mapView)
    NMapView mMapView;
    @BindView(R.id.btn_mylocation)
    ImageButton btn_mylocation;
    @BindView(R.id.btn_product)
    ImageButton btn_product;
    @BindView(R.id.map_progressBar)
    ProgressBar progressBar;

    NMapContext mMapContext;
    NMapViewerResourceProvider mMapViewerResourceProvider;
    NMapOverlayManager mOverlayManager;
    NMapLocationManager mMapLocationManager;
    NMapCompassManager mMapCompassManager;
    NMapMyLocationOverlay mMyLocationOverlay;
    NMapPOIdata poiData;
    NMapPOIdataOverlay poiDataOverlay;
    NMapController mMapController;
    NGeoPoint productPoint;
    NMapOverlayItem mMapOverlayItem;
    Boolean focusMyLocation = false;

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

        productPoint = new NGeoPoint((int)(lng * 1E6), (int)(lat * 1e6));


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMapContext = new NMapContext(this);
        mMapContext.onCreate();
        mMapContext.setupMapView(mMapView);

        mMapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정


        mMapViewerResourceProvider = new NMapViewerResourceProvider(this); // 리소스 프로바이더
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider); // 오버레이 매니저

        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();

        // 마커박는 작업
        int markerId = NMapPOIflagType.PIN;
//        Drawable pin = ContextCompat.getDrawable(getApplicationContext(), R.drawable.gp_pin);
        poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(lng, lat, productName,markerId, 0);
        mMapView.getMapController().setMapCenter(lng,lat,11);
        mMapController.animateTo(productPoint);

//        if (myLat != 0) {
//            Drawable myPin = getResources().getDrawable(R.drawable.my_pin);
//            myPin.setBounds(0, 0, 50, 70);
//            poiData.addPOIitem(myLng, myLat, productName, myPin, 0);
//        }

        poiData.endPOIdata();

        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);

        mMapView.setClickable(true); // 맵 클릭 가능하게
        mMapView.setScalingFactor(2.0f,false);

        // location manager
        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        // compass manager
        mMapCompassManager = new NMapCompassManager(this);

        // create my location overlay
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
    }

    @OnClick(R.id.btn_mylocation)
    public void myLocation() {
        progressBar.setVisibility(View.VISIBLE);
        focusMyLocation = true;

        //현재 위치 나침반
        /*
        if (mMapLocationManager.isMyLocationEnabled()) {

            if (!mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(true);

                mMapCompassManager.enableCompass();

                mMapView.setAutoRotateEnabled(true, false);

                mMapView.requestLayout();
            } else {
                stopMyLocation();
            }

            mMapView.postInvalidate();
        } else {
        */

        boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
        if (!isMyLocationEnabled) {
            Toast.makeText(this, "Please enable a My Location source in system settings",
                    Toast.LENGTH_LONG).show();

            Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(goToSettings);

            return;
        }
    }

    private void stopMyLocation() {
        if (mMyLocationOverlay != null) {
            mMapLocationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(false);

                mMapCompassManager.disableCompass();

                mMapView.setAutoRotateEnabled(false, false);

            }
        }
        focusMyLocation = false;
        progressBar.setVisibility(View.INVISIBLE);
    }


    @OnClick(R.id.btn_product)
    public void productLocation() {
        focusMyLocation = false;

        mMapController.animateTo(productPoint);

    }


    /* MyLocation Listener */
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

            if (mMapController != null) {
                if (focusMyLocation) {
                    mMapController.animateTo(myLocation);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

            // stop location updating
            //			Runnable runnable = new Runnable() {
            //				public void run() {
            //					stopMyLocation();
            //				}
            //			};
            //			runnable.run();

            Toast.makeText(MapDetailActivity.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(MapDetailActivity.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);

            stopMyLocation();
        }

    };

    @Override
    public void onStart() {
        progressBar.setVisibility(View.INVISIBLE);
        mMapContext.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        stopMyLocation();
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

package com.eastblue.cisuba.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eastblue.cisuba.Activity.MapDetailActivity;
import com.eastblue.cisuba.Activity.ProductDetailActivity;
import com.eastblue.cisuba.Adapter.NearAdapter;
import com.eastblue.cisuba.Gps.GpsUtil;
import com.eastblue.cisuba.Map.NMapPOIflagType;
import com.eastblue.cisuba.Map.NMapViewerResourceProvider;
import com.eastblue.cisuba.Model.ProductModel;
import com.eastblue.cisuba.Network.Product;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;
import com.eastblue.cisuba.Util.PermissionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapItemizedOverlay;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nirhart.parallaxscroll.views.ParallaxListView;

import static android.R.attr.width;

/**
 * Created by PJC on 2017-02-07.
 */

public class LocationFragment extends Fragment {

    private static final String TAG = LocationFragment.class.getSimpleName();

    View rootView = null;

    @BindView(R.id.lv_near)
    ParallaxListView lvNear;

    @BindView(R.id.pb_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_my_location)
    TextView tvMyLocation;

    NMapView mMapView;
    NMapContext mMapContext;
    NMapOverlayManager mOverlayManager;
    NMapViewerResourceProvider mMapViewerResourceProvider;
    NMapLocationManager mMapLocationManager;
    NMapCompassManager mMapCompassManager;
    NMapMyLocationOverlay mMyLocationOverlay;
    NMapController mMapController;
    NMapPOIdataOverlay poiDataOverlay;
    NMapPOIdata poiData;
    boolean isSelectPOI = false;
    NGeoPoint myLocation;
    final String CLIENT_ID = "N_PMI_hG0G1FAFhg8alc";

    NearAdapter nearAdapter;

    int currentPage = 0;
    int loadSize = 5;
    Boolean firstLoading = true;
    public static Boolean firstSelect = true;
    Boolean lastItemVisibleFlag = false;
    Boolean isGPSLoad = false;

    double mLat;
    double mLng;

    // GPS
    boolean isGetLocation = false;
    boolean isGPSEnabled = false;
    Location location;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    protected LocationManager locationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.rootView == null) {
            View rootView = inflater.inflate(R.layout.fragment_location, container, false);
            ButterKnife.bind(this, rootView);
            this.rootView = rootView;
            init();

            lvNear.addParallaxedHeaderView(mMapView);


        }
        return this.rootView;
    }


    void init() {
        mMapView = new NMapView(getActivity());

        mMapContext = new NMapContext(getActivity());
        mMapContext.onCreate();
        mMapContext.setupMapView(mMapView);
        mMapView.setLayoutParams(new NMapView.LayoutParams(NMapView.LayoutParams.MATCH_PARENT, 1300));
        mMapView.setClientId(CLIENT_ID);
        // initialize map view
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
        mMapViewerResourceProvider = new NMapViewerResourceProvider(getActivity());
        mOverlayManager = new NMapOverlayManager(getActivity(), mMapView, mMapViewerResourceProvider);
        mMapView.setScalingFactor(2.0f, false);
        mMapContext.onStart();

        // location manager
        mMapLocationManager = new NMapLocationManager(this.getActivity());
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        // compass manager
        mMapCompassManager = new NMapCompassManager(this.getActivity());

        // create my location overlay
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();


        boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
        if (!isMyLocationEnabled) {
            Toast.makeText(getActivity(), "Please enable a My Location source in system settings",
                    Toast.LENGTH_LONG).show();

            Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(goToSettings);

            return;
        }

        Log.d("frag", "init");

        lvNear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel productModel = (ProductModel) nearAdapter.getItem(position);
                if (!productModel.isFreePartner) {
                    startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("id", productModel.id)
                            .putExtra("gps", true)
                            .putExtra("lat", mLat)
                            .putExtra("lng", mLng));
                }
            }
        });

        lvNear.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    if (!firstLoading) {
                        nearProduct(currentPage, loadSize, 1, 4, mLat, mLng);
                    }
                }
            }
        });

        nearAdapter = new NearAdapter(getActivity());
        lvNear.setAdapter(nearAdapter);

        // GPS
        Boolean isGpsOn = SmartLocation.with(getActivity()).location().state().isGpsAvailable();
        Boolean isNetworkOn = SmartLocation.with(getActivity()).location().state().isNetworkAvailable();

        if (isGpsOn || isNetworkOn) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getActivity(), "위치 찾기를 켜주세요.", Toast.LENGTH_SHORT).show();
        }

        //testGPS();

        SmartLocation.with(getActivity()).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        if (!isGetLocation) {
                            isGetLocation = true;
                            progressBar.setVisibility(View.GONE);

                            double lat = location.getLatitude();
                            double lng = location.getLongitude();

                            try {
                                tvMyLocation.setText(GpsUtil.geoToAddress(getActivity(), lat, lng));
                                //setMarker(lat, lng, "");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            mLat = lat;
                            mLng = lng;
                            nearAdapter.setLocation(lat, lng);
                            nearProduct(currentPage, loadSize, 1, 4, lat, lng);
                        }
                    }
                });

        //initGPS();
    }

    void testGPS() {
        isGetLocation = true;
        progressBar.setVisibility(View.GONE);

        double lat = 37.538484;
        double lng = 127.082294;

        try {
            tvMyLocation.setText(GpsUtil.geoToAddress(getActivity(), lat, lng));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mLat = lat;
        mLng = lng;
        nearAdapter.setLocation(lat, lng);
        nearProduct(currentPage, loadSize, 1, 4, lat, lng);
    }

    void initGPS() {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Boolean isGpsOn, isNetworkOn;
        isGpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d(TAG + ":CHECK", "check");

        if (checkPermission()) {
            Log.d(TAG, "checkPermission");
            if (isGpsOn) {
                Log.d(TAG + ":GPS", "GPS");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, gpsListener);
            }
            if (isNetworkOn) {
                Log.d(TAG + ":NETWORK", "NETWORK");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, networkListener);
            }
        } else {
            Toast.makeText(getActivity(), "GPS 권한이 없습니다.", Toast.LENGTH_LONG).show();
        }

        /*
        if(PermissionUtil.State.isGPSon) {
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                registerLocationUpdates();
            } else {
                //Toast.makeText(getActivity(), "GPS 가 꺼져있습니다.", Toast.LENGTH_SHORT).show();
           }
        } else {
            Toast.makeText(getActivity(), "GPS 권한을 체크해주세요.", Toast.LENGTH_SHORT).show();
        }*/
    }

    /*
    private void registerLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000000, 100, mLocationListener);

        if(locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                // 위도 경도 저장
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                mLat = lat;
                mLng = lng;

                nearAdapter.setLocation(lat, lng);
                nearProduct(currentPage, loadSize, 1, 4, lat, lng);
            } else {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000, 1, mLocationListener);

                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location != null) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    mLat = lat;
                    mLng = lng;

                    nearAdapter.setLocation(lat, lng);
                    nearProduct(currentPage, loadSize, 1, 4, lat, lng);
                }
            }
        }
    } */

    void nearProduct(int page, int size, int area, int filter, double lat, double lng) {

        Log.d("nearProduct", page + " " + size);

        HttpUtil.api(Product.class).nearProduct(page, size, area, filter, String.valueOf(lat), String.valueOf(lng), new Callback<List<ProductModel>>() {
            @Override
            public void success(List<ProductModel> productModels, Response response) {
                int markerId = NMapPOIflagType.SPOT;


                Log.d("size", productModels.size() + "");

                poiData = new NMapPOIdata(productModels.size(), mMapViewerResourceProvider);

                poiData.beginPOIdata(productModels.size());
                if (firstLoading) {
                    nearAdapter.setArray((ArrayList<ProductModel>) productModels);
                    for (int i = 0; i < productModels.size(); i++) {
                        double lat = Double.parseDouble(productModels.get(i).lat);
                        double lng = Double.parseDouble(productModels.get(i).lng);
                        String name = productModels.get(i).partnerName;
                        poiData.addPOIitem(lng, lat, name, markerId, 0);
                    }
                    firstLoading = false;
                } else {
                    for (int i = 0; i < productModels.size(); i++) {
                        nearAdapter.addItem(productModels.get(i));

                        double lat = Double.parseDouble(productModels.get(i).lat);
                        double lng = Double.parseDouble(productModels.get(i).lng);
                        String name = productModels.get(i).partnerName;
                        poiData.addPOIitem(lng, lat, name, markerId, 0);
                    }
                }

                poiData.endPOIdata();
                poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
                mOverlayManager.setOnCalloutOverlayViewListener(new NMapOverlayManager.OnCalloutOverlayViewListener() {
                    @Override
                    public View onCreateCalloutOverlayView(NMapOverlay nMapOverlay, NMapOverlayItem nMapOverlayItem, Rect rect) {
                        try {
                            String productName = nMapOverlayItem.getTitle();
                            ProductModel selectProduct = (ProductModel) nearAdapter.getItem(productName);

                            if (!isSelectPOI) {
                                nearAdapter.add(0, selectProduct);
                                nearAdapter.setSelectedIndex(0);
                                nearAdapter.notifyDataSetChanged();
                                isSelectPOI = true;
                            } else {
                                nearAdapter.setItem(0, selectProduct);
                                nearAdapter.setSelectedIndex(999);
                                nearAdapter.notifyDataSetChanged();

                            }

                        } catch (NullPointerException e) {

                        }
                        return null;
                    }
                });
//                poiDataOverlay.setOnFocusChangeListener(new NMapItemizedOverlay.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChanged(NMapItemizedOverlay nMapItemizedOverlay, NMapOverlayItem nMapOverlayItem) {
//                        try {
//                            String productName = nMapOverlayItem.getTitle();
//                            ProductModel selectProduct = (ProductModel) nearAdapter.getItem(productName);
//                            nearAdapter.add(0, selectProduct);
//                        } catch (NullPointerException e) {
//
//                        }
//
//                    }
//                });
                poiDataOverlay.showAllPOIdata(0);

                currentPage++;
                nearAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            /*
            double lat = location.getLatitude();
            double lng = location.getLongitude();

                try {
                    tvMyLocation.setText(GpsUtil.geoToAddress(getActivity(), lat, lng));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                nearAdapter.setLocation(lat, lng);
                nearProduct(currentPage, loadSize, 1, 4, lat, lng);

                isGPSLoad = true;*/
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    LocationListener gpsListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG + ":GPS", "lat : " + location.getLatitude() + " lng : " + location.getLongitude());
            locationManager.removeUpdates(gpsListener);
            locationManager.removeUpdates(networkListener);

            if (!isGetLocation) {
                isGetLocation = true;

                double lat = location.getLatitude();
                double lng = location.getLongitude();

                nearAdapter.setLocation(lat, lng);
                nearProduct(currentPage, loadSize, 1, 4, lat, lng);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    LocationListener networkListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG + ":NETWORK", "lat : " + location.getLatitude() + " lng : " + location.getLongitude());
            locationManager.removeUpdates(gpsListener);
            locationManager.removeUpdates(networkListener);

            if (!isGetLocation) {
                isGetLocation = true;

                double lat = location.getLatitude();
                double lng = location.getLongitude();

                nearAdapter.setLocation(lat, lng);
                nearProduct(currentPage, loadSize, 1, 4, lat, lng);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Latitudessss", provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Latitude", provider);
        }
    };

    Boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }

        return true;
    }

    void setMarker(double lat, double lng, String name) {
        int markerId = NMapPOIflagType.PIN;


        poiData.beginPOIdata(1);
        poiData.addPOIitem(lng, lat, name, markerId, 0);
        poiData.endPOIdata();

        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

//
//        mMapView.getMapController().setMapCenter(lng, lat, 11);
    }

    private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

        @Override
        public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {
        }

        @Override
        public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {
        }

        @Override
        public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {
        }

        @Override
        public void onLongPressCanceled(NMapView nMapView) {
        }

        @Override
        public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {
        }

        @Override
        public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
            mapView.getParent().requestDisallowInterceptTouchEvent(true);
        }
    };

    /* MyLocation Listener */
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint _myLocation) {

            if (mMapController != null) {

                mMapController.animateTo(_myLocation);
                myLocation = _myLocation;

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

            Toast.makeText(getActivity(), "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(getActivity(), "Your current location is unavailable area.", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);

            stopMyLocation();
        }

    };

    private void stopMyLocation() {
        if (mMyLocationOverlay != null) {
            mMapLocationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(false);

                mMapCompassManager.disableCompass();

                mMapView.setAutoRotateEnabled(false, false);

            }
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapContext.onStart();
        Log.d("frag", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
        if (myLocation != null) {
            mMapController.animateTo(myLocation);
            poiDataOverlay.showAllPOIdata(0);
        }
        Log.d("frag", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
        Log.d("frag", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapContext.onStop();
        Log.d("frag", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapContext.onDestroy();
        Log.d("frag", "onDestroy");
    }
}

package com.eastblue.cisuba.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eastblue.cisuba.Activity.ProductDetailActivity;
import com.eastblue.cisuba.Adapter.NearAdapter;
import com.eastblue.cisuba.Gps.GpsUtil;
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

/**
 * Created by PJC on 2017-02-07.
 */

public class LocationFragment extends Fragment {

    private static final String TAG = LocationFragment.class.getSimpleName();

    View rootView = null;

    @BindView(R.id.lv_near)
    ListView lvNear;

    @BindView(R.id.pb_bar) ProgressBar progressBar;
    @BindView(R.id.tv_my_location) TextView tvMyLocation;

    NearAdapter nearAdapter;

    int currentPage = 0;
    int loadSize = 5;
    Boolean firstLoading = true;
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
        if(this.rootView == null) {
            View rootView = inflater.inflate(R.layout.fragment_location, container, false);
            ButterKnife.bind(this, rootView);
            this.rootView = rootView;
            init();
        }
        return this.rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("frag", "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("frag", "onDestroy");
    }

    void init() {

        Log.d("frag", "init");

        lvNear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel productModel = (ProductModel) nearAdapter.getItem(position);
                if(!productModel.isFreePartner) {
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

        if(isGpsOn || isNetworkOn) {
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
                        if(!isGetLocation) {
                            isGetLocation = true;
                            progressBar.setVisibility(View.GONE);

                            double lat = location.getLatitude();
                            double lng = location.getLongitude();

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

        if(checkPermission()) {
            Log.d(TAG, "checkPermission");
            if(isGpsOn) {
                Log.d(TAG + ":GPS", "GPS");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, gpsListener);
            }
            if(isNetworkOn) {
                Log.d(TAG + ":NETWORK", "NETWORK");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, networkListener);
            }
        }
        else {
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

                Log.d("size", productModels.size() + "");

                if(firstLoading) {
                    nearAdapter.setArray((ArrayList<ProductModel>) productModels);
                    firstLoading = false;
                } else {
                    for (int i = 0; i < productModels.size(); i++) {
                        nearAdapter.addItem(productModels.get(i));
                    }
                }

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

            if(!isGetLocation) {
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

            if(!isGetLocation) {
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
            Log.d("Latitudessss",provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Latitude",provider);
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
}

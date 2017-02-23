package com.eastblue.cisuba.Fragment;

import android.Manifest;
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
import android.widget.TextView;
import android.widget.Toast;

import com.eastblue.cisuba.Activity.ProductDetailActivity;
import com.eastblue.cisuba.Adapter.NearAdapter;
import com.eastblue.cisuba.Gps.GpsUtil;
import com.eastblue.cisuba.Model.ProductModel;
import com.eastblue.cisuba.Network.Product;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by PJC on 2017-02-07.
 */

public class LocationFragment extends Fragment {

    @BindView(R.id.lv_near)
    ListView lvNear;

    @BindView(R.id.tv_my_location) TextView tvMyLocation;

    NearAdapter nearAdapter;

    int currentPage = 0;
    int loadSize = 5;
    Boolean firstLoading = true;
    Boolean lastItemVisibleFlag = false;

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
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    void init() {

        lvNear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel productModel = (ProductModel) nearAdapter.getItem(position);
                startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("id", productModel.id));
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
                        getProduct(currentPage, loadSize, 1, 2);
                    }
                }
            }
        });

        nearAdapter = new NearAdapter(getActivity());
        lvNear.setAdapter(nearAdapter);
        initGPS();
    }

    void initGPS() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
    }

    void getProduct(int page, int size, int area, int filter) {
        HttpUtil.api(Product.class).getProduct(page, size, area, filter, new Callback<List<ProductModel>>() {
            @Override
            public void success(List<ProductModel> productModels, Response response) {
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

    void nearProduct(int page, int size, int area, int filter, double lat, double lng) {
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

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            try {
                tvMyLocation.setText(GpsUtil.geoToAddress(getActivity(), lat, lng));
            } catch (IOException e) {
                e.printStackTrace();
            }

            nearAdapter.setLocation(lat, lng);
            nearProduct(0, 10, 1, 3, lat, lng);
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
}

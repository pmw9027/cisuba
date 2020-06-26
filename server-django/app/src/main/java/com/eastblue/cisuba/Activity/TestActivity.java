package com.eastblue.cisuba.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.eastblue.cisuba.R;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;

public class TestActivity extends AppCompatActivity {

    NMapView mMapView;

    final String CLIENT_ID = "N_PMI_hG0G1FAFhg8alc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapView = new NMapView(this);
        mMapView.setClientId(CLIENT_ID);

        setContentView(mMapView);

        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();

    }
}

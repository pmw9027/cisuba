package com.eastblue.cisuba.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.PermissionUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.imv_splash)
    ImageView imvSplash;

    private static final int DELAY = 2000;

    String [] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    Handler delayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        Picasso.with(this).load(R.drawable.splash).fit().into(imvSplash);

        if (!checkPermission()) {
            ActivityCompat.requestPermissions( this,
                    permissions,
                    101);
        } else {
            delayHandler.sendEmptyMessageDelayed(0, 2000);
        }

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY); */
    }

    boolean checkPermission(){
        for(String permission : permissions){
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    permission);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED)  return false;
        }

        PermissionUtil.State.isGPSon = true;
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==101){
            delayHandler.sendEmptyMessageDelayed(0, 2000);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

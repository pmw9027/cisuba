package com.eastblue.cisuba.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eastblue.cisuba.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.join_close)
    void joinClose() {
        finish();
    }


}
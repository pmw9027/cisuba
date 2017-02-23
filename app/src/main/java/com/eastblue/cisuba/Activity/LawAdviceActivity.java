package com.eastblue.cisuba.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eastblue.cisuba.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LawAdviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_advice);
        ButterKnife.bind(this);

        init();
    }

    void init() {

    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }
}

package com.eastblue.cisuba.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.eastblue.cisuba.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.btn_notice) Button btnNotice;
    @BindView(R.id.btn_faq) Button btnFaq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    void init() {
        if(getIntent().getStringExtra("TYPE").equals("FAQ")) {
            onFaq();
        } else {
            onNotice();
        }
    }

    @OnClick(R.id.btn_notice)
    void onNotice() {
        btnFaq.setTextColor(Color.BLACK);
        btnNotice.setTextColor(getResources().getColor(R.color.product_color));
    }

    @OnClick(R.id.btn_faq)
    void onFaq() {
        btnNotice.setTextColor(Color.BLACK);
        btnFaq.setTextColor(getResources().getColor(R.color.product_color));
    }
}

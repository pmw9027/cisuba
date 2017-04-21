package com.eastblue.cisuba.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import com.eastblue.cisuba.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sung on 2017. 4. 14..
 */

public class TicketActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_create)
    Button btnCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    void init() {

    }


    @OnClick(R.id.btn_create)
    void createQR() {


    }



    @OnClick(R.id.btn_scan)
    void scanQR() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(QRcodeActivity.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // QR코드/바코드를 스캔한 결과 값을 가져옵니다.
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        // 결과값 출력
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(result.getContents() + " [" + result.getFormatName() + "]")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


}

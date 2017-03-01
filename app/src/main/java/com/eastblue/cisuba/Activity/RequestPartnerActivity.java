package com.eastblue.cisuba.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.eastblue.cisuba.Network.Partner;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RequestPartnerActivity extends AppCompatActivity {

    @BindView(R.id.imv_image) ImageView imvImage;
    @BindView(R.id.et_email_address) EditText etEmailAddress;
    @BindView(R.id.et_partner_name) EditText etPartnerName;
    @BindView(R.id.et_phone_number) EditText etPhoneNumber;
    @BindView(R.id.et_name) EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_partner);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_request_partner)
    public void requestPartner() {

        if(
                TextUtils.isEmpty(etEmailAddress.getText().toString()) ||
                TextUtils.isEmpty(etPartnerName.getText().toString()) ||
                TextUtils.isEmpty(etPhoneNumber.getText().toString()) ||
                TextUtils.isEmpty(etName.getText().toString())) {
            Toast.makeText(this, "공란을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else {

            HttpUtil.api(Partner.class).requestPartner(
                    etPartnerName.getText().toString(),
                    etName.getText().toString(),
                    etEmailAddress.getText().toString(),
                    etPhoneNumber.getText().toString(),
                    new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                        }
                    });
        }

    }
}

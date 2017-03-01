package com.eastblue.cisuba.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eastblue.cisuba.Manager.NetworkManager;
import com.eastblue.cisuba.Model.CodeModel;
import com.eastblue.cisuba.Network.Partner;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LawAdviceActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.tv_info)
    TextView tvInfo;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_content)
    EditText etContent;

    String selectType = "0";

    Button[] typeBtnArray;

    private static final int TYPE_SIZE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_advice);
        ButterKnife.bind(this);

        typeBtnArray = new Button[TYPE_SIZE];
        typeBtnArray[0] = (Button) findViewById(R.id.btn_type_0);
        typeBtnArray[1] = (Button) findViewById(R.id.btn_type_1);
        typeBtnArray[2] = (Button) findViewById(R.id.btn_type_2);
        typeBtnArray[3] = (Button) findViewById(R.id.btn_type_3);
        typeBtnArray[4] = (Button) findViewById(R.id.btn_type_4);

        for(int i=0; i<typeBtnArray.length; i++) {
            typeBtnArray[i].setOnClickListener(this);
            typeBtnArray[i].setTag(i);
        }
    }

    @OnClick(R.id.btn_send)
    public void sendBtn() {
        if(TextUtils.isEmpty(etName.getText().toString()) || TextUtils.isEmpty(etContent.getText().toString()) || TextUtils.isEmpty(etPhone.getText().toString())) {
            Toast.makeText(LawAdviceActivity.this, "모든 빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtil.api(Partner.class).add_rawAdvice(selectType, etName.getText().toString(), etContent.getText().toString(), etPhone.getText().toString(), new Callback<CodeModel>() {
            @Override
            public void success(CodeModel codeModel, Response response) {
                if(codeModel.code.equals("1")) {
                    Toast.makeText(LawAdviceActivity.this, "법률서비스가 신청되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @OnClick(R.id.btn_call)
    public void callBtn() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02-6022-2220"));
        startActivity(intent);
    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        int type_number = (int) button.getTag();

        tvInfo.setText("씨서바 [" + button.getText() + "]분야 무료 상담하기");

        for(int i=0; i<typeBtnArray.length; i++) {
            typeBtnArray[i].setTextColor(Color.BLACK);
        }

        button.setTextColor(getResources().getColor(R.color.lunch_text));
        selectType = String.valueOf(type_number);
    }
}

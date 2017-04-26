package com.eastblue.cisuba.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.eastblue.cisuba.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity {
    EditText et_email;
    EditText et_password, et_passwordcheck;
    EditText et_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);

        et_email = (EditText)findViewById(R.id.input_join_email);
        et_password = (EditText)findViewById(R.id.input_join_pass);
        et_passwordcheck = (EditText)findViewById(R.id.input_join_pass_config);
        et_name = (EditText)findViewById(R.id.input_join_name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.join_close)
    void joinClose() {
        finish();
    }

    @OnClick(R.id.email_join)
    void email_join() {
        String email = et_email.getText().toString();
        String en_pass = et_name.getText().toString();

        if(!et_password.getText().toString().equals(et_passwordcheck.getText().toString()))
        {
            Toast.makeText(this,"비밀번호가 틀립니다",Toast.LENGTH_SHORT).show();
        }
        try {
            en_pass = LoginActivity.Encrypt(et_password.getText().toString(), "cisuba");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("email : "+et_email.getText().toString());
        System.out.println("pass : "+et_password.getText().toString());
        System.out.println("pass check : "+et_passwordcheck.getText().toString());
        System.out.println("name : "+et_name.getText().toString());
    }


}
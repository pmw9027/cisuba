package com.eastblue.cisuba.Adapter;

import android.app.Activity;
import android.content.Context;

import com.eastblue.cisuba.CisubaApplication;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

/**
 * Created by pjc on 2017. 1. 5..
 */

public class KakaoSDKAdapter extends KakaoAdapter {

    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {

            @Override
            public Context getApplicationContext() {
                return CisubaApplication.getGlobalApplicationContext();
            }
        };
    }

    @Override
    public ISessionConfig getSessionConfig() {
        return new ISessionConfig() {
            @Override
            public AuthType[] getAuthTypes() {
                return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
            }

            @Override
            public boolean isUsingWebviewTimer() {
                return false;
            }

            @Override
            public boolean isSecureMode() {
                return false;
            }

            @Override
            public ApprovalType getApprovalType() {
                return ApprovalType.INDIVIDUAL;
            }

            @Override
            public boolean isSaveFormData() {
                return true;
            }
        };
    }
}

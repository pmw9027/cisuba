package com.eastblue.cisuba;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.eastblue.cisuba.Adapter.KakaoSDKAdapter;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by pjc on 2017. 1. 5..
 */

public class CisubaApplication extends Application {

    private static CisubaApplication mInstance;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static Activity getCurrentActivity() {
        Log.d("TAG", "++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        CisubaApplication.currentActivity = currentActivity;
    }

    public static CisubaApplication getGlobalApplicationContext() {
        if(mInstance == null)
            throw new IllegalStateException("this application does not inherit GlobalApplication");
        return mInstance;
    }
}

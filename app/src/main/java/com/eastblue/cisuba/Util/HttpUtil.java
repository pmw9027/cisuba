package com.eastblue.cisuba.Util;

import android.content.Context;

import com.eastblue.cisuba.Manager.NetworkManager;

/**
 * Created by pjc on 2017. 1. 3..
 */

public class HttpUtil {

    public static Context context;

    public static <T> T api(Class<T> cls) {
        return NetworkManager.getInstance().getAPI(cls);
    }
}

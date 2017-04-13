package com.eastblue.cisuba.Manager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by PJC on 2016-05-04.
 */
public class NetworkManager {
    public static final String SERVER_URL = "http://cisuba.net";

    static NetworkManager networkManager;

    private HashMap<Class, Object> apiMap = new HashMap<>();
    private RestAdapter restAdapter = null;

    private NetworkManager() {
        Gson gson = new GsonBuilder()
                .setDateFormat(DateFormat.FULL, DateFormat.FULL)
                .create();

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(15 * 1000, TimeUnit.MILLISECONDS);
        OkClient client = new OkClient(httpClient);

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(SERVER_URL)
                .setClient(client)
                .setConverter(new GsonConverter(gson))
                .build();
    }

    public static NetworkManager getInstance() {
        if (networkManager == null) {
            networkManager = new NetworkManager();
        }
        return networkManager;
    }

    /**
     * api 호출시 사용하게되는 함수
     * @param cls api 정의 클래스
     * @param <T>
     * @return
     */
    public <T> T getAPI(Class<T> cls) {
        Object hit = apiMap.get(cls);
        if (hit == null) {
            hit = restAdapter.create(cls);
            apiMap.put(cls, hit);
        }
        return (T) hit;
    }
}


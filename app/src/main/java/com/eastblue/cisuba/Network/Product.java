package com.eastblue.cisuba.Network;

import com.eastblue.cisuba.Model.ProductModel;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * Created by PJC on 2017-02-22.
 */

public interface Product {

    @GET("/cisuba")
    void getProduct(
            @Query("page") String page,
            @Query("size") String size,
            @Query("area") String area,
            @Query("filter") String filter,
            Callback<List<ProductModel>> callback
    );
}

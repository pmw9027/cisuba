package com.eastblue.cisuba.Network;

import com.eastblue.cisuba.Model.ProductModel;
import com.squareup.okhttp.Call;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;


/**
 * Created by PJC on 2017-02-22.
 */

public interface Product {

    @GET("/cisuba")
    void getProduct(
            @Query("page") int page,
            @Query("size") int size,
            @Query("area") int area,
            @Query("filter") int filter,
            Callback<List<ProductModel>> callback
    );

    @GET("/get_topareapartner_list")
    void getTopPartner(
            @Query("area") String area,
            @Query("size") String size,
            Callback<List<ProductModel>> callback
    );

    @GET("/item_partner/{pk}")
    void getPartnerItem(
            @Path("pk") String pk,
            Callback<ProductModel> callback
    );
}

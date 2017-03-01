package com.eastblue.cisuba.Network;

import com.eastblue.cisuba.Model.CodeModel;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by PJC on 2017-02-22.
 */

public interface Partner {

    @FormUrlEncoded
    @POST("/partner_join")
    void requestPartner(
            @Field("businessName") String businessName,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/advice/add_rawAdvice")
    void add_rawAdvice(
            @Field("type") String type,
            @Field("name") String name,
            @Field("content") String content,
            @Field("phone") String phone,
            Callback<CodeModel> callback
    );
}

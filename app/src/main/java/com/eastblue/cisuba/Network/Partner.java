package com.eastblue.cisuba.Network;

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

}

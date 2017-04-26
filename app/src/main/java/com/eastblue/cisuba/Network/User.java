package com.eastblue.cisuba.Network;

import com.eastblue.cisuba.Model.UserModel;
import com.squareup.okhttp.Call;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Jo on 2017. 4. 24..
 */

public interface User {
/*
    @GET("/insert/{email}/{password}/{username}/{phone}")
    void getPartnerItem(
            @Path("pk") String pk,
            Callback<UserModel> callback
    );
*/
    @GET("/get_user/{email}")
    void get_user_inform(
            @Path("email") String email,
            Callback<UserModel> callback
    );


}

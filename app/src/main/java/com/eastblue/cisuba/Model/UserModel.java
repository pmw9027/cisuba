package com.eastblue.cisuba.Model;

import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jo on 2017-04-24.
 */

public class UserModel {
    @SerializedName("id")
    public String id;

    @SerializedName("email")
    public String email;

    @SerializedName("username")
    public String username;

    @SerializedName("last_login")
    public String last_login;

    @SerializedName("date_joined")
    public String date_joined;

    @SerializedName("phone")
    public String phone;

}

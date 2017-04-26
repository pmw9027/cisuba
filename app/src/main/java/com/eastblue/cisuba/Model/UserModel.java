package com.eastblue.cisuba.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jo on 2017-04-24.
 */

public class UserModel {

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("username")
    public String username;

    @SerializedName("last_login")
    public String last_login;

    @SerializedName("date_joined")
    public String date_joined;

    @SerializedName("phone")
    public String phone;

}

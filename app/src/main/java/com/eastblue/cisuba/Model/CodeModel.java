package com.eastblue.cisuba.Model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by PJC on 2017-02-27.
 */

public class CodeModel {
    @SerializedName("code")
    public String code;

    @SerializedName("user_name")
    public String user_name;

    @SerializedName("message")
    public String message;
}

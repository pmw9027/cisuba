package com.eastblue.cisuba.Model;

import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PJC on 2017-02-22.
 */

public class ProductModel {

    @SerializedName("id")
    public String id;

    @SerializedName("partnerName")
    public String partnerName;

    @SerializedName("mainThumbnail")
    public String mainThumbnail;

    @SerializedName("hit")
    public String hit;

    @SerializedName("isFreePartner")
    public Boolean isFreePartner;

    @SerializedName("gubunAdress")
    public String gubunAdress;

    @SerializedName("highlightAddress")
    public String highlightAddress;

    @SerializedName("shortAddress")
    public String shortAddress;

    @SerializedName("detailAddress")
    public String detailAddress;

    @SerializedName("detailAbout")
    public String detailAbout;

    @SerializedName("useAbout")
    public String useAbout;

    @SerializedName("startStime")
    public String startStime;

    @SerializedName("startEtime")
    public String startEtime;

    @SerializedName("phone")
    public String phone;

    @SerializedName("discount")
    public String discount;

    @SerializedName("lat")
    public String lat;

    @SerializedName("lng")
    public String lng;

    @SerializedName("morningStime")
    public String morningStartTime;

    @SerializedName("morningEtime")
    public String morningEndTime;

    @SerializedName("lunchStime")
    public String lunchStartTime;

    @SerializedName("lunchEtime")
    public String lunchEndTime;

    @SerializedName("dinnerStime")
    public String dinnerStartTime;

    @SerializedName("dinnerEtime")
    public String dinnerEndTime;

    @SerializedName("morningPrice")
    public String morningPrice;

    @SerializedName("lunchPrice")
    public String lunchPrice;

    @SerializedName("dinnerPrice")
    public String dinnerPrice;

    @SerializedName("tag")
    public List<TagModel> tagList;

    @SerializedName("partnerproduct_image")
    public List<ProductImageModel> imageList;
}

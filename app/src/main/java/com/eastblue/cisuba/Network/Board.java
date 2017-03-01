package com.eastblue.cisuba.Network;

import com.eastblue.cisuba.Model.BannerModel;
import com.eastblue.cisuba.Model.BoardModel;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by PJC on 2017-02-28.
 */

public interface Board {

    @GET("/get_board")
    void getBoard(
            @Query("type") int type,
            Callback<List<BoardModel>> callback
    );

}

package com.eastblue.cisuba.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.eastblue.cisuba.Adapter.NearAdapter;
import com.eastblue.cisuba.Model.ProductModel;
import com.eastblue.cisuba.Network.Product;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by PJC on 2017-02-07.
 */

public class LocationFragment extends Fragment {

    @BindView(R.id.lv_near) ListView lvNear;

    NearAdapter nearAdapter;

    int currentPage = 0;
    int loadSize = 5;
    Boolean firstLoading = true;
    Boolean lastItemVisibleFlag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    void init() {

        lvNear.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    if(!firstLoading) {
                        getProduct(currentPage, loadSize, 1, 2);
                    }
                }
            }
        });

        nearAdapter = new NearAdapter(getActivity());
        lvNear.setAdapter(nearAdapter);
        getProduct(currentPage, loadSize, 1, 2);
    }

    void getProduct(int page, int size, int area, int filter) {
        HttpUtil.api(Product.class).getProduct(page, size, area, filter, new Callback<List<ProductModel>>() {
            @Override
            public void success(List<ProductModel> productModels, Response response) {
                if(firstLoading) {
                    nearAdapter.setArray((ArrayList<ProductModel>) productModels);
                    firstLoading = false;
                } else {
                    for (int i = 0; i < productModels.size(); i++) {
                        nearAdapter.addItem(productModels.get(i));
                    }
                }

                currentPage++;
                nearAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}

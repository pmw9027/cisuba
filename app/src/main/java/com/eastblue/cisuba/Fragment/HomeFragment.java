package com.eastblue.cisuba.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.eastblue.cisuba.Adapter.RankAdapter;
import com.eastblue.cisuba.Model.ProductModel;
import com.eastblue.cisuba.Network.Product;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by PJC on 2017-02-07.
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.slider) SliderLayout mBannerSlider;
    @BindView(R.id.grv_count_4) GridView gridView;

    RankAdapter rankAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {

        mBannerSlider.stopAutoCycle();

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("BANNER", R.drawable.banner_1);
        file_maps.put("BANNER2", R.drawable.banner2);

        for(String name : file_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(name))
                    .description("")
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mBannerSlider.addSlider(textSliderView);



        }

        rankAdapter = new RankAdapter(getActivity());
        gridView.setAdapter(rankAdapter);
        getTopProduct();
    }

    void getTopProduct() {
        HttpUtil.api(Product.class).getTopPartner("1", "4", new Callback<List<ProductModel>>() {
            @Override
            public void success(List<ProductModel> productModels, Response response) {
                rankAdapter.setArray((ArrayList<ProductModel>) productModels);
                rankAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}

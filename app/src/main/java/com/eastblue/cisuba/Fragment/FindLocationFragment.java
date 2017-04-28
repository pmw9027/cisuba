package com.eastblue.cisuba.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.eastblue.cisuba.Activity.ProductDetailActivity;
import com.eastblue.cisuba.Adapter.NearAdapter;
import com.eastblue.cisuba.Model.ProductModel;
import com.eastblue.cisuba.Network.Product;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.String.LocationCode;
import com.eastblue.cisuba.Util.HttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by PJC on 2017-02-07.
 */

public class FindLocationFragment extends Fragment{

    @BindView(R.id.lin_gunbun_tab) LinearLayout linTab;
    @BindView(R.id.lv_near) ListView listView;

    Button[] tabButtons;

    NearAdapter nearAdapter;

    int selectArea = 1; // DEFAULT 0

    int currentPage = 0;
    int loadSize = 10;
    int loadType = 3;
    Boolean firstLoading = true;
    Boolean lastItemVisibleFlag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_find, container, false);
        ButterKnife.bind(this, rootView);
        init(rootView);
        return rootView;
    }

    void init(View v) {

        initTab();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel productModel = (ProductModel) nearAdapter.getItem(position);
                if(!productModel.isFreePartner) {
                    startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("id", productModel.id));
                }
                else{
                    Toast.makeText(getContext(), "제휴 준비중 입니다", Toast.LENGTH_LONG).show();

                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    if(!firstLoading) {
                        getProduct(currentPage, loadSize, selectArea, loadType);
                    }
                }
            }
        });

        nearAdapter = new NearAdapter(getActivity());
        listView.setAdapter(nearAdapter);

        getProduct(currentPage, loadSize, selectArea, loadType);
    }

    void initTab() {

        tabButtons = new Button[LocationCode.getInstance().getMap().size()];

        int i=0;
        for(Map.Entry<String, Integer> elem : LocationCode.getInstance().getMap().entrySet() ){
            Button button = makeTabButton(elem.getKey());
            tabButtons[i] = button;
            linTab.addView(button);
            i++;
        }
    }

    void getProduct(int page, int size, int area, int filter) {

        Log.d("area", area + "");

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

                Log.d("nearAdapter", "called");
                nearAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    View.OnClickListener onTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Button clickBtn = (Button) v;

            if(clickBtn.getText().equals("전체")) {
                loadType = 3;
            } else {
                loadType = 2;
            }

            firstLoading = true;
            currentPage = 0;
            nearAdapter.removeAll();
            nearAdapter.notifyDataSetChanged();

            for(int i=0; i<tabButtons.length; i++) {
                tabButtons[i].setTextColor(getResources().getColor(R.color.Black));
            }
            ((Button) v).setTextColor(getResources().getColor(R.color.product_color));
            selectArea = LocationCode.getInstance().getMap().get(((Button) v).getText());

            if(selectArea == 0) {
                getProduct(currentPage, loadSize, selectArea, 3);
                loadType = 3;
            } else {
                getProduct(currentPage, loadSize, LocationCode.getInstance().getMap().get(((Button) v).getText()), 2);
                loadType = 2;
            }
        }
    };

    Button makeTabButton(String text) {
        Button button = new Button(getActivity());
        button.setText(text);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        if(text.equals("전체")) {
            button.setTextColor(getResources().getColor(R.color.product_color));
        } else {
            button.setTextColor(getResources().getColor(R.color.Black));
        }

        button.setBackgroundColor(getResources().getColor(R.color.White));
        button.setOnClickListener(onTabClickListener);
        return button;
    }
}

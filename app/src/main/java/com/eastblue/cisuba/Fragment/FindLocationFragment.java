package com.eastblue.cisuba.Fragment;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

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

    int currentPage = 0;
    int loadSize = 5;
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        listView.setAdapter(nearAdapter);
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

    View.OnClickListener onTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            currentPage = 0;
            nearAdapter.removeAll();
            nearAdapter.notifyDataSetChanged();

            for(int i=0; i<tabButtons.length; i++) {
                tabButtons[i].setTextColor(getResources().getColor(R.color.Black));
            }
            ((Button) v).setTextColor(getResources().getColor(R.color.product_color));
            getProduct(currentPage, loadSize, LocationCode.getInstance().getMap().get(((Button) v).getText()), 2);
        }
    };

    Button makeTabButton(String text) {
        Button button = new Button(getActivity());
        button.setText(text);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        button.setTextColor(getResources().getColor(R.color.Black));
        button.setBackgroundColor(getResources().getColor(R.color.White));
        button.setOnClickListener(onTabClickListener);
        return button;
    }
}

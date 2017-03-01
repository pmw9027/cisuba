package com.eastblue.cisuba.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.eastblue.cisuba.Activity.LawAdviceActivity;
import com.eastblue.cisuba.Activity.ProductDetailActivity;
import com.eastblue.cisuba.Adapter.NearAdapter;
import com.eastblue.cisuba.Adapter.RankAdapter;
import com.eastblue.cisuba.Model.ProductModel;
import com.eastblue.cisuba.Network.Product;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.String.ProductTypeCode;
import com.eastblue.cisuba.Util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
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

public class HomeFragment extends Fragment {

    @BindView(R.id.slider) SliderLayout mBannerSlider;
    @BindView(R.id.grv_count_4) GridView gridView;
    @BindView(R.id.lv_items) ListView lvItems;
    @BindView(R.id.lin_tab) LinearLayout linTab;
    @BindView(R.id.home_scrollview) ScrollView scrollView;

    Button tabButtonArray[];
    View tabSelectorArray[];

    RankAdapter rankAdapter;
    NearAdapter nearAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {

        recommendTabInit();

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
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    if(slider.getBundle().getString("extra").equals("BANNER2")) {
                        Intent intent = new Intent(getActivity(), LawAdviceActivity.class);
                        startActivity(intent);
                    }
                }
            });

            mBannerSlider.addSlider(textSliderView);

            scrollView.pageScroll(View.FOCUS_UP);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel productModel = (ProductModel) rankAdapter.getItem(position);
                if(!productModel.isFreePartner) {
                    startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("id", productModel.id));
                }
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel productModel = (ProductModel) nearAdapter.getItem(position);
                if(!productModel.isFreePartner) {
                    startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("id", productModel.id));
                }
            }
        });

        rankAdapter = new RankAdapter(getActivity());
        nearAdapter = new NearAdapter(getActivity());
        gridView.setAdapter(rankAdapter);
        lvItems.setAdapter(nearAdapter);

        getTopProduct();
        getPartnerFilter(0, 10, 1);
    }

    void getTopProduct() {
        HttpUtil.api(Product.class).getTopPartner("1", "4", new Callback<List<ProductModel>>() {
            @Override
            public void success(List<ProductModel> productModels, Response response) {
                rankAdapter.setArray((ArrayList<ProductModel>) productModels);
                rankAdapter.notifyDataSetChanged();
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    void getPartnerFilter(int page, int size, int type) {

        Log.d("type", type + "");

        HttpUtil.api(Product.class).getPartnerTypeFilter(page, size, type, new Callback<List<ProductModel>>() {
            @Override
            public void success(List<ProductModel> productModels, Response response) {
                nearAdapter.removeAll();
                for(int i=0; i<productModels.size(); i++) {
                    nearAdapter.setArray((ArrayList<ProductModel>) productModels);
                }
                nearAdapter.notifyDataSetInvalidated();
                setListViewHeightBasedOnChildren(lvItems);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    void recommendTabInit() {
        tabButtonArray = new Button[ProductTypeCode.getInstance().getMap().size()];
        tabSelectorArray = new View[ProductTypeCode.getInstance().getMap().size()];

        HashMap<String, Integer> productMap = ProductTypeCode.getInstance().getMap();
        for (Map.Entry<String, Integer> entry : productMap.entrySet()) {
            LinearLayout tabItem = addTabItem(entry.getKey(), entry.getValue());
            linTab.addView(tabItem);
        }
    }

    LinearLayout addTabItem(String item, int position) {

        LinearLayout tabContainer = new LinearLayout(getActivity());
        tabContainer.setOrientation(LinearLayout.VERTICAL);

        Button tabBtn = new Button(getActivity());
        tabBtn.setOnClickListener(tabItemClickListener);
        tabBtn.setText(item);
        tabBtn.setBackgroundColor(Color.parseColor("#ebebebeb"));
        tabBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tabBtn.setTag(position);

        if(position == 1) {
            tabBtn.setTextColor(getResources().getColor(R.color.product_color));
        }

        View bottomSelector = new View(getActivity());
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        bottomSelector.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size));

        tabButtonArray[position] = tabBtn;
        tabSelectorArray[position] = bottomSelector;

        tabContainer.setTag(position);

        tabContainer.addView(tabBtn);
        tabContainer.addView(bottomSelector);
        return tabContainer;
    }

    View.OnClickListener tabItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button tabItem = (Button) v;
            int itemPosition = (int) tabItem.getTag();

            // RESET TAB
            for(int i=0; i<tabButtonArray.length; i++) {
                tabButtonArray[i].setTextColor(Color.parseColor("#555555"));
            }

            for(int i=0; i<tabSelectorArray.length; i++) {
                tabSelectorArray[i].setBackgroundColor(Color.parseColor("#ebebebeb"));
            }

            tabButtonArray[itemPosition].setTextColor(getResources().getColor(R.color.product_color));
            tabSelectorArray[itemPosition].setBackgroundColor(getResources().getColor(R.color.product_color));

            getPartnerFilter(0, 10, itemPosition);
        }
    };

    public void setListViewHeightBasedOnChildren(ListView listView) {

        int totalHeight = 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < nearAdapter.getCount(); i++) {
            View listItem = nearAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + 300;
        listView.setLayoutParams(params);

        listView.requestLayout();
    }
}

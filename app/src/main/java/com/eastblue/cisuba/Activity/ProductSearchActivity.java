package com.eastblue.cisuba.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class ProductSearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.lv_near)
    ListView lvNear;

    NearAdapter nearAdapter;

    String searchName;

    int currentPage = 0;
    int loadSize = 5;
    Boolean firstLoading = true;
    Boolean lastItemVisibleFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        ButterKnife.bind(this);

        init();
    }

    void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchName = getIntent().getStringExtra("name");

        nearAdapter = new NearAdapter(this);
        lvNear.setAdapter(nearAdapter);

        lvNear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel productModel = (ProductModel) nearAdapter.getItem(position);
                if(!productModel.isFreePartner) {
                    startActivity(new Intent(ProductSearchActivity.this, ProductDetailActivity.class).putExtra("id", productModel.id));
                }
            }
        });

        lvNear.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    if(!firstLoading) {
                        searchProduct(currentPage, loadSize, searchName);
                    }
                }
            }
        });

        searchProduct(0, loadSize, searchName);
    }

    void searchProduct(int page, int size, String name) {
        HttpUtil.api(Product.class).searchPartner(page, size, name, new Callback<List<ProductModel>>() {
            @Override
            public void success(List<ProductModel> productModels, Response response) {

                Log.d("size", productModels.size() + "");

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

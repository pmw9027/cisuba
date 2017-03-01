package com.eastblue.cisuba.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.eastblue.cisuba.Adapter.BoardAdapter;
import com.eastblue.cisuba.Model.BoardModel;
import com.eastblue.cisuba.Network.Board;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NoticeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.elv_notice) ExpandableListView expandableListView;
    @BindView(R.id.btn_notice) Button btnNotice;
    @BindView(R.id.btn_faq) Button btnFaq;

    BoardAdapter boardAdapter;

    private static final int BOARD_NOTICE = 0;
    private static final int BOARD_QUESTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    void init() {

        boardAdapter = new BoardAdapter(this);
        expandableListView.setAdapter(boardAdapter);

        if(getIntent().getStringExtra("TYPE").equals("FAQ")) {
            onFaq();
        } else {
            onNotice();
        }

        getBoard(BOARD_NOTICE);
    }

    void getBoard(int type) {
        HttpUtil.api(Board.class).getBoard(type, new Callback<List<BoardModel>>() {
            @Override
            public void success(List<BoardModel> boardModels, Response response) {

                Log.d("notice", boardModels.size() + "");


                boardAdapter.mList.clear();

                for(int i=0; i<boardModels.size(); i++) {
                    boardAdapter.addItem(boardModels.get(i));
                }
                boardAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    @OnClick(R.id.btn_notice)
    void onNotice() {
        btnFaq.setTextColor(Color.BLACK);
        btnNotice.setTextColor(getResources().getColor(R.color.product_color));
        getBoard(BOARD_NOTICE);
    }

    @OnClick(R.id.btn_faq)
    void onFaq() {
        btnNotice.setTextColor(Color.BLACK);
        btnFaq.setTextColor(getResources().getColor(R.color.product_color));
        getBoard(BOARD_QUESTION);
    }
}
